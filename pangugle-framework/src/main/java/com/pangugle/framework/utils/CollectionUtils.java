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
package com.pangugle.framework.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;


public class CollectionUtils {
	
	private static final Object[] mEmptyObjectArray = {};

	public static Object[] emptyObjectArray()
	{
		return mEmptyObjectArray;
	}
	
	public static Map<String, Object> asHashMap(Map<Integer, Object> input)
	{
		if(input == null || input.isEmpty()) return Collections.emptyMap();
		Map<String, Object> temp = new HashMap<String, Object>();
		for(Integer key : input.keySet())
		{
			temp.put(key + "", input.get(key));
		}
		return temp;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List map2List(Map<String, ?> maps)
	{
		if(maps == null || maps.isEmpty()) return Collections.emptyList();
		
		List list = Lists.newArrayList();
		for(String key : maps.keySet())
		{
			list.add(maps.get(key));
		}
		return list;
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}


	
}
