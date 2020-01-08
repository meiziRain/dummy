package com.meizi.admin.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @ClassName CipherUtils
 * @Description Cipher工具类
 * @Author Sam
 * @Date 2019/8/8 15:09
 * @Version 1.0
 */
class CipherUtils {

    /**
     *  文本转字节数组
     * @param text  文本内容
     * @return      字节数组
     */
    public static byte[] toBytes(String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    /**
     *  字节数组转文本
     * @param bytes 字节数组
     * @return      文本内容
     */
    public static String toText(byte[] bytes) {
        String text = new String(bytes, StandardCharsets.UTF_8);
        return text;
    }

    /**
     *  对文本内容进行base64 decode
     * @param text  文本
     * @return      decode后的字节数组
     */
    public static byte[] decode(String text) {
        byte[] decode = Base64.getDecoder().decode(text);
        return decode;
    }

    /**
     *  对字节数组进行base64 encode
     * @param bytes 字节数组
     * @return      encode后的文本
     */
    public static String encode(byte[] bytes) {
        String encode = Base64.getEncoder().encodeToString(bytes);
        return encode;
    }

}