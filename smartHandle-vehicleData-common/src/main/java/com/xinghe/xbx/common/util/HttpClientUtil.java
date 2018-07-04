package com.xinghe.xbx.common.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;  
/* 
 * 利用HttpClient进行 https get post请求的工具类
 * java 原生 调用 https 的方法 一直不成功 因此整理  httpClient 方法
 * 目前 java 原生 调用 https 的方法 已成功 ，从执行效率上考虑 建议直接用原生方法
 */

@SuppressWarnings({"rawtypes","unchecked"})
public class HttpClientUtil {
	
	private static HttpClient httpClient = null; ;
	public static HttpClient getHttpClient(int readTimeout){
		 if(httpClient == null){          
	          synchronized (HttpClient.class) {         
	             if(httpClient == null){
	            	 HttpParams httpParameters = new BasicHttpParams();  
	                 HttpConnectionParams.setConnectionTimeout(httpParameters, readTimeout);//设置请求超时 秒  
	                 HttpConnectionParams.setSoTimeout(httpParameters, readTimeout); //设置等待数据超时 秒  
	                 HttpConnectionParams.setSocketBufferSize(httpParameters, 8192);
	                 try {
						httpClient = new SSLClient(httpParameters);
	                 } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
	                 }             
	             }          
	          }       
	       }
	   return httpClient;
	}
	
	
	public static String doGet(String url, int readTimeout) {
		return doGet( url,null,readTimeout,"utf-8");
	}
	
	public static String doPost(String url, int readTimeout) {
		return doPost( url,null,readTimeout,"utf-8");
	}
	
	public static String doPost(String url,Map<String,String> map,int readTimeout,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{
        	
            httpClient = getHttpClient(readTimeout);
            
//            httpClient = new SSLClient();  
            httpPost = new HttpPost(url);  
            //设置参数  
            if(map!=null && !map.isEmpty()){
                List<NameValuePair> list = new ArrayList<NameValuePair>();  
                Iterator iterator = map.entrySet().iterator();  
                while(iterator.hasNext()){  
                    Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                    list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
                }  
                if(list.size() > 0){  
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                    httpPost.setEntity(entity);  
                }
            }

            
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity(); 
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }
    

    
	public static String doGet(String url,Map<String,String> map,int readTimeout,String charset) {
    	HttpClient httpClient = null;  
    	HttpGet httpGet = null;  
        String result = null;  
        String param = "";
        HttpResponse response = null;
        try{
            httpClient = getHttpClient(readTimeout);
            //设置参数 
            if(map!=null && !map.isEmpty()){
            	List<NameValuePair> list = new ArrayList<NameValuePair>();  
                Iterator iterator = map.entrySet().iterator();  
                while(iterator.hasNext()){
                    Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                    list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
                }
                
                
                if(list.size() > 0){  
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
//                    httpPost.setEntity(entity);  
                    param = EntityUtils.toString(entity);
                    System.out.println(param);  
                } 
            }
            
            
            //创建Get请求  
            httpGet = new HttpGet(url+"?"+param); 
            
            response = httpClient.execute(httpGet);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        } 
        return result;  
    }
}
