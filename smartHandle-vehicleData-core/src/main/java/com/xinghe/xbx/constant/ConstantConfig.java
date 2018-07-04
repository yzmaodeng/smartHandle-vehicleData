package com.xinghe.xbx.constant;

public class ConstantConfig {
	
	
	
	/*----------------------车辆交易所--------------------------------*/
	/**渠道信息---apikey*/
    public final static String APIKEY_INFO="apikey^%s";
    /**渠道信息--channelName*/
    public final static String CHANNEL_INFO="channel^%s";
    
    
    /*--------------------------------------------------------------*/
	
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
	/** PDF文件上传子目录*/
	public final static String PDF_IMG_PATH = "report";
	/** 签名文件上传子目录*/
	public final static String BRAND_LOGO_PATH = "brand/logo";
	/** 签名文件上传子目录*/
	public final static String MFGR_LOGO_PATH = "mfgr/logo";
	/** 签名文件上传子目录*/
	public final static String SERIES_LOGO_PATH = "series/logo";
	/** icon文件上传子目录*/
	public final static String SERIES_ICON_PATH = "series/icon";
    /**user*/
    public final static String USER_MANAGER_INFO="userManage%s";
    /**征信用户的权限*/
    public final static String USER_FUNC_MAP_KEY="AdminUserFuncRedise^";
    /**交易所用户的权限*/
    public final static String USER_FUNC_MAP_KEY_AUTOCHAIN="AutocUserFuncRedise^";
    /**md5 加 salt*/
	public final static String MD5_SALT_BRAND_IMAGE = "2017#brand#"; 
	public final static String MD5_SALT_MFGR_IMAGE = "2017#mfgr#";
	public final static String MD5_SALT_SERIES_IMAGE = "2017#series#";
	public final static String MD5_SALT_ICON_IMAGE = "2017#icon#";
	
	/**
	 * 数据表字段 status_audit
	 */
	 public final static Integer IS_VALID = 0;
	 public final static Integer IS_INVALID = -1;
	
	 
	 /** db 状态值 status */ 
    public final static Integer STATUS_NORMAL = 0;
    public final static Integer STATUS_DEL = -1;
    public final static Integer STATUS_ONE = 1;
    public final static Integer STATUS_TWO = 2;
    
    
    /**
     * 权限 1，2，4 基础权限    3、5、6、7
     */
    public final static Integer ROLE_AUTH_1 = 1;
    public final static Integer ROLE_AUTH_2 = 2;
    public final static Integer ROLE_AUTH_3 = 3;
    public final static Integer ROLE_AUTH_4 = 4;
    public final static Integer ROLE_AUTH_5 = 5;
    public final static Integer ROLE_AUTH_6 = 6;
    public final static Integer ROLE_AUTH_7 = 7;
    
    /**
     * reids中的占位符
     * (查库返回空时使用)
     */
    public final static String REDIS_PLACEHOLDER="redis";
    public final static Integer REDIS_PLACEHOLDER_VALUE = Integer.MAX_VALUE;
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
    
    /*-------------------------账号管理start----------------------------*/
    /**测试用户签名*/
    public final static String USER_NAME_PIC = "http://www.xingbaoxin.com/img/user/sign/test.png";
    /**fefault*/
    public final static Integer USER_TYPE_DEFAULT = 0;
    /**系统管理员*/
    public final static Integer USER_TYPE_1 = 1;
    /**报告员*/
    public final static Integer USER_TYPE_2 = 2;
    /**审核员*/
    public final static Integer USER_TYPE_3 = 3;
    /**BD*/
    public final static Integer USER_TYPE_4 = 4;
    
   /*-------------------------账号管理 end------------------------------*/  
   
    /*-------------------------账号权限管理 start------------------------------*/  
    /**系统管理员*/
    public final static String USER_ROLE_1 = "1";
    /**报告员*/
    public final static String USER_ROLE_2 = "2";
    /**审核员*/
    public final static String USER_ROLE_3 = "3";
    /**BD*/
    public final static String USER_ROLE_4 = "4";
    /*-------------------------账号权限管理 end------------------------------*/  
    
    /*-------------------------用户登录状态start----------------------------*/
    /**登录*/
    public final static Integer ONLINE = 1;
    /**退出*/
    public final static Integer OFFLINE = 0;
   /*-------------------------账号是否有效标示 end------------------------------*/
    
    /*-------------------------账号是否有效标示start----------------------------*/
    /**default*/
    public final static Integer USER_IS_DEFAULT = -1;
    /**有效*/
    public final static Integer USER_IS_VALID = 0;
    /**无效*/
    public final static Integer USER_IS_INVALID = 1;
   /*-------------------------账号是否有效标示 end------------------------------*/  
   
    /*-------------------------用户登录start----------------------------*/
    /** 管理端登录信息 */
    public final static String COOKIE_XBX_ADMIN_LOGIN_TOKEN = "xbxadminlogo.png";
    /**将管理端用户名存入cookie中*/
    public final static String COOKIE_XBX_ADMIN_USERNAME = "xbxadusername.png";
    
    public final static String COOKIE_XBX_ADMIN_USERNAME_C = "xbxadusernamec.png";

    public final static String COOKIE_XBX_ADMIN_USERNAME_PIC = "xbxadusernamepic.png";
    public final static String MD5_SALT_LONGIN_4S = "2016#login#4s%0731"; 
    public final static String MD5_SALT_LONGIN_CLIENT = "2016#login#client%0731"; 
    public final static String MD5_SALT_LONGIN_ADMIN = "2017#login#admin%0829"; 
    /** cookie拼接串 */
    public final static String COOKIE_4S_CONCAT_STR = "#";
    /** 登录信息过期时间 */
    public final static Integer EXPIRE_COOKIE_4S_LOGIN_TOKEN = 24*60*60;
    /**用户名登陆保存时间*/
    public final static Integer EXPIRE_COOKIE_XBX_USERNAME_TOKEN = 24*60*60*7;
    /** 提示框展示标识 */
    public final static String COOKIE_PRICE_TIPS = "chesongPtips.png";
    /** 提示框展示标识 过期时间 */
    public final static Integer EXPIRE_COOKIE_PRICE_TIPS = EXPIRE_COOKIE_4S_LOGIN_TOKEN;
   /*-------------------------账号是否有效标示 end------------------------------*/  
  
    /*-------------------------channel相关配置 end------------------------------*/ 
    /** 渠道类型-测试 */
    public final static int CHANNEL_TYPE_TEST = 0;
    /** 渠道类型-正式 */
    public final static int CHANNEL_TYPE_CEREMONIAL = 1;
    /*-------------------------channel相关配置 end------------------------------*/  

    
}