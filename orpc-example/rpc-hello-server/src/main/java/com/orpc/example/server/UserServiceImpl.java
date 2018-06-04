package com.orpc.example.server;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orpc.example.api.UserService;
import com.orpc.example.domain.User;
import com.orpc.server.annotion.RpcService;

@RpcService(UserService.class)
public class UserServiceImpl implements UserService {
	Logger logger = LoggerFactory.getLogger(UserService.class);

	@Override
	public User view(long id) {
		logger.info("user-->{}", id);
		return new User(id, "小明", "666666@qq.com", "北京丰台", Instant.now());
	}
}
