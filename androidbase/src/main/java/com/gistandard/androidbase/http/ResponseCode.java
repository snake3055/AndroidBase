package com.gistandard.androidbase.http;

/**
 * Description:本地网络请求响应状态码
 * Name:         ResponseCode
 * Author:       zhangjingming
 * Date:         2015-12-24
 */

public class ResponseCode {

    // 成功状态码
    public static final int RESPONSE_CODE_SUCCESS = 1;

    // 默认错误码
    public static final int RESPONSE_ERROR_DEFAULT = 0x1000;

    // 服务器错误码
    public static final int RESPONSE_ERROR_SERVER = 0x1001;

    // 数据解析错误码
    public static final int RESPONSE_ERROR_PARSE = 0x1002;

    // 网络连接错误码
    public static final int RESPONSE_ERROR_NETWORK = 0x1003;
}
