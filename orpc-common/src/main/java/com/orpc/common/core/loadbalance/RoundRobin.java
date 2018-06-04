package com.orpc.common.core.loadbalance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 轮询
 */
public class RoundRobin {
	Integer pos = 0;

	public Object get(Map<Object, Object> map) {
		// 重新创建一个map，避免出现由于服务上线和下线导致的并发问题
		Map<Object, Object> serverMap = new LinkedHashMap<>();
		serverMap.putAll(map);

		// 取ip地址list
		Set<Object> keySet = serverMap.keySet();
		ArrayList<Object> keyList = new ArrayList<>();
		keyList.addAll(keySet);

		Object server = null;
		synchronized (pos) {
			if (pos >= keySet.size()) {
				pos = 0;
			}
			server = keyList.get(pos);
			pos++;
		}
		return server;
	}
}
