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
package com.pangugle.framework.sms;


import java.io.IOException;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.sms.impl.AliSmsServiceImpl;
import com.pangugle.framework.utils.StringUtils;

public class SmsManager {
	
	private Log LOG = LogFactory.getLog(SmsManager.class);
	
	private CacheManager cache;
	private SmsService mSmsService;
	
	public SmsManager() {
		this.cache = CacheManager.getInstance();
		this.mSmsService = new AliSmsServiceImpl();
	}
	
	/**
	 * 目前是用阿里云的
	 * @param mobile
	 * @param code
	 * @param ip
	 * @param callback
	 */
	public void send(String mobile, String code, String ip, Callback<Boolean> callback) 
	{
//		String content = mValidCodeTemplate.replace("${validcode}", code);
		String content = code;
		if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(ip) || StringUtils.isEmpty(content)) 
		{
			callback.execute(false);
		} 
		else
		{
			SmsModel model = new SmsModel(ip,mobile);
			model.loadStatus(cache);
			//detectBeforeSend(model);
			if (model.checkValid()){
				mSmsService.send(mobile, content, new Callback<Boolean>() {
					public void execute(Boolean o) {
						if (o){
							model.incrSend();
							model.incrRequst();
							//detectAfterSend(model);
							model.saveStatus(cache);
							LOG.info("[ mobile : " + mobile + ", ip : " + ip + " ], " + "content is " + content);
						}
						if(callback != null) callback.execute(o);
					}
				});
			} 
		}
	}
	
	private void test(int index)
	{
		LOG.debug("start send " + index + " ......");
		String content = "256845";
		send("xxxxxxx", content, "192.168.1.11", new Callback<Boolean>() {
			public void execute(Boolean o) {
				System.out.println(o);
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		SmsManager smsClient = new SmsManager();
	
//		smsClient.testStatus();
//		smsClient.testClean(mobile);
		int index = 0;
//		for(int i = 0; i < 10; i ++)
//		{
			smsClient.test(index ++);
//			ThreadUtils.sleep(61000);
//		}
		System.in.read();
		
	}
	
}
