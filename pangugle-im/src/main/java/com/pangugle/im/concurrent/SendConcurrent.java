package com.pangugle.im.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendConcurrent {
	
	// 并发线程数
	public static int CONCURRENT_SIZE = 10;

	private static SendConcurrent instance = null;

	private ExecutorService pool = null;

	public static SendConcurrent getInstance() {
		if (instance == null)
			instance = new SendConcurrent();
		return instance;
	}

	private SendConcurrent() {
		initPool();
	}

	private void initPool() {
		// 创建一个可重用固定线程数的线程池：
		// 目前给定的最大数目是10线程，可以KChat运维过程中根据综
		// 合性能和压力情况调整此值以便达到最优状态
		pool = Executors.newFixedThreadPool(CONCURRENT_SIZE);
	}

	public void execute(Runnable r) {
		pool.execute(r);
	}

}