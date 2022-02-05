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
package com.pangugle.framework.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.context.MyAppContext;
import com.pangugle.framework.context.MyEnvironment;

@EnableAutoConfiguration
public class SpringBootManager {
	
//	private static final int DEFAULT_SERVER_PORT = 55555;
	
	private static int mServerPort = -1;
	
	public static void run(Class<?> clazz, String serverPortKey, String logName, String... args) {
		MyAppContext.init(clazz, logName);
		// 配置服务端口
		MyConfiguration conf = MyConfiguration.getInstance();
		int port = conf.getInt(serverPortKey);
		mServerPort = port;
		run(clazz, port, args);
	}
	
	public static int getServerPort()
	{
		return mServerPort;
	}
	
//	private static void run(Class<?> clazz, String... args)
//	{
//		MyAppContext.init(clazz);
//		run(clazz, DEFAULT_SERVER_PORT, args);
//	}
	
	private static void run(Class<?> clazz, int port, String... args) {
		System.setProperty("server.port", port + "");
		SpringApplication.run(clazz, args);
		System.out.println("==========> env  = " + MyEnvironment.getEnv());
		System.out.println("==========> port = " + port);
	}
	
}

