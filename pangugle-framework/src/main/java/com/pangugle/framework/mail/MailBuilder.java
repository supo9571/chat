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
package com.pangugle.framework.mail;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.pangugle.framework.conf.MyConfiguration;


public class MailBuilder {
	
	private MailConfig config;
	
	public MailBuilder(MailConfig config){
		this.config = config;
	}
	public static MailBuilder getInstance(MyConfiguration conf){
		return getInstance(conf,null);
	}
	public static MailBuilder getInstance(MyConfiguration conf , String name){
		if (name==null || "".equals(name)) name="mail";
		MailConfig config = new MailConfig();
		config.setHost(conf.getString(name+".smtp.host"));
		config.setPort(conf.getInt(name+".smtp.port",25));
		config.setAuth(conf.getBoolean(name+".smtp.auth", false));
		config.setUsername(conf.getString(name+".smtp.username"));
		config.setPassword(conf.getString(name+".smtp.password"));
		//from
		config.setFromAddress(conf.getString(name+".from.address"));
		config.setFromName(conf.getString(name+".from.name"));
		config.setFromAddress(conf.getString(name+".bounce.address",config.getFromAddress()));
		config.setDefaultCharset(conf.getString(name+".chareset","utf8"));
		//startTls and ssl
		config.setStartTlsEnabled(conf.getBoolean(name+".starttls.enabled", false));
		config.setStartTlsRequired(conf.getBoolean(name+".starttls.required", false));
		config.setSslOnConnect(conf.getBoolean(name+".ssl.enabled", false));
		config.setSslCheckServerIdentity(conf.getBoolean(name+".ssl.checkserveridentity", false));
		return new MailBuilder(config);
	}

	
	 public Email createEmail(Class<? extends Email> clazz) throws Exception {
	        Email email = clazz.newInstance();
	        email.setStartTLSEnabled(config.isStartTlsEnabled());
	        email.setStartTLSRequired(config.isStartTlsRequired());
	        email.setSSLOnConnect(config.isSslOnConnect());
	        email.setSSLCheckServerIdentity(config.isSslCheckServerIdentity());
	        email.setHostName(config.getHost());
	        email.setSmtpPort(config.getPort());
	        if (config.getBounceAddress()!=null)
	        	email.setBounceAddress(config.getBounceAddress());
	        email.setCharset(config.getDefaultCharset());        
	        email.setFrom(config.getFromAddress(),config.getFromName(),config.getDefaultCharset());
	        if(config.isAuth()) {
	            email.setAuthentication(config.getUsername(),config.getPassword());
	        }
	        return email;
	 }
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MyConfiguration conf = MyConfiguration.getInstance();
		MailBuilder builder = MailBuilder.getInstance(conf,"mail.passport");
		try {
			Email mail = builder.createEmail(SimpleEmail.class);
			mail.addCc("chinawab@gmail.com");
			mail.setSubject("this is a test mail");
			mail.setMsg("Simple mail text");
			mail.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 

	}
	

}
