package com.orpc.example.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.orpc.client.RpcClient;
import com.orpc.example.api.HelloService;
import com.orpc.example.api.UserService;
import com.orpc.example.domain.User;

@SpringBootApplication(scanBasePackages = "com.orpc")
public class ClientApplication {

	@Autowired
	private RpcClient rpcClient;

	@PostConstruct
	public void run() {
		HelloService helloService = rpcClient.create(HelloService.class);
		String result = helloService.say("world");
		System.err.println(result);

		UserService userService = rpcClient.create(UserService.class);
		User user = userService.view(111000L);
		System.err.println("User返回： "+user);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClientApplication.class, args).close();
	}

}
