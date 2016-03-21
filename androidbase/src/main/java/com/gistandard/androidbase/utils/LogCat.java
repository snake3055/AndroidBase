package com.gistandard.androidbase.utils;

import android.util.Log;

import java.util.Locale;

/**
 * Description: 日志类，App中所有日志输出必须统一调用该类，不可使用系统Log方法
 *              日志输出格式  class.method[line]: message
 * Name:         LogCat
 * Author:       zhangjingming
 * Date:         2015-12-22
 */

public class LogCat {

    /**
     * 是否输出日志，默认不输出
     * true: 输出日志
     * false: 不输出日志
     */
    private static boolean outputLog = true;

    /**
     * 是否输出日志，默认不输出
     * @param output true: 输出日志 false: 不输出日志
     */
    public static void setOutputLog(boolean output) {
        outputLog = output;
    }

    /**
     * v方法
     * @param TAG 标记
     * @param format 格式
     * @param args 参数
     */
    public static void v(String TAG, String format, Object... args) {
        if (outputLog)
            Log.v(TAG, buildMessage(format, args));
    }

    /**
     * d方法
     * @param TAG 标记
     * @param format 格式
     * @param args 参数
     */
    public static void d(String TAG, String format, Object... args) {
        if (outputLog)
            Log.d(TAG, buildMessage(format, args));
    }

    /**
     * i方法
     * @param TAG 标记
     * @param format 格式
     * @param args 参数
     */
    public static void i(String TAG, String format, Object... args) {
        if (outputLog)
            Log.i(TAG, buildMessage(format, args));
    }

    /**
     * w方法
     * @param TAG 标记
     * @param format 格式
     * @param args 参数
     */
    public static void w(String TAG, String format, Object... args) {
        if (outputLog)
            Log.w(TAG, buildMessage(format, args));
    }

    /**
     * e方法，不记录异常
     * @param TAG 标记
     * @param format 格式
     * @param args 参数
     */
    public static void e(String TAG, String format, Object... args) {
        if (outputLog)
            Log.e(TAG, buildMessage(format, args));
    }

    /**
     * e方法，记录异常
     * @param TAG
     * @param tr
     * @param format
     * @param args
     */
    public static void e(String TAG, Throwable tr, String format, Object... args) {
        if (outputLog)
            Log.e(TAG, buildMessage(format, args), tr);
    }

    /**
     * wtf方法，不记录异常
     * @param TAG
     * @param format
     * @param args
     */
    public static void wtf(String TAG, String format, Object... args) {
        if (outputLog)
            Log.wtf(TAG, buildMessage(format, args));
    }

    /**
     * wtf方法，记录异常
     * @param TAG
     * @param tr
     * @param format
     * @param args
     */
    public static void wtf(String TAG, Throwable tr, String format, Object... args) {
        if (outputLog)
            Log.wtf(TAG, buildMessage(format, args), tr);
    }

    /**
     * 构造日志信息，输出格式class.method[line]: message
     * @param format
     * @param args
     * @return 日志信息
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        int lineNum = -1;
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(LogCat.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                lineNum = trace[i].getLineNumber();
                break;
            }
        }
        return String.format(Locale.US, "%s[%d]: %s", caller, lineNum, msg);
    }
}
