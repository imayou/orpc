package com.orpc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.orpc")
public class ServerApplication {
	public static void main(String[] args) throws Exception {
		// start embedded zookeeper server
		new EmbeddedZooKeeper(2181, false).start();
		SpringApplication.run(ServerApplication.class, args);
	}
}
