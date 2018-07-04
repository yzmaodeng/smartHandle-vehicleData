/*
 * Copyright (c) 2012 Sohu. All Rights Reserved
 */
package com.xinghe.xbx.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * <p>
 * 网络工具类
 * </p>
 *
 * @author Mr.Jin
 * @version 1.0
 * @Date 2012-10-10
 */
public class HttpHelper {

    private static Logger logger = Logger.getLogger(HttpHelper.class);

    private static final String userAgent = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";

    public static final int DEFAULT_TIMEOUT = 2000;

    public static String doGet(String url) {
        return doGet(url, HTTP.UTF_8, DEFAULT_TIMEOUT);
    }

    public static String doGet(String url, int timeout) {
        return doGet(url, HTTP.UTF_8, timeout);
    }

    public static String doGet(String url, String encode) {
        return doGet(url, encode, DEFAULT_TIMEOUT);
    }

    public static String doGet(String url, String encode, int timeout) {
        HttpURLConnection conn = null;
        ByteArrayOutputStream output;
        URL u;
        try {
            u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(timeout);
            InputStream in = conn.getInputStream();
            output = new ByteArrayOutputStream();
            byte buffer[] = new byte[4096];
            for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;)
                output.write(buffer, 0, bytesRead);
            return output.toString(encode);
        } catch (Exception e) {
        	e.printStackTrace();
            logger.error("call http get error:", e);
        } finally{
            if(conn != null){
                conn.disconnect();
            }
        }
        return null;
    }


    public static String doPost(String url, Map<String, String> params){
        return doPost(url, params,  HTTP.UTF_8, DEFAULT_TIMEOUT);
    }

    public static String doPost(String url, Map<String, String> params, int timeout){
        return doPost(url, params,  HTTP.UTF_8, timeout);
    }


    public static String doPost(String url, Map<String, String> params, String encode){
        return doPost(url, params, encode, DEFAULT_TIMEOUT);
    }

    public static String doPost(String url, Map<String, String> params, String encode, int timeout){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", userAgent);
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        String result = "";
        for(String key : keySet) {
            postParams.add(new BasicNameValuePair(key, params.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, encode));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            logger.error("call http post error:", e);
        }finally{
            if(httpClient != null){
                httpClient.getConnectionManager().shutdown();
            }
        }
        return result;
    }


    /**
     * 获取二进制流
     */
    public static byte[] getBinary(String url) {
        HttpURLConnection conn = null;
        ByteArrayOutputStream output;
        URL u;
        try {
            u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            InputStream in = conn.getInputStream();
            output = new ByteArrayOutputStream();
            byte buffer[] = new byte[4096];
            for (int bytesRead = 0; (bytesRead = in.read(buffer)) != -1;)
                output.write(buffer, 0, bytesRead);
            conn.disconnect();
            return output.toByteArray();
        } catch (Exception e) {
            logger.info("getBinary error" ,e);
        }
        return null;
    }

//    public static String post(String url, Map<String, String> params) {
//        PostMethod postMethod = null;
//        try {
//
//            HttpClient client = new HttpClient();
//
//            postMethod = new PostMethod(url);
//            postMethod.addRequestHeader("Accept",
//                    "textml,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            postMethod
//                    .addRequestHeader(
//                            "User-Agent",
//                            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36");
//            postMethod.addRequestHeader("Connection", "keep-alive");
//            Set<String> keySet = params.keySet();
//            String result = "";
//            for (String key : keySet) {
//                postMethod.setParameter(key, params.get(key));
//            }
//            int status = client.executeMethod(postMethod);
//            InputStream is = postMethod.getResponseBodyAsStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    is));
//            StringBuffer stringBuffer = new StringBuffer();
//            String str = "";
//            while ((str = br.readLine()) != null) {
//                stringBuffer.append(str);
//            }
//            return stringBuffer.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (postMethod != null) {
//                postMethod.releaseConnection();
//            }
//        }
//        return null;
//    }
   

}
