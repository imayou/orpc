package com.orpc.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.orpc")
public class ServerApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ServerApplication.class, args);
	}

}
