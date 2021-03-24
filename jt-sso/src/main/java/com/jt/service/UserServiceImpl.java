package com.jt.service;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	/**
	 * true 表示用户已经存在  false表示用户可以使用
	 * 1.param 用户参数 
	 * 2.type  1 username/2 phone/3 email
	 *    将 type转化为具体字段 
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		String column = 
		type==1?"username":(type==2?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		int count = userMapper.selectCount(queryWrapper);
		return count==0?false:true;
	}

	@Transactional	//事务控制
	@Override
	public void saveUser(User user) {
		user.setEmail(user.getPhone())
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		userMapper.insert(user);
		//int a = 1/0; //测试当用户入库失败后用户展现情况
	}
}
