package com.meizi.admin.util;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName SmCipher
 * @Description 国密工具
 * @Author Sam
 * @Date 2019/7/5 10:44
 * @Version 1.0
 */
public class SmCipher {

    /**
     * SmCipher实例Reference
     */
    private static final AtomicReference<SmCipher> SM_CIPHER_REFERENCE =
            new AtomicReference<SmCipher>();

    /**
     * SM2实例Reference
     */
    private static final AtomicReference<Sm2> SM2_REFERENCE =
            new AtomicReference<Sm2>();

    /**
     * SM4实例Reference
     */
    private static final AtomicReference<Sm4> SM4_REFERENCE =
            new AtomicReference<Sm4>();

    /**
     * 设置成单例
     */
    private SmCipher() {
    }

    /**
     * 获取实例
     *
     * @return 返回实例对象
     */
    public static SmCipher instance() {
        for (; ; ) {
            SmCipher smCipher = SM_CIPHER_REFERENCE.get();

            if (!Objects.isNull(smCipher)) {
                return smCipher;
            }

            smCipher = new SmCipher();

            if (SM_CIPHER_REFERENCE.compareAndSet(null, smCipher)) {
                return smCipher;
            }
        }
    }

    /**
     *  SM2
     * @return SM2
     */
    public Sm2 sm2() {
        for (; ; ) {
            Sm2 sm2 = SM2_REFERENCE.get();

            if (!Objects.isNull(sm2)) {
                return sm2;
            }

            sm2 = new Sm2();

            if (SM2_REFERENCE.compareAndSet(null, sm2)) {
                return sm2;
            }
        }
    }

    /**
     *  SM4
     * @return SM4
     */
    public Sm4 sm4() {
        for (; ; ) {
            Sm4 sm4 = SM4_REFERENCE.get();

            if (!Objects.isNull(sm4)) {
                return sm4;
            }

            sm4 = new Sm4();

            if (SM4_REFERENCE.compareAndSet(null, sm4)) {
                return sm4;
            }
        }
    }

    /**
     * SM2
     */
    public class Sm2 {

        /**
         *  SM2 生成公私钥对
         * @return 位置0:私钥;位置1:公钥
         */
        public String[] generateKeyPair() {
            AsymmetricCipherKeyPair keyPair = Sm2Utils.generateKeyPair();

            ECPrivateKeyParameters privateKey =
                    (ECPrivateKeyParameters) keyPair.getPrivate();
            ECPublicKeyParameters publicKey =
                    (ECPublicKeyParameters) keyPair.getPublic();

            byte[] privateKeyEncoded = privateKey.getD().toByteArray();
            byte[] publicKeyEncoded = publicKey.getQ().getEncoded(false);

            String[] keyPairArray =
                    new String[]{CipherUtils.encode(privateKeyEncoded),
                            CipherUtils.encode(publicKeyEncoded)};

            return keyPairArray;
        }


        /**
         *  SM2签名
         * @param privateKey    私钥
         * @param plain         原始数据
         * @return              签名
         */
        public String sign(String privateKey, String plain) {
            byte[] bytes = Sm2Utils.sign(CipherUtils.toBytes(plain),
                    CipherUtils.decode(privateKey));
            String text = CipherUtils.encode(bytes);
            return text;
        }

        /**
         *  SM2验签
         * @param publicKey 公钥
         * @param plain     原始数据
         * @param sign      签名
         * @return          验签结果
         */
        public boolean verify(String publicKey, String plain, String sign) {
            boolean verify = Sm2Utils.verify(CipherUtils.toBytes(plain),
                    CipherUtils.decode(publicKey),
                    CipherUtils.decode(sign));
            return verify;
        }

        /**
         *  SM2加密
         * @param publicKey 公钥
         * @param plain     原始数据
         * @return          加密后的密文
         */
        public String encrypt(String publicKey, String plain) {
            byte[] bytes = Sm2Utils.encrypt(CipherUtils.toBytes(plain),
                    CipherUtils.decode(publicKey));
            String text = CipherUtils.encode(bytes);
            return text;
        }

        /**
         *  SM2解密
         * @param privateKey    私钥
         * @param cipher        加密后的密文
         * @return              原始数据
         */
        public String decrypt(String privateKey, String cipher) {
            byte[] bytes = Sm2Utils.decrypt(CipherUtils.decode(cipher),
                    CipherUtils.decode(privateKey));
            String text = CipherUtils.toText(bytes);
            return text;
        }
    }

    /**
     * SM4
     */
    public class Sm4 {
        /**
         *  SM4 生成密钥
         * @return  密钥
         */
        public String generateKey() {
            byte[] bytes = Sm4Utils.generateKey();
            String secretKey = CipherUtils.encode(bytes);
            return secretKey;
        }

        /**
         *  SM4加密
         * @param secretKey 密钥
         * @param plain     原始数据
         * @return          加密后的数据
         */
        public String encrypt(String secretKey, String plain) {
            byte[] bytes = Sm4Utils.encrypt(CipherUtils.toBytes(plain), CipherUtils.decode(secretKey));
            String text = CipherUtils.encode(bytes);
            return text;
        }

        /**
         *  SM4解密
         * @param secretKey 密钥
         * @param cipher    密文
         * @return          解密后的数据
         */
        public String decrypt(String secretKey, String cipher) {
            byte[] bytes = Sm4Utils.decrypt(CipherUtils.decode(cipher),
                    CipherUtils.decode(secretKey));
            String text = CipherUtils.toText(bytes);
            return text;
        }
    }

}