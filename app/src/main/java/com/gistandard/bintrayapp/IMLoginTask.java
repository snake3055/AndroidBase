package com.gistandard.bintrayapp;

import com.alibaba.fastjson.JSONObject;
import com.gistandard.androidbase.http.BaseResponse;
import com.gistandard.androidbase.http.BaseTask;
import com.gistandard.androidbase.http.IResponseListener;
import com.gistandard.androidbase.utils.MD5Util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:即时通讯登录任务
 * Name:         IMLoginTask
 * Author:       zhangjingming
 * Date:         2016-01-30
 */

public class IMLoginTask extends BaseTask<IMLoginRequest, BaseResponse> {

    /**
     * 网络请求构造函数
     *
     * @param request  请求消息体
     * @param listener 响应消息监听对象
     */
    public IMLoginTask(IMLoginRequest request, IResponseListener listener) {
        super(request, listener);
    }

    @Override
    protected BaseResponse parseResponse(String response) {
        return JSONObject.parseObject(response, BaseResponse.class);
    }

    @Override
    protected String getBaseURL() {
        return "http://172.16.2.141:8089/vlep_im";
    }

    @Override
    protected String getURLPath() {
        return "/userController/planLogin.do";
    }

    @Override
    protected Map<String, Object> buildMessage(IMLoginRequest request) {
        if (null == request)
            return null;

        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("appTag", "0001");
        params.put("isPush", "1");
        params.put("pscId", "43452");
        params.put("deviceToken", "00000000-4fd6-faa4-0000-0000782b42d1");
        params.put("deviceType", "3");
        params.put("account", "CN0075519630405000001");
        return params;
    }

    @Override
    protected Map<String, String> buildHeader() {
        Map<String, String> headerMap = new ConcurrentHashMap<>();
        // 获取报头参数
        long timeStamp = new Date().getTime();
        String hashData = "000000" + timeStamp;
        hashData = MD5Util.calMd5(hashData);

        // 设置报头
        headerMap.put("HASHDATA", hashData);
        headerMap.put("TIMESTAMP", String.valueOf(timeStamp));
        headerMap.put("TOKEN", "000000");
        headerMap.put("APPTAG", "0001");
        return headerMap;
    }

    @Override
    protected Integer getMethod() {
        return null;
    }

}
