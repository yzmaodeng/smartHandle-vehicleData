package com.xinghe.xbx.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@SuppressWarnings("static-access")
public class IdGeneratorHelper {
	private static final Log log = LogFactory.getLog(IdGeneratorHelper.class);

	private static IdGeneratorHelper instance = null;
	public static IdGeneratorHelper getInstance(){
		 if(instance == null){          
	          synchronized (IdGeneratorHelper.class) {         
	             if(instance == null){           
	            	 instance = new IdGeneratorHelper();               
	             }          
	          }       
	       }      
	   return instance;
	 }
	 
	/**
	 * 生成订单id
	 * 订单号规则：
	 * 流水号+业务类型+混淆码
	 * 业务类型 2位数字字符串（必须2位）
	 * @param type
	 * @return
	 */
	public  Long getDefaultSeq(Integer type) {
    	
    	Long id = SequenceHelper.getInstance().getId(SequenceHelper.defaultSeq);
    	
    	if(id == null || type == null){
    		log.error("id == " + id + " || type ==" + type);
    		return null;
    	}
    	
    	if(type < 10 || type > 99){
    		log.error("type < 10 || type > 99 id=" + id);
    		return null;
    	}
    	
        int random = (int)(Math.random()*90) + 10;//确保 两位数随机

        return id * 10000 +  type * 100 + random;
    }

	/** 
	* @Title: getHGZId 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param i
	* @param @return    入参
	* @return Long    返回类型
	* @author 臧亮 
	* @throws
	* @date 2018年4月3日 下午1:42:34 
	* @version V1.0   
	*/
	public Long getHGZId(Integer hgzType) {
		Long id = SequenceHelper.getInstance().getId(SequenceHelper.HGZ_seq);
    	if(id == null || hgzType == null){
    		log.error("id == " + id + " || type ==" + hgzType);
    		return null;
    	}
    	
    	if(hgzType < 1 || hgzType > 9){
    		log.error("type < 1 || type > 9 id=" + id);
    		return null;
    	}
        int random = (int)(Math.random()*90) + 10;//确保 两位数随机
        return id * 10000 +  hgzType * 100 + random;
	
	}
}
