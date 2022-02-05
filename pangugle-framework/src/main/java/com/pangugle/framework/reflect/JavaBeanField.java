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
package com.pangugle.framework.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.pangugle.framework.utils.StringUtils;

public class JavaBeanField {
	private static final String DEFAULT_METHOD_NAME = "getColumnPrefix";
	public static final char UNDERLINE = '_';
	
	private Map<String, Field> maps = new HashMap<>();
	
	public JavaBeanField(Class<?> cls)
	{
		try {
			String columnPrefix = StringUtils.getEmpty();
			Method[] methos = cls.getMethods(); //当前类的所有方法，不包括父类的属性
			for(Method m : methos)
			{
				if(m.getName().equals(DEFAULT_METHOD_NAME)) {
					columnPrefix = (String) m.invoke(null);
					break;
				}
			}
			Field[] arrf=cls.getDeclaredFields();
			//遍历属性
			String newColumnPrefix = "";
			for(Field f : arrf){
			    //设置忽略访问校验
			    f.setAccessible(true);
			    if(!StringUtils.isEmpty(columnPrefix) && StringUtils.isEmpty(newColumnPrefix))
			    {
					newColumnPrefix = columnPrefix + UNDERLINE;
			    }
			    String dbFieldName = newColumnPrefix + StringUtils.camelToUnderline(f.getName());
			    maps.put(dbFieldName, f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Field getField(String key)
	{
		return maps.get(key);
	}



	
	

}
