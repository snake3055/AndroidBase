package com.gistandard.bintrayapp;

import com.gistandard.androidbase.http.BaseRequest;

/**
 * Description:即时通讯登录请求
 * Name:         IMLoginRequest
 * Author:       zhangjingming
 * Date:         2016-01-30
 */

public class IMLoginRequest extends BaseRequest {

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPscId() {
        return pscId;
    }

    public void setPscId(String pscId) {
        this.pscId = pscId;
    }

    private String account;
    private String pscId;


}