package com.xinghe.xbx.constant;

import java.util.ResourceBundle;

/**
 * 客户端常量
 * 
 * @author leifu
 * @Date 2014年6月21日
 * @Time 上午10:54:34
 */
public class NotifyConfig {

    /* 邮件发送相关常量 */
    public static final String MAIL_SMTP_AUTH;
    public static final String MAIL_SMTP_HOST;
    public static final String MAIL_SMTP_USER;
    public static final String MAIL_SMTP_PWD;
    public static final String MAIL_SMTP_SEND_MAIL;
    public static final String MAIL_SMTP_SEND_UNAME;
    public static final String MAIL_ENCODE;
    
    /* 短信发送相关常量 */
    public static final String SMS_USER_NAME;
    public static final String SMS_USER_PWD;
    public static final Integer SMS_RETRY_LIMIT;
    public static final String SMS_AUTOGRAPH;

    static {
        ResourceBundle rb = ResourceBundle.getBundle("cacheCloudClient");
        MAIL_SMTP_HOST = rb.getString("mail_smtp_host");
        // 发送邮件服务器
        MAIL_SMTP_AUTH = rb.getString("mail_smtp_auth");
        // 发送者邮件全名
        MAIL_SMTP_SEND_MAIL = rb.getString("mail_smtp_send_mail");
        // 发送者用户名
        MAIL_SMTP_SEND_UNAME = rb.getString("mail_smtp_send_uname");
        // 邮件认证：用户名
        MAIL_SMTP_USER = rb.getString("mail_smtp_user");
        // 邮件认证：用户密码
        MAIL_SMTP_PWD = rb.getString("mail_smtp_pwd");
        // 邮件编码
        MAIL_ENCODE = rb.getString("mail_encode");

        // 短信发送账户名
        SMS_USER_NAME = rb.getString("sms_user_name");
        // 短信发送账户密码
        SMS_USER_PWD = rb.getString("sms_user_pwd");
        // 短信发送失败最大重试次数
        SMS_RETRY_LIMIT = Integer.valueOf(rb.getString("sms_retry_limit"));
        // 短信签名
        SMS_AUTOGRAPH = rb.getString("sms_autograph");
        
    }

}
