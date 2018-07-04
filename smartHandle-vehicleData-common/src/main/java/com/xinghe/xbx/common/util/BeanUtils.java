package com.xinghe.xbx.common.util;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes","unchecked"})
public class BeanUtils extends org.apache.commons.beanutils.BeanUtils {
   
	public static List<Object>  listTransformation(String className,List objectList) {
	   if(objectList!=null&&objectList.size()>0){
			List<Object>newObjectList=new ArrayList<Object>();
			for(Object Object:objectList){
				Object newoObject=(Object)BeanUtils.objectTransformation(className, Object);
				newObjectList.add(newoObject);
			}
			return newObjectList;
		}
	   return null;
	}
   
   public static Object  objectTransformation(String className,Object dest) {
	   try {
		   Object object = createObject(className);
		   copyProperties(object, dest);
		   return object;
	   } catch (Exception e) {
		// TODO Auto-generated catch block
		   e.printStackTrace();
		   return null;
	   }
   }
   //
   public static Object  createObject(String className) {
	   try {
		   if(className.contains("class")){
			   className=className.replaceFirst("class ", "");
		   }
		   if(className.contains("List")){
			   className="java.util.ArrayList";
		   }
		   Object object = (Object)Class.forName(className).newInstance();
		   return object;
	   } catch (Exception e) {
		   e.printStackTrace();
		   return null;
	   }
   }
   
   public static void copyProperties(Object dest, Object orig) {
       try {
    	   org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }
   
   
public static List<Map> getBeanProperties(Object bean) {
	   List <Map> propertiesList=new ArrayList<>();
       Class beanClass = (Class)bean.getClass();
       Field[] fieds = beanClass.getDeclaredFields();
       for(int i = 0 ; i < fieds.length; i++){
           Field f = fieds[i];
           f.setAccessible(true); //设置些属性是可以访问
           Map propMap=new HashMap();
           propMap.put(f.getName(),f.getType());
           propertiesList.add(propMap);
       }
       Class supperClass = (Class)beanClass.getSuperclass();
       Field[] supperFied = supperClass.getDeclaredFields();
       for(int i = 0 ; i < supperFied.length; i++){
           Field f = supperFied[i];
           f.setAccessible(true); //设置些属性是可以访问
           Map propMap=new HashMap();
           propMap.put(f.getName(),f.getType());
           propertiesList.add(propMap);
       }
       return propertiesList;
   }
   public static void setBeanPropertie(Object obj, String att, Object value, Class<?>type){
	   try { 
		   if(type.toString().endsWith("sql.Date")){
			   java.sql.Date sqlDate=(java.sql.Date)value;
			   Date date=new Date(sqlDate.getTime());
			   Method met = obj.getClass().getMethod("set" + initStr(att), date.getClass()); 
			   met.invoke(obj, date);  
		   }if(type.toString().endsWith("Timestamp")){
			   java.sql.Timestamp sqlDate=(java.sql.Timestamp)value;
			   Date date=new Date(sqlDate.getTime());
			   Method met = obj.getClass().getMethod("set" + initStr(att), date.getClass()); 
			   met.invoke(obj, date);  
		   }else if(type.toString().endsWith("Integer")&&value!=null){
			   int intValue=(Integer)value;
			   Method met = obj.getClass().getMethod("set" + initStr(att), int.class); 
			   met.invoke(obj, intValue);  
		   }else if(type.toString().endsWith("BigDecimal")&&value!=null){
			   int intValue=(Integer)value;
			   Method met = obj.getClass().getMethod("set" + initStr(att), int.class); 
			   met.invoke(obj, intValue);  
		   }else{
			   Method met = obj.getClass().getMethod("set" + initStr(att), type); 
			  
			   met.invoke(obj, value);  
			  
		   }
            
           
       }catch (Exception e){  
           e.printStackTrace();  
       }  
	   
   }
   public static Object getFieldValueByName(String fieldName, Object o) {
		try {
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + fieldName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			return value;
		} catch (Exception e) {
			return null;
		}
	}
   public static String initStr(String old){   
       String str = old.substring(0,1).toUpperCase() + old.substring(1) ;  
       return str ;  
   }  
}
