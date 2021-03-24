package com.jt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

@Service
public class RedisService {
	
	@Autowired(required = false)//调用时才注入
	private JedisSentinelPool sentinelPool;
	
	//封装方法  get
	public String get(String key) {
		Jedis jedis = sentinelPool.getResource();
		String result = jedis.get(key);
		jedis.close();
		return result;
	}
	
	public void set(String key,String value) {
		Jedis jedis = sentinelPool.getResource();
		jedis.set(key, value);
		jedis.close();
	}
	
	public void setex(String key,int seconds,String value) {
		Jedis jedis = sentinelPool.getResource();
		jedis.setex(key, seconds, value);
		jedis.close();
	}
	
}
