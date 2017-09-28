package com.orpc.example.server;

import com.orpc.example.api.HelloService;
import com.orpc.server.annotion.RpcService;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

	@Override
	public String say(String name) {
		System.out.println("收到---> " + name);
		return "hello " + name;
	}

}
