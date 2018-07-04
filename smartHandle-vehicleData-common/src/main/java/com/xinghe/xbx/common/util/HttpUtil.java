package com.xinghe.xbx.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.log4j.Logger;


public final class HttpUtil {

//	public static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();

//	private static int connectionTimeOut = 1000;
//
//	private static int socketTimeOut = 6000;
//
//	private static int maxConnectionPerHost = 10;
//
//	private static int maxTotalConnections = 10;

//	private static HttpClient httpClient;

	private static Logger log = Logger.getLogger( HttpUtil.class );

//	static {
//		manager.getParams().setConnectionTimeout( connectionTimeOut );
//		manager.getParams().setSoTimeout( socketTimeOut );
//		manager.getParams().setDefaultMaxConnectionsPerHost( maxConnectionPerHost );
//		manager.getParams().setMaxTotalConnections( maxTotalConnections );
//
//	}

	public static class HttpResponse {
		public String content;
		public LinkedList<String> cookieStringList = new LinkedList<String>();
	}

	 

	public static String doInnerPost( String url, String param, int retryNum ,int readTimeout) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doPost( url, param,readTimeout );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry POST num:" + i );
			}
		}
	}
	/**
	 * 
	 * @param url  请求地址
	 * @param requestMethod  请求方式（GET）
	 * @param readTimeout  超时时间
	 * @param retryNum  重复提交的次数
	 * @return
	 * @throws Exception 
	 */
	public static String doInnerHttpsPost(String url, String param ,int retryNum,int readTimeout) throws Exception{
		for ( int i = 1;; ++i ) {
			try {
				return doHttpsPost(url,param, readTimeout, "utf-8");
			} catch ( Exception e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry HTTPS POST num:" + i );
			}
		}
	}
	/**
	 * 
	 * @param url  请求地址
	 * @param requestMethod  请求方式（GET）
	 * @param readTimeout  超时时间
	 * @param retryNum  重复提交的次数
	 * @return
	 * @throws Exception 
	 */
	public static String doInnerHttpsPost(String url, String param ,int retryNum) throws Exception{
		for ( int i = 1;; ++i ) {
			try {
//				return httpsRequest( url, requestMethod,outputStr );
				return doHttpsPost(url,param, 3000, "utf-8");
			} catch ( Exception e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry HTTPS POST num:" + i );
			}
		}
	}
	public static String doInnerPostWithCS( String url, String param, int retryNum, int timeOut,String charSet ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doPostWithCS( url, param, timeOut,charSet );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry POST num:" + i );
			}
		}
	}
	public static String doInnerPostWithCS( String url, String param, int retryNum, String charSet ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doPostWithCS( url, param, charSet );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry POST num:" + i );
			}
		}
	}

	public static String doInnerPost( String url, String param, int retryNum, String cookies ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doPost( url, param, cookies );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry POST num:" + i );
			}
		}
	}
	private static String doPostWithCS( String url, String param, Integer timeOut,String charSet) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( timeOut );
			connection.setReadTimeout( 3000 );
			connection.setRequestMethod( "POST" );
			connection.setInstanceFollowRedirects( true );
			connection.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)" );
			connection.setDoInput( true );
			connection.setDoOutput( true );
			writer = new PrintWriter( connection.getOutputStream() );
			writer.print( param );
			writer.flush();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), charSet ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( writer != null ) {
				writer.close();
			}
			if ( connection != null )
				connection.disconnect();
		}
	}
	private static String doPostWithCS( String url, String param, String charSet ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 3000 );
			connection.setReadTimeout( 3000 );
			connection.setRequestMethod( "POST" );
			connection.setInstanceFollowRedirects( true );
			connection.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)" );
			connection.setDoInput( true );
			connection.setDoOutput( true );
			writer = new PrintWriter( connection.getOutputStream() );
			writer.print( param );
			writer.flush();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), charSet ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( writer != null ) {
				writer.close();
			}
			if ( connection != null )
				connection.disconnect();
		}
	}

	private static String doPost( String url, String param,int readTimeout ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 3000 );
			connection.setReadTimeout( readTimeout );
			connection.setRequestMethod( "POST" );
			connection.setInstanceFollowRedirects( true );
			connection.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)" );
			connection.setDoInput( true );
			connection.setDoOutput( true );
			writer = new PrintWriter( connection.getOutputStream() );
			writer.print( param );
			writer.flush();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( writer != null ) {
				writer.close();
			}
			if ( connection != null )
				connection.disconnect();
		}
	}

	private static String doPost( String url, String param, String cookies ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 1000 );
			connection.setReadTimeout( 1000 );
			connection.setRequestMethod( "POST" );
			connection.setInstanceFollowRedirects( true );
			connection.setRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)" );
			connection.setRequestProperty( "Cookie", cookies );
			connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );
			connection.setDoInput( true );
			connection.setDoOutput( true );
			writer = new PrintWriter( connection.getOutputStream() );
			// writer.print( URLEncoder.encode(param,"utf-8") );
			writer.print( param );
			writer.flush();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( writer != null ) {
				writer.close();
			}
			if ( connection != null )
				connection.disconnect();
		}
	}

	public static String doInnerGet( String url, int retryNum ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doGet( url );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry GET num:" + i );
			}
		}
	}

	public static String doInnerGet( String url, int retryNum, String charset ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doGet( url, 30000, charset );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry GET num:" + i );
			}
		}
	}

	public static String doInnerGet( String url, int retryNum, int readTimeout ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doGet( url, readTimeout );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					log.info("INFO-->HttpUtil-->doInnerGet |param:url="+url+" |retryNum="+retryNum+"|readTimeout="+readTimeout);
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
			}
		}
	}
	public static String doInnerGet( String url, int retryNum , int connectTimeout, int readTimeout) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doGet( url, connectTimeout,readTimeout );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					log.info("INFO-->HttpUtil-->doInnerGet |param:url="+url+" |retryNum="+retryNum+"|readTimeout="+readTimeout);
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
			}
		}
	}
	public static String doInnerHttpsGet( String url, int retryNum, int readTimeout) throws Exception {
		for ( int i = 1;; ++i ) {
			try {
//				return doGet( url, 3000, charset );
				return doHttpsGet(url,null,readTimeout, "utf-8");
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
				log.info( "retry GET num:" + i );
			}
		}
	}
	
	public static HttpResponse doInnerGetResponse( String url, int retryNum ) throws IOException {
		for ( int i = 1;; ++i ) {
			try {
				return doGetResponse( url );
			} catch ( IOException e ) {
				if ( i == retryNum )
					throw e;
				try {
					Thread.sleep( 10 );
				} catch ( InterruptedException e1 ) {
				}
			}
		}
	}

	private static HttpResponse doGetResponse( String url ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 5000 );
			connection.setReadTimeout( 1000 );
			connection.setRequestMethod( "GET" );
			connection.setInstanceFollowRedirects( false );
			connection.connect();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			reader.close();
			connection.disconnect();

			// build an instance of HttpResponse
			HttpResponse response = new HttpResponse();
			String key;
			for ( int i = 1; ( key = connection.getHeaderFieldKey( i ) ) != null; i++ ) {
				if ( key.equalsIgnoreCase( "set-cookie" ) ) {
					String cookieVal = connection.getHeaderField( i );
// cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
					response.cookieStringList.add( cookieVal );
				}
			}
			response.content = sb.toString();
			return response;
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( connection != null )
				connection.disconnect();
		}
	}

	private static String doGet( String url ) throws IOException {
		return doGet( url, 8000 );
	}
	private static String doGet( String url, int connectTimeout ,int readTimeout ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( connectTimeout );
			connection.setReadTimeout( readTimeout );
			connection.setRequestMethod( "GET" );
			connection.setInstanceFollowRedirects( true );
			connection.connect();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), "utf-8" ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( connection != null )
				connection.disconnect();
		}
	}
	private static String doGet( String url, int readTimeout ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 8000 );
			connection.setReadTimeout( readTimeout );
			connection.setRequestMethod( "GET" );
			connection.setInstanceFollowRedirects( true );
			connection.connect();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), "utf-8" ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( connection != null )
				connection.disconnect();
		}
	}

	private static String doGet( String url, int readTimeout, String charset ) throws IOException {
		URL url1 = null;
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		try {
			url1 = new URL( url );
			connection = (HttpURLConnection) url1.openConnection();
			connection.setConnectTimeout( 3000 );
			connection.setReadTimeout( readTimeout );
			connection.setRequestMethod( "GET" );
			connection.setInstanceFollowRedirects( true );
			connection.connect();
			reader = new BufferedReader( new InputStreamReader( connection.getInputStream(), charset ) );
			StringBuffer sb = new StringBuffer();
			String line;
			while ( ( line = reader.readLine() ) != null ) {
				sb.append( line ).append( "\n" );
			}
			reader.close();
			connection.disconnect();
			return sb.toString();
		} finally {
			if ( reader != null ) {
				try {
					reader.close();
				} catch ( IOException e1 ) {
				}
			}
			if ( connection != null )
				connection.disconnect();
		}
	}
	
    /** 
     * 发送https请求共用体  
     */  
	public static String doHttpsPost(String url,String param,int readTimeout, String charset) throws Exception{  
        //请求结果 
        PrintWriter out = null;  
        BufferedReader br = null; 
        InputStreamReader inr = null;
        InputStream in = null;
        URL realUrl = null;  
        HttpsURLConnection conn = null;  
        String result = "";  
        //请求参数获取  
        String postpar = "";  
        //字符串请求参数  
        if(param != null){  
            postpar = param;  
        }
        
        SSLSocketFactory ssf= XBXX509TrustManager.getSSFactory();  
        try {  
            realUrl= new URL(url);  
            conn = (HttpsURLConnection)realUrl.openConnection();  
            conn.setSSLSocketFactory(ssf); 
            
            conn.setConnectTimeout( 3000 );
            conn.setReadTimeout(readTimeout);
			
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            
            conn.setDoOutput(true);  
            conn.setDoInput(true);
            conn.setUseCaches(false);
            out = new PrintWriter(conn.getOutputStream());  
            out.print(postpar);  
            out.flush();
            
            StringBuffer buffer = new StringBuffer(); 
            in = conn.getInputStream();    
            inr = new InputStreamReader(in, charset);    
            br = new BufferedReader(inr);    
            String str = null;    
            while ((str = br.readLine()) != null) {    
                buffer.append(str);    
            } 
             
            result = buffer.toString();
            
        }
//        catch(Exception e){
//        	e.printStackTrace();
//        }
        
        finally {  
            try {
                if (br != null) {  
                	br.close();
                	br = null;
                } 
                if (inr != null) {  
                	inr.close();
                	br = null;
                } 
                
                if (in != null) {  
                	in.close();
                	br = null;
                } 
                
                if (out != null) {  
                    out.close();
                    out = null;
                }  
 
                if (conn != null) {  
                	conn.disconnect();
                	conn = null;
                } 
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        
        return result;  
    }  
    
    
    /** 
     * 发送https请求共用体  
     */
	public static String doHttpsGet(String url,String param,int readTimeout, String charset) throws Exception{  
        //请求结果 
 
        BufferedReader br = null; 
        InputStreamReader inr = null;
        InputStream in = null;
        URL realUrl = null;  
        HttpsURLConnection conn = null;  
		
		

        String result = ""; 

        //查询地址  
        String queryString = url;  
        //请求参数获取  
        String postpar = "";  
        //字符串请求参数  
        if(param!=null){
            postpar = param;
            queryString += "?" + param;
        }
        
        SSLSocketFactory  ssf= XBXX509TrustManager.getSSFactory();  
        try {  
            realUrl= new URL(queryString);  
            conn = (HttpsURLConnection)realUrl.openConnection();  
            conn.setSSLSocketFactory(ssf); 
            
            conn.setConnectTimeout( 3000 );
            conn.setReadTimeout( readTimeout );
            
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
            
            conn.connect();  
            
            StringBuffer buffer = new StringBuffer(); 
            in = conn.getInputStream();    
            inr = new InputStreamReader(in, charset);    
            br = new BufferedReader(inr);    
            String str = null;    
            while ((str = br.readLine()) != null) {    
                buffer.append(str);    
            } 
             
            result = buffer.toString();
             
              
        }catch(Exception e){
        	e.printStackTrace();
        }
        
        finally {  
            try {  
            	if (br != null) {  
                	br.close();
                	br = null;
                } 
                if (inr != null) {  
                	inr.close();
                	br = null;
                } 
                
                if (in != null) {  
                	in.close();
                	br = null;
                } 
                
                if (conn != null) {  
                	conn.disconnect();
                	conn = null;
                } 
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        
        return result;  
    }  
}
