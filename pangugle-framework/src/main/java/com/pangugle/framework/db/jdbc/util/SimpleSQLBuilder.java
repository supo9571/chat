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
package com.pangugle.framework.db.jdbc.util;

import com.pangugle.framework.utils.StringUtils;

public class SimpleSQLBuilder {

	public static String insert(String table, String... keys)
	{
		String sql = "insert into " +  table + "(" + StringUtils.join(keys, ",") + ") " +
				     "values(" + StringUtils.join("?", ",", keys.length) + ")";
		return sql;
	}
	
	public static String buildDeleteSQL(String table, String... keys)
	{
		StringBuffer sb = new StringBuffer("delete from " + table + " where ");
		if(keys != null && keys.length > 0)
		{
			boolean first = true;
			String andString = " and ";
			for(String key : keys)
			{
				if(first) first = false; else sb.append(andString);
				sb.append(key);
			}
		} 
		return sb.toString();
	}
	
	public static String buildCountSQL(String table, String... keys)
	{
		StringBuffer buffer = new StringBuffer("select count(*) from " + table);
		if(keys != null && keys.length > 0)
		{
			buffer.append(" where ");
			boolean first = true;
			String andString = " and ";
			for(String key : keys)
			{
				if(first) first = false; else buffer.append(andString);
				buffer.append(key);
			}
		} 
		return buffer.toString();
	}
	
	public static void main(String[] args)
	{
		String[] insert = {"a", "b"};
		System.out.println("test insert SQL => " + SimpleSQLBuilder.insert("test_user", insert));
		
		String[] countStr = {"a = ?", "b = ?", "c = ?"};
		System.out.println("test count SQL => " + SimpleSQLBuilder.buildCountSQL("test_user", countStr));
		
		String[] deleteStr = {"a = ?", "b = ?", "c = ?"};
		System.out.println("test delete SQL => " + SimpleSQLBuilder.buildDeleteSQL("test_user", deleteStr));
	}
	
	
}
