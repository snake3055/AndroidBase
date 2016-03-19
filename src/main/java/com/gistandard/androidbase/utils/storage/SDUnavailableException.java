/**
 * 文 件 名:  SDUnavailableException.java
 * 版    权:  Caredear Communication Technologies Co., Ltd. Copyright 2011-2013,  All rights reserved
 * 描    述:  sd卡空间不足时抛出的异常
 * 修 改 人:  zww
 * 修改时间:  2014-1-21
 * 修改内容:  <修改内容>
 */

package com.gistandard.androidbase.utils.storage;

/**
 * sd卡不可用时抛出的异常
 * @author zww
 */
public class SDUnavailableException extends Exception {
    private String msg = null;

    /**
     * 构造方法
     * 
     * @param msg 异常信息
     */
    public SDUnavailableException(String msg) {
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
