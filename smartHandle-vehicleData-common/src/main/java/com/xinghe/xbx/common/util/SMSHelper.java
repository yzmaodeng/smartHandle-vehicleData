package com.xinghe.xbx.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.xinghe.xbx.common.constant.CommonConstantConfig;

public class SMSHelper {

	private static final Logger log = Logger.getLogger(SMSHelper.class);
	
	
	private static SMSHelper instance = null;
	 
//	public static int number=1;//记录重发的次数
	
	private String smsName;
	private String smsPwd;
	
	public static SMSHelper getInstance(){
		if (instance == null) {
			synchronized (SMSHelper.class) {
				if (instance == null) {
					instance = new SMSHelper();
				}
			}
		}
		return instance;
	}
	/**
	* 调用短信接口
	* @param postData
	* @param postUrl
	* @return
	* @throws UnsupportedEncodingException 
	*/
	public String SMS(String phone,String msgModel)  {
	  String postUrl="http://cf.51welink.com/submitdata/service.asmx/g_Submit";
	//  String code = RandomHelper.generateNum(4); 
//	  String msgModel="您的验证码是:"+code+"。请不要把验证码泄露给其他人。【微网通联】";
	  String postData;	
	  try {
//		  postData = "sname=DL-liulimei&spwd=liulimei123&scorpid=&sprdid=1012888&sdst="+phone+"&smsg="+java.net.URLEncoder.encode(msgModel,"utf-8");
		  postData = "sname=" + smsName + "&spwd=" + smsPwd + "&scorpid=&sprdid=1012888&sdst="+phone+"&smsg="+java.net.URLEncoder.encode(msgModel,"utf-8");
	          //发送POST请求
	           URL url = new URL(postUrl);
	           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	           conn.setRequestMethod("POST");
	           conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	           conn.setRequestProperty("Connection", "Keep-Alive");
	           conn.setUseCaches(false);
	           conn.setDoOutput(true);
	           conn.setRequestProperty("Content-Length", "" + postData.length());
	           OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
	           out.write(postData);
	           out.flush();
	           out.close();
	           //获取响应状态
	          if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
	               System.out.println("connect failed!");
	               return "";
	          }
	
	           //获取响应内容体
	           String line, result = "";
	           BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	           while ((line = in.readLine()) != null) {
	              result += line + "\n";
	           }
	           in.close();
	           return result;
	       }catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
	           e.printStackTrace(System.out);
	       }
	       return "";
	   }
	/**
	 * 解析返回状态信息
	 * @param xml
	 * @return
	 */
	public  Map<String,Object> dom4jXml(String xml) throws Exception{
		Map<String,Object>map=new HashMap<String, Object>();
		try {
	//		SAXReader reader=new SAXReader();
		    Document doc;
		    doc=DocumentHelper.parseText(xml);
		    Element root=doc.getRootElement();
		    Element state = root.element("State");  
		    Element msgId=root.element("MsgID");
		    Element msgState=root.element("MsgState");
		    Element reserve=root.element("Reserve"); 
		    map.put("state",state.getTextTrim());
		    map.put("msgId", msgId.getTextTrim());
		    map.put("msgState", msgState.getTextTrim());
		    map.put("reserve", reserve.getTextTrim());
		    return map;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception->SMSHelper--dom4jXml-->"+"param:"+xml);
		}
		return null;
	}
//	/**
//	 * 验证发送失败逻辑
//	 * @param retryIndex
//	 * @param map
//	 * @param phone
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
//	public  Map<String,Object> multipleSend(int retryIndex, Map<String,Object> map,String phone,
//		 String msgModel) throws Exception{
//	 String value=(String) map.get("state");
//	 if(("0").equals(value)){
//		 return map;
//	 }else if(("-99").equals(value)){
//		 // 重试次数索引值
//		 if(retryIndex <= 3){
//			 log.info("retry:"+retryIndex+"SMSHelper--multipleSend-->"+"param:"+map+"--"+phone);
//			 retryIndex++;
//			 multipleSend(retryIndex,
//					 dom4jXml(SMS(phone,msgModel)),phone,msgModel);
//		 }
//	 }else if(value.equals("2001")){
//		 return map;
//	 }else{
//		 return map;
//	 }
//	 return map;
//	}
	
	/**
	 * 验证发送失败逻辑
	 * @param retryIndex 失败重试次数 (首次传值：1)
	 * @param phone 发送电话号码
	 * @param msgModel 消息体
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> multipleSend(Integer retryIndex,String phone,String msgModel) throws Exception{
		String strXml= SMS(phone, msgModel);
		Map<String,Object> map = dom4jXml(strXml);
		if(map != null){
			String value=(String) map.get("state");
			if(("0").equals(value)){
				 log.info("SMSHelper--multipleSend-->send success !");
				 return map;
			}else if(("-99").equals(value)){
				// 重试次数索引值
				if(retryIndex != null && 
						 retryIndex.intValue() <= CommonConstantConfig.SMS_RETRY_LIMIT.intValue()){
					log.info("SMSHelper--multipleSend-->" + "retry:"+retryIndex +"-->param: "
					 	+ "phone="+phone+",msgModel="+msgModel);
					retryIndex++;
					multipleSend(retryIndex, phone, msgModel);
				}	 
			}
			log.info("SMSHelper--multipleSend-->returnCode:" + value);
		}
		return map;
	}
	
	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}
	
	public void setSmsPwd(String smsPwd) {
		this.smsPwd = smsPwd;
	}
	 
}
