package com.gistandard.androidbase.http.extension;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:volley请求扩展类
 * Name:         RequestEx
 * Author:       zhangjingming
 * Date:         2016-05-22
 */

public abstract class RequestEx<T> extends Request<T> {

    /**
     * 扩展基类Method,添加上传下载方法
     */
    public interface MethodEx extends Method {
        // 上传
        int Upload = 8;
        // 下载，暂不支持
        int Download = 9;
    }

    /**
     * 构造函数
     * @param url
     * @param listener
     */
    public RequestEx(String url, Response.ErrorListener listener) {
        super(url, listener);
    }

    /**
     * 构造函数
     * @param method
     * @param url
     * @param listener
     */
    public RequestEx(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    /**
     * 获取包内容
     * @return 包内容
     * @throws AuthFailureError
     * @throws IOException
     */
    public InputStream getMultipartBody() throws AuthFailureError, IOException {
        return null;
    }
}
