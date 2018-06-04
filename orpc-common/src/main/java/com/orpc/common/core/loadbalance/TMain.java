package com.orpc.common.core.loadbalance;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TMain {
	public static void main(String[] args) {
		Map<Object, Object> map = new LinkedHashMap<>();
		map.put("192.168.1.1", 1);// 权重1
		map.put("192.168.1.2", 2);
		map.put("192.168.1.3", 3);
		map.put("192.168.1.4", 4);
		map.put("192.168.1.5", 1);
		map.put("192.168.1.6", 1);
		map.put("192.168.1.7", 2);
		map.put("192.168.1.8", 1);
		map.put("192.168.1.9", 4);
		map.put("192.168.1.10", 1);
		map.put("192.168.1.11", 1);
		map.put("192.168.1.12", 3);
		map.put("192.168.1.13", 1);
		map.put("192.168.1.14", 1);
		map.put("192.168.1.15", 2);
		map.put("192.168.1.16", 4);
		map.put("192.168.1.17", 1);
		map.put("192.168.1.18", 1);
		map.put("192.168.1.19", 2);
		map.put("192.168.1.20", 3);
		map.put("192.168.1.21", 4);
		RoundRobin roundRobin = new RoundRobin();
		RoundRobinWeight roundRobinWeight = new RoundRobinWeight();
		boolean flag = false;
		ExecutorService executor = Executors.newFixedThreadPool(4);//4个并发线程
		for (int i = 1; i < 1001; i++) {
			if (flag) {// 轮询
				for(int j = 1; j < 20; j++){
					CompletableFuture.runAsync(()->{
						System.out.println(roundRobin.get(map) +"---"+ Thread.currentThread().getId());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						/*if (index % 10 == 0) {
							System.err.println(roundRobin.get(map) + "\t");
							//System.err.print("--" + roundRobin.pos);
						} else {
							System.out.print(roundRobin.get(map) + "\t");
						}*/
					}, executor);
				}
			}
			if (true) {// 加权轮询
				/*if (i % 10 == 0) {
					System.err.println(roundRobinWeight.get(map) + "\t");
					//System.err.print("--" + roundRobinWeight.pos);
				} else {
					System.out.print(roundRobinWeight.get(map) + "\t");
				}*/
				for(int j = 1; j < 20; j++){
					CompletableFuture.runAsync(()->{
						System.out.println(roundRobinWeight.get(map) +"---"+ Thread.currentThread().getId());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}, executor);
				}
			}
			if (flag) {// 随机
				if (i % 10 == 0) {
					System.err.println(Random.get(map) + "\t");
				} else {
					System.out.print(Random.get(map) + "\t");
				}
			}
			if (flag) {// 加权随机
				if (i % 10 == 0) {
					System.err.println(RandomWeight.get(map) + "\t");
				} else {
					System.out.print(RandomWeight.get(map) + "\t");
				}
			}
			if (flag) {// 哈希
				java.util.Random r = new java.util.Random();
				if (i % 10 == 0) {
					System.err.println(Hash.get(map,"192.168.1.19") + "\t");
				} else {
					System.out.print(Hash.get(map,"192.168.1." + r.nextInt(map.size())) + "\t");
				}
			}
		}
		executor.shutdown();
	}
}
