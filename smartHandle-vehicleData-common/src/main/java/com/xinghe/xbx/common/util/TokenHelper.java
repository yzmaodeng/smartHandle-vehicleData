package com.xinghe.xbx.common.util;

import org.apache.log4j.Logger;

public class TokenHelper {
//    static final String TOKEN_KEY = "";

    private static Logger logger = Logger.getLogger(TokenHelper.class);

	private static TokenHelper instance = null;

	public static TokenHelper getInstance(){
		if (instance == null) {
			synchronized (TokenHelper.class) {
				if (instance == null) {
					instance = new TokenHelper();
				}
			}
		}
		return instance;
	}

    /**
     * 最近一次生成令牌值的时间戳。
     */
    private long previous;
    /**
     * 根据用户会话ID和当前的系统时间生成一个唯一的令牌。
     */
    public synchronized String generateToken() {
        
    	long current = System.currentTimeMillis();

        if (current == previous) {
        	current++;
        }
            
        logger.info(previous);
        previous = current;
            
        int random = (int)(Math.random()*100);
            
        StringBuffer b = new StringBuffer();
        
        return  MD5.crypt(b.append(current).append(random).toString());
    }
}