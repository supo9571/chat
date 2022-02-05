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
package com.pangugle.framework.db;

public class MyDBConfigManager {

	public static final int DEFAULT_JDBC_INIT_SIZE = 1;
	public static final int DEFAULT_JDBC_MIN_IDLE = 5;
	public static final int DEFAULT_JDBC_MAX_ACTIVE = 20;
	public static final int DEFAULT_JDBC_TIME_BETWEEN_EVICTION_RUN = 1000 * 60; // 空闲检测时间
	public static final int DEFAULT_JDBC_IDLE_MINUTE = 60 * 3; // 60 分钟, mysql
																// 默认最大超时为8小时

	public static final String DB_GLOBAL_MASTER = "globaldb.master";
	public static final String DB_GLOBAL_SLAVE = "globaldb.slave1";


	public static enum DBCenter {
		GLOBAL_MASTER {
			public String getValue() {
				return DB_GLOBAL_MASTER;
			}
		},
		GLOBAL_SLAVE {
			public String getValue() {
				return DB_GLOBAL_SLAVE;
			}
		},;
		public abstract String getValue();
	}
}
