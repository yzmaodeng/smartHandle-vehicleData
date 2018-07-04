package com.xinghe.xbx.common.util.cipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author pengxia
 *
 */
public class Cipher {
    private static final long serialVersionUID = 8623916527059626163L;
    private static String persisFile = "cipher";
    private static byte[] cipher;
    private static Cipher instance;
    private SecureRandom secureRandom;
    private Random random;

    private Cipher() {
        secureRandom = new SecureRandom();
        random = new Random(secureRandom.nextLong());
        recoverCipher();
        if (cipher == null)
            createCipher();
    }

    private void createCipher() {
        cipher = null;
        cipher = new byte[24];
        random.nextBytes(cipher);
        persistCipher();
    }

    public static Cipher getInstance() {
        if (instance == null)
            instance = new Cipher();
        return instance;
    }

    /**
     * @return
     */
    public byte[] getCipher() {
        return cipher;
    }

    /**
     * 重新生成密码
     */
    public void reloadCipher() {
        createCipher();
    }

    public void recoverCipher() {
        try {
            FileInputStream fis = new FileInputStream(persisFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            cipher = (byte[]) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.err.println("Exception during deserialization:" + e);
        }
    }

    public void persistCipher() {
        try {
            FileOutputStream fos = new FileOutputStream(persisFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cipher);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            System.err.println("Exception during serialization:" + e);
        }
    }

    public static void main(String[] args) {
        Cipher cipher = Cipher.getInstance();
        cipher.reloadCipher();
    }

    // 转换成十六进制字符串
    private String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
    }
}
