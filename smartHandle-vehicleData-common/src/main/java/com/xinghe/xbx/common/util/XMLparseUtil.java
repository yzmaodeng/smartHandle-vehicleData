package com.xinghe.xbx.common.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.thoughtworks.xstream.XStream;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
@SuppressWarnings({"unchecked","rawtypes"})
public class XMLparseUtil {

	 //打log用
	private static final Log logger = LogFactory.getLog(XMLparseUtil.class ); 

    /**
     * 通过反射的方式遍历对象的属性和属性值，方便调试
     *
     * @param o 要遍历的对象
     * @throws Exception
     */
    public static void reflect(Object o) throws Exception {
		Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            XMLparseUtil.log(f.getName() + " -> " + f.get(o));
        }
    }

    public static byte[] readInput(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.close();
        in.close();
        return out.toByteArray();
    }

    public static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }


    public static InputStream getStringStream(String sInputString) {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
        }
        return tInputStringStream;
    }

    public static Object getObjectFromXML(String xml, Class tClass) {
        //将从API返回的XML数据映射到Java对象
        XStream xStreamForResponseData = new XStream();
        xStreamForResponseData.alias("xml", tClass);
        xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
        return xStreamForResponseData.fromXML(xml);
    }

    public static String getStringFromMap(Map<String, Object> map, String key, String defaultValue) {
        if (key == "" || key == null) {
            return defaultValue;
        }
        String result = (String) map.get(key);
        if (result == null) {
            return defaultValue;
        } else {
            return result;
        }
    }

    public static int getIntFromMap(Map<String, Object> map, String key) {
        if (key == "" || key == null) {
            return 0;
        }
        if (map.get(key) == null) {
            return 0;
        }
        return Integer.parseInt((String) map.get(key));
    }

    /**
     * 打log接口
     * @param log 要打印的log字符串
     * @return 返回log
     */
    public static String log(Object log){
        logger.info(log.toString());
        //System.out.println(log);
        return log.toString();
    }

    /**
     * 读取本地的xml数据，一般用来自测用
     * @param localPath 本地xml文件路径
     * @return 读到的xml字符串
     */
    public static String getLocalXMLString(String localPath) throws IOException {
        return XMLparseUtil.inputStreamToString(XMLparseUtil.class.getResourceAsStream(localPath));
    }
    /**
     * 解析通知的参数
     * @param request
     * @return
     */
    public static Map<String,String> parseXml(HttpServletRequest request){
		  
		  Map<String,String> messageMap=new HashMap<String, String>();
		   
		  InputStream inputStream=null;
		  try {
		      //读取request Stream信息
		      inputStream=request.getInputStream();
		   
	          SAXReader reader = new SAXReader();
		      Document document=null;
		 
		      document =  reader.read(inputStream);
		   
		      Element root=document.getRootElement();
		  
		  
		      List<Element> elementsList=root.elements();
		   
			  for(Element e:elementsList){
			      messageMap.put(e.getName(),e.getText());
			  }
		  
		      inputStream.close();
		      inputStream=null;
		  } catch (Exception e) {
		      e.printStackTrace();
		  }
		  return messageMap;
	}
    public static String parseXmls(HttpServletRequest request){  
		try {
			InputStream inputStream = request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;  
    }
}
