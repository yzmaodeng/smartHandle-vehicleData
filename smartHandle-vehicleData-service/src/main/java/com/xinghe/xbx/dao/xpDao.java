/**  
* @Title: xpDao.java  
* @Package com.xinghe.xbx.auto.chains.dao  
* @Description: TODO(用一句话描述该文件做什么)  
* @author xbx 
* @date 2018年5月14日下午4:44:14  
* @version V1.0  
*/ 
package com.xinghe.xbx.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.xinghe.xbx.vo.xp;
import com.xinghe.xbx.vo.xpTest;

/**  
* @ClassName: xpDao  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author xupei
* @date 2018年5月14日下午4:44:14  
*    
*/
@Repository("xpDao1")
public class xpDao {
	@Resource(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	String tableName = "xp";
	String tableName1 = "xp_test";
	
	public List<xp> getxpList(){
		String sql = " SELECT  id , "
				+"sfz            ," 
				+"name     ," 
				+"cph      ," 
				+"clhplx          " 
				+ "FROM "+tableName;
		 List<xp> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<xp>(xp.class));
		 if(list!=null&&list.size()>0){
			 return list;
		 }
		 return null;
	}
	
	public int saveXpTest(xpTest bean){
		int flag = 0;
		try{
	        final String SQL = "INSERT INTO " + tableName1 +"("
	        	+ "身份证后6位," + "姓名,"+"车牌号,"+"车辆号牌类型," 
	            + "车辆型号," + "所有人姓名,"+"车辆识别码," 
	        	+ "发动机号," + "初次登记日期 )" 
	            + "  VALUES(?,?,?,?,?,?,?,?,?)";
	        Object[] params = new Object[] {
	        		bean.get身份证后6位(),
	        		bean.get姓名(),
	        		bean.get车牌号(),
	        		bean.get车辆号牌类型(),
	        		bean.get车辆型号(),
	        		bean.get所有人姓名(),
	        		bean.get车辆识别码(),
	        		bean.get发动机号(),
	        		bean.get初次登记日期()
	           };
	        flag = jdbcTemplate.update(SQL, params);
		}catch(Exception e){
			e.printStackTrace();
		}
        return flag;
		
	}
}
