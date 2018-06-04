package com.orpc.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.orpc")
public class ClientApplication {
//
//	@Autowired
//	private RpcClient rpcClient;
//
//	@PostConstruct
//	public void run() {
//		HelloService helloService = rpcClient.create(HelloService.class);
//		String result = helloService.say("world");
//		System.err.println(result);
//
//		UserService userService = rpcClient.create(UserService.class);
//		/*User user = userService.view(111000L);
//		System.err.println("User返回： " + user);*/
//
//		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
//		//Semaphore semaphore = new Semaphore(0,true);
//		fixedThreadPool.execute(() -> {
//			for (int i = 0; i < 100; i++) {
//				System.out.println(userService.view(111000L)+"----->"+i);
//				//semaphore.release();
//			}
//		});
//		/*try {
//			semaphore.acquire(100);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}*/
//		fixedThreadPool.shutdown();
//	}
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new HystrixRequestContextFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(ClientApplication.class, args).close();
		SpringApplication.run(ClientApplication.class, args);
	}
	

}
