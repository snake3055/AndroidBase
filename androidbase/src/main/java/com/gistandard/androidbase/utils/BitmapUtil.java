package com.gistandard.androidbase.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by mabing on 16/1/13.
 */
public class BitmapUtil {
//    覆盖层六边形使用
    public static  Bitmap combineImages(Bitmap bgd, Bitmap fg) {
        Bitmap bmp;
        int width = bgd.getWidth() > fg.getWidth() ?
                bgd.getWidth() : fg.getWidth();
        int height = bgd.getHeight() > fg.getHeight() ?
                bgd.getHeight() : fg.getHeight();

        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, paint);
        return bmp;
    }
}
