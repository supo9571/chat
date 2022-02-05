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
package com.pangugle.framework.sms.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.sms.SmsService;

public class AliSmsServiceImpl implements SmsService {
	
	private static final int DEFAULT_THREAD_COUNT = 20;

	private static final String TIMEOUT = "5000"; // 3s
	
	private static final Log LOG = LogFactory.getLog(AliSmsServiceImpl.class);

	private static final String product = "Dysmsapi";// 短信API产品名称（短信产品名固定，无需修改）
	private static final String domain = "dysmsapi.aliyuncs.com";// 短信API产品域名（接口地址固定，无需修改）
	private IAcsClient mAcsclient;
	private String mSmsCode;
	private String mSign; 

	private ExecutorService mThreadPool =  Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
	
	public AliSmsServiceImpl() {
		MyConfiguration conf = MyConfiguration.getInstance();
		try {
			this.mSmsCode = conf.getString("aliyun.sms.code");
			this.mSign = conf.getString("aliyun.sms.sign");
			String accesskey = conf.getString("aliyun.AccessKeyId");
			String accessKeySecret = conf.getString("aliyun.AccessKeySecret");
			// 设置超时时间-可自行调整
			System.setProperty("sun.net.client.defaultConnectTimeout", TIMEOUT);
			System.setProperty("sun.net.client.defaultReadTimeout", TIMEOUT);
			// 初始化ascClient需要的几个参数

			// 初始化ascClient,暂时不支持多region（请勿修改）
			IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accesskey, accessKeySecret);
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
			this.mAcsclient = new DefaultAcsClient(profile);
		} catch (ClientException e) {
			LOG.error("init aliyun sms error:", e);
		}

	}

	@Override
	public void send(String mobile, String code, Callback<Boolean> callback) {
		mThreadPool.execute(new Task(mobile, code, callback));
	}

	@Override
	public void sendMore(String mobiles, String content, Callback<Boolean> callback) {
		
	}
	
	private class Task implements Runnable
	{
		private String mobile; 
		private String code;
		private Callback<Boolean> callback;
		
		public Task(String mobile, String code, Callback<Boolean> callback)
		{
			this.mobile = mobile;
			this.code = code;
			this.callback = callback;
		}
		
		public void run() {
			boolean result = false;
			try {
				// 组装请求对象
				SendSmsRequest request = new SendSmsRequest();
				// 使用post提交
				request.setMethod(MethodType.POST);
				// 必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
				request.setPhoneNumbers(mobile);
				// 必填:短信签名-可在短信控制台中找到
				request.setSignName(mSign);
				// 必填:短信模板-可在短信控制台中找到
				request.setTemplateCode(mSmsCode);
				// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
				// 友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
				request.setTemplateParam("{\"code\":\""+ code + "\"}");
				// 可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
				// request.setSmsUpExtendCode("90997");
				// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
				//request.setOutId("yourOutId");
				// 请求失败这里会抛ClientException异常
				
				SendSmsResponse sendSmsResponse = mAcsclient.getAcsResponse(request);
				if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
					// 请求成功
					result = true;
				}
				LOG.debug(sendSmsResponse.getMessage());
			} catch (Exception e) {
				LOG.error("send sms error", e);
			}
			callback.execute(result);
		}
		
	}
	
	public static void main(String[] args)
	{
		AliSmsServiceImpl sms = new AliSmsServiceImpl();
		sms.send("xxxxxxx", "152486", new Callback<Boolean>() {
			
			@Override
			public void execute(Boolean o) {
				System.out.println("result = " + o);
			}
		});
	}

}
