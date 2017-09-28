package com.orpc.example.server;

import java.util.Date;

import com.orpc.example.api.UserService;
import com.orpc.example.domain.User;
import com.orpc.server.annotion.RpcService;

@RpcService(UserService.class)
public class UserServiceImpl implements UserService {
	@Override
	public User view(long id) {
		return new User(id, "小明", "666666@qq.com", "北京丰台", new Date());
	}
}
