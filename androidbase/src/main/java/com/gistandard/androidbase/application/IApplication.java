package com.gistandard.androidbase.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.gistandard.androidbase.http.extension.VolleyEx;
import com.gistandard.androidbase.utils.SPUtils;
import com.gistandard.androidbase.utils.ToastUtils;

/**
 * Description:应用程序基类
 * Name:         IApplication
 * Author:       zhangjingming
 * Date:         2016-03-21
 */

public class IApplication extends Application {

    private static RequestQueue requestQueue;

    /**
     * 初始化函数，初始化默认工具依赖类
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);  // 初始化默认Toast
        SPUtils.initPreferences(this); // 初始化默认本地存储
        requestQueue = VolleyEx.newRequestQueue(this); // 初始化volley请求队列，全局唯一
    }

    /**
     * 获取网络请求队列
     * @return 网络请求队列
     */
    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
