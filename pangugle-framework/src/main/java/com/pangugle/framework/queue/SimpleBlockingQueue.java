/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.queue;

import com.pangugle.framework.service.Callback;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleBlockingQueue<T> {

	private BlockingQueue<T> queue;
	
	private int capacity;
	private AtomicInteger  currentIndex = new AtomicInteger(0);
	
	private boolean isStart = true;
	
	public SimpleBlockingQueue(int capacity)
	{
		this.capacity = capacity;
		this.queue = new LinkedBlockingQueue<>(capacity);
	}
	
	public void add(T t)
	{
		try {
			queue.put(t);
			currentIndex.incrementAndGet();
		} catch (Exception e) {
		}
	}
	
	public void onCallback(Callback<T> callback)
	{
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while(isStart)
				{
					try {
						T t = queue.take();
						currentIndex.decrementAndGet();
						callback.execute(t);
					} catch (Exception e) {
					}
				}
			}
		});
		thread.start();
	}
	
	public boolean isFull()
	{
		return currentIndex.get() >= capacity;
	}
	
	public static void main(String[] args)
	{
		SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<String>(100);
		
		
		queue.onCallback(new Callback<String>() {
			
			@Override
			public void execute(String o) {
				System.out.println("consumer " + o);
			}
		});
		
		queue.add("aaa");
		queue.add("aaa");
		queue.add("aaa");

	}
	
}
