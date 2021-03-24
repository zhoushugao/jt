package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//表示redis配置类
@Configuration //xml
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	//redis.nodes=192.168.175.129:7000,192.168.175.129:7001,192.168.175.129:7002,192.168.175.129:7003,192.168.175.129:7004,192.168.175.129:7005
	@Value("${redis.nodes}")
	private String redisNodes;
	
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		//1.根据,号拆分为多个node
		String[] strNode = redisNodes.split(",");
		//IP:端口
		for (String node : strNode) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = 
					new HostAndPort(host, port);
			nodes.add(hostAndPort);
		}
		
		return new JedisCluster(nodes);
	}
}
	
	/*
	 * @Value("${redis.sentinels}") private String jedisSentinelNodes;
	 * 
	 * @Value("${redis.sentinel.masterName}") private String masterName;
	 * 
	 * //通过哨兵机制实现redis操作.
	 * 
	 * @Bean public JedisSentinelPool jedisSentinelPool() { Set<String> sentinels =
	 * new HashSet<>(); sentinels.add(jedisSentinelNodes); return new
	 * JedisSentinelPool(masterName, sentinels); }
	 */
	
	
	
	
	
	/*
	 * @Value("${redis.nodes}")
	private String redisNodes; //node1,node2,node3
	 * 
	 * @Bean //<bean id="" class=""> 
	 * public ShardedJedis shardedJedis() {
	 * List<JedisShardInfo> shards = new ArrayList<>(); //ip:端口,ip:端口 String[] nodes
	 * = redisNodes.split(","); for (String node : nodes) { String host =
	 * node.split(":")[0]; int port = Integer.parseInt(node.split(":")[1]);
	 * JedisShardInfo info = new JedisShardInfo(host, port); shards.add(info); }
	 * 
	 * return new ShardedJedis(shards); }
	 */

	
	
	
	/*
	 * @Value("${jedis.host}") private String host;
	 * 
	 * @Value("${jedis.port}") private Integer port;
	 * 
	 * @Bean public Jedis jedis() {
	 * 
	 * return new Jedis(host, port); }
	 */

