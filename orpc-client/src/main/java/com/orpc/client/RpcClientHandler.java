package com.orpc.client;

import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.orpc.common.core.RpcResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);

	// 存放请求编号与响应对象之间的映射关系
	private ConcurrentMap<String, RpcResponse> responseMap;

	public RpcClientHandler(ConcurrentMap<String, RpcResponse> responseMap) {
		this.responseMap = responseMap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
		// 建立请求编号与响应对象之间的映射关系
		responseMap.put(rpcResponse.getRequestId(), rpcResponse);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("client caught exception", cause);
		ctx.close();
	}

}
