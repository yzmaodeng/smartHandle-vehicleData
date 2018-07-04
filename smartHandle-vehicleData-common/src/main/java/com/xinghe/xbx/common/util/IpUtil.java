package com.xinghe.xbx.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtil {

	private static final Logger LOG = LoggerFactory.getLogger(IpUtil.class);
	
	private static IpUtil ipUtil = null;
	public static IpUtil getInstance(){
		 if(ipUtil == null){          
	          synchronized (IpUtil.class) {         
	             if(ipUtil == null){           
	            	 ipUtil = new IpUtil();               
	             }          
	          }       
	       }      
	   return ipUtil;
	}
	public String getActualIp(HttpServletRequest request){
//        String ip = request.getParameter("ip");
//        if (ip==null||ip.equals("")) {
//            ip = request.getHeader("X-Forwarded-For");//inx处理请求的时候赋值给header
//        }
//        if (ip==null||ip.equals("")){
//            ip = request.getRemoteAddr();
//        }
//        return ip;
		 String ipAddress = null;   
         //ipAddress = request.getRemoteAddr();   
         ipAddress = request.getHeader("x-forwarded-for");   
         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
          ipAddress = request.getHeader("Proxy-Client-IP");   
         }   
         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
             ipAddress = request.getHeader("WL-Proxy-Client-IP");   
         }   
         if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
          ipAddress = request.getRemoteAddr();   
          if(ipAddress.equals("127.0.0.1")){   
           //根据网卡取本机配置的IP   
           InetAddress inet=null;   
        try {   
         inet = InetAddress.getLocalHost();   
        } catch (UnknownHostException e) {   
         e.printStackTrace();   
        }   
        ipAddress= inet.getHostAddress();   
          }   
                
         }   
         //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
         if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
             if(ipAddress.indexOf(",")>0){   
                 ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
             }   
         }   
         return ipAddress;    
    }
	/**
	 * 
	* @date 2018年1月2日 下午4:41:38
	* @Title: getIpAddr 
	* @Description: 获取外网IP
	* @param request
	* @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static  String getIpAddr(HttpServletRequest request) {   
        String ipAddress = null;   
        InetAddress inet=null;  
        //ipAddress = request.getRemoteAddr();   
        ipAddress = request.getHeader("x-forwarded-for");   
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
        	ipAddress = request.getHeader("Proxy-Client-IP");   
        }   
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
            ipAddress = request.getHeader("WL-Proxy-Client-IP");   
        }   
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {   
	        ipAddress = request.getRemoteAddr();   
	           if(ipAddress.equals("127.0.0.1")|| ipAddress.equals("0:0:0:0:0:0:0:1")){
	           //根据网卡取本机配置的IP   
		       try {   
		           inet = InetAddress.getLocalHost();   
		       } catch (UnknownHostException e) {   
		           e.printStackTrace();   
		       }   
		       ipAddress= inet.getHostAddress();   
	        }   
       }   
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割   
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15   
            if(ipAddress.indexOf(",")>0){   
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));   
            }   
        }  
        return ipAddress;    
     } 
}
