package com.xinghe.xbx.common.constant;

public class VerificationConstantConfig {
	/*-------------------------verification result Code begin----------------------------*/
	/** 成功*/
    public final static int RESULT_CODE_SUCCESS = 0;
	/** 系统错误*/
    public final static int RESULT_CODE_SYSTEM_ERROR = -1;
	/** 默认错误*/
    public final static int RESULT_CODE_DEFAULT = -99;
    
	/** apikey无效*/
    public final static int RESULT_CODE_APIKEY_INVALID = 1001;
	/** apikey到期*/
    public final static int RESULT_CODE_APIKEY_EXPIRE = 1002;
    
	/** apikey无权访问接口*/
    public final static int RESULT_CODE_APIKEY_INTERFACE_INVALID = 2001;
    /** 接口到期*/
    public final static int RESULT_CODE_APIKEY_INTERFACE_EXPIRE = 2002;
    /** 当天次数已用完*/
    public final static int RESULT_CODE_APIKEY_NO_NUMBER_TODAY = 2003;
    /** 接口测试次数已用完*/
    public final static int RESULT_CODE_APIKEY_NO_NUMBER_TEST = 2004;
    /** 接口次数已用完*/
    public final static int RESULT_CODE_APIKEY_NO_NUMBER = 2005;
    
    /** 签名错误*/
    public final static int RESULT_CODE_SIGN_ERROR = 3001;  
	/*-------------------------verification result Code end----------------------------*/
    /*-------------------------channel相关配置 end------------------------------*/ 
    /** 渠道类型-测试 */
    public final static int CHANNEL_TYPE_TEST = 0;
    /** 渠道类型-正式 */
    public final static int CHANNEL_TYPE_CEREMONIAL = 1;
    /*-------------------------channel相关配置 end------------------------------*/  

}