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

public class SelectSQLBuilder extends BaseSQLBuilder{
	
	public static SelectSQLBuilder builder()
	{
		SelectSQLBuilder buider = new SelectSQLBuilder();
		return buider;
	}
	
	public SelectSQLBuilder select(String... columns)
	{
		buffer.append("select ");
		join(",", columns);
		return this;
	} 
	
	public SelectSQLBuilder from(String... tables)
	{
		buffer.append(" from ");
		join(",", tables);
		return this;
	}
	
	public SelectSQLBuilder leftJoin(String leftJoin)
	{
		buffer.append(" left join " + leftJoin);
		return this;
	}
	
	public SelectSQLBuilder innerJoin(String innerJoin)
	{
		buffer.append(" left join " + innerJoin);
		return this;
	}
	
	public SelectSQLBuilder where(String... whereParames)
	{
		buffer.append(" where ");
		join(" and ", whereParames);
		return this;
	} 
	
	public SelectSQLBuilder orderby(String... columns)
	{
		buffer.append(" order by ");
		join(",", columns);
		return this;
	} 
	
	public SelectSQLBuilder groupby(String... columns)
	{
		buffer.append(" group by ");
		join(",", columns);
		return this;
	}

	public SelectSQLBuilder limit(int index,int size)
	{
		buffer.append(" limit ");
		join(",",index+",");
		join(",",String.valueOf(size));
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
		SelectSQLBuilder buider = new SelectSQLBuilder();
		buider.select("username", "pwd")
			  .from("user", "doc_file")
			  .where("u1 = ?", "u2 = ?", "u3 = ?")
			  .groupby("test")
			  .orderby("username desc", "id desc");
		System.out.println(buider.buider());
	}
	
}
