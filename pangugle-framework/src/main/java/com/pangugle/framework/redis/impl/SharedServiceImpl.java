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
package com.pangugle.framework.redis.impl;

import java.util.List;
import java.util.Map;

import com.pangugle.framework.redis.RedisService;
import com.pangugle.framework.redis.client.SharedJedisManager;

/**
 * 
 * @author Administrator
 *
 */
public class SharedServiceImpl implements RedisService {
	
	private SharedJedisManager client = SharedJedisManager.getInstance();

	// ============================== get ==============================
	public String getString(String key) {
		return client.getString(key);
	}

	public List<Object> getList(String... keys)
	{
		return client.getStringByPipeline(keys);
	}

	public void setString(String key, String value, int expire) {
		if(expire <= 0) 
		{
			client.setString(key, value);
		}
		else
		{
			client.setString(key, value, expire);
		}
	}

	public void delete(String key) {
		client.delete(key);
	}
	
	public boolean exists(String key)
	{
		return client.exists(key);
	}
	
	public List<Object> getStringByPipeline(String... keys)
	{
		return client.getStringByPipeline(keys);
	}
	public void setStringByPipeline(Map<String, Object> keyValue)
	{
		client.setStringByPipeline(keyValue);
	}

	public static void main(String[] args)
	{
//		SharedServiceImpl2 service = MyBeanFactory.getInstance(SharedServiceImpl2.class);
//		service.setString("test", "testvalue", 10);
//		System.out.println(service.getString("test"));
	}

}
