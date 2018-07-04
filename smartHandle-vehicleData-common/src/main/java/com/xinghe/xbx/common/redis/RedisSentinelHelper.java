package com.xinghe.xbx.common.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.serializable.ProtostuffSerializer;
import com.sohu.tv.cachecloud.client.basic.util.StringUtil;
import com.xinghe.xbx.common.util.SpringUtil;

public class RedisSentinelHelper {
    private static Logger logger = Logger.getLogger(RedisSentinelHelper.class);
	
    private static JedisSentinelPool jedisSentinelPool;

    private static ProtostuffSerializer protostuffSerializer;
    
    public  void setJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
		RedisSentinelHelper.jedisSentinelPool = jedisSentinelPool;
	}

	public  void setProtostuffSerializer(ProtostuffSerializer protostuffSerializer) {
		RedisSentinelHelper.protostuffSerializer = protostuffSerializer;
	}

	private static RedisSentinelHelper instance;
	
    public static RedisSentinelHelper getInstance(){
        if (instance == null) {
            synchronized (RedisSentinelHelper.class) {
                if (instance == null){
                    instance = new RedisSentinelHelper();
	                if(jedisSentinelPool == null){
	                	jedisSentinelPool = (JedisSentinelPool)SpringUtil.getBean("jedisSentinelPool");
	                }
	                if(protostuffSerializer == null){
	                	protostuffSerializer = (ProtostuffSerializer)SpringUtil.getBean("protostuffSerializer");
	                }
                }
            }
        }
        return instance;
    }
    
	public Long incr(String key) {
    	key = key.replaceAll("@", "#");
        Jedis jedis = null;
        Long cnt = null;
        try {
            jedis = jedisSentinelPool.getResource();
            cnt = jedis.incr(key.getBytes());
        } catch (Exception e) {
            logger.error("incr error", e);
        } finally {
            jedis.close();
        }
        logger.debug("incr key="+key);
        return cnt;
    }
    
    public Long decr(String key) {
    	key = key.replaceAll("@", "#");
        Jedis jedis = null;
        Long cnt = null;
        try {
            jedis = jedisSentinelPool.getResource();
            cnt = jedis.decr(key.getBytes());
        } catch (Exception e) {
            logger.error("decr error", e);
        } finally {
            jedis.close();
        }
        logger.debug("decr key="+key);
        return cnt;
    }

    public boolean add(String key, Serializable value) {
    	key = key.replaceAll("@", "#");
        return set(key, value,0);
    }


    public boolean set(String key, Serializable value,int expireTime) {
    	key = key.replaceAll("@", "#");
        boolean b = false;
        Jedis jedis = null;
        if (value != null) {
            try {
                byte[] bytes = protostuffSerializer.serialize(value);
                jedis = jedisSentinelPool.getResource();

                if (expireTime <= 0) {
                    jedis.set(key.getBytes(), bytes);
                } else {
                    jedis.setex(key.getBytes(), expireTime, bytes);
                }

                b = true;
            } catch (Exception e) {
                logger.error("set(String key, V value,int expireTime)"+ e.getMessage(), e);
            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }else{
            logger.debug("set(String key, V value,int expireTime) value == null");
        }
        return b;
    }

    public Boolean remove(String key) {
    	key = key.replaceAll("@", "#");
        try {
            Jedis jedis = null;
            try {
                jedis = jedisSentinelPool.getResource();
                jedis.del(key);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
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
            Jedis jedis = null;
            byte[] bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.get(key.getBytes());
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
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

    public String getString(String key) {
    	key = key.replaceAll("@", "#");
        try {
            Jedis jedis = null;
            byte[] bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.get(key.getBytes());
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
            }

            if (bytes == null || bytes.length <= 0) {
                logger.debug("get(String key) not_exist key="+key);
                return null;
            }
            String v = new String(bytes,"UTF-8");
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
    public Long getLong(String key) {
    	key = key.replaceAll("@", "#");
        try {
            Jedis jedis = null;
            byte[] bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.get(key.getBytes());
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
            }
            Long value = -1L;

            if (bytes == null || bytes.length <= 0) {
                logger.debug("get(String key) not_exist key="+key);
                return value;
            }
            Serializable v = (Serializable)protostuffSerializer.deserialize(bytes);
            if (v == null) {
                logger.debug("get(String key) not_exist key="+key);
                return value;
            }
            try {
				value = Long.parseLong((String)v);
			} catch (Exception e) {
				logger.error("get(String key) error parse long  exception key="+key);
	            logger.error(e.getMessage(), e);
				return value;
			}
            logger.debug("get(String key) suc key="+key);
            return  value;
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
    public Boolean sadd(String key, Serializable value) {
    	key = key.replaceAll("@", "#");
        if (value == null) {
            logger.debug("sadd(String key, V value) value == null key="+key);
            return null;
        }
        try {
            byte[] bytes = protostuffSerializer.serialize(value);
            Jedis jedis = null;
            try {
                jedis = jedisSentinelPool.getResource();
                jedis.sadd(key.getBytes(), bytes);
            } catch (Exception e) {
                logger.error("sadd(String key, V value) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
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
            Jedis jedis = null;
            try {
                jedis = jedisSentinelPool.getResource();
                jedis.srem(key.getBytes(), bytes);
            } catch (Exception e) {
                logger.error("srem(String key, V value) error key="+key);
            } finally {
                jedis.close();
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
            Jedis jedis = null;
            Set<byte[]> bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.smembers(key.getBytes());
            } catch (Exception e) {
                logger.error("smembers(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
            }


            if (bytes == null || bytes.size() <= 0) {
                return null;
            }

            ArrayList<Serializable> arrayList = new ArrayList<Serializable>();
            for(byte[] temp : bytes){
            	Serializable v = (Serializable)protostuffSerializer.deserialize(temp);
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
	 * * <p>
      *     Description:向有序表中批量插入一个成员
      * </p>
      * @param zsetKey   有序集合的key
      * @param score    元素对应的score值，根据该值进行有序集合的排序
      * @param value   存入的value列表
	 * @return
	 * @throws Exception
	 */
	public boolean zadd(String zsetKey,double score,Serializable value) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		if (value == null) {
			logger.debug("zadd(String zsetKey,double score,String value) value == null key="+zsetKey);
			return false;
		}
		try {
			byte[] bytes = protostuffSerializer.serialize(value);
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				jedis.zadd(zsetKey.getBytes(), score, bytes);
			} catch (Exception e) {
				logger.error("zadd(String zsetKey,double score,String value) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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
      *     Description:批量删除有序表中的元素
      * </p>
      * @param zsetKey   有序列表的键值列表
	 * @param start
	 * @param end
	 * @return 删除元素个数
	 * @throws Exception
	 */
	public Long zremByRangeScore(String zsetKey,double start,double end) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				result = jedis.zremrangeByScore(zsetKey, start, end);
			} catch (Exception e) {
				logger.error("zremByRangeScore(String zsetKey,double start,double end) error key="+zsetKey + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				result = jedis.zremrangeByRank(zsetKey, start, end);
			} catch (Exception e) {
				logger.error("zremByRangeScore(String zsetKey,double start,double end) error key="+zsetKey + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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

	public Long zcard(String zsetKey) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		Long result = 0L;
		try {
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				result = jedis.zcard(zsetKey);
			} catch (Exception e) {
				logger.error("zcard(String zsetKey,double start,double end) error key="+zsetKey);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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
			Jedis jedis = null;
			Set<byte[]> bytes = null;
			try {
				jedis = jedisSentinelPool.getResource();
				bytes = jedis.zrangeByScore(zsetKey.getBytes(), minScore, maxScore);
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
			} finally {
				jedis.close();
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
	public ArrayList<Serializable> zrange(String zsetKey,int start,int end) throws Exception{
		zsetKey = zsetKey.replaceAll("@", "#");
		ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			Jedis jedis = null;
			Set<byte[]> bytes = null;
			try {
				jedis = jedisSentinelPool.getResource();
				bytes = jedis.zrange(zsetKey.getBytes(),start,end);
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
			} finally {
				jedis.close();
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
			Jedis jedis = null;
			Set<byte[]> bytes = null;
			try {
				jedis = jedisSentinelPool.getResource();
				bytes = jedis.zrevrange(zsetKey.getBytes(),start,end);
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
			} finally {
				jedis.close();
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
            Jedis jedis = null;
            try {
                jedis = jedisSentinelPool.getResource();
                result = jedis.lpush(key.getBytes(), bytes);
            } catch (Exception e) {
                logger.error("lpush(String key, V value) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            logger.debug("lpush(String key, V value) success key="+key);

            return result;
        } catch (Exception e) {
            logger.error("lpush(String key, V value) inner_error key="+key);
            logger.error("lpush(String key, V value)"+e.getMessage(), e);
            return result;
        }
    }

    public ArrayList<Serializable> lrange (String key, long start,long end) {
    	key = key.replaceAll("@", "#");
    	ArrayList<Serializable> result = new ArrayList<Serializable>() ;
		try {
			Jedis jedis = null;
			List<byte[]> bytes = null;
			try {
				jedis = jedisSentinelPool.getResource();
				bytes = jedis.lrange(key.getBytes(),start,end);
				if (bytes == null || bytes.size() <= 0) {
					return null;
				}

				for(byte[] temp : bytes){
					Serializable v = (Serializable)protostuffSerializer.deserialize(temp);
					result.add(v);
				}
				logger.debug("lrange(String key, long start,long end) suc key="+key);
				return result;
			} catch (Exception e) {
				logger.error("lrange(String key, long start,long end) error key="+key);
				logger.error(e.getMessage(), e);
			} finally {
				jedis.close();
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
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				result = jedis.llen(key.getBytes());
			} catch (Exception e) {
				logger.error("llen(String key) error key="+key);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				jedis.ltrim(key.getBytes(), start, end);
			} catch (Exception e) {
				logger.error("ltrim(String key,long start,long end) error key="+key + ";" + start + ";" +end);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
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
			Jedis jedis = null;
			try {
				jedis = jedisSentinelPool.getResource();
				jedis.lrem(key.getBytes(), count, bytes);
			} catch (Exception e) {
				logger.error("lrem(String key,long count,String value) error key="+key + ";" + count + ";" +value);
				logger.error(e.getMessage(), e);
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
			result = true;
			logger.debug("lrem(String key,long count,String value) success key="+key + ";" + count + ";" +value);
		} catch (Exception e) {
			logger.error("lrem(String key,long count,String value) inner_error key="+key + ";" + count + ";" +value);
			logger.error("lrem(String key,long count,String value)"+e.getMessage(), e);
		}
       return result;
	}

	public Serializable getShiro(String key) {
        try {
            Jedis jedis = null;
            byte[] bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.get(key.getBytes());
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
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
	 public boolean setShiro(String key, Object value,int expireTime) {

	        boolean b = false;
	        Jedis jedis = null;
	        if (value != null) {
	            try {
	                byte[] bytes = protostuffSerializer.serialize(value);
//	                byte[] bytes = SerializeHelper.object2Bytes(value);
	                jedis = jedisSentinelPool.getResource();

	                if (expireTime <= 0) {
	                    jedis.set(key.getBytes(), bytes);
	                } else {
	                    jedis.setex(key.getBytes(), expireTime, bytes);
	                }

	                b = true;
	            } catch (Exception e) {
	                logger.error("set(String key, V value,int expireTime)"+ e.getMessage(), e);
	            }finally {
	                if (jedis != null) {
	                    jedis.close();
	                }
	            }
	        }else{
	            logger.debug("set(String key, V value,int expireTime) value == null");
	        }
	        return b;
	    }
	 
	 public Set<byte[]> getKeys(String keyName){
		  Jedis resource=null;
		  Set<byte[]> keys=null;
		 if(StringUtil.isBlank(keyName)){
			  logger.error("getKeys(String keyName) inner_error key="+keyName);
			  return null;
		 }
		 try {
			 resource = jedisSentinelPool.getResource();
			 keys= resource.keys(keyName.getBytes());
	            
	        } catch (Exception e) {
	            logger.error("getKeys", e);
	        } finally {
	        	  if (resource != null) {
	        		  resource.close();
	                }
	        }
	        logger.debug("getKeys="+keyName);
	        return keys;
		 
		 
	 }
	 public boolean setShiroSesion(String key, Object value,int expireTime) {
	        boolean b = false;
	        Jedis jedis = null;
	        if (value != null) {
	            try {
	            	
	            	
	            	byte[] bytes = SerializeUtil.serialize(value);
	                jedis = jedisSentinelPool.getResource();
	                if (expireTime <= 0) {
	                    jedis.set(key.getBytes(), bytes);
	                } else {
	                    jedis.setex(key.getBytes(), expireTime, bytes);
	                }

	                b = true;
	            } catch (Exception e) {
	                logger.error("set(String key, V value,int expireTime)"+ e.getMessage(), e);
	            }finally {
	                if (jedis != null) {
	                    jedis.close();
	                }
	            }
	        }else{
	            logger.debug("set(String key, V value,int expireTime) value == null");
	        }
	        return b;
	    }


	public Serializable getShiroSesion(String key) {
        try {
            Jedis jedis = null;
            byte[] bytes = null;
            try {
                jedis = jedisSentinelPool.getResource();
                bytes = jedis.get(key.getBytes());
            } catch (Exception e) {
                logger.error("get(String key) error key="+key);
                logger.error(e.getMessage(), e);
            } finally {
                jedis.close();
            }

            if (bytes == null || bytes.length <= 0) {
                logger.debug("get(String key) not_exist key="+key);
                return null;
            }
            Serializable v = (Serializable)SerializeUtil.deserialize(bytes);
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
}
