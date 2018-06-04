package com.orpc.common.core.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 随机
 */
public class Random {
	public static Object get(Map<Object, Object> map) {
		// 重新创建一个map，避免出现由于服务上线和下线导致的并发问题
		Map<Object, Object> serverMap = new HashMap<>();
		serverMap.putAll(map);

		// 取ip地址list
		Set<Object> keySet = serverMap.keySet();
		ArrayList<Object> keyList = new ArrayList<>();
		keyList.addAll(keySet);

		java.util.Random random = new java.util.Random();
		int randomPos = random.nextInt(keyList.size());

		Object server = keyList.get(randomPos);
		return server;
	};
}
