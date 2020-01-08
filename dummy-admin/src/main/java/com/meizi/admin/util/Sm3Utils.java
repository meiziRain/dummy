package com.meizi.admin.util;

import org.bouncycastle.crypto.digests.SM3Digest;

/**
 * @ClassName Sm3Utils
 * @Description SM3工具类
 * @Author Sam
 * @Date 2019/7/18 9:27
 * @Version 1.0
 */
class Sm3Utils {

    /**
     * The length of sm3 output is 32 bytes
     */
    private static final int SM3DIGEST_LENGTH = 32;

    /**
     *  SM3计算hash
     * @param data
     * @return
     */
    public static byte[] hash(byte[] data) {

        byte[] result = new byte[SM3DIGEST_LENGTH];

        SM3Digest sm3digest = new SM3Digest();

        sm3digest.update(data, 0, data.length);
        sm3digest.doFinal(result, 0);

        return result;
    }
}