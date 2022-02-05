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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.http.HttpCallback;
import com.pangugle.framework.http.HttpMediaType;
import com.pangugle.framework.http.HttpSesstionManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.sms.SmsService;
import com.pangugle.framework.utils.ThreadUtils;

import okhttp3.Request;
import okhttp3.Response;

public class RWSmsServiceImpl implements SmsService{
	
	private static final String CODING_TYPE = "UTF-8";
	
	public static final String DEFAULT_NODE = "sms";
	public static final String DEFAULT_NODE_MASS = "sms2";
	
	private static final Log LOG = LogFactory.getLog(RWSmsServiceImpl.class);
	private static final HttpSesstionManager mHttpManager = new HttpSesstionManager(10, 300, 100); 

	private String mUrl;
	private String mUserid;
	private String mAccount;
	private String mPassword;
	
	public RWSmsServiceImpl(MyConfiguration conf, String node)
	{
		mUrl = conf.getString(node + ".url");
		mUserid = conf.getString(node + ".userid");
		mAccount = conf.getString(node + ".account");
		mPassword = conf.getString(node + ".password");
	}
	
	@Override
	public void send(String mobile, String content, Callback<Boolean> callback) {
		try {
			sendSms(mUrl, mUserid, mAccount, mPassword, mobile, content, new HttpCallback() {
				
				@Override
				public void onFailure(Throwable e) {
					callback.execute(false);
					LOG.error("send sms error:", e);
				}

				@Override
				public void onSuccess(Request request, Response response, byte[] data)
				{
					String result = new String(data);
					boolean rs = checkSuccess(RWSmsServiceImpl.this, result);
					if (callback != null) callback.execute(rs);
				}
			});
		} catch (UnsupportedEncodingException e) {
			LOG.error("unsupport encoding error:", e);
		}
	}

	@Override
	public void sendMore(String mobiles, String content, Callback<Boolean> callback) {
		send(mobiles, content, callback);
	}
	
	private static void sendSms(String url, String userid, String account,
			String password, String mobile, String content, HttpCallback callback) throws UnsupportedEncodingException {

		StringBuffer send = new StringBuffer();
		send.append("action=send");
		send.append("&userid=" + userid);
		send.append("&account=" + URLEncoder.encode(account, CODING_TYPE));
		send.append("&password=" + URLEncoder.encode(password, CODING_TYPE));
		send.append("&mobile=" + mobile);
		send.append("&content=" + URLEncoder.encode(content, CODING_TYPE));
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("action", "send");
		parameter.put("userid", userid);
		parameter.put("account", account);
		parameter.put("password", password);
		parameter.put("mobile", mobile);
		parameter.put("content", content);

		mHttpManager.asyncPost(url, parameter, HttpMediaType.FORM, callback);
	}
	
	private static boolean checkSuccess(SmsService service, String result) {
		boolean rs = false;
		try {
			Document document = DocumentHelper.parseText(result);
			Element root =  document.getRootElement();
			String returnstatus = root.elementText("returnstatus");// Success | Faild
			String remainpoint = root.elementText("remainpoint");
			if(returnstatus != null && returnstatus.toUpperCase().equalsIgnoreCase("SUCCESS")) {
				
				int remainpointCount = Integer.parseInt(remainpoint);
				// 剩下 2000条,1000条,500条,100条时提醒一次给倪总
				if(remainpointCount == 2000 || 
				   remainpointCount == 1000 ||
				   remainpointCount == 500  || 
				   remainpointCount == 100  ||
				   remainpointCount == 50)
				{
					service.send("13859912070", "短信只剩下 " + remainpointCount + "条!", null);
				}else if(remainpointCount <= 1) {
					service.send("13859912070", "短信余额不足, 已经无法发送短信!", null);
					// 发送失败或暂停10分钟
					ThreadUtils.sleep( 60 * 10 * 1000);
				}
				rs = true;
			} else {
				//LOG.error("send sms failure, the remainpointcount is zero");
			}
		} catch (Exception e) {
			LOG.error("process sms xml error:", e);
		}
		return rs;
	}
	
	public static void main(String[] args) throws IOException
	{
		MyConfiguration conf = MyConfiguration.getInstance();
		RWSmsServiceImpl service = new RWSmsServiceImpl(conf, DEFAULT_NODE);
		service.send("18020735014", "【MBA智库】252545(动态验证码)，请在10分钟内填写。请不要把验证码泄漏给其他人", new Callback<Boolean>() {
			
			@Override
			public void execute(Boolean o) {
				System.out.println("result = " + o);
			}
		});
		System.in.read();
	}
	

}
