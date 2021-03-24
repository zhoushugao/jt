package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 业务说明:校验用户是否存在
	 *http://sso.jt.com/user/check/{param}/{type}
	 * 返回值:SysResult
	 * 由于跨域请求 所以返回值必须特殊处理callback(json)
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param,
								 @PathVariable Integer type,
								 String callback) {
		JSONPObject object = null;
		try {
			boolean flag = userService.checkUser(param,type);
			object = new JSONPObject(callback, SysResult.ok(flag));
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONPObject(callback,SysResult.fail());
		}
		return object;
	}
	
	//httpClient.doPost(url, Map<K,V> getKey????);
	//http://sso.jt.com/user/register
	@RequestMapping("/register")
	public SysResult saveUser(String userJSON) {
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	
	/**
	 * 	利用跨域实现用户信息回显
	 * http://sso.jt.com/user/query/73d9809e7972bbf330f1fb35cb638d28?callback=jsonp1560762546338&_=1560762546390
	 * @return
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,String callback) {
		
		String userJSON = jedisCluster.get(ticket);
		if(StringUtils.isEmpty(userJSON)) {
			//回传数据需要经过200判断 SysResult对象
			return new JSONPObject(callback, SysResult.fail());
		}else {
			return new JSONPObject(callback, SysResult.ok(userJSON));
			}
	}
	
	
	
	
	
	
}
