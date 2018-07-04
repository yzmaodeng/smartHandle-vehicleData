package com.xinghe.xbx.common.util;

import org.apache.log4j.Logger;

/**
 * VIN格式验证
 */
public class TimestampVerificationUtil{
	private static final Logger logger = Logger.getLogger(TimestampVerificationUtil.class);
	/**
	 * 
	 * @Title: 请求 timestamp是否在指定时间之内
	 * @param  timestamp
	 * @return Integer
	 * 0:成功
	 * 1:时间格式错误
	 * 2:调用时间必须在5分钟之内
	 * 3:系统错误
	 */
	public static Integer timestampValidDate (Long timestamp){
		String timestampTmp = timestamp.toString();
		//timestamp精确到秒，长度是10位
		if(timestampTmp.length() != 10){
			logger.error("ERROR->CommonServiceImpl-->timestampValidDate|ERROR = timestamp length error|param:timestamp = "+timestamp);
			return 1;
		}
		Long nowTimestamp = System.currentTimeMillis()/1000;
		Long startTimestamp = nowTimestamp - 300;
		Long endTimestamp = nowTimestamp + 300;
		
		if(startTimestamp == null || endTimestamp == null){
			logger.error("ERROR->CommonServiceImpl-->timestampValidDate|ERROR = startTimestamp or endTimestamp is null|"
	    		    +"param:"+"startTimestamp="+startTimestamp+"|endTimestamp="+endTimestamp);
			return 3;
		}
		
		if(timestamp > startTimestamp &&  timestamp < endTimestamp){
			return 0;
		}
		return  2;
	}	
}