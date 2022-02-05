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
package com.pangugle.framework.utils;

import java.lang.management.ManagementFactory;

public class RuntimeUtils {
	
	public static String getPID()
	{
		String name = ManagementFactory.getRuntimeMXBean().getName();
		return name.split("@")[0];  
	}
	
	public static void logMemory()
	{
		Runtime runtime = Runtime.getRuntime();
		long maxMemory = runtime.maxMemory() / 1024/ 1024; 
		long freeMemory = runtime.freeMemory() / 1024/ 1024;
		long totalMemory = runtime.totalMemory() / 1024/ 1024;
		
		System.out.println("可以获得最大内存：" + maxMemory + " M ");
		System.out.println("所分配的内存大小：" + freeMemory + " M ");
		System.out.println("已经分配的内存大小：" + totalMemory + " M ");
	}

	public static void main(String[] args)
	{
		String name = ManagementFactory.getRuntimeMXBean().getName();    
		System.out.println(name);    
		String pid = name.split("@")[0];    
		System.out.println("Pid is:" + pid);   
	}
	
}
