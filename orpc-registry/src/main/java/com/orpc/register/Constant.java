package com.orpc.register;

public interface Constant {
	int ZK_SESSION_TIMEOUT = 5000;
	int ZK_CONNECTION_TIMEOUT = 1000;
	String ZK_REGISTRY_PATH = "/orpc-registry";
	String ZK_DATA_PATH = ZK_REGISTRY_PATH + "data";
}
