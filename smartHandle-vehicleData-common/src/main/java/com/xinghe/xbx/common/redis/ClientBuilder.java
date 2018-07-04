package com.xinghe.xbx.common.redis;


/**
 * cachecloud-redis客户端builder
 * @author leifu
 * @Date 2015年2月5日
 * @Time 下午12:11:26
 */
public class ClientBuilder {

    /**
     * 构造redis cluster的builder
     *
     * @param appId
     * @return
     */
    public static PipelineClusterBuilder redisCluster(final long appId) {
        return new PipelineClusterBuilder(appId);
    }

}
