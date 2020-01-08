package com.meizi.admin.util;


import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @ClassName Sm2Utils
 * @Description SM2工具类
 * @Author Sam
 * @Date 2019/7/18 9:20
 * @Version 1.0
 */
class Sm2Utils {

    private static final int COORDS_SIZE = 32;
    private static final int POINT_SIZE = COORDS_SIZE * 2 + 1;
    private static final int R_SIZE = 32;
    private static final int S_SIZE = 32;

    /**
     * The length of sm3 output is 32 bytes
     */
    private static final int SM3DIGEST_LENGTH = 32;

    private static final BigInteger SM2_P = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF", 16);
    private static final BigInteger SM2_A = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC", 16);
    private static final BigInteger SM2_B = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
    private static final BigInteger SM2_N = new BigInteger("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16);
    private static final BigInteger SM2_H = ECConstants.ONE;
    private static final BigInteger SM2_GX = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    private static final BigInteger SM2_GY = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);

    /**
     * To get the curve from the equation y^2=x^3+ax+b according the coefficient a and b,
     * with the big prime p, the order n, the cofactor h, and obtain the generator g and the domain's parameters
     */
    private static final ECCurve CURVE = new ECCurve.Fp(SM2_P, SM2_A, SM2_B, SM2_N, SM2_H);
    private static final ECPoint G = CURVE.createPoint(SM2_GX, SM2_GY);
    private static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(CURVE, G, SM2_N);

    //-----------------Key Generation Algorithm-----------------

    /**
     * key pair generation
     *
     * @return key pair
     */
    public static AsymmetricCipherKeyPair generateKeyPair() {
        SecureRandom random = new SecureRandom();
        return generateKeyPair(random);
    }

    public static AsymmetricCipherKeyPair generateKeyPair(SecureRandom random) {

        ECKeyGenerationParameters keyGenerationParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, random);
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();

        // To generate the key pair
        keyPairGenerator.init(keyGenerationParams);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * public key retrieval
     *
     * @param privateKey private key
     * @return publicKey
     */
    public static byte[] retrievePublicKey(byte[] privateKey) {
        ECPoint publicKeyPoint = DOMAIN_PARAMS.getG().multiply(new BigInteger(1, privateKey)).normalize();
        return publicKeyPoint.getEncoded(false);
    }

    //-----------------Digital Signature Algorithm-----------------

    /**
     * signature generation
     *
     * @param data       data to be signed
     * @param privateKey private key
     * @return signature
     */
    public static byte[] sign(byte[] data, byte[] privateKey) {

        SecureRandom random = new SecureRandom();
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(new BigInteger(1, privateKey), DOMAIN_PARAMS);
        CipherParameters params = new ParametersWithRandom(privKey, random);

        return sign(data, params);
    }

    public static byte[] sign(byte[] data, byte[] privateKey, SecureRandom random, String id) {

        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(new BigInteger(1, privateKey), DOMAIN_PARAMS);
        CipherParameters params = new ParametersWithID(new ParametersWithRandom(privKey, random), toBytes(id));

        return sign(data, params);
    }

    public static byte[] sign(byte[] data, CipherParameters params) {

        SM2Signer signer = new SM2Signer();

        // To get Z_A and prepare parameters
        signer.init(true, params);
        // To fill the whole message to be signed
        signer.update(data, 0, data.length);
        // To get and return the signature result;

        byte[] encodedSignature;
        try {
            encodedSignature = signer.generateSignature();
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }

        // To decode the signature
        ASN1Sequence sig = ASN1Sequence.getInstance(encodedSignature);
        byte[] rBytes = bigIntegerTo32Bytes(ASN1Integer.getInstance(sig.getObjectAt(0)).getValue());
        byte[] sBytes = bigIntegerTo32Bytes(ASN1Integer.getInstance(sig.getObjectAt(1)).getValue());

        byte[] signature = new byte[R_SIZE + S_SIZE];
        System.arraycopy(rBytes, 0, signature, 0, R_SIZE);
        System.arraycopy(sBytes, 0, signature, R_SIZE, S_SIZE);

        return signature;
    }

    /**
     * verification
     *
     * @param data      data to be signed
     * @param publicKey public key
     * @param signature signature to be verified
     * @return true or false
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] signature) {

        ECPoint pubKeyPoint = resolvePubKeyBytes(publicKey);
        ECPublicKeyParameters pubKey = new ECPublicKeyParameters(pubKeyPoint, DOMAIN_PARAMS);

        return verify(data, pubKey, signature);
    }

    public static boolean verify(byte[] data, byte[] publicKey, byte[] signature, String id) {

        ECPoint pubKeyPoint = resolvePubKeyBytes(publicKey);
        ECPublicKeyParameters pubKey = new ECPublicKeyParameters(pubKeyPoint, DOMAIN_PARAMS);
        ParametersWithID params = new ParametersWithID(pubKey, toBytes(id));
        return verify(data, params, signature);
    }

    public static boolean verify(byte[] data, CipherParameters params, byte[] signature) {

        SM2Signer verifier = new SM2Signer();

        // To get Z_A and prepare parameters
        verifier.init(false, params);
        // To fill the whole message
        verifier.update(data, 0, data.length);
        // To verify the signature

        byte[] rBytes = new byte[R_SIZE];
        byte[] sBytes = new byte[S_SIZE];
        System.arraycopy(signature, 0, rBytes, 0, R_SIZE);
        System.arraycopy(signature, R_SIZE, sBytes, 0, S_SIZE);

        BigInteger r = new BigInteger(1, rBytes);
        BigInteger s = new BigInteger(1, sBytes);
        byte[] encodedSignature = new byte[0];
        try {
            encodedSignature = new DERSequence(new ASN1Encodable[]{new ASN1Integer(r), new ASN1Integer(s)}).getEncoded();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return verifier.verifySignature(encodedSignature);
    }

    //-----------------Public Key Encryption Algorithm-----------------

    /**
     * encryption
     *
     * @param plainBytes plaintext
     * @param publicKey  public key
     * @return ciphertext
     */
    public static byte[] encrypt(byte[] plainBytes, byte[] publicKey) {

        SecureRandom random = new SecureRandom();

        return encrypt(plainBytes, publicKey, random);
    }

    public static byte[] encrypt(byte[] plainBytes, byte[] publicKey, SecureRandom random) {

        ECPoint pubKeyPoint = resolvePubKeyBytes(publicKey);
        ECPublicKeyParameters pubKey = new ECPublicKeyParameters(pubKeyPoint, DOMAIN_PARAMS);
        ParametersWithRandom params = new ParametersWithRandom(pubKey, random);

        return encrypt(plainBytes, params);
    }

    public static byte[] encrypt(byte[] plainBytes, ECPublicKeyParameters pubKey) {

        SecureRandom random = new SecureRandom();
        ParametersWithRandom params = new ParametersWithRandom(pubKey, random);

        return encrypt(plainBytes, params);
    }

    public static byte[] encrypt(byte[] plainBytes, CipherParameters params) {

        SM2Engine encryptor = new SM2Engine();

        // To prepare parameters
        encryptor.init(true, params);

        // To generate the twisted ciphertext c1c2c3.
        // The latest standard specification indicates that the correct ordering is c1c3c2
        byte[] c1c2c3 = new byte[0];
        try {
            c1c2c3 = encryptor.processBlock(plainBytes, 0, plainBytes.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }

        // get correct output c1c3c2 from c1c2c3
        byte[] c1c3c2 = new byte[c1c2c3.length];
        System.arraycopy(c1c2c3, 0, c1c3c2, 0, POINT_SIZE);
        System.arraycopy(c1c2c3, POINT_SIZE, c1c3c2, POINT_SIZE + SM3DIGEST_LENGTH, plainBytes.length);
        System.arraycopy(c1c2c3, POINT_SIZE + plainBytes.length, c1c3c2, POINT_SIZE, SM3DIGEST_LENGTH);

        return c1c3c2;
    }

    /**
     * decryption
     *
     * @param cipherBytes ciphertext
     * @param privateKey  private key
     * @return plaintext
     */
    public static byte[] decrypt(byte[] cipherBytes, byte[] privateKey) {

        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(new BigInteger(1, privateKey), DOMAIN_PARAMS);

        return decrypt(cipherBytes, privKey);
    }

    public static byte[] decrypt(byte[] cipherBytes, CipherParameters params) {

        SM2Engine decryptor = new SM2Engine();

        // To prepare parameters
        decryptor.init(false, params);

        // To get c1c2c3 from ciphertext whose ordering is c1c3c2
        byte[] c1c2c3 = new byte[cipherBytes.length];
        System.arraycopy(cipherBytes, 0, c1c2c3, 0, POINT_SIZE);
        System.arraycopy(cipherBytes, POINT_SIZE, c1c2c3, c1c2c3.length - SM3DIGEST_LENGTH, SM3DIGEST_LENGTH);
        System.arraycopy(cipherBytes, SM3DIGEST_LENGTH + POINT_SIZE, c1c2c3, POINT_SIZE, c1c2c3.length - SM3DIGEST_LENGTH - POINT_SIZE);

        // To output the plaintext
        try {
            return decryptor.processBlock(c1c2c3, 0, c1c2c3.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * To convert BigInteger to byte[] whose length is 32
     *
     * @param b bigInteger
     * @return bytes
     */
    private static byte[] bigIntegerTo32Bytes(BigInteger b) {
        byte[] tmp = b.toByteArray();
        byte[] result = new byte[32];
        if (tmp.length > result.length) {
            System.arraycopy(tmp, tmp.length - result.length, result, 0, result.length);
        } else {
            System.arraycopy(tmp, 0, result, result.length - tmp.length, tmp.length);
        }
        return result;
    }

    /**
     * To retrieve the public key point from publicKey in byte array mode
     *
     * @param publicKey publicKey
     * @return ECPoint
     */
    private static ECPoint resolvePubKeyBytes(byte[] publicKey) {
        return CURVE.decodePoint(publicKey);
    }

    public static ECCurve getCurve() {
        return CURVE;
    }

    public static ECDomainParameters getDomainParams() {
        return DOMAIN_PARAMS;
    }

    public static byte[] toBytes(String str) {
        return toBytes(str, StandardCharsets.UTF_8);
    }

    public static byte[] toBytes(String str, Charset charset) {
        byte[] bytes = str.getBytes(charset);
        return bytes;
    }
}