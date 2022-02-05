package com.pangugle.im.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioConvertConcurrent {
	
	// 并发线程数
	public static int CONCURRENT_SIZE = 50;

	private static AudioConvertConcurrent instance = null;

	private ExecutorService pool = null;

	public static AudioConvertConcurrent getInstance() {
		if (instance == null)
			instance = new AudioConvertConcurrent();
		return instance;
	}

	private AudioConvertConcurrent() {
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
