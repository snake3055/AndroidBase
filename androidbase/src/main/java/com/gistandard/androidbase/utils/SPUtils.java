package com.gistandard.androidbase.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Description:应用程序本地缓存操作类
 * Name:         SPUtils
 * Author:       zhangjingming
 * Date:         2015-12-23
 */

public class SPUtils {
    /**
     * 本地缓存对象
     */
    private static SharedPreferences sharedPreferences;

    /**
     * 获取本地缓存对象
     * @return SharedPreferences
     */
    public static void initPreferences(Context context) {
        if (null == sharedPreferences)
            sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                    Context.MODE_PRIVATE);
    }

    /**
     * 获取本地缓存的字符串
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 获取本地缓存的字符串集合
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static Set<String> getStringSet(String key, Set<String> defValue) {
        return sharedPreferences.getStringSet(key, defValue);
    }

    /**
     * 获取本地缓存的整型数据
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * 获取本地缓存的长整型数据
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static long getLong(String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * 获取本地缓存的浮点数
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static float getFloat(String key, float defValue) {
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * 获取本地缓存的boolean数据
     * @param key 关键字
     * @param defValue 如果没有本地数据，返回默认值
     * @return 本地缓存数据
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 保存字符串到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 保存字符串集合到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putStringSet(String key, Set<String> value) {
        sharedPreferences.edit().putStringSet(key, value).commit();
    }

    /**
     * 保存整型数据到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 保存长整型数据到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 保存浮点数到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    /**
     * 保存boolean型数据到本地缓存
     * @param key 关键字
     * @param value 保存的数据
     */
    public static void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 移除本地保存的数据
     * @param key 关键字
     */
    public static void remove(String key) {
        sharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清空本地保存的所有数据
     */
    public static void clear() {
        sharedPreferences.edit().clear().commit();
    }
}
