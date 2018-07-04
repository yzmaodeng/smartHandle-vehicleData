/**
 * 
 */
package com.xinghe.xbx.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;



/**
 * @author jing
 *
 */
public class RequestUtil {
	
	public static final String ENCODING = "UTF-8";
	private static Logger log = Logger.getLogger(RequestUtil.class);

	public static short getShort(HttpServletRequest request, String key,
			short defaultValue) {
		String str = request.getParameter(key);
		return getShort(str, defaultValue);
	}

	public static int getInt(HttpServletRequest request, String key,
			int defaultValue) {
		String str = request.getParameter(key);
		return getInt(str, defaultValue);
	}

	public static long getLong(HttpServletRequest request, String key,
			long defaultValue) {
		String str = request.getParameter(key);
		return getLong(str, defaultValue);
	}

	public static String getString(HttpServletRequest request, String key,
			String defaultValue) {
		String str = request.getParameter(key);
		return getString(str, defaultValue);
	}

	public static float getFloat(HttpServletRequest request,String key,
			float defaultValue){
		String str=request.getParameter(key);
		return getFloat(str,defaultValue);
	}
	
	public static double getDouble(HttpServletRequest request,String key,
			double defaultValue){
		String str=request.getParameter(key);
		return getDouble(str,defaultValue);
	}
	
	/**
	 * 
	 * @param response
	 * @param cacheAge
	 */
	public static void setCacheHeader(HttpServletResponse response, int cacheAge) {
		response.setHeader("Pragma", "Public");
		// HTTP 1.1
		response.setHeader("Cache-Control", "max-age=" + cacheAge);
		response.setDateHeader("Expires", System.currentTimeMillis() + cacheAge
				* 1000L);
	}

	/**
	 * 
	 * @param response
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// HTTP 1.0
		response.setHeader("Pragma", "No-cache");
		// HTTP 1.1
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String dump(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();

		Enumeration names = request.getAttributeNames();
		sb.append("\nrequest attributes: \n");
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			sb.append("name = [" + name + "] value = ["
					+ request.getAttribute(name) + "]\n");
		}

		names = request.getParameterNames();
		sb.append("\nrequest parameter: \n");
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			sb.append("name = [" + name + "] value = ["
					+ request.getParameter(name) + "]\n");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String xReq = request.getHeader("X-Requested-With");
		return (xReq != null);
	}

	/**
	 * @param request
	 * @return
	 */
	public static String getRefer(HttpServletRequest request) {
		return request.getHeader("REFERER");
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String encodeURL(String url) {
		try {
			return java.net.URLEncoder.encode(url, ENCODING);
		} catch (UnsupportedEncodingException e) {
			// do nothing
			return null;
		}
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String decodeURL(String url) {
		
		if(StringUtils.isBlank(url)){
			return null;
		}
		try {
			return java.net.URLDecoder.decode(url, ENCODING);
		} catch (UnsupportedEncodingException e) {
			log.error("error decode for input string " + url);
			return null;
		} catch (IllegalArgumentException e) {
			log.error("error decode for input string " + url);
			return null;
		} catch (Exception e) {
			log.error("error decode for input string " + url);
			return null;
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestCompleteURL(HttpServletRequest request) {
		return request.getRequestURL().append("?").append(
				request.getQueryString()).toString();
	}

	/**
	 * 
	 * @param response
	 * @param name
	 * @param value
	 * @param expiry
	 * @param domain
	 * @param path
	 *            .
	 */
	public static void setCookie(HttpServletResponse response, String name,
			String value, int expiry, String domain, String path) {
		if (log.isDebugEnabled()) {
			log.debug("Setting cookie '" + name + " [ " + value
					+ " ] ' on path '" + path + "'");
		}

		Cookie cookie = new Cookie(name, value);
		cookie.setSecure(false);
		if (domain != null)
			cookie.setDomain(domain);

		cookie.setPath(path != null ? path : "/");

		cookie.setMaxAge(expiry); // 30 days

		response.addCookie(cookie);
	}

	/**
	 * 
	 * @param response
	 * @param name
	 * @param domain
	 * @param path
	 *            .
	 */
	public static void deleteCookie(HttpServletResponse response, String name,
			String domain, String path) {
		Cookie cookie = new Cookie(name, "");
		deleteCookie(response, cookie, domain, path);
	}

	/**
	 * 批量删除某个域名下的cookie
	 * 
	 * @param response
	 * @param names
	 * @param domain
	 * @param path
	 */
	public static void deleteCookies(HttpServletResponse response,
			String[] names, String domain, String path) {
		if (null == names) {
			return;
		}
		for (int i = 0; i < names.length; i++) {
			Cookie cookie = new Cookie(names[i], "");
			deleteCookie(response, cookie, domain, path);
		}

	}

	/**
	 * 
	 * @param response
	 * @param cookie
	 * @param domain
	 * @param path
	 *            .
	 */
	public static void deleteCookie(HttpServletResponse response,
			Cookie cookie, String domain, String path) {
		if (cookie != null) {
			if (log.isDebugEnabled()) {
				log.debug("Deleting cookie '" + cookie.getName()
						+ "' on domain '" + cookie.getDomain() + "' path '"
						+ path + "'");
			}
			// Delete the cookie by setting its maximum age to zero
			cookie.setMaxAge(0);
			if (domain != null)
				cookie.setDomain(domain);
			cookie.setPath(path != null ? path : "/");
			response.addCookie(cookie);
		}
	}

	/**
	 * 
	 * @param request
	 * @param name
	 * @return .
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		Cookie returnCookie = null;

		if (cookies == null) {
			return returnCookie;
		}

		for (int i = 0; i < cookies.length; i++) {
			Cookie thisCookie = cookies[i];

			if (thisCookie.getName().equals(name)) {
				// cookies with no value do me no good!
				if (!thisCookie.getValue().equals("")) {
					returnCookie = thisCookie;

					break;
				}
			}
		}

		return returnCookie;
	}

	/**
	 * 获取Client IP : 此方法能够穿透squid 和 proxy
	 * 
	 * @param request
	 * @return .
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("dian-remote");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.indexOf(",") > 0)
			ip = ip.substring(0, ip.indexOf(","));
		return ip;
	}

	/**
	 * 获取client端的实际ip地址 之所以封装，是因为需要处理使用squid的情况
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Forwarded-For");
		if (remoteAddr != null) {
			return remoteAddr.split(",")[0];
		}
		return request.getRemoteAddr();
	}

	/**
	 * 处理js escape后的串
	 * 
	 * @author wangmeng
	 * 
	 * @param src
	 * @return
	 */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 在java端模拟js escape
	 * 
	 * @author wangmeng
	 * 
	 * @param src
	 * @return
	 */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}
	
	public static short getShort(String stringNumber, short defaultValue) {
        if (stringNumber == null) {
            return defaultValue;
        }

        try {
            return Short.parseShort(stringNumber);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static int getInt(String stringNumber, int defaultValue) {
        if (stringNumber == null) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(stringNumber);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static long getLong(String stringNumber, long defaultValue) {
        if (stringNumber == null) {
            return defaultValue;
        }

        try {
            return Long.parseLong(stringNumber);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static String getString(String input, String defaultValue) {
        if (StringUtils.isBlank(input)) {
            return defaultValue.trim();
        }

        return input.trim();
    }

    public static float getFloat(String input,float defaultValue){
    	if(StringUtils.isBlank(input)){
    		return defaultValue;
    	}
    	try {
			return Float.parseFloat(input);
		} catch (Exception e) {
			return defaultValue;
		}
    }
    
    public static double getDouble(String input,double defaultValue){
    	if(StringUtils.isBlank(input)){
    		return defaultValue;
    	}
    	try {
			return Double.parseDouble(input);
		} catch (Exception e) {
			return defaultValue;
		}
    }
	/**(
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getString("", "aaaa"));
	}

}
