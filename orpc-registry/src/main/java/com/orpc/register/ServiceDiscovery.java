package com.orpc.register;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * @ 服务发现
 * 
 * @author ayou
 */
@Component
public class ServiceDiscovery {
	private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

	@Value("${orpc.registry-address}")
	private String zkAddress;

	public String discover(String name) {
		ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
		logger.debug("connect to zookeeper");
		try {
			// 获取service 节点
			String servicePath = Constant.ZK_REGISTRY_PATH + "/" + name;
			if (!zkClient.exists(servicePath)) {
				throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
			}
			List<String> addressList = zkClient.getChildren(servicePath);
			if (CollectionUtils.isEmpty(addressList)) {
				throw new RuntimeException(String.format("can not find any addreess node on path: %s", servicePath));
			}
			// 获取address节点
			String address;
			int size = addressList.size();
			if (size == 1) {
				// 若只有一个地址
				address = addressList.get(0);
				logger.debug("get only address node: {}", address);
			} else {// 这里应该有一堆算法，暂时用随机
				address = addressList.get(ThreadLocalRandom.current().nextInt(size));
				logger.debug("get random address node: {}", address);
			}
			// 获取address节点的值
			String addressPath = servicePath + "/" + address;
			return zkClient.readData(addressPath);
		} finally {
			zkClient.close();
		}
	}
}
