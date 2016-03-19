package com.gistandard.androidbase.http;

/**
 * Description:抽象请求基类
 * Name:         BaseRequest
 * Author:       zhangjingming
 * Date:         2016-01-21
 */

public abstract class BaseRequest {
    private long reqId;//请求ReqId

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }
}
