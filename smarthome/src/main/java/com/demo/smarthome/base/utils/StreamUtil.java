package com.demo.smarthome.base.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    /**
     * 将流转化成字符串
     * 
     * @param is
     *            输入流
     * @param encode
     *            转换成字符串的编码格式，默认UTF-8
     * @return
     * @throws IOException
     */
    public static String stream2String(InputStream is, String encode) throws IOException {
        if (encode == null || "".equals(encode.trim())) {
            encode = "UTF-8";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        is.close();
        bos.close();
        return bos.toString(encode);
    }
}
