package com.orpc.common.core.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 源地址哈希法
 */
public class Hash {
	public static Object get(Map<Object, Object> map, String ip) {
		// 重新创建一个map，避免出现由于服务上线和下线导致的并发问题
		Map<Object, Object> serverMap = new HashMap<>();
		serverMap.putAll(map);

		// 取ip地址list
		Set<Object> keySet = serverMap.keySet();
		ArrayList<Object> keyList = new ArrayList<>();
		keyList.addAll(keySet);

		int hashCode = ip.hashCode();
		int serverListSize = keyList.size();
		int serverPos = hashCode % serverListSize;
		return keyList.get(serverPos);
	}
}
