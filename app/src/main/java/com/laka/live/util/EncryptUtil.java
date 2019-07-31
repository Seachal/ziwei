package com.laka.live.util;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by luwies on 16/3/14.
 */
public class EncryptUtil {

    public static final String AES = "AES";

    public static final String RSA = "RSA/ECB/NoPadding";

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param key     加密密码
     * @return
     */
    public static byte[] aesEncrypt(byte[] content, byte[] key) {
        int len = content.length;
        int max = 16;
        int a = len % max;
        byte[] toEncrypt;
        if (a > 0) {
            int need = max - a;
            toEncrypt = new byte[len + need];
            System.arraycopy(content, 0, toEncrypt, 0, len);
            for (int i = 0; i < need; i++) {
                toEncrypt[len + i] = 0;
            }
        } else {
            toEncrypt = content;
        }

        return aes(toEncrypt, key, Cipher.ENCRYPT_MODE);
    }

    public static byte[] aesDecrypt(byte[] content, byte[] key) {
        return aes(content, key, Cipher.DECRYPT_MODE);
    }

    public static byte[] aes(byte[] content, byte[] key, int mode) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");// 创建密码器
            cipher.init(mode, keySpec);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            Log.error("test", "ex :" + e);
        } catch (NoSuchPaddingException e) {
            Log.error("test", "ex :" + e);
        } catch (InvalidKeyException e) {
            Log.error("test", "ex :" + e);
        } catch (IllegalBlockSizeException e) {
            Log.error("test", "ex :" + e);
        } catch (BadPaddingException e) {
            Log.error("test", "ex :" + e);
        }
        return null;
    }

    /**
     * 得到公钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) {
        try {

            byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            Log.error("test", "ex ", e);
        } catch (InvalidKeySpecException e) {
            Log.error("test", "ex ", e);
        }
        return null;
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param data      需加密数据的byte数据
     * @param publicKey 公钥
     * @return 加密后的byte型数据
     */
    public static byte[] rsaEncryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            Log.error("test", "ex ", e);
            return null;
        }
    }
}
