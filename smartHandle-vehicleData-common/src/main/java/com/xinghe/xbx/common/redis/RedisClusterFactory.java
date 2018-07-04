/**
 * 
 */
package com.xinghe.xbx.common.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

//import com.sohu.tv.builder.ClientBuilder;
//import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.PipelineCluster;

/**
 * @author jing
 *
 */
public class RedisClusterFactory {
	
	 private final Logger logger = Logger.getLogger(this.getClass());
	 
	 private PipelineCluster redisCluster;

	 private int appId;

	 public void init() {
	        //根据自己需要设置poolConfig
	        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
	        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
	        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
	        poolConfig.setMaxWaitMillis(1000L);
	        poolConfig.setJmxEnabled(true);
	        try {
	            //根据自己需要修改参数
	        	redisCluster = ClientBuilder.redisCluster(appId)
		                .setJedisPoolConfig(poolConfig)
		                .setTimeout(2000)
		                .build();
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	        }
	    }

	    public PipelineCluster getRedisCluster() {
	        return redisCluster;
	    }

	    public void setAppId(int appId) {
	        this.appId = appId;
	    }  
}
