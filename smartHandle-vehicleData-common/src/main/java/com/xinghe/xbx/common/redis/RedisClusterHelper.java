package com.xinghe.xbx.common.redis;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineCluster;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.serializable.ProtostuffSerializer;

/**
 * redisSentinel

 * @param <V>
 */
public class RedisClusterHelper {
	private static Logger logger = Logger.getLogger(RedisClusterHelper.class);


    private static RedisClusterHelper instance;

    public static RedisClusterHelper getInstance(){
        if (instance == null) {
            synchronized (RedisClusterHelper.class) {
                if (instance == null)
                    instance = new RedisClusterHelper();
            }
        }
        return instance;
    }

    /**
     * jedisPool
     */
//    private JedisSentinelPool jedisSentinelPool;

    private PipelineCluster redisCluster;
    /**
     *
     */
    private ProtostuffSerializer protostuffSerializer;

    public void setProtostuffSerializer(
            ProtostuffSerializer protostuffSerializer) {
        this.protostuffSerializer = protostuffSerializer;
    }

    public void setRedisCluster(PipelineCluster redisCluster) {
        this.redisCluster = redisCluster;
    }
    
    public Long incr(String key) {
    	key = key.replaceAll("@", "#");
        Long cnt = null;
        try {
        	cnt = redisCluster.incr(key);
        } catch (Exception e) {
            logger.error("incr error", e);
        }
        logger.debug("incr key="+key);
        return cnt;
    }
    
    public Long incr(String key, int sec) {
    	key = key.replaceAll("@", "#");
        Long cnt = null;
        try {
        	cnt = redisCluster.incr(key);
        	redisCluster.expire(key, sec);
        } catch (Exception e) {
            logger.error("incr error", e);
        }
        logger.debug("incr key="+key);
        return cnt;
    }
    
    public Long decr(String key) {
    	key = key.replaceAll("@", "#");
        Long cnt = null;
        try {
            cnt = redisCluster.decr(key);
        } catch (Exception e) {
            logger.error("decr error", e);
        } 
        logger.debug("decr key="+key);
        return cnt;
    }

    public String getByIncrDecr(String key) {
    	key = key.replaceAll("@", "#");
    	String cnt = null;
        try {
        	cnt = redisCluster.get(key);
        } catch (Exception e) {
            logger.error("getByIncrDecr error", e);
        }
        logger.debug("getByIncrDecr key="+key);
        return cnt;
    }
    
    public boolean add(String key, Serializable value) {
        return set(key, value,0);
    }


    public boolean hset(String key, String field, String value,int expireTime){
        key = key.replaceAll("@", "#");
        boolean b = false;
        if (value != null) {
            try {
                redisCluster.hset(key, field, value);
                redisCluster.expire(key, expireTime);
                b = true;
            } catch (Exception e) {
                logger.error("hset(String key, V value,int expireTime)"+ e.getMessage(), e);
            }
        }else{
            logger.debug("hset(String key, V value,int expireTime) value == null");
        }
        return b;
    }

    /**
     * 没有验证!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param key
     * @param fields
     * @return
     */
    public String hget(String key,String fields){
    	if(StringUtils.isBlank(key)||StringUtils.isBlank(fields)){
    		return null;
    	}
    	key = key.replaceAll("@", "#");
    	try {
             String bytes = null;
             bytes =redisCluster.hget(key, fields);
            logger.debug("hmget(String key) suc key="+key);
            if (StringUtils.isBlank(bytes)) {
                return null;
            }
            return bytes;
        } catch (Exception e) {
            logger.error("hget(String key) error timout key="+key + " fields=" + fields);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public List<String> hmget(String key, String... fields) {
        if(fields == null || fields.length <= 0){
            return null;
        }
        key = key.replaceAll("@", "#");
        ArrayList<String> list = new  ArrayList<String>();
        try {
            List<String> list_ = redisCluster.hmget(key, fields);
            
            logger.debug("hmget(String key) suc key="+key);
            if (list_ == null || list_.size() <= 0) {
                return null;
            }
            
            for(String s : list_){
            	if(s == null){
            		continue;
            	}
            	list.add(s);
            }
            return list;
        } catch (Exception e) {
            logger.error("hmget(String key) error timout key="+key + " fields=" + Arrays.asList(fields));
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    //新添加的-----待验证
    public List<String> hvals(String key) {
        if(StringUtils.isBlank(key)){
            return null;
        }
        key = key.replaceAll("@", "#");
        try {
            List<String> list = redisCluster.hvals(key);
            
            logger.debug("hvals(String key) suc key="+key);
            if (list == null || list.size() <= 0) {
                return null;
            }
            return list;
        } catch (Exception e) {
            logger.error("hmget(String key) error timout key="+key );
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取所有key值
     * @param key
     * @return
     */
    public List<String> hkeys(String key) {
    	if(StringUtils.isBlank(key)){
    		return null;
    	}
    	key = key.replaceAll("@", "#");
    	try {
    		Set<String> set = redisCluster.hkeys(key);
    		List<String> list = new ArrayList<String>(set);
    		logger.debug("hkeys(String key) suc key="+key);
    		if (list == null || list.size() <= 0) {
    			return null;
    		}
    		return list;
    	} catch (Exception e) {
    		logger.error("hkeys(String key) error timout key="+key );
    		logger.error(e.getMessage(), e);
    		return null;
    	}
    }
    /**
     * 没有验证!!!!!!!!!!!!!!!!!!!
     * @param key
     * @param map
     * @return
     */
    public String hmset(String key,Map<String,String>map,int expireTime){
    	if(StringUtils.isBlank(key)|| map.size()<0){
    		return null;
    	}
    	 key = key.replaceAll("@", "#");
         try {
              String value = null;
              value= redisCluster.hmset(key, map);
              redisCluster.expire(key, expireTime);
             logger.debug("hmset(String key) suc key="+key);
             if (StringUtils.isBlank(value)) {
                 return null;
             }
             return value;
         } catch (Exception e) {
             logger.error("hmset(String key) error timout key="+key + " fields=" + Arrays.asList(map));
             logger.error(e.getMessage(), e);
             return null;
         }
    }
    /**
     * 检查hash中的二级key是否存在
     * @param key
     * @param field
     * @return
     */
    public boolean hexist(String key,String field){
    	key = key.replaceAll("@", "#");
        boolean exists = false;
        if(key==null||field==null){
        	return false;
        }
        try {
        	exists=redisCluster.hexists(key.getBytes(), field.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("hexists(String key, V value)"+ e.getMessage(), e);
		}
    	return exists;
    }
    
    public boolean set(String key, Serializable value,int expireTime) {
    	key = key.replaceAll("@", "#");
        boolean b = false;
        if (value != null) {
            try {
                byte[] bytes = protostuffSerializer.serialize(value);
                if (expireTime <= 0) {
                	redisCluster.set(key, bytes);
                } else {
                	redisCluster.setex(key, expireTime, bytes);
                }

                b = true;
            } catch (Exception e) {
                logger.error("set(String key, V value,int expireTime)"+ e.getMessage(), e);
            }
        }else{
            logger.debug("set(String key, V value,int expireTime) value == null");
        }
        return b;
    }

    public Boolean remove(String key) {
    	key = key.replaceAll("@", "#");
        try {
            try {
                redisCluster.del(key);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } 
            logger.debug("remove(String key) suc key="+key);
            return true;
        } catch (Exception e) {
            logger.error("remove(String key) error key="+key + e.getMessage(), e);
            return false;
        }
    }

    public Serializable get(String key) {
    	key = key.replaceAll("@", "#");
        try {	
            byte[] bytes = null;
            try {
            	bytes = redisCluster.getBytes(key);
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            }

            if (bytes == null || bytes.length <= 0) {
                logger.debug("get(String key) not_exist key="+key);
                return null;
            }
            Serializable v = (Serializable)protostuffSerializer.deserialize(bytes);
            if (v == null) {
                logger.debug("get(String key) not_exist key="+key);
                return null;
            }
            logger.debug("get(String key) suc key="+key);
            return  v;
        } catch (JedisException e) {
            logger.error("get(String key) error time_out key="+key);
            logger.error(e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("get(String key) inner_error key="+key);
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    public Serializable lpop(String key){
        key = key.replaceAll("@", "#");
        try {
            byte[] bytes = null;
            try {
                bytes = redisCluster.lpopBytes(key);
            } catch (Exception e) {
                logger.error("lpop(String key) error key="+key);
                logger.error(e.getMessage(), e);
            }

            if (bytes == null || bytes.length <= 0) {
                logger.debug("lpop(String key) not_exist key="+key);
                return null;
            }
            Serializable v = (Serializable)protostuffSerializer.deserialize(bytes);
            if (v == null) {
                logger.debug("lpop(String key) not_exist key="+key);
                return null;
            }
            logger.debug("lpop(String key) suc key="+key);
            return  v;
        } catch (JedisException e) {
            logger.error("lpop(String key) error time_out key="+key);
            logger.error(e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("lpop(String key) inner_error key="+key);
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    public Boolean sadd(String key, Serializable value) {
    	key = key.replaceAll("@", "#");
        if (value == null) {
            logger.debug("sadd(String key, V value) value == null key="+key);
            return null;
        }
        try {
            byte[] bytes = protostuffSerializer.serialize(value);
            try {
                redisCluster.sadd(key, bytes);
            } catch (Exception e) {
                logger.error("sadd(String key, V value) error key="+key);
                logger.error(e.getMessage(), e);
            } 
            logger.debug("sadd(String key, V value) success key="+key);

            return true;
        } catch (Exception e) {
            logger.error("sadd(String key, V value) inner_error key="+key);
            logger.error("sadd(String key, V value)"+e.getMessage(), e);
            return false;
        }
    }

    public Boolean srem(String key, Serializable value) {
    	key = key.replaceAll("@", "#");
        if (value == null) {
            logger.debug("srem(String key, V value) value == null key="+key);
            return null;
        }

        try {
            byte[] bytes = protostuffSerializer.serialize(value);

            try {
                redisCluster.srem(key, bytes);
            } catch (Exception e) {
                logger.error("srem(String key, V value) error key="+key);
            }
            logger.debug("srem(String key, V value) success key="+key);
            return true;
        } catch (Exception e) {
            logger.error("srem(String key, V value) inner_error key="+key);
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public ArrayList<Serializable> smembers(String key) {
    	key = key.replaceAll("@", "#");
        try {
            Set<String> bytes = null;
            try {
                bytes = redisCluster.smembers(key);
            } catch (Exception e) {
                logger.error("smembers(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } 

            if (bytes == null || bytes.size() <= 0) {
                return null;
            }

            ArrayList<Serializable> arrayList = new ArrayList<Serializable>();
            for(String temp : bytes){
            	Serializable v = (Serializable)protostuffSerializer.deserialize(temp.getBytes());
                arrayList.add(v);
            }
            logger.debug("smembers(String key) suc key="+key);
            return arrayList;
        } catch (JedisException e) {
            logger.error("smembers(String key) error timout key="+key);
            logger.error(e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.error("smembers(String key) error key="+key);
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * key 是否存在
     * @param key
     * @return
     */
	public boolean existKey(String key) {
		key = key.replaceAll("@", "#");
		boolean b = false;
		try {
			b = redisCluster.exists(key);
			logger.debug("existKey(String key) success key="+key);
			return b;
		} catch (Exception e) {
			logger.error("existKey(String key) inner_error key="+key);
			logger.error("existKey(String key)"+e.getMessage(), e);
			return b;
		}
	}
    
    /**
     * key 删除
     * @param key
     * @return
     */
	public long delKey(String key) {
		key = key.replaceAll("@", "#");
		long count = 0l;
		try {
			count = redisCluster.del(key);
			logger.debug("delKey(String key) success key="+key);
			return count;
		} catch (Exception e) {
			logger.error("delKey(String key) inner_error key="+key);
			logger.error("delKey(String key)"+e.getMessage(), e);
			return count;
		}
	}
    /**
	 * * <p>
      *     Description:向有序表中批量插入一个成员
      * </p>
      * @param zsetKey   有序集合的key
      * @param score    元素对应的score值，根据该值进行有序集合的排序
      * @param value   存入的value列表
	 * @return
	 * @throws Exception
	 */
	public boolean zadd(String zsetKey,double score,Serializable value,int expireTime) {
		zsetKey = zsetKey.replaceAll("@", "#");
		if (value == null) {
			logger.debug("zadd(String zsetKey,double score,String value) value == null key="+zsetKey);
			return false;
		}
		try {
			byte[] bytes = protostuffSerializer.serialize(value);

			try {
				redisCluster.zadd(zsetKey, score, bytes);
				redisCluster.expire(zsetKey,expireTime);
			} catch (Exception e) {
				logger.error("zadd(String zsetKey,double score,String value) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 
			logger.debug("zadd(String zsetKey,double score,String value) success key="+zsetKey);

			return true;
		} catch (Exception e) {
			logger.error("zadd(String zsetKey,double score,String value) inner_error key="+zsetKey);
			logger.error("zadd(String zsetKey,double score,String value)"+e.getMessage(), e);
			return false;
		}
	}
	
	/**
	 *  * <p>
      *     Description:删除有序表中的元素
      * </p>
      * @param zsetKey   有序列表的键值列表
	 * @param member
	 * @param end
	 * @return 删除元素个数
	 * @throws Exception
	 */
	public Long zrem(String zsetKey,Serializable value,int expireTime) {
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			byte[] bytes = protostuffSerializer.serialize(value);
			try {
				result = redisCluster.zrem(zsetKey,bytes);
				redisCluster.expire(zsetKey,expireTime);
			} catch (Exception e) {
				logger.error("zrem(String zsetKey,Serializable value) error key="+zsetKey + "|value=" + value);
				logger.error(e.getMessage(), e);
			} 
			logger.debug("zrem(String zsetKey,Serializable value) success key="+zsetKey + "|value=" + value);
		} catch (Exception e) {
			logger.error("zrem(String zsetKey,Serializable value) inner_error key="+zsetKey + "|value=" + value);
			logger.error("zrem(String zsetKey,Serializable value)"+e.getMessage(), e);
		}
       return result;
	}
	
	public Long zrank(String zsetKey,Serializable value) {
		zsetKey = zsetKey.replaceAll("@", "#");
		if (value == null) {
			logger.debug("zrank(String zsetKey,Serializable value) value == null key="+zsetKey);
			return null;
		}
		try {
			Long index = null;
			byte[] bytes = protostuffSerializer.serialize(value);

			try {
				index = redisCluster.zrank(zsetKey, bytes);
				
			} catch (Exception e) {
				logger.error("zrank(String zsetKey,Serializable value) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 
			logger.debug("zrank(String zsetKey,Serializable value) success key="+zsetKey);

			return index;
		} catch (Exception e) {
			logger.error("zrank(String zsetKey,Serializable value) inner_error key="+zsetKey);
			logger.error("zrank(String zsetKey,Serializable value)"+e.getMessage(), e);
			return null;
		}
	}
	
	public boolean zMemberExist(String zsetKey,Serializable value) {
		Long retu = zrank(zsetKey,value);
		if(retu == null){
			return false;
		}else{
			return true;
		}
	}
	/**
	 *  * <p>
      *     Description:批量删除有序表中的元素
      * </p>
      * @param zsetKey   有序列表的键值列表
	 * @param start
	 * @param end
	 * @return 删除元素个数
	 * @throws Exception
	 */
	public Long zremByRangeScore(String zsetKey,double start,double end) {
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			try {
				result = redisCluster.zremrangeByScore(zsetKey, start, end);
			} catch (Exception e) {
				logger.error("zremByRangeScore(String zsetKey,double start,double end) error key="+zsetKey + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			} 
			logger.debug("zremByRangeScore(String zsetKey,double start,double end) success key="+zsetKey + ";" + start + ";" +end);
		} catch (Exception e) {
			logger.error("zremByRangeScore(String zsetKey,double score,String value) inner_error key="+zsetKey + ";" + start + ";" +end);
			logger.error("zremByRangeScore(String zsetKey,double score,String value)"+e.getMessage(), e);
		}
       return result;
	}
	/**
	 *  * <p>
      *     Description:批量删除有序表中的元素
      * </p>
      * @param zsetKey   有序列表的键值列表
	 * @param start
	 * @param end
	 * @return 删除元素个数
	 * @throws Exception
	 */
	public Long zremrangeByRank(String zsetKey,long start,long end) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			try {
				result = redisCluster.zremrangeByRank(zsetKey, start, end);
			} catch (Exception e) {
				logger.error("zremByRangeScore(String zsetKey,double start,double end) error key="+zsetKey + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			}
			logger.debug("zremByRangeScore(String zsetKey,double start,double end) success key="+zsetKey + ";" + start + ";" +end);
		} catch (Exception e) {
			logger.error("zremByRangeScore(String zsetKey,double score,String value) inner_error key="+zsetKey + ";" + start + ";" +end);
			logger.error("zremByRangeScore(String zsetKey,double score,String value)"+e.getMessage(), e);
		}
       return result;
	}
	/**
	 * Description:获取zset的成员个数
	 * @param zsetKey
	 * @return
	 * @throws Exception
	 */

	public Long zcard(String zsetKey) {
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			try {
				result = redisCluster.zcard(zsetKey);
			} catch (Exception e) {
				logger.error("zcard(String zsetKey,double start,double end) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 
			logger.debug("zcard(String zsetKey,double start,double end) success key="+zsetKey );
		} catch (Exception e) {
			logger.error("zcard(String zsetKey,double score,String value) inner_error key="+zsetKey);
			logger.error("zcard(String zsetKey,double score,String value)"+e.getMessage(), e);
		}
       return result;
	}
	/**
	 *  * <p>
      *     Description:从有序表中获取score值在[minScore,maxScore]之间的成员
      * </p>
      * @param zsetKey  有序集合的键值
      * @param minScore min
      * @param maxScore max
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Serializable> zrangeByScore(String zsetKey,double minScore,double maxScore) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			Set<byte[]> bytes = null;
			try {
				bytes = redisCluster.zrangeByScoreBytes(zsetKey, minScore, maxScore);
				if (bytes == null || bytes.size() <= 0) {
					return null;
				}

				for(byte[] temp : bytes){
					Serializable v = (Serializable)protostuffSerializer.deserialize(temp);
					result.add(v);
				}
				logger.debug("zrangeByScore(String key) suc key="+zsetKey);
				return result;
			} catch (Exception e) {
				logger.error("zrangeByScore(String key) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 

			return null;
		} catch (JedisException e) {
			logger.error("zrangeByScore(String key) error timout key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		} catch (Exception e) {
			logger.error("zrangeByScore(String key) error key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	/**
	 * 从有序表中获取指定范围内的集合成员
	 * @param zsetKey
	 * @param start
	 * @param end:-1表示取最后一个
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Serializable> zrange(String zsetKey,int start,int end){
		zsetKey = zsetKey.replaceAll("@", "#");
		ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			Set<byte[]> bytes = null;
			try {
				bytes = redisCluster.zrangeBytes(zsetKey,start,end);
				if (bytes == null || bytes.size() <= 0) {
					return null;
				}

				for(byte[] temp : bytes){
					Serializable v = (Serializable)protostuffSerializer.deserialize(temp);
					result.add(v);
				}
				logger.debug("zrangeByScore(String key) suc key="+zsetKey);
				return result;
			} catch (Exception e) {
				logger.error("zrangeByScore(String key) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 
			return null;

		} catch (JedisException e) {
			logger.error("zrangeByScore(String key) error timout key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		} catch (Exception e) {
			logger.error("zrangeByScore(String key) error key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 从有序表中获取指定范围内的集合成员,从大到小
	 * @param zsetKey
	 * @param start
	 * @param end:-1表示取最后一个
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Serializable> zrevrange(String zsetKey,int start,int end) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			Set<byte[]> bytes = null;
			try {
				bytes = redisCluster.zrevrangeBytes(zsetKey,start,end);
				if (bytes == null || bytes.size() <= 0) {
					return null;
				}

				for(byte[] temp : bytes){
					Serializable v = (Serializable)protostuffSerializer.deserialize(temp);
					result.add(v);
				}
				logger.debug("zrangeByScore(String key) suc key="+zsetKey);
				return result;
			} catch (Exception e) {
				logger.error("zrangeByScore(String key) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} 
			return null;

		} catch (JedisException e) {
			logger.error("zrangeByScore(String key) error timout key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		} catch (Exception e) {
			logger.error("zrangeByScore(String key) error key="+zsetKey);
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	//list
    public long lpush(String key, Serializable value) {
    	key = key.replaceAll("@", "#");
    	Long result = 0L;
        if (value == null) {
            logger.debug("lpush(String key, V value) value == null key="+key);
            return result;
        }
        try {
            byte[] bytes = protostuffSerializer.serialize(value);
            try {
                result = redisCluster.lpush(key, bytes);
            } catch (Exception e) {
                logger.error("lpush(String key, V value) error key="+key);
                logger.error(e.getMessage(), e);
            } 
            logger.debug("lpush(String key, V value) success key="+key);

            return result;
        } catch (Exception e) {
            logger.error("lpush(String key, V value) inner_error key="+key);
            logger.error("lpush(String key, V value)"+e.getMessage(), e);
            return result;
        }
    }

    public long rpush(String key, Serializable value) {
        key = key.replaceAll("@", "#");
        Long result = 0L;
        if (value == null) {
            logger.debug("rpush(String key, V value) value == null key="+key);
            return result;
        }
        try {
            byte[] bytes = protostuffSerializer.serialize(value);
            try {
                result = redisCluster.rpush(key, bytes);
            } catch (Exception e) {
                logger.error("rpush(String key, V value) error key="+key);
                logger.error(e.getMessage(), e);
            }
            logger.debug("rpush(String key, V value) success key="+key);

            return result;
        } catch (Exception e) {
            logger.error("rpush(String key, V value) inner_error key="+key);
            logger.error("rpush(String key, V value)"+e.getMessage(), e);
            return result;
        }
    }

    public ArrayList<Serializable> lrange (String key, long start,long end) {
    	key = key.replaceAll("@", "#");
    	ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			List<String> bytes = null;
			try {

				bytes = redisCluster.lrange(key,start,end);
				if (bytes == null || bytes.size() <= 0) {
					return null;
				}

				for(String temp : bytes){
					Serializable v = (Serializable)protostuffSerializer.deserialize(temp.getBytes());
					result.add(v);
				}
				logger.debug("lrange(String key, long start,long end) suc key="+key);
				return result;
			} catch (Exception e) {
				logger.error("lrange(String key, long start,long end) error key="+key);
				logger.error(e.getMessage(), e);
			} 
			return null;

		} catch (JedisException e) {
			logger.error("lrange(String key, long start,long end) error timout key="+key);
			logger.error(e.getMessage(), e);
			return null;
		} catch (Exception e) {
			logger.error("lrange(String key, long start,long end) error key="+key);
			logger.error(e.getMessage(), e);
			return null;
		}
    }
    
	public Long llen(String key) throws Exception{
		key = key.replaceAll("@", "#");
		Long result = 0L;
		try {
			try {
				result = redisCluster.llen(key);
			} catch (Exception e) {
				logger.error("llen(String key) error key="+key);
				logger.error(e.getMessage(), e);
			}
			logger.debug("llen(String key) success key="+key );
		} catch (Exception e) {
			logger.error("llen(String key) inner_error key="+key);
			logger.error("llen(String key)"+e.getMessage(), e);
		}
       return result;
	}
	
	/**
	 *  裁剪
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public boolean ltrim(String key,long start,long end) throws Exception{
		key = key.replaceAll("@", "#");
		boolean result = false;
		try {
			try {
				redisCluster.ltrim(key, start, end);
			} catch (Exception e) {
				logger.error("ltrim(String key,long start,long end) error key="+key + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			}
			result = true;
			logger.debug("ltrim(String key,long start,long end) success key="+key + ";" + start + ";" +end);
		} catch (Exception e) {
			logger.error("ltrim(String key,long start,long end) inner_error key="+key + ";" + start + ";" +end);
			logger.error("ltrim(String key,long start,long end)"+e.getMessage(), e);
		}
       return result;
	}
	
	/**
	 *  删除
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public boolean lrem(String key,long count,Serializable value) throws Exception{
		key = key.replaceAll("@", "#");
		boolean result = false;

		try {
			byte[] bytes = protostuffSerializer.serialize(value);
			try {
				redisCluster.lrem(key, count, bytes);
			} catch (Exception e) {
				logger.error("lrem(String key,long count,String value) error key="+key + ";" + count + ";" +value);
				logger.error(e.getMessage(), e);
			} 
			result = true;
			logger.debug("lrem(String key,long count,String value) success key="+key + ";" + count + ";" +value);
		} catch (Exception e) {
			logger.error("lrem(String key,long count,String value) inner_error key="+key + ";" + count + ";" +value);
			logger.error("lrem(String key,long count,String value)"+e.getMessage(), e);
		}
       return result;
	}
	
	public Map<String,JedisPool> getClusterNodes() throws Exception{
		Map<String,JedisPool> clusterMap = null;
		try {
			clusterMap = redisCluster.getClusterNodes();
			if(clusterMap == null || clusterMap.size() == 0 ){
				return null;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		return clusterMap;
	}
	

}
