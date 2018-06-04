package com.orpc.example.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orpc.client.RpcClient;
import com.orpc.example.client.command.UserCommand;
import com.orpc.example.domain.User;

@RestController
@RequestMapping("/user")
public class ClentController {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private RpcClient rpcClient;

	@RequestMapping("/get/{id}")
	public String get(@PathVariable("id") String id) {
		User user = new User();
		// 成功
		for (int i = 0; i < 30; i++) {
			UserCommand userCommand = new UserCommand(rpcClient, Long.parseLong(id));
			user = userCommand.execute();
			logger.info("{}", user);
		}
		// 失败
		for (int i = 0; i < 30; i++) {
			UserCommand userCommand = new UserCommand(rpcClient, null);
			user = userCommand.execute();
			logger.info("{}", user);
		}
		//
		for (int i = 0; i < 10; i++) {
			UserCommand userCommand = new UserCommand(rpcClient, Long.parseLong(id));
			user = userCommand.execute();
			logger.info("{}", user);
		}
		return "ko...";
	}
}
