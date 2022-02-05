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
package com.pangugle.framework.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pangugle.framework.utils.FastJsonHelper;


/**
 * 接口返回模块
 * 默认{error:0, errmsg:success}
 * @author Administrator
 *
 */
public class ApiJsonTemplate {
	
	private Map<String, Object> dataMap = new HashMap<String, Object>();
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	public ApiJsonTemplate()
	{
		this(SystemErrorResult.SUCCESS);
	}
	
	public ApiJsonTemplate(ErrorResult result)
	{
		setJsonResult(result);
	}
	
	public void setJsonResult(ErrorResult error)
	{
		dataMap.put("code", error.getCode());
		dataMap.put("msg", error.getError());
	}
	
	public void setError(int code, String msg)
	{
		dataMap.put("code", code);
		dataMap.put("msg", msg);
	}
	
	public ApiJsonTemplate addKeyValue(String key, Object value)
	{
		dataMap.put(key, value);
		return this;
	}
	
	/**
	 * success
	 * @param data
	 * @return
	 */
	public ApiJsonTemplate setData(Object data)
	{
		dataMap.put("data", data);
		setJsonResult(SystemErrorResult.SUCCESS);
		return this;
	} 
	
	public String toJSONString()
	{
		return FastJsonHelper.jsonEncode(dataMap);
	}


	public String toString()
	{
		return toJSONString();
	}
	
	public static String buildErrorResult(ErrorResult result)
	{
		return new ApiJsonTemplate(result).toJSONString();
	}
	
	public static void main(String[] args) 
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("key", "v1");
		
		Map<String, Object> dataMap2 = new HashMap<String, Object>();
		dataMap2.put("key", "v1");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		list.add(dataMap);
		list.add(dataMap2);
		
		ApiJsonTemplate template = new ApiJsonTemplate();
		template.setJsonResult(SystemErrorResult.ERR_EXIST);
		System.out.println(template.toJSONString());
	}


}
