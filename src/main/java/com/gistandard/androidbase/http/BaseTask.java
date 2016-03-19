package com.gistandard.androidbase.http;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.gistandard.androidbase.utils.LogCat;
import com.gistandard.androidbase.utils.NetworkKit;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Description:http网络请求任务基类，只适用于非上传下载的普通网络请求
 * Name:         BaseTask
 * Author:       zhangjingming
 * Date:         2015-12-23
 */

public abstract class BaseTask<T extends BaseRequest, V extends BaseResponse> extends AjaxCallBack {

    // 日志tag
    protected final String LOG_TAG = this.getClass().getSimpleName();

    // 请求消息体
    protected T request;

    // 请求ID
    private long requestId;

    // 请求是否可以被取消，默认可以取消请求
    private boolean cancelable = true;

    // 请求是否已被取消
    private boolean isCanceled = false;

    // final http对象
    private final FinalHttp finalHttp = new FinalHttp();

    // 响应消息监听对象
    private IResponseListener responseListener;

    // 默认数据类型
    private final String DEFAULT_CONTENT_TYPE = "application/json";

    // 默认超时时间
    private final int DEFAULT_TIMEOUT = 15 * 1000;

    // 默认重连次数
    private final int DEFAULT_RETRY_COUNT = 0;

    /**
     * 网络请求构造函数
     * @param request 请求消息体
     * @param listener 响应消息监听对象
     */
    public BaseTask(T request, IResponseListener listener) {
        this.request = request;
        this.responseListener = listener;
        this.requestId = new Date().getTime(); // 请求ID为系统当前时间

        finalHttp.configCharset(HTTP.UTF_8); // 默认UTF-8编码
        finalHttp.configTimeout(DEFAULT_TIMEOUT); // 默认15s超时
        finalHttp.configRequestExecutionRetryCount(DEFAULT_RETRY_COUNT); // 默认0次重试
//        finalHttp.configSSLSocketFactory(); // 默认不使用ssl
        if (null != request) // 添加请求ID
            request.setReqId(this.requestId);
    }

    /**
     * 异步执行网络请求，通过listener返回响应结果
     */
    public void excute(Context context) {
        // 网络无连接，返回网络错误
        if(!NetworkKit.isNetworkAvailable(context) && !isCanceled && responseListener != null){
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_NETWORK, "");
            return;
        }

        // 根据网络请求类型获取服务器URL
        String url = getURL();
        LogCat.i(LOG_TAG, "request url: %s", url);

        buildHeader(finalHttp);
        AjaxParams params = buildMessage(request);
        if (null == params) {
            // 添加json数据到entity
            StringEntity entity = null;
            if (null != request) {
                String strJson = JSON.toJSONString(request);
                LogCat.d(LOG_TAG, strJson);
                try {
                    entity = new StringEntity(strJson, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_PARSE, "");
                    return;
                }
            }

            if (null == entity) // 如果没有参数，使用get请求
                finalHttp.get(url, this);
            else // 如果有参数，使用post请求
                finalHttp.post(url, entity, DEFAULT_CONTENT_TYPE, this);
        } else
            finalHttp.post(url, params, this);
    }

    /**
     * 同步执行网络请求，通过listener返回响应结果
     */
    public void excuteSync(Context context) {
        // 网络无连接，返回网络错误
        if(!NetworkKit.isNetworkAvailable(context) && !isCanceled && responseListener != null){
            responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_NETWORK, "");
            return;
        }

        // 根据网络请求类型获取服务器URL
        String url = getURL();
        LogCat.i(LOG_TAG, "request url: %s", url);

        Object result;
        buildHeader(finalHttp);
        AjaxParams params = buildMessage(request);
        if (null == params) {
            // 添加json数据到entity
            StringEntity entity = null;
            if (null != request) {
                String strJson = JSON.toJSONString(request);
                LogCat.d(LOG_TAG, strJson);
                try {
                    entity = new StringEntity(strJson, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_PARSE, "");
                    return;
                }
            }

            if (null == entity) // 如果没有参数，使用get请求
                result = finalHttp.getSync(url);
            else // 如果有参数，使用post请求
                result = finalHttp.postSync(url, entity, DEFAULT_CONTENT_TYPE);
        } else {
            result = finalHttp.postSync(url, params);
        }

        // 处理返回结果
        processResponse(result);
    }

    /**
     * 取消当前网络请求，目前只是不传递网络请求数据给视图层
     * 只有cancelable=true才能成功
     */
    public void cancel() {
        if (isCancelable())
            isCanceled = true;
    }

    /**
     * 设置是否可取消网络请求
     * @param cancelable 是否可取消网络请求
     */
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    /**
     * 获取是否可取消网络请求
     * @return true:可取消 false:不可取消
     */
    public boolean isCancelable() {
        return this.cancelable;
    }

    /**
     * 获取请求ID
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
     * @return url
     */
    public String getURL() {
        return getBaseURL() + getURLPath();
    }

    /**
     * 处理消息响应结果
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

        // 根据服务端响应码判断请求是否成功
        if (ResponseCode.RESPONSE_CODE_SUCCESS == response.getResponseCode()) { // 请求成功
            if (0 == response.getRequestId())
                response.setRequestId(requestId);
            responseListener.onTaskSuccess(response);
        } else { //请求失败
            responseListener.onTaskError(requestId, response.getResponseCode(), response.getResponseMessage());
        }
    }

    /**
     * final http回调，网络请求成功
     * @param t 响应结果
     */
    @Override
    public void onSuccess(Object t) {
        LogCat.i(LOG_TAG, "response success");
        processResponse(t);
    }

    /**
     * final http回调，网络请求失败
     * @param t 异常
     * @param errorNo 错误码
     * @param strMsg 错误信息
     */
    @Override
    public void onFailure(Throwable t, int errorNo, String strMsg) {
        LogCat.e(LOG_TAG, "errorNo: %d, errorMsg: %s", errorNo, strMsg);
        if (null != t && t instanceof UnknownHostException) {
            t.printStackTrace();
            if (null != responseListener && !isCanceled)
                responseListener.onTaskError(requestId, ResponseCode.RESPONSE_ERROR_SERVER, "");
        } else
            processResponse(null);
    }

    /**
     * 子类实现解析网络响应结果方法
     * @param response string类型响应结果
     * @return BaseResponse类型响应结果
     */
    protected abstract V parseResponse(String response) throws Exception;

    /**
     * 获取主机地址
     * @return 主机地址
     */
    protected abstract String getBaseURL();

    /**
     * 获取路径地址
     * @return 路径地址
     */
    protected abstract String getURLPath();

    /**
     * 构造请求消息体
     * @param request 请求数据
     * @return 消息体
     */
    protected abstract AjaxParams buildMessage(final T request);

    /**
     * 构造请求报文头部
     * @param finalHttp finalHttp实体
     */
    protected abstract void buildHeader(final FinalHttp finalHttp);
}
