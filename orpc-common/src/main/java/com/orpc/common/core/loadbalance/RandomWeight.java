package com.orpc.common.core.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 加权随机
 */
public class RandomWeight {
	public static Object get(Map<Object, Object> map) {
		// 重新创建一个map，避免出现由于服务上线和下线导致的并发问题
		Map<Object, Object> serverMap = new HashMap<>();
		serverMap.putAll(map);

		// 取ip地址list
		Set<Object> keySet = serverMap.keySet();
		Iterator<Object> it = keySet.iterator();

		List<Object> serverList = new ArrayList<>();

		while (it.hasNext()) {
			Object server = it.next();
			Integer weight = (Integer) serverMap.get(server);
			for (int i = 0; i < weight; i++) {
				serverList.add(server);
			}
		}

		Random random = new Random();
		int randomPos = random.nextInt(serverList.size());
		Object server = serverList.get(randomPos);

		return server;
	}
}
