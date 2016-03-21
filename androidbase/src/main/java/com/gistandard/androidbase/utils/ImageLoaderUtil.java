package com.gistandard.androidbase.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * public static DisplayImageOptions options = new
 * DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_circle_def) // 设置图片在下载期间显示的图片
 * .showImageForEmptyUri(R.drawable.ic_circle_def)// 设置图片Uri为空或是错误的时候显示的图片
 * .showImageOnFail(R.drawable.ic_circle_def) // 设置图片加载/解码过程中错误时候显示的图片
 * .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
 * .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
 * .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
 * .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
 * .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
 * .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
 * .displayer(new RoundedBitmapDisplayer(1000, 0))// 是否设置为圆角，弧度为多少
 * .build();// 构建完成;
 */
public class ImageLoaderUtil {


    private static final String TAG = "ImageLoader";


    public static DisplayImageOptions optionsRect = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.jx_default).showImageForEmptyUri(R.drawable.jx_default)// 设置图片Uri为空或是错误的时候显示的图片
//            .showImageOnFail(R.drawable.jx_default) // 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
            .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
            .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
            .build();// 构建完成;


    public static DisplayImageOptions optionsPhoto = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.drawable.custom_photo_default).showImageForEmptyUri(R.drawable.custom_photo_default)// 设置图片Uri为空或是错误的时候显示的图片
//            .showImageOnFail(R.drawable.custom_photo_default) // 设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
            .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
            .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
            .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
            .displayer(new RoundedBitmapDisplayer(1000, 0))// 是否设置为圆角，弧度为多少
            .build();// 构建完成;


    public static DisplayImageOptions noResetThumOptions = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(false).cacheOnDisk(false)
            .imageScaleType(ImageScaleType.EXACTLY).build();

    /**
     * 新方式-防止oom
     */
    public static void init(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory().memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13).diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator())// .writeDebugLogs()
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);
    }

    public static void displayImage(String url, ImageView iv) {
        ImageLoader.getInstance().displayImage(url, iv, optionsRect);
    }

    public static void displayImg(String url, ImageView iv) {
        ImageLoader.getInstance().displayImage(url, iv, noResetThumOptions);
    }


    public static Boolean isCache(String key) {
        if (ImageLoader.getInstance().getDiskCache().get(key) == null) {
            return false;
        }
        return ImageLoader.getInstance().getDiskCache().get(key).exists();
    }

    public static void displayImage(String url, ImageView iv, DisplayImageOptions option) {
        ImageLoader.getInstance().displayImage(url, iv, option);
    }

    public static void displayPhoto(String url, ImageView iv) {
        ImageLoader.getInstance().displayImage(url, iv, optionsPhoto);
    }

    public static void stop() {
        try {
            ImageLoader.getInstance().stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        try {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
