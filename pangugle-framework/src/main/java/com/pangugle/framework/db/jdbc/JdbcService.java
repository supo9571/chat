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
package com.pangugle.framework.db.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.pangugle.framework.service.Callback;

public interface JdbcService {
	
	public Connection getConnection() throws SQLException;
	
	public JdbcTemplate getJdbcTemplate();

	public long count(String sql, Object... values);

	public BigDecimal bigDecimalCount(String sql, Object... values);

	public <T> T queryForObject(String sql, Class<T> requiredType);
	public <T> T queryForObject(String sql, Class<T> requiredType, Object...values);
	
	public <T> List<T> queryForList(String sql, Class<T> requiredType, Object...values);
	public Map<String, Object> queryForMap(String sql, Object... values);
	public List<Map<String,Object>> queryForListMap(String sql, Object...values);
	
	public void queryResultSet(Callback<ResultSet> callback, String sql, Object... values);
	
	public <T> void queryAll(Callback<T> callback, String sql, Class<T> requiredType, Object...values);

	public int executeUpdate(String sql, Object... values);
	public long executeInsert(String sql,Object... values);//返回主键

}
