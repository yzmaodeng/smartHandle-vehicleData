/**
 * 
 */
package com.xinghe.xbx.service.util.sequence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author jing
 * 
 */
@Repository("sequenceDao")
public class SequenceDao{
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public Map<String, Long> getSequence(String seqname){
		String sql = "select last_value as id_index ,last_value+cache_count as id_last,span from dalsequence where  name=? for update";
		Map<String,Long> map = new HashMap<String,Long>();
		map = (Map<String,Long>)jdbcTemplate.queryForObject(sql, new Object[]{seqname},new RowMapper() {
			public Map<String,Long> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Long idIndex = rs.getLong("id_index");
				Long lastIndex = rs.getLong("id_last");
				Long span = rs.getLong("span");
				Map<String,Long> map = new HashMap<String,Long>();
				map.put("id_index", idIndex);
				map.put("id_last", lastIndex);
				map.put("span", span);
				return map;
			}
		});
		return map;
	}
	
	public void updateSequence(String seqname){
		String usql = "update dalsequence set last_value = last_value+cache_count where name = ?" ;
		jdbcTemplate.update( usql , new Object[]{seqname});
	}	

}
