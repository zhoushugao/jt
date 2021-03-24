package com.jt.util;

import com.jt.pojo.User;

//ThreadLocal是线程安全的
public class UserThreadLocal {
	/**
	 * 如何存取多个数据
	 * 用Map<k,v>集合
	 */
	private static ThreadLocal<User> userthread = new ThreadLocal<>();
	
	//把数据存入线程 ThreadLocal本地变量
	public static void set(User user) {
		userthread.set(user);
	}
	
	//获取线程ThreadLocal本地变量的数据
	public static User get() {
		return userthread.get();
	}
	
	//使用ThreadLocal切记内存泄漏,使用后关闭
	public static void remove() {
		userthread.remove();
	}
	
}
