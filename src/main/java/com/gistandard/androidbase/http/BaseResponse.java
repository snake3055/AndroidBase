package com.gistandard.androidbase.http;

/**
 * Description:抽象响应基类
 * Name:         BaseResponse
 * Author:       zhangjingming
 * Date:         2016-01-21
 */

public abstract class BaseResponse {

    protected long requestId;

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getRequestId() {
        return this.requestId;
    }

    public abstract int getResponseCode();

    public abstract void setResponseCode(int responseCode);

    public abstract String getResponseMessage();

    public abstract void setResponseMessage(String responseMessage);
}
