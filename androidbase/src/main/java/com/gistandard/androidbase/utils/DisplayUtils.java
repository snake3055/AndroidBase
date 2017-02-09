package com.gistandard.androidbase.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * dp、sp 转换为 px 的工具类
 *
 * @author fxsky 2012.11.12
 */
public class DisplayUtils {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context Activity 上下文
     * @return int[] 长度为2
     */
    public static int[] getScreenSize(Context context) {
        int[] size = new int[2];
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        size[0] = metric.widthPixels;
        size[1] = metric.heightPixels;
        return size;
    }

    /**
     * 获取屏幕亮度
     * @param activity 上下文
     * @return 亮度值
     */
    public static float getScreenBrightness(Activity activity) {
        float value = 0;
        ContentResolver cr = activity.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            return value / 100;
        } catch (Settings.SettingNotFoundException e) {
            return 0.6f;
        }

    }

    /**
     * 设置当前窗口的亮度（activity）
     *
     * @param context    上下文
     * @param brightness 亮度值(0~1.0)
     */
    public static void setActivityBrightness(Activity context, float brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = brightness;
        context.getWindow().setAttributes(lp);
    }

    /**
     * 保持屏幕常亮
     *
     * @param context 上下文
     * @return PowerManager.WakeLock
     */
    public static PowerManager.WakeLock KeepScreenOn(Context context) {
        PowerManager manager = ((PowerManager) context.getSystemService(Context.POWER_SERVICE));
        PowerManager.WakeLock wakeLock =
                manager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE, "ATAAW");
        wakeLock.acquire();
        return wakeLock;
    }

    /**
     * 获取手机DPI
     * @param activity 上下文
     * @return DIP值
     */
    public static String getPhoneDpi(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        //ldpi  	0.75 倍
        //mdi		1	倍
        //hdpi		1.5	倍
        //xhdpi		2	倍
        //xxhdpi	3	倍
        //xxxhpdi	4	倍
        String dpiStr = "mdpi"; //默认mdpi值
        if (0.75 == density)
            dpiStr = "mdpi";
        else if (1 == density)
            dpiStr = "mdpi";
        else if (1.5 == density)
            dpiStr = "hdpi";
        else if (2 == density)
            dpiStr = "xhdpi";
        else if (3 == density)
            dpiStr = "xxhdpi";
        else if (4 == density)
            dpiStr = "xxhdpi";
        return dpiStr;
    }

    /**
     * 获取状态栏高度
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        if (null == context)
            return 0;

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
