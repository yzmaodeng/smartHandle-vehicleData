package com.xinghe.xbx.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.xinghe.xbx.common.util.cipher.CryptoUtil;


public class CookieHelper{
	private static final Logger log = Logger.getLogger(CookieHelper.class);
	private static CookieHelper instance = null;
	/** cookie域 */
	private String domain;
	
	public static CookieHelper getInstance(){
		if (instance == null) {
			synchronized (CookieHelper.class) {
				if (instance == null) {
					instance = new CookieHelper();
				}
			}
		}
		return instance;
	}
  

	/**
	 * 设置 cookie 
	 * @param request
	 * @param response
	 */
	public void setCookie(String key,String value,int maxage,String domain,HttpServletRequest request,HttpServletResponse response){
		Cookie cookie = new Cookie(key,value);
		cookie.setDomain(domain);
//		cookie.setDomain(".tv.sohu.com");
//		cookie.setMaxAge(2 * 60 * 60 * 1000);
		cookie.setMaxAge(maxage);
		cookie.setPath("/");
//		cookie.setSecure()
		response.addCookie(cookie);
	}
	
	/**
	 * 设置 cookie 
	 * @param request
	 * @param response
	 */
	public void setCookie(String key,String value,int maxage,HttpServletRequest request,HttpServletResponse response){
		Cookie cookie = new Cookie(key,value);
		if(StringUtils.isNotBlank(domain)){
			cookie.setDomain(domain);
		}
//		cookie.setDomain(".tv.sohu.com");
//		cookie.setMaxAge(2 * 60 * 60 * 1000);
		cookie.setMaxAge(maxage);
		cookie.setPath("/");
//		cookie.setSecure()
		response.addCookie(cookie);
	}

	/**
	 * 获取Cookies
	 * @param cookies
	 * @return
	 */
	public String getCookie(HttpServletRequest request,String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0){
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}
	
//  	public void setEncryptCookie_US( HttpServletRequest request, HttpServletResponse response,String key,String value,boolean encrypt) {
//        int maxage = 60*60*24;
//		String domain = ".tv.sohu.com";
//		String envalue = "";
//		if(encrypt){
//			envalue = CryptoUtil.encrypt(value,WeiXinConstants.cipher_u);
//		}else{
//			envalue = value;
//		}
//		
//    	setCookie(key,envalue,maxage,domain,request,response);
//    	log.info("CookieUtil--setEncryptCookie_US suc "
//    			+ "key=" + key +"|value=" + value +"|envalue=" + envalue
//    			+ "domain=" + domain +"|maxage=" + maxage);
//  	}
  	
  	public void setEncryptCookie( HttpServletRequest request, HttpServletResponse response,String key,String value,String seek,boolean encrypt) {
        int maxage = 60*60*24;
		String domain = ".tv.sohu.com";
		String envalue = "";
		if(encrypt){
//			envalue = CryptoUtil.encrypt(value,WeiXinConstants.cipher);
			envalue = CryptoUtil.encrypt(value,seek);
		}else{
			envalue = value;
		}
		
    	setCookie(key,envalue,maxage,domain,request,response);
    	log.info("CookieUtil--setEncryptCookie_PRO suc "
    			+ "key=" + key +"|value=" + value +"|envalue=" + envalue
    			+ "domain=" + domain +"|maxage=" + maxage);
  	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
  	
}