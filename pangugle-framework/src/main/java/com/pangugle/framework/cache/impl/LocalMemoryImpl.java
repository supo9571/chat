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
package com.pangugle.framework.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pangugle.framework.cache.CacheService;
import com.pangugle.framework.utils.StringUtils;

public class LocalMemoryImpl implements CacheService{
	
	private Map<String, Object> maps = new HashMap<String,Object>();

	@Override
	public void setString(String key, String value, int seconds) {
		Map<String, Object> valueMaps = new HashMap<String, Object>();
		valueMaps.put("value", value);
		valueMaps.put("seconds", seconds);
		valueMaps.put("start", System.currentTimeMillis());
		maps.put(key, valueMaps);
	}

	@Override
	public String getString(String key) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> valueMaps = (Map<String, Object>) maps.get(key);
			if(valueMaps == null || valueMaps.isEmpty()) return null;
			String value = (String) valueMaps.get("value");
			int seconds = StringUtils.asInt(valueMaps.get("seconds"));
			long start = StringUtils.asLong(valueMaps.get("start"));
			if((System.currentTimeMillis() - start) / 1000 <= seconds)
			{
				return value;
			}
		} catch (Exception e) {
		}
		delete(key);
		return null;
	}

	@Override
	public void delete(String key) {
		maps.remove(key);
	}


	@Override
	public void setMultiKeys(Map<String, Object> keyValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> List<T> getMultiString(Class<T> clazz, String... keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String key) {
		// TODO Auto-generated method stub
		return false;
	}

}
