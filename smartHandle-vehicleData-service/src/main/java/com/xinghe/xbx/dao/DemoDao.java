package com.xinghe.xbx.dao;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("demoDao")
public class DemoDao {
	private final Logger logger = LoggerFactory.getLogger(getClass()); 
	@Resource(name = "jdbcTemplate")
	JdbcTemplate jdbcTemplate;
	
	String tableName1 = "test1";
	String tableName2 = "test3";

	public Integer saveTest1() {
		int flag = 0;
	        final String SQL = "INSERT INTO " + tableName1 +"("
	            + "  id," + "name )" 
	            + " VALUES(?,?)";
	        Object[] params = new Object[] { 
	        		1,
	        		2
	           };
	        
	        flag = jdbcTemplate.update(SQL, params);
           return flag;
	}
	
	
	public Integer saveTest2() {
		int flag = 0;
	        final String SQL = "INSERT INTO " + tableName2 +"("
		            + "  id," + "name )" 
		            + " VALUES(?,?)";
		        Object[] params = new Object[] { 
		        		1,
		        		2
		           };
	        
	        flag = jdbcTemplate.update(SQL, params);
	        return flag;
	}
	
}
