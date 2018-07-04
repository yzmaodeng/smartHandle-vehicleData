package com.xinghe.xbx.common.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import com.xinghe.xbx.common.util.MD5;
import redis.clients.jedis.PipelineCluster;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long appId = 10000;
//		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//		JedisCluster redisCluster = ClientBuilder.redisCluster(appId)
//		    .setJedisPoolConfig(poolConfig)
//		    .setConnectionTimeout(1000)
//		    .setSoTimeout(1000)
//		    .setMaxRedirections(5)
//		    .build();
//		
//		redisCluster.set("key1", "value111");
//		System.out.println(redisCluster.get("key1"));
		
		String str  = "dfd^fdsfds";
		String aa[] = str.split("\\^");
		for(String s :aa){
			System.out.println(s);
		}
		
		GenericObjectPoolConfig poolConfig1 = new GenericObjectPoolConfig();
		PipelineCluster redisCluster = ClientBuilder.redisCluster(appId)
		    .setJedisPoolConfig(poolConfig1)
		    .setTimeout(1000)
		    .setMaxRedirections(5)
		    .build();
		
		for(int i = 0; i< 10;i++){
			redisCluster.incr("aaa");
			System.out.println(redisCluster.get("aaa"));
		}

		
		redisCluster.set("key1", "value1116666");
		System.out.println(redisCluster.get("key1"));
		
		
		String md5Pwd=MD5.crypt("123456");
		md5Pwd=MD5.crypt(md5Pwd);
		System.out.println(md5Pwd);
		

    }

}
