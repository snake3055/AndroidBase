package com.gistandard.androidbase.http;

import android.content.Context;
import android.text.TextUtils;

import com.gistandard.androidbase.R;
import com.gistandard.androidbase.utils.LogCat;

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

    /**
     * 获取系统错误信息
     * @param context 上下文
     * @param responseCode 响应码
     * @param responseMsg 响应消息
     * @return
     */
    public static String getErrorMessage(Context context, int responseCode, String responseMsg) {
        if (!TextUtils.isEmpty(responseMsg) || RESPONSE_CODE_SUCCESS == responseCode)
            return responseMsg;

        if (null == context) {
            LogCat.e("ResponseCode", "==== Context invalid ====");
            return "";
        }

        switch (responseCode) {
            case RESPONSE_ERROR_NETWORK:
                return context.getString(R.string.error_network);


            case RESPONSE_ERROR_PARSE:
                return context.getString(R.string.error_parse);

            case RESPONSE_ERROR_SERVER:
                return context.getString(R.string.error_server);

            case RESPONSE_ERROR_DEFAULT:
            default:
                return context.getString(R.string.error_system);
        }
    }
}
