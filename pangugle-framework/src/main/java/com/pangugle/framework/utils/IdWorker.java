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

import java.util.Date;

public class IdWorker {
	
	private static final int DEFAULT_NUMBER_ID_LEN = 21; // 数字id长度
   
	/**
	 * 数字id生成器
	 * 生成规则：yyyyMMddHHmmss + 随机数(len - 15), 每秒最多生成999999个
	 * @param len
	 * @return
	 */
	private static String generatorNumberId(int len)
	{
		if(len <= 15) {
			throw new RuntimeException("len > 15");
		}
		String timeString = DateUtils.convertString(new Date(System.currentTimeMillis()));
		return timeString + RandomStringUtils.generator0_9(len - 15);
	}
	
	/**
	 * 20位id
	 * @return
	 */
	public static String generatorNumberId()
	{
		return generatorNumberId(DEFAULT_NUMBER_ID_LEN);
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(generatorNumberId());
	}


}
