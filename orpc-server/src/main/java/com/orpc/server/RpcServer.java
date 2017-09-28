package com.orpc.server;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.orpc.common.core.RpcRequest;
import com.orpc.common.core.RpcResponse;
import com.orpc.common.serialize.RpcDecoder;
import com.orpc.common.serialize.RpcEncoder;
import com.orpc.register.ServiceRegistry;
import com.orpc.server.annotion.RpcService;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(RpcServiceHandler.class);

	@Value("${orpc.port}")
	private int port;

	@Autowired
	private ServiceRegistry serviceRegistry;

	private Map<String, Object> handlerMap = new HashMap<>();

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		// 扫描带有@RpcService注解的服务类
		Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
		if (!serviceBeanMap.isEmpty()) {
			for (Object serviceBean : serviceBeanMap.values()) {
				RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
				String serviceName = rpcService.value().getName();
				handlerMap.put(serviceName, serviceBean);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		EventLoopGroup childGroup = new NioEventLoopGroup();
		try {
			// 启动RPC服务
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(group, childGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeLine = channel.pipeline();
					pipeLine.addLast(new RpcDecoder(RpcRequest.class));// 解码RPC请求
					pipeLine.addLast(new RpcEncoder(RpcResponse.class));// 解码RPC请求
					pipeLine.addLast(new RpcServiceHandler(handlerMap));// 解码RPC请求
				}
			});
			ChannelFuture future = bootstrap.bind(port).sync();
			logger.debug("server started, listening on {}", port);
			// 注册PRC服务地址
			String serviceAddress = InetAddress.getLocalHost().getHostAddress() + ":" + port;
			for (String interfaceName : handlerMap.keySet()) {
				serviceRegistry.register(interfaceName, serviceAddress);
				logger.debug("register service:{}=>{}", interfaceName, serviceAddress);
			}
			// 释放资源
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("server exception", e);
		} finally {
			// 关闭RPC服务
			childGroup.shutdownGracefully();
			group.shutdownGracefully();
		}
	}

}
