package com.orpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.orpc.common.core.RpcRequest;
import com.orpc.common.core.RpcResponse;
import com.orpc.common.serialize.RpcDecoder;
import com.orpc.common.serialize.RpcEncoder;
import com.orpc.register.ServiceDiscovery;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

@Component
public class RpcClient {
	private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
	@Autowired
	private ServiceDiscovery serviceDiscovery;

	// 存放请求编号与响应对象之间的关系
	private ConcurrentMap<String, RpcResponse> responseMap = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// 创建PRC请求对象
						RpcRequest request = new RpcRequest();
						request.setRequestId(UUID.randomUUID().toString());
						request.setInterfaceName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);
						// 获取RPC服务地址
						String serviceName = interfaceClass.getName();
						String serviceAddress = serviceDiscovery.discover(serviceName);
						logger.debug("discover service: {} => {}", serviceName, serviceAddress);
						if (StringUtils.isBlank(serviceAddress)) {
							throw new RuntimeException("server address is empty");
						}
						// 从RPC服务地址中解析主机名与端口号
						String[] array = StringUtils.split(serviceAddress, ":");
						String host = array[0];
						int port = Integer.parseInt(array[1]);
						// 发送RPC请求
						RpcResponse response = send(request, host, port);
						System.out.println(response);
						if (response == null) {
							logger.error("send request failure", new IllegalStateException("response is null"));
							return null;
						}
						if (response.hasException()) {
							logger.error("response has exception", response.getException());
							return null;
						}
						if(null != response.getResult()) {
							System.out.println(response.getResult());
						}
						// 获取响应结果
						return response.getResult();
					}
				});
	}

	private RpcResponse send(RpcRequest request, String host, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new RpcEncoder(RpcRequest.class));
					pipeline.addLast(new RpcDecoder(RpcResponse.class));
					pipeline.addLast(new RpcClientHandler(responseMap));
				}
			});
			ChannelFuture future = bootstrap.connect(host, port).sync();
			// 写入RPC请求
			Channel channel = future.channel();
			channel.writeAndFlush(request).sync();
			channel.closeFuture().sync();
			// 获取RPC响应对象
			return responseMap.get(request.getRequestId());
		} catch (Exception e) {
			logger.error("client exception", e);
			return null;
		} finally {
			// 关闭RPC链接
			group.shutdownGracefully();
			// 移除请求编号与响应对象之间的映射关系
			//responseMap.remove(request.getRequestId());
		}

	}
}
