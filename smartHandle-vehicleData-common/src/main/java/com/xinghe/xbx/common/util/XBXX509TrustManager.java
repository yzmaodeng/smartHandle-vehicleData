package com.xinghe.xbx.common.util;

import java.security.KeyManagementException;  
import java.security.NoSuchAlgorithmException;  
import java.security.NoSuchProviderException;  
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLSocketFactory;  
import javax.net.ssl.TrustManager;  
import javax.net.ssl.X509TrustManager;  
public class XBXX509TrustManager   implements X509TrustManager {  
  
    public XBXX509TrustManager(){
    	
    }
    
    @Override  
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)  
            throws CertificateException {  
        // TODO Auto-generated method stub  
          
    }  
  
    @Override  
    public void checkServerTrusted(X509Certificate[] arg0, String arg1)  
            throws CertificateException {  
        // TODO Auto-generated method stub  
          
    }  
  
    @Override  
    public X509Certificate[] getAcceptedIssuers() {  
        // TODO Auto-generated method stub  
        return null;  
    }  
      
    public static SSLSocketFactory getSSFactory() throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException{  
        TrustManager[] tm = { new XBXX509TrustManager()};  
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
//        SSLSocketFactory ssf = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
        sslContext.init(null, tm, new java.security.SecureRandom());  
        SSLSocketFactory ssf = sslContext.getSocketFactory();  
        return  ssf;  
    }  
}  
