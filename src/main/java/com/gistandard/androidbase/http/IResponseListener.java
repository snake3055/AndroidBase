package com.gistandard.androidbase.http;

import com.gistandard.androidbase.http.BaseResponse;

/**
 * Description:服务端响应回调接口
 * Name:         IResponseListener
 * Author:       zhangjingming
 * Date:         2015-12-23
 */

public interface IResponseListener {

    /**
     * 服务端返回响应码成功回调接口
     *
     * @param @param response
     * @return void
     */
    public void onTaskSuccess(BaseResponse response);

    /**
     * 服务端返回错误回调接口
     * @param requestId 请求ID
     * @param responseCode 错误码
     * @param responseMsg 错误信息
     */
    public void onTaskError(long requestId, int responseCode, String responseMsg);
}
