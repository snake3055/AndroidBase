package com.gistandard.androidbase.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.Toast;


/**
 * toast弹出窗类，项目的toast弹出框必须从这里开始
 * <br>不允许自行实例化
 *
 * @author Danel
 */
@SuppressLint("ShowToast")
public final class ToastUtils {
    static Toast mToast = null;
    static Context context = null;

    /**
     * 初始化toast context
     *
     * @param context
     */
    public static void init(Context context) {
        ToastUtils.context = context;
    }

    /**
     * 长时间气泡弹出
     *
     * @param message
     */
    public static void toastLong(String message) {
        getToast();
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 长时间气泡弹出
     *
     * @param resId
     */
    public static void toastLong(int resId) {
        getToast();
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 改变位置，长时间显示气泡
     *
     * @param message
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void toastLong(String message, int gravity, int xOffset, int yOffset) {
        getToast();
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    /**
     * 自定义view长时间显示气泡
     *
     * @param view
     */
    public static void toastLong(View view) {
        getToast();
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    /**
     * 改变位置，自定义View长时间显示气泡
     *
     * @param view
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void toastLong(View view, int gravity, int xOffset, int yOffset) {
        getToast();
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    /**
     * 短时间弹出气泡
     *
     * @param message
     */
    public static void toastShort(String message) {
        getToast();
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 短时间弹出气泡
     *
     * @param resId
     */
    public static void toastShort(int resId) {
        getToast();
        mToast.setText(resId);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 改变位置，短时间显示气泡
     *
     * @param message
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void toastShort(String message, int gravity, int xOffset, int yOffset) {
        getToast();
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(gravity, xOffset, yOffset);
        mToast.show();
    }

    /**
     * 自定义view短时间显示气泡
     *
     * @param view
     */
    public static void toastShort(View view) {
        getToast();
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 改变位置，自定义View短时间显示气泡
     *
     * @param view
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void toastShort(View view, int gravity, int xOffset, int yOffset) {
        getToast();
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 获得一个气泡的实例
     *
     * @return
     */
    public static Toast getToast() {
        if (null != mToast) {
            mToast.cancel();
        }
        mToast = null;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        return mToast;
    }

}
