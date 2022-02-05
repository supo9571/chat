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

import org.apache.commons.text.RandomStringGenerator;

public class RandomStringUtils {
	
	private static char [][] pairs = {{'a','z'},{'0','9'}};
	
	private static RandomStringGenerator mA_To_Z_Generator = new RandomStringGenerator.Builder().withinRange('a', 'z').build();
	private static RandomStringGenerator mNumber_Generator = new RandomStringGenerator.Builder().withinRange('0', '9').build();
	private static RandomStringGenerator m0_To_Z_Generator = new RandomStringGenerator.Builder().withinRange(pairs).build();
	
	public static String generatorA_Z(int len)
	{
		return mA_To_Z_Generator.generate(len);
	}
	
	public static String generator0_9(int len)
	{
		return mNumber_Generator.generate(len);
	}
	
	public static String generator0_Z(int len)
	{
		return m0_To_Z_Generator.generate(len);
	}
	
	public static void main(String[] args)
	{
		System.out.println(generator0_9(32));
	}
	
}
