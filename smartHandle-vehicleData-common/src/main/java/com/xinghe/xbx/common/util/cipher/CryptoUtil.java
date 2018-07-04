package com.xinghe.xbx.common.util.cipher;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;



public class CryptoUtil {
    private static CryptoServiceProvider cryptoServiceProvider = new BaseServiceProvider();
    private static Cipher cipher = Cipher.getInstance();
    /**
     * 给String加密 .
     * @param src .
     * @return .
     */
    public static String encrypt(String src) {
        if (src == null)
            return null;
        if ("".equals(src))
            return null;
        String cryptograph = null;
        try {
            // 加密
            byte[] encoded = cryptoServiceProvider.encrypt(src.getBytes("UTF-8"), cipher.getCipher());
            // 编码
            cryptograph = new String(XBase64.encodeBytes(encoded).getBytes(), "UTF-8");
            return cryptograph;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return src;
        }
    }
    
    /**
     * 给String加密 .(密钥固定)
     * @param src .
     * @return .
     */
    public static String encrypt(String src,String cipher) {
    	if(StringUtils.isBlank(src)){
    		return "";
    	}
//        if (src == null)
//            return null;
//        if ("".equals(src))
//            return null;
        String cryptograph = null;
        try {
            // 加密
            byte[] encoded = cryptoServiceProvider.encrypt(src.getBytes("UTF-8"), cipher.getBytes("UTF-8"));
            // 编码
            cryptograph = new String(XBase64.encodeBytes(encoded).getBytes(), "UTF-8");
            return cryptograph;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return src;
        }
    }
    
    /**
     * 解密 .
     * @param cryptoGraph .
     * @return .
     */
    public static String decrypt(String cryptoGraph) {
        if (cryptoGraph == null)
            return null;
        if ("".equals(cryptoGraph))
            return null;
        try {
            // 解码
            byte[] decoded = cryptoGraph.getBytes("UTF-8");
            // 解密
            byte[] srcBytes = cryptoServiceProvider.decrypt(XBase64.decode(decoded, 0, decoded.length), cipher.getCipher());
            String src=null;
            if(srcBytes!=null&&srcBytes.length>0){
            	 src = new String(srcBytes, "UTF-8");
            }
            return src;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return cryptoGraph;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * 解密 .(密钥固定)
     * @param cryptoGraph .
     * @return .
     */
    public static String decrypt(String cryptoGraph,String cipher) {
    	if(StringUtils.isBlank(cryptoGraph)){
    		return "";
    	}
    	
//        if (cryptoGraph == null)
//            return null;
//        if ("".equals(cryptoGraph))
//            return null;
        try {
            // 解码
            byte[] decoded = cryptoGraph.getBytes("UTF-8");
            // 解密
            byte[] srcBytes = cryptoServiceProvider.decrypt(XBase64.decode(decoded, 0, decoded.length),  cipher.getBytes("UTF-8"));
            String src=null;
            if(srcBytes!=null&&srcBytes.length>0){
            	 src = new String(srcBytes, "UTF-8");
            }
            return src;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return cryptoGraph;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
  
}
