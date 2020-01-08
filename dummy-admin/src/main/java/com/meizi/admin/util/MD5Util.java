package com.meizi.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public final class MD5Util {

    private static Logger logger = LoggerFactory.getLogger(MD5Util.class);

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',  '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String encrypt(String str) {

        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(str.getBytes("UTF-8"));
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException NsEx) {
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的字符编码类型" + e);
            return null;
        }
    }

    private static String byte2hex(byte[] bstr) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < bstr.length; n++) {
            stmp = (Integer.toHexString(bstr[n] & 0XFF));
            if (stmp.length() == 1){
                hs.append("0");
                hs.append(stmp);
            }else{
                hs.append(stmp);
            }
        }
        return hs.toString();
    }


    /**
     * 对字节数组md5加密
     * @param bytes
     * @return
     */
    public static String getMD5String(byte[] bytes) {
        try {
            MessageDigest alg = MessageDigest.getInstance("MD5");
            alg.update(bytes);
            return bufferToHex(alg.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error("md5 encript byte[] error:"+e.getMessage());
            return null;

        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            char c0 = hexDigits[(bytes[l] & 0xf0) >> 4];
            char c1 = hexDigits[bytes[l] & 0xf];
            stringbuffer.append(c0);
            stringbuffer.append(c1);
        }
        return stringbuffer.toString();
    }
}
