package com.xinghe.xbx.common.util.cipher;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseServiceProvider implements CryptoServiceProvider {
	private static final Log log = LogFactory.getLog(BaseServiceProvider.class);
    public static String Algorithm = "DESede"; 
 
    /**
     * @see com.sohu.photoblog.security.CryptoServiceProvider#encrypt(byte[],byte[])
     */
    public byte[] encrypt(byte[] src, byte[] keybyte) {
        try {
            if(src==null
                    ||src.length<=0
                    ||keybyte==null
                    ||keybyte.length<=0){
                return null;
            }
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); // 生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm);// 加密
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * @see com.sohu.photoblog.security.CryptoServiceProvider#decrypt(byte[],byte[])
     */
    public byte[] decrypt(byte[] src, byte[] keybyte) {
        try {
            if(src==null
                    ||src.length<=0
                    ||keybyte==null
                    ||keybyte.length<=0){
                return null;
            }
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);//生成密钥
            Cipher c1 = Cipher.getInstance(Algorithm);// 解密
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (Exception e1) {
        	log.error("##decrypt error="+e1.getMessage());
        }  
        return null;
    }

    /**
     * 转换成十六进制字符串
     */
    public static String byte2hex(byte[] b) {
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
