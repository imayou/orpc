package com.orpc.example.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.orpc.client.RpcClient;
import com.orpc.example.api.HelloService;

@SpringBootApplication(scanBasePackages = "com.orpc")
public class HelloClientApplication {

	@Autowired
	private RpcClient rpcClient;

	@PostConstruct
	public void run() {
		HelloService helloService = rpcClient.create(HelloService.class);
		String result = helloService.say("world");
		System.err.println(result);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(HelloClientApplication.class, args);
	}

}
