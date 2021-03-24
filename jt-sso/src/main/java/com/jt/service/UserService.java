package com.jt.service;

import com.jt.pojo.User;

public interface UserService {

	boolean checkUser(String param, Integer type);

	void saveUser(User user);

}
