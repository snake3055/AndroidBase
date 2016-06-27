package com.gistandard.androidbase.http;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gistandard.androidbase.application.IApplication;
import com.gistandard.androidbase.http.extension.MultipartEntity;
import com.gistandard.androidbase.http.extension.RequestEx;
import com.gistandard.androidbase.utils.LogCat;
import com.gistandard.androidbase.utils.NetworkKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:http网络请求任务基类，只适用于非上传下载的普通网络请求
 * Name:         BaseTask
 * Author:       zhangjingming
 * Date:         2015-12-23
 */

public abstract class BaseTask<T extends BaseRequest, V extends BaseResponse> implements com.android.volley.Response.Listener, com.android.volley.Response.ErrorListener {

    // 日志tag
    protected final String LOG_TAG = getClass().getSimpleName();

    // 请求消息体
    protected T request;

    // 请求ID
    private long requestId;

    // 请求是否可以被取消，默认可以取消请求
    private boolean cancelable = true;

    // 请求是否已被取消
    private boolean isCanceled = false;

    // 是否缓存
    private boolean shouldCached = false;

    // 网络请求队列
    private final static RequestQueue requestQueue = IApplication.getRequestQueue();

    // 响应消息监听对象
    private IResponseListener responseListener;

    // 超时时间
    private int timeOut = DEFAULT_TIMEOUT;

    // 重连次数
    private int retryCount = DEFAULT_RETRY_COUNT;

    // 默认超时时间
    private final static int DEFAULT_TIMEOUT = 15 * 1000;

    // 默认重连次数
    private final static int DEFAULT_RETRY_COUNT = 0;

    /**
     * 网络请求构造函数
     *
     * @param request  请求消息体
     * @param listener 响应消息监听对象
     */
    public BaseTask(T request, IResponseListener listener) {
        this.request = request;
        this.responseListener = listener;
        this.requestId = new Date().getTime(); // 请求ID为系统当前时间
        if (null != request) // 添加请求ID
            request.setReqId(this.requestId);
    }

    /**
     * 异步执行网络请求，通过listener返回响应结果
     */
    public void excute(Context context) {
        // 网络无连接，返回网络错误
        if (!NetworkKit.isNetworkAvailable(context) && !isCanceled && responseListener != null) {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_NETWORK, "");
            return;
        }

        String url = getURL();
        LogCat.i(LOG_TAG, "request url: %s", url);
        StringRequestEx stringRequestEx = new StringRequestEx(getURL(), this);
        stringRequestEx.setShouldCache(shouldCached);
        stringRequestEx.setRetryPolicy(new DefaultRetryPolicy(timeOut, retryCount, 0));
        requestQueue.add(stringRequestEx);
    }

    /**
     * 获取日志tag
     * @return 日志tag
     */
    public String getLogTag() {
        return LOG_TAG;
    }

    /**
     * 设置是否需要缓存
     *
     * @param shouldCached true:保存该次请求的response，下次请求时使用缓存 false:不缓存
     */
    public void setShouldCached(boolean shouldCached) {
        this.shouldCached = shouldCached;
    }

    /**
     * 设置超时时间，默认为15s
     *
     * @param timeOut 超时时间
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 设置重连次数，默认值为0
     *
     * @param retryCount 重连次数
     */
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * 取消当前网络请求，目前只是不传递网络请求数据给视图层
     * 只有cancelable=true才能成功
     */
    public void cancel() {
        if (isCancelable()) {
            isCanceled = true;
        }
    }

    /**
     * 设置是否可取消网络请求
     *
     * @param cancelable 是否可取消网络请求
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * 获取是否可取消网络请求
     *
     * @return true:可取消 false:不可取消
     */
    public boolean isCancelable() {
        return this.cancelable;
    }

    /**
     * 获取请求ID
     *
     * @return 请求ID
     */
    public long getRequestId() {
        return this.requestId;
    }

    /**
     * 检查任务和响应是否匹配
     *
     * @param response 响应消息体
     * @return true:适配 false:不适配
     */
    public boolean match(BaseResponse response) {
        if (null == response)
            return false;

        return (this.getRequestId() == response.getRequestId());
    }

    /**
     * 检查任务和响应是否匹配
     *
     * @param requestId 请求ID
     * @return true:适配 false:不适配
     */
    public boolean match(long requestId) {
        return (getRequestId() == requestId);
    }

    /**
     * 获取完整URL
     *
     * @return url
     */
    public String getURL() {
        return getBaseURL() + getURLPath();
    }

    private T getRequest() {
        return this.request;
    }

    /**
     * 网络请求响应，接收到网络数据
     *
     * @param response 响应对象
     */
    @Override
    public void onResponse(Object response) {
        LogCat.i(LOG_TAG, "response received");
        processResponse(response);
    }

    /**
     * 网络请求失败
     *
     * @param error 错误对象
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        if (null == responseListener || isCanceled)
            return;

        if (error instanceof ServerError) {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_SERVER, "");
        } else if (error instanceof ParseError) {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_PARSE, "");
        } else if (error instanceof NetworkError) {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_NETWORK, "");
        } else if (error instanceof TimeoutError) {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_TIMEOUT, "");
        } else {
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_DEFAULT, "");
        }
    }

    /**
     * 处理消息响应结果
     *
     * @param result 消息响应结果
     */
    private void processResponse(Object result) {
        if (null == responseListener || isCanceled)
            return;

        if (null == result) { // 如果结果为空，返回默认错误
            LogCat.e(LOG_TAG, "response result is null");
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_DEFAULT, "");
            return;
        }

        LogCat.d(LOG_TAG, "url: %s\n response: %s", getURL(), result.toString());
        V response = null;
        try {
            response = parseResponse(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == response) { // json解析后结果为空，返回解析错误
            LogCat.e(LOG_TAG, "response result parse error");
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_PARSE, "");
            return;
        }

        // 如果服务器没有返回requestId,填写客户端id
        if (0 == response.getRequestId())
            response.setRequestId(requestId);

        // 根据服务端响应码判断请求是否成功
        if (ResponseCode.RESPONSE_CODE_SUCCESS == response.getResponseCode()) { // 请求成功
            responseListener.onTaskSuccess(response);
        } else { //请求失败
            responseListener.onTaskError(requestId, response.getResponseCode(), response.getResponseMessage());
        }
    }

    /**
     * 子类实现解析网络响应结果方法
     *
     * @param response string类型响应结果
     * @return BaseResponse类型响应结果
     */
    protected abstract V parseResponse(String response) throws Exception;

    /**
     * 获取主机地址
     *
     * @return 主机地址
     */
    protected abstract String getBaseURL();

    /**
     * 获取路径地址
     *
     * @return 路径地址
     */
    protected abstract String getURLPath();

    /**
     * 构造请求消息体
     *
     * @param request 请求数据
     * @return 消息体
     */
    protected abstract Map<String, Object> buildMessage(final T request);

    /**
     * 构造请求报文头部
     *
     * @return 报文头
     */
    protected abstract Map<String, String> buildHeader();

    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    protected abstract Integer getMethod();

    /**
     * A canned request for retrieving the response body at a given URL as a String.
     */
    private static class StringRequestEx extends RequestEx<String> {
        private final BaseTask callback;
        private Map<String, Object> params;
        private MultipartEntity multipartEntity;

        // json数据类型
        private final static String JSON_CONTENT_TYPE = "application/json; charset=";

        /**
         * Creates a new request with the given method.
         *
         * @param url    URL to fetch the string at
         * @param callback 回调
         */
        public StringRequestEx(String url, BaseTask callback) {
            super(url, callback);
            this.callback = callback;
            this.params = callback.buildMessage(callback.getRequest());
        }

        @Override
        protected void deliverResponse(String response) {
            callback.onResponse(response);
        }

        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            String parsed;
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        public int getMethod() {
            Integer method = callback.getMethod();
            if (null != method)
                return method.intValue();
            else {
                if (null == params && null == callback.getRequest())
                    return Method.GET;
                else
                    return Method.POST;
            }
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            if (null == params) {
                String strJson = JSON.toJSONString(callback.getRequest());
                LogCat.d(callback.getLogTag(), strJson);
                try {
                    return strJson.getBytes(getParamsEncoding());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    callback.onErrorResponse(new ParseError());
                    return null;
                }
            }

            byte[] body = super.getBody();
            LogCat.i(callback.getLogTag(), "request body: %s", new String(body));
            return super.getBody();
        }

        @Override
        public InputStream getMultipartBody() throws AuthFailureError, IOException {
            if (null != params) {
                multipartEntity = new MultipartEntity();
                int currentIndex = 0;
                int lastIndex = params.entrySet().size() - 1;

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (null != entry) {
                        Object value = entry.getValue();
                        boolean isLast = currentIndex == lastIndex;
                        if (value instanceof String)
                            multipartEntity.addPart(entry.getKey(), (String) value, isLast);
                        else if (value instanceof File)
                            multipartEntity.addPart(entry.getKey(), ((File) value).getName(), new FileInputStream((File) value), isLast);
                        else if (value instanceof InputStream)
                            multipartEntity.addPart(entry.getKey(), "nofilename", (InputStream) value, isLast);
                        else
                            throw new AuthFailureError();
                    }
                    currentIndex++;
                }

                return multipartEntity.getContent();
            }
            return super.getMultipartBody();
        }


        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> tempParams = new ConcurrentHashMap<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (null != entry) {
                    Object value = entry.getValue();
                    if (value instanceof String)
                        tempParams.put(entry.getKey(), (String) value);
                    else
                        throw new AuthFailureError();
                }
            }
            return tempParams;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headMap = callback.buildHeader();
            return null == headMap ? super.getHeaders() : headMap;
        }

        @Override
        public String getBodyContentType() {
            if (null != multipartEntity)
                return multipartEntity.getContentType().getValue();

            if (null == params && null != callback.getRequest())
                return JSON_CONTENT_TYPE + getParamsEncoding();
            else
                return super.getBodyContentType();
        }
    }
}
