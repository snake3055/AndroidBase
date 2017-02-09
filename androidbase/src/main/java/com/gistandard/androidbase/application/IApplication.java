package com.gistandard.androidbase.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.gistandard.androidbase.http.extension.HurlStackEx;
import com.gistandard.androidbase.http.extension.VolleyEx;
import com.gistandard.androidbase.utils.LogCat;
import com.gistandard.androidbase.utils.SPUtils;
import com.gistandard.androidbase.utils.ToastUtils;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Description:应用程序基类
 * Name:         IApplication
 * Author:       zhangjingming
 * Date:         2016-03-21
 */

public class IApplication extends Application {
    /**
     * 日志tag
     */
    protected final static String LOG_TAG = IApplication.class.getSimpleName();

    private static RequestQueue requestQueue;
    private static Application application;

    /**
     * 初始化函数，初始化默认工具依赖类
     */
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        if (!isMainProcess())
            return;

        ToastUtils.init(this);  // 初始化默认Toast
        SPUtils.initPreferences(this); // 初始化默认本地存储
        initRequestQueue();
    }

    /**
     * 获取网络请求队列
     * @return 网络请求队列
     */
    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * 获取全局context
     * @return 全局context
     */
    public static Application getApplication() {
        return application;
    }

    /**
     * 获取https keystore
     * @return keystore
     */
    public KeyStore getKeyStore() {
        return null;
    }

    /**
     * 是否是主进程
     *
     * @return
     */
    protected boolean isMainProcess() {
        String mainProcessName = getPackageName();
        String processName = getProcessName();
        return TextUtils.equals(processName, mainProcessName);
    }

    /**
     * 获取进程名称
     *
     * @return
     */
    private String getProcessName() {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }

    private void initRequestQueue() {
        // 初始化volley请求队列，全局唯一
        if (null == getKeyStore())
            requestQueue = VolleyEx.newRequestQueue(this);
        else
            try {
                requestQueue = VolleyEx.newRequestQueue(this, initClientStack(getKeyStore()));
            } catch (Exception e) {
                LogCat.e(LOG_TAG, "**** Volley request queue init fail ****");
                e.printStackTrace();
            }
    }

    private static HurlStackEx initClientStack(KeyStore keyStore) throws Exception {
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), new SecureRandom());
        return new HurlStackEx(null, context.getSocketFactory());
    }
}
