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
package com.pangugle.framework.mq;

import java.io.IOException;
import java.util.Map;

import com.google.common.collect.Maps;
import com.pangugle.framework.mq.impl.RedisMQImpl;
import com.pangugle.framework.mq.impl.RocketMQImpl;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.ThreadUtils;

public class MQManager{
	
	Map<String, MQSupport> maps = Maps.newConcurrentMap();
	
	private interface ManagerInternal {
		public MQManager mgr = new MQManager();
	}
	
	public static MQManager getInstance()
	{
		return ManagerInternal.mgr;
	}
	
	private MQManager()
	{
		maps.put(MQImpl.REDIS.name(), new RedisMQImpl());
		maps.put(MQImpl.ROCKETMQ.name(), new RocketMQImpl());
	}
	
	public MQSupport getMQ()
	{
		return maps.get(MQImpl.ROCKETMQ.name());
	}
	
	public static enum MQImpl{
		REDIS, // redis
		ROCKETMQ; // rocketmq
	}
	
	public static void main(String[] args) throws InterruptedException, IOException
	{
		String queue = "pangugle_test";
		
		String tags = null;
		
		MQSupport mq = MQManager.getInstance().getMQ();
		
		mq.subscribe(queue, tags, new Callback<String>() {
			public void execute(String o) {
				System.out.println("consuemr 1 " + o);
			}
		});
		
//		mq.consume(queue, tags, new Callback<String>() {
//			public void execute(String o) {
//				System.out.println("consuemr 1 " + o);
//			}
//		});
		
		for(int i = 0; i < 1000; i ++)
		{
			mq.sendMessage(queue, "i = " + i, tags);
			ThreadUtils.sleep(1000);
		}
		
		System.in.read();
	}
	
}
