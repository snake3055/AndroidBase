package com.gistandard.androidbase.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Service Util
 * Created by zhouwenwu on 16/1/30.
 */
public class ServiceUtil {
    /**
     * 判断Service 是否在运行
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
