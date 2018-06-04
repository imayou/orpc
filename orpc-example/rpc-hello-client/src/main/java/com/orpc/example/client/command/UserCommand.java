package com.orpc.example.client.command;

import java.time.Instant;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.orpc.client.RpcClient;
import com.orpc.example.api.UserService;
import com.orpc.example.domain.User;

public class UserCommand extends HystrixCommand<User> {
	private Long id;
	
	private RpcClient rpcClient;

	/**
	 * 隔离  降级  熔断 限流
	 * @param rpcClient
	 * @param id
	 */
	public UserCommand(RpcClient rpcClient, Long id) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserService"))//一个服务
				.andCommandKey(HystrixCommandKey.Factory.asKey("getUserCommand"))     //一个服务的一个接口
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("getUserPool"))  //线程池名称
				.andThreadPoolPropertiesDefaults(
						HystrixThreadPoolProperties.Setter()
						.withCoreSize(15)//默认15个线程
						.withQueueSizeRejectionThreshold(10))//当线程池满后有个等待对列为10 当这个对列也满了 就直接走失败或熔断
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter()
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(150)));//最多能有多少个线程同时调用断熔方法
		this.rpcClient = rpcClient;
		this.id = id;
	}

	@Override
	protected User run() throws Exception {
		if (id == null) {//先丢一个异常
			throw new Exception();
		}
		UserService userService = rpcClient.create(UserService.class);
		User user = userService.view(id);
		return user;
	}
	
	@Override
	protected User getFallback() {
		System.err.println("进入熔断器，id=" + id);  
		return new User(2L, "熔断", "熔断器", "熔断器", Instant.now());
	}
}
