package com.gistandard.androidbase.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Description:获取设备信息类
 * Name:         PhoneInfo
 * Author:       zhangjingming
 * Date:         2015-12-23
 */

public class PhoneInfo {

    /**
     * 获取设备Android uid
     * @param paramContext 应用程序上下文
     * @return id
     */
    public static String getAndroidId(Context paramContext) {
        return Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
    }

    /**
     * 获取手机MAC地址，只有手机开启wifi才能获取到mac地址
     * @param paramContext  应用程序上下文
     * @return 手机MAC地址
     */
    public static String getLocalMacAddress(Context paramContext) {
        String str = ((WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE))
                .getConnectionInfo().getMacAddress();
        if ((str == null) || ("".equals(str))) {
            return paramContext.getSharedPreferences("mac_address", 0).getString("mac_address", "");
        }
        SharedPreferences.Editor localEditor = paramContext.getSharedPreferences("mac_address", 0)
                .edit();
        localEditor.putString("mac_address", str);
        localEditor.apply();
        return str;
    }

    /**
     * 获取手机IMEI
     * @param paramContext 应用程序上下文
     * @return 手机IMEI
     */
    public static String getOriginalIMEI(Context paramContext) {
        String str = ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        if (str != null) {
            str = str.trim();
        }
        return str;
    }

    /**
     * 获取手机sim卡的序列号（IMSI）
     * @param paramContext 应用程序上下文
     * @return sim卡的序列号
     */
    public static String getOriginalIMSI(Context paramContext) {
        String str = ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE))
                .getSubscriberId();
        if (str != null) {
            str = str.trim();
        }
        return str;
    }

    /**
     * 判断是否为平板设备
     * @param paramContext 应用程序上下文
     * @return true:平板
     *         false:手机
     */
    public static boolean isTablet(Context paramContext) {
        WindowManager wm = (WindowManager) paramContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于7寸则为tablet
        if (screenInches >= 7.0) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机型号
     * @return 手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     * @return 手机品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机系统版本
     * @return 手机系统版本
     */
    public static String getVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取唯一设备ID
     * @param context
     * @return
     */
    public static String getDeviceKey(Context context) {
        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String androidId, tmDevice;
        tmDevice = String.valueOf(tm.getDeviceId());
        androidId = String.valueOf(android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID));
        UUID deviceUuid = new UUID(androidId.hashCode(), (long) tmDevice.hashCode());
        return deviceUuid.toString();
    }

}
