package com.xinghe.xbx.common.constant;

public class CommonConstantConfig {
	/**utf-8编码*/
	public final static String CODE_UTF8="utf-8";
	/**http重试次数*/
	public final static Integer HTTP_RETRY_NUM = 3;
	/**http超时时间*/
	public final static Integer HTTP_READ_TIMEOUT = 75000;//车史接口和保险接口调用超时时间为60秒。
	/**http超时时间 发邮件使用*/
	public final static Integer HTTP_READ_TIMEOUT_SEND_EMAIL = 1000 * 60 * 60 * 2;
	/** 正则转义字符 */
	public final static String REGULAR_ESCAPE_CHAR = "\\";
	/** 短信失败最大重试次数 */
	public final static Integer SMS_RETRY_LIMIT = 3;
	/** 图片上传路径 */
	public final static String URL_SEPARATOR = "/";
	/**apikey_interface信息--接口管理*/
    public final static String APIKEY_INTERFACE_INFO="apikey^interface^%s^%s";
	
    /*-------------------------redis 过期时间start----------------------------*/
    public final static Integer EXPIRE_TIME_MIN_1 = 60; // 1 min

    public final static Integer EXPIRE_TIME_MIN_30 = 60 * 30; // 30 min
    
    public final static Integer EXPIRE_TIME_HOUR_1 = 3600; // 1 hour

    public final static Integer EXPIRE_TIME_DAY_1 = 3600 * 24; // 1 day
    /**3天=24小时乘以3*/
    public final static Integer EXPIRE_TIME_DAY_3 = 24 * 3;//3 day
    
    public final static Integer EXPIRE_TIME_DAY_30 = 3600 * 24 * 30; // 30 day
    
    public final static Integer EXPIRE_TIME_MIN_10 = 60 * 10;
    
    public final static Integer EXPIRE_DEFAULT = 60 * 5;
    /*-------------------------redis 过期时间end ----------------------------*/
  

}