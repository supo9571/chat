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

public class UpdateSQLBuilder {
	
	private StringBuffer buffer = new StringBuffer();
	
	public static UpdateSQLBuilder builder()
	{
		UpdateSQLBuilder buider = new UpdateSQLBuilder();
		return buider;
	}
	
	public UpdateSQLBuilder update(String... tables)
	{
		buffer.append("update ").append(StringUtils.join(tables, ","));
		return this;
	} 
	
	public UpdateSQLBuilder set(String... columns)
	{
		buffer.append(" set " + StringUtils.join(columns, ", "));
		return this;
	}
	
	public UpdateSQLBuilder where(String... whereParames)
	{
		buffer.append(" where " + StringUtils.join(whereParames, " and "));
		return this;
	} 
	
	public String buider()
	{
		return buffer.toString();
	}

	public String toString()
	{
		return buider();
	}
	
	public static void main(String[] args)
	{
		UpdateSQLBuilder buider = new UpdateSQLBuilder();
		buider.update("user", "doc_file", "doc_file")
			  .set("u1 = ?", "pwd = ?")
			  .where("u1 = ?", "u2 = ?", "u3 = ?");
		System.out.println(buider.buider());
	}
	
}
