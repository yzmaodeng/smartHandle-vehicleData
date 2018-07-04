package com.xinghe.xbx.common.util.cipher;

/**
 * @author pengxia
 * 
 */
public interface CryptoServiceProvider {
    public static final String ALGORITHM_DES = "DES";
    public static final String ALGORITHM_DESEDE = "DESede";

    /**
     * 加密 .
     * @param data  为被加密的数据缓冲区（源）.
     * @param key  为加密密钥，长度为24字节 .
     * @return
     * @throws Exception .
     */
    public byte[] encrypt(byte[] data, byte[] key);

    /**
     * 解密 .
     * 
     * @param cryptograph
     *            为加密后的缓冲区 .
     * @param key
     *            为解密密钥，长度为24字节 .
     * @return
     * @throws Exception .
     */
    public byte[] decrypt(byte[] cryptograph, byte[] key);
}
