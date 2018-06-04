package com.orpc.common.core.loadbalance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 加权轮询法
 */
public class RoundRobinWeight {
	Integer pos = 0;

	public Object get(Map<Object, Object> map) {
		// 重新创建一个map，避免出现由于服务上线和下线导致的并发问题
		Map<Object, Object> serverMap = new LinkedHashMap<>();
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
		Object server = null;
		synchronized (pos) {
			if (pos >= serverList.size()) {
				pos = 0;
			}
			server = serverList.get(pos);
			pos++;
		}
		return server;
	}
}
