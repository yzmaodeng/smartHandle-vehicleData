package com.xinghe.xbx.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinghe.xbx.common.util.SpringUtil;
import com.xinghe.xbx.service.util.sequence.SequenceService;

public class SequenceHelper {
	private static final Log log = LogFactory.getLog(SequenceHelper.class);
	public static final String defaultSeq ="default_seq";
	public static final String HGZ_seq ="HGZ_seq"; //车型的的id
	
	public static final Long MAXID =999999999L; //取最大的 表的下标  
	//单例
	private static SequenceHelper instance = null;
	
	private static SequenceService sequenceService = null;
	public static SequenceHelper getInstance(){
		 if(instance == null){          
	          synchronized (SequenceHelper.class) {         
	             if(instance == null){           
	            	 instance = new SequenceHelper();  
	            	 if(sequenceService == null){
	            		 sequenceService = (SequenceService)SpringUtil.getBean("sequenceService");
	            	 }
	             }          
	          }       
	       }      
	   return instance;
	 }
	//返回Id 主键
    public static Long getId(String key) {
        return sequenceService.nextValue(key);//自动生成的id做主键
    }
}
