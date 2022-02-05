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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

/**
 * 
 * @author zgz
 *
 */
public class FastJsonHelper {
	
	private static Log LOG = LogFactory.getLog(FastJsonHelper.class);
	
	public static String jsonEncode(Object obj)
	{
		return JSON.toJSONString(obj, 
				SerializerFeature.DisableCircularReferenceDetect,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullListAsEmpty,
				SerializerFeature.WriteNullBooleanAsFalse,
				SerializerFeature.WriteMapNullValue);
	}
	
	public static <T> T jsonDecode(String json, Class<T> clazz)
	{
		try {
			return (T)JSON.parseObject(json, clazz);
		} catch (Exception e) {
			//LOG.debug("parse object error and the json is = " + json , e);
		}
		return null;
	}
	
	public static JSONObject toJSONObject(String jsonString) {
		JSONObject json = null;
		try {
			json = JSON.parseObject(jsonString);
		} catch (Exception e) {
			//LOG.error("to json objec error, and the json " + jsonString, e);
		}
		return json;
	}
	
	public static <T> List<T> parseArray(String json, Class<T> clazz)
	{
		try {
			return JSONObject.parseArray(json, clazz);
		} catch (Exception e) {
			LOG.error("parse array error, and then json = " + json, e);
		}
		return null;
	}
	
	public static JSONArray parseArray(String json)
	{
		try {
			return JSONObject.parseArray(json);
		} catch (Exception e) {
			//LOG.error("parse array error, and then json = " + json, e);
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String jsonString) {
		Map<String, Object> map = JSON.parseObject(jsonString, Map.class);
		return map;
	}

	/**
	 * 获取第一个key值
	 *
	 * @return
	 */
	public static String getFirstKey(JSONObject jsonObject) {
		String obj = null;
		for (String str : jsonObject.keySet()) {
			obj = str;
			if (obj != null) {
				break;
			}
		}
		return  obj;
	}

	public static void main(String[] args) {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("test", 1);
		
		String jsonString = jsonEncode(json);
		System.out.println("对象转成JSON字符串 : " + jsonString);
		System.out.println("JSON字符串转成对象 : ");
		JSONObject jsonObj = jsonDecode(jsonString, JSONObject.class);
		System.out.println(jsonObj);
	}
	
}
