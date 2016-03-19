
package com.gistandard.androidbase.utils.storage;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;


/**
 * SD card util
 * @author  zww
 */
public class SDCardUtil {

    /**
     * 获取SDCard根目录
     * @return
     */
    public static String getSDCardRootPath(){
        File sdDir = null;
        if(checkSDCardAvaliable())
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 检查SD卡是否存在
     *
     * @return boolean
     */
    public static boolean checkSDCardAvaliable() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    /**
     * 对sdcard的检查，主要是检查sd是否可用，并且sd卡的存储空间是否充足
     *
     * @param io 写入sd卡的数据
     * @throws SDUnavailableException
     * @throws SDNotEnouchSpaceException
     */
    public static void checkSD(byte[] io) throws SDUnavailableException, SDNotEnouchSpaceException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new SDUnavailableException("sd_Unavailable");
        } else {
            if (io.length >= getFreeSD()) {
                // 通知UI
                throw new SDNotEnouchSpaceException("sd_NotEnoughSpace");
            }
        }
    }

    /**
     * 对sdcard的检查，主要是检查sd是否可用，并且sd卡的存储空间是否充足
     *
     * @param streamLength 数据的长度
     * @throws SDUnavailableException
     * @throws SDNotEnouchSpaceException
     */
    public static void checkSD(int streamLength) throws SDUnavailableException,
            SDNotEnouchSpaceException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new SDUnavailableException("sd_Unavailable");
        } else {
            if (streamLength >= getFreeSD()) {
                // 通知UI
                throw new SDNotEnouchSpaceException("sd_NotEnoughSpace");
            }
        }
    }

    /**
     * 获取SD卡的剩余空间
     *
     * @return SD卡的剩余的字节数
     */
    public static long getFreeSD() {
        long nAvailableCount = 0l;
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
            nAvailableCount = (long) (stat.getBlockSize() * ((long) stat.getAvailableBlocks()));
        } catch (Exception e) {
        }
        return nAvailableCount;
    }


}
