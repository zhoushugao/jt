package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

@Component  //将拦截器交给spring容器管理
public class UserInterceptor implements HandlerInterceptor  {

	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 在spring4的版本中要求必须重写三个方法,不管是否需要
	 * 在spring5版本中在接口中添加了default属性,则省略不写,在自己需要的时候在重写
	 */

	/**
	 * 返回值结果:
	 * 		true:拦截放行
	 * 		false:请求拦截. 重定向到登录页面
	 * 业务逻辑:
	 * 1.用户是否登录,必须获取cookie里面的token(ticket)
	 * 2.判断redis缓存服务器中是否有数据: 有数据代表登录了,没有则重定向到登录页面
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = null;
		//1.获取cookie
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//判断token是否有效
		if(!StringUtils.isEmpty(token)) {
			//判断redis中是否有数据
			String userJSON = jedisCluster.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis中有用户数据
				//将userJSON转化为user对象
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			   
			//将user数据保存到request域中(方法一)
				//request.setAttribute("JT_USER", user);
			
			//数据保存到session中,记住用完关闭(方法二)动态获取userId	
			//request.getSession().setAttribute("JT_USER", user);	
			
			//将数据存到ThreadLocal里面	(方法三)动态获取userId
			UserThreadLocal.set(user);
			return true;
			}
		}
		//重定向到登录页面
		response.sendRedirect("/user/login.html");
		return false;//表示拦截
	}
	
	@Override   //用完session后进行关闭
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//使用session后关闭
		//request.getSession().removeAttribute("JT_USER");
	
		//使用ThreadLocal后关闭,进行移除操作
		UserThreadLocal.remove();
		
	}
}
