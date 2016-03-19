
package com.gistandard.androidbase.utils.storage;

/**
 * SDNotEnouchSpaceException
 * sd card存储空间不够时抛出此异常
 * @author  zww
 */
public class SDNotEnouchSpaceException extends Exception {
    private String msg = null;

    /**
     * 构造方法
     * 
     * @param msg 异常信息
     */
    public SDNotEnouchSpaceException(String msg) {
        this.msg = msg;
    }

    /**
     * 重写的方法，获取异常信息
     * 
     * @return 异常信息
     */
    @Override
    public String getMessage() {
        return msg;
    }
}
