package com.orpc.register;

import javax.annotation.PostConstruct;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @category 服务注册
 * @author ayou
 */
@Component
public class ServiceRegistry {
	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

	@Value("${orpc.registry-address}")
	private String zkAddress;
	private ZkClient zkClient;

	@PostConstruct
	public void init() {
		// 创建zk客户端
		zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
		logger.debug("connect to zookeeper");
	}

	public void register(String serviceName, String serviceAddress) {
		// 创建register节点（持久）
		String registryPath = Constant.ZK_REGISTRY_PATH;
		if (!zkClient.exists(registryPath)) {
			zkClient.createPersistent(registryPath);
			logger.debug("create registry node:{}", registryPath);
		}
		// 创建service节点（永久）
		String servicePath = registryPath + "/" + serviceName;
		if (!zkClient.exists(servicePath)) {
			zkClient.createPersistent(servicePath);
			logger.debug("create srevice node:{}", registryPath);
		}

		// 创建address节点（零时）
		String addressPath = servicePath + "/address-";
		String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);

		logger.debug("create address node:{}", addressNode);
	}
}
