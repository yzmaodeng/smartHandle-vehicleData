package com.xinghe.xbx.common.util;

import java.io.Serializable;

import org.apache.log4j.Logger;
import com.xinghe.xbx.common.constant.CommonConstantConfig;
import com.xinghe.xbx.common.redis.RedisSentinelHelper;

/**
 * VIN格式验证
 */
public class DoubleOrderingVerificationUtil{
	private static final Logger logger = Logger.getLogger(DoubleOrderingVerificationUtil.class);
	/**
	 * @Title: 判断key在缓存中是否存在
	 */
	public  static boolean paramStrIsExist(String key){
		// 获取缓存
		String paramStr = (String) RedisSentinelHelper.getInstance().get(key);
		logger.info("INFO->DoubleOrderingVerificationUtil-->paramStrIsExist|INFO = paramStr is "+paramStr+" |param:key = "+key);
		if (paramStr == null) {
			return true;
        }
		return false;
	}
	
	/**
	 * @Title: 将key存入缓存
	 */
	public static void setParamStr(String key,String paramStr){
		logger.info("INFO->DoubleOrderingVerificationUtil-->setParamStr|param:key = "+key+"| paramStr= "+paramStr);
		RedisSentinelHelper.getInstance().set(key, (Serializable) paramStr, CommonConstantConfig.EXPIRE_TIME_MIN_1);
	}
	
	

}