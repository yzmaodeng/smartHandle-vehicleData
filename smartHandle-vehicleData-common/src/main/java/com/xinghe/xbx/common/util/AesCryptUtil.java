package com.xinghe.xbx.common.util;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


public class AesCryptUtil {
	 private static final String AES_ALGORITHM = "AES";
	    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	    private static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	    /**
	     * AES 加密
	     *
	     * @param data 明文
	     * @param key  16、24、32 字节秘钥
	     * @return 密文
	     */
	    public static String encrypt(final String data, final String key) {
	        byte[] datas = data.getBytes(DEFAULT_ENCODING);
	        byte[] keys = key.getBytes(DEFAULT_ENCODING);
	        byte[] results = aesTemplate(datas, keys, AES_ALGORITHM, AES_TRANSFORMATION, true);
	        if (results == null) {
	            return null;
	        } else {
	            // 使用Base64编码
	            results = Base64.getEncoder().encode(results);
	            return new String(results, DEFAULT_ENCODING);
	        }
	    }

	    /**
	     * AES 解密
	     *
	     * @param data 密文
	     * @param key  16、24、32 字节秘钥
	     * @return 明文
	     */
	    public static String decrypt(final String data, final String key) {
	        // 使用Base64进行解码
	        byte[] datas = Base64.getDecoder().decode(data.getBytes(DEFAULT_ENCODING));

	        byte[] keys = key.getBytes(DEFAULT_ENCODING);
	        byte[] results = aesTemplate(datas, keys, AES_ALGORITHM, AES_TRANSFORMATION, false);

	        if (results == null) {
	            return null;
	        } else {
	            return new String(results, DEFAULT_ENCODING);
	        }
	    }

	    private static byte[] aesTemplate(final byte[] data, final byte[] key, final String algorithm, final String transformation, final boolean isEncrypt) {
	        if (data == null || data.length == 0 || key == null || key.length == 0) return null;
	        try {
	            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
	            Cipher cipher = Cipher.getInstance(transformation);
	            SecureRandom random = new SecureRandom();
	            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, random);
	            return cipher.doFinal(data);
	        } catch (Throwable e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
