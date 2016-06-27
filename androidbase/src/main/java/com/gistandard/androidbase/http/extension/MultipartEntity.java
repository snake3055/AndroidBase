package com.gistandard.androidbase.http.extension;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Description:分包实体类，转载自apache.http包
 * Name:         MultipartEntity
 * Author:       zhangjingming
 * Date:         2016-05-22
 */

public class MultipartEntity implements HttpEntity {
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String boundary = null;

    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private boolean isSetLast = false;
    private boolean isSetFirst = false;

    /**
     * 构造函数
     */
    public MultipartEntity() {
        final StringBuffer buf = new StringBuffer();
        final Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();

    }

    /**
     * 输出第一个分割符
     */
    public void writeFirstBoundaryIfNeeds() {
        if (!isSetFirst) {
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        isSetFirst = true;
    }

    /**
     * 输出中间分隔符
     */
    public void writeMiddleBoundary() {
        try {
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出最后一个分隔符
     */
    public void writeLastBoundaryIfNeeds() {
        if (isSetLast) {
            return;
        }

        try {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        isSetLast = true;
    }

    /**
     * 添加String包内容
     * @param key 键值
     * @param value 内容
     */
    public void addPart(final String key, final String value, boolean isLast) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            if (isLast)
                writeLastBoundaryIfNeeds();
            else
                writeMiddleBoundary();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文件包内容
     * @param key 键值
     * @param fileName 文件名
     * @param fin 输入流
     * @param isLast 是否最后一个
     */
    public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast) {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    /**
     * 添加文件包内容
     * @param key 键值
     * @param fileName 文件名
     * @param fin 输入流
     * @param type 类型
     * @param isLast 是否最后一个
     */
    public void addPart(final String key, final String fileName, final InputStream fin, String type, final boolean isLast) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            final byte[] tmp = new byte[4096];
            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            if (isLast)
                writeLastBoundaryIfNeeds();
            else
                writeMiddleBoundary();
            out.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加文件包内容
     * @param key 键值
     * @param value 文件句柄
     * @param isLast 是否最后一个
     */
    public void addPart(final String key, final File value, final boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取包长度
     * @return 包长度
     */
    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    /**
     * 获取包类型
     * @return
     */
    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    /**
     * 是否分块传输
     * @return 是/否
     */
    @Override
    public boolean isChunked() {
        return false;
    }

    /**
     * 是否重复传输
     * @return 是否
     */
    @Override
    public boolean isRepeatable() {
        return false;
    }

    /**
     * 是否流传输
     * @return 是否
     */
    @Override
    public boolean isStreaming() {
        return false;
    }

    /**
     * 写入方法
     * @param outstream 输出流
     * @throws IOException
     */
    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        outstream.write(out.toByteArray());
    }

    /**
     * 获取内容编码方式
     * @return 内容编码方式
     */
    @Override
    public Header getContentEncoding() {
        return null;
    }

    /**
     * 传输
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    @Override
    public void consumeContent() throws IOException,
            UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
                    "Streaming entity does not implement #consumeContent()");
        }
    }

    /**
     * 获取包内容
     * @return 包内容
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    @Override
    public InputStream getContent() throws IOException,
            UnsupportedOperationException {
        return new ByteArrayInputStream(out.toByteArray());
    }
}
