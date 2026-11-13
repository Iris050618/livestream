package com.winter.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

/**
 * 对称加密
 */
public class AesEncryptUtil {
    //钥匙是16字节
    private static final String KEY = "nLfSadFxVbjeW2OJ";

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/CBC/PKCS7Padding";
    // CBC必须设置iv 16字节
    private static final String IV = "123456789abcdefg";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static String encrypt(String content) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        byte[] aseKey = KEY.getBytes();
        Key key = new SecretKeySpec(aseKey, "AES");
        AlgorithmParameters _iv = generateIV(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, key,_iv);
        byte[] b = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        // 采用base64算法进行转码,避免出现中文乱码
        return org.apache.commons.codec.binary.Base64.encodeBase64String(b);
    }

    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        // Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    public static String decrypt(String encrypted) throws Exception {
        String result = null;
        byte[] encrypted64 = org.apache.commons.codec.binary.Base64.decodeBase64(encrypted);
        byte[] key64 = KEY.getBytes();
        byte[] iv64 = IV.getBytes();
        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator.getInstance("AES").init(128);
        result = new String(decrypt(encrypted64, key64, generateIV(iv64)), StandardCharsets.UTF_8);
        return result;
    }
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv)
            throws Exception {
        Key key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        // 设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

}
