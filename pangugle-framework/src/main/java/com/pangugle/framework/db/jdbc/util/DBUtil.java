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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringEscapeUtils;


public class DBUtil {
	
	public static int executeUpdate(Connection conn, String sql, Object... values) throws SQLException
	{
		int rs = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = executeUpdate(pstmt, values);
		} catch (SQLException e) {
			throw e;
		} finally {
			closeStatement(pstmt);
		}
		return rs;
	}
	
	public static ResultSet executeQuery(PreparedStatement pstmt, Object... values) throws SQLException
	{
		if(values != null)
		{
			int len = values.length;
			for(int i = 0; i < len; i ++)
			{
				setValue(pstmt, i + 1, values[i]);
			}
		}
		return pstmt.executeQuery();
	}
	
	public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeStatement(Statement st)
	{
		try {
			if(st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		st = null;
	}
	
	public static void rollback(Connection conn)
	{
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setAutoCommit(Connection conn, boolean autoCommit)
	{
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int executeUpdate(PreparedStatement pstmt, Object... values) throws SQLException
	{
		if(values != null)
		{
			int len = values.length;
			for(int i = 0; i < len; i ++)
			{
				setValue(pstmt, i + 1, values[i]);
			}
		}
		return pstmt.executeUpdate();
	}
	
	
	private static void setValue(PreparedStatement pstmt, int index, Object value) throws SQLException
	{
		if(value instanceof String)
		{
			String valueString = escapeSql((String)value);
			pstmt.setObject(index, valueString);
		} else
		{
			pstmt.setObject(index, value);
		}
	}
	
	public static String escapeSql(String value)
	{
		String valueString = StringEscapeUtils.escapeSql((String)value);
		return valueString;
	}
	
	public static void main(String[] args)
	{
	}
	
}
