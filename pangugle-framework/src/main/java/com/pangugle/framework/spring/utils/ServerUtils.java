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
package com.pangugle.framework.spring.utils;

import org.apache.commons.lang3.StringUtils;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.spring.SpringBootManager;
import com.pangugle.framework.utils.NetUtils;

public class ServerUtils {
	
	private static MyConfiguration conf = MyConfiguration.getInstance();
	
	public static String getApiServer()
	{
		String apiServer = conf.getString("api.server");
		if(StringUtils.isEmpty(apiServer))
		{
			apiServer = "http://" + NetUtils.getLocalHost() + ":" + SpringBootManager.getServerPort();
		}
		return apiServer;
	}
	
	public static String getStaticServer()
	{
		String server = conf.getString("static.server");
		if(StringUtils.isEmpty(server))
		{
			server = "http://" + NetUtils.getLocalHost() + ":" + SpringBootManager.getServerPort();
		}
		return server;
	}
	
	public static String getUploadServer()
	{
		String server = conf.getString("upload.server");
		if(StringUtils.isEmpty(server))
		{
			server = "http://" + NetUtils.getLocalHost() + ":" + SpringBootManager.getServerPort();
		}
		return server;
	}
	
	public static String getSocketServer()
	{
		String socketServer = conf.getString("socket.server");
		int port = conf.getInt("socketio.port");
		if(StringUtils.isEmpty(socketServer))
		{
			socketServer = "http://" + NetUtils.getLocalHost() + ":" + port;
		}
		return socketServer;
	}
	
	public static String getOfficialServer()
	{
		String server = conf.getString("official.server");
		if(StringUtils.isEmpty(server))
		{
			server = "http://" + NetUtils.getLocalHost() + ":" + SpringBootManager.getServerPort();
		}
		return server;
	}
	
	public static void main(String[] args)
	{
		System.out.println(getApiServer());
		System.out.println(getSocketServer());
	}

}
