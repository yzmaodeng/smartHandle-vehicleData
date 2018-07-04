package com.xinghe.xbx.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sohu.tv.cachecloud.client.basic.heartbeat.ClientStatusEnum;
import com.sohu.tv.cachecloud.client.basic.heartbeat.HeartbeatInfo;
import com.sohu.tv.cachecloud.client.basic.util.ConstUtils;
import com.sohu.tv.cachecloud.client.basic.util.HttpUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.PipelineCluster;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * redis cluster 客户端builder
 * Created by yijunzhang on 14-7-27.
 */
public class PipelineClusterBuilder {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	  private final long appId;
	  private GenericObjectPoolConfig jedisPoolConfig;
	  private PipelineCluster pipelineCluster;
	  private int timeout = 1;

	  private int maxRedirections = 3;

	  private final Lock lock = new ReentrantLock();

	  PipelineClusterBuilder(long appId) {
	    this.appId = appId;
	    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	    poolConfig.setMaxTotal(40);
	    poolConfig.setMaxIdle(16);
	    poolConfig.setMinIdle(8);

	    poolConfig.setMaxWaitMillis(1000L);
	    poolConfig.setJmxNamePrefix("jedis-pool");
	    poolConfig.setJmxEnabled(true);
	    this.jedisPoolConfig = poolConfig;
	  }

	  public PipelineCluster build() {
	    if (this.pipelineCluster == null) {
	      while (true) {
	        try {
	          this.lock.tryLock(10L, TimeUnit.SECONDS);
	          if (this.pipelineCluster != null) {
	            PipelineCluster localPipelineCluster = this.pipelineCluster;
	            return localPipelineCluster;
	          }
	         
              String url = String.format(ConstUtils.REDIS_CLUSTER_URL, String.valueOf(appId));
              logger.info("PipelineCluster url" + url);
              String response = HttpUtils.doGet(url);
              ObjectMapper objectMapper = new ObjectMapper();
              HeartbeatInfo heartbeatInfo = null;
              try {
                  heartbeatInfo = objectMapper.readValue(response, HeartbeatInfo.class);
              } catch (IOException e) {
                  logger.error("remote build error, appId: {}", appId, e);
              }
              if (heartbeatInfo == null) {
                  continue;
              }
	          
	          /** 检查客户端版本 **/
              if (heartbeatInfo.getStatus() == ClientStatusEnum.ERROR.getStatus()) {
                  throw new IllegalStateException(heartbeatInfo.getMessage());
              } else if (heartbeatInfo.getStatus() == ClientStatusEnum.WARN.getStatus()) {
                  logger.warn(heartbeatInfo.getMessage());
              } else {
                  logger.info(heartbeatInfo.getMessage());
              }

              Set<HostAndPort> nodeList = new HashSet<HostAndPort>();
              //形如 ip1:port1,ip2:port2,ip3:port3
              String nodeInfo = heartbeatInfo.getShardInfo();
              //为了兼容,如果允许直接nodeInfo.split(" ")
              nodeInfo = nodeInfo.replace(" ", ",");
              String[] nodeArray = nodeInfo.split(",");
              for (String node : nodeArray) {
                  String[] ipAndPort = node.split(":");
                  if (ipAndPort.length < 2) {
                      continue;
                  }
                  String ip = ipAndPort[0];
                  int port = Integer.parseInt(ipAndPort[1]);
                  nodeList.add(new HostAndPort(ip, port));
              }

	          
	          this.pipelineCluster = new PipelineCluster(this.jedisPoolConfig, nodeList, this.timeout, this.maxRedirections);
	         
	          return pipelineCluster;
	        }
	        catch (Throwable e)
	        {
	          this.logger.error(e.getMessage(), e);
	        } finally {
	          this.lock.unlock();
	        }
	        try {
	          TimeUnit.MILLISECONDS.sleep(200 + new Random().nextInt(1000));
	        } catch (InterruptedException e) {
	          this.logger.error(e.getMessage(), e);
	        }
	      }
	    }
	    return this.pipelineCluster;
	  }

	  public PipelineClusterBuilder setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
	    this.jedisPoolConfig = jedisPoolConfig;
	    return this;
	  }

	  public PipelineClusterBuilder setTimeout(int timeout) {
	    this.timeout = timeout;
	    return this;
	  }

	  public PipelineClusterBuilder setMaxRedirections(int maxRedirections) {
	    this.maxRedirections = maxRedirections;
	    return this;
	  }
}
