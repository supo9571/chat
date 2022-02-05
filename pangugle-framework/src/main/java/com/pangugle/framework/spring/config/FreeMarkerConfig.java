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
package com.pangugle.framework.spring.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.context.MyEnvironment;
import com.pangugle.framework.spring.exception.FreemarkerException;
import com.pangugle.framework.utils.NetUtils;
import com.pangugle.framework.utils.StringUtils;

@Configuration
public class FreeMarkerConfig {

    @Autowired
    private freemarker.template.Configuration configuration;
    
    
	@PostConstruct
    public void setConfigure() throws Exception {
		configuration.setAPIBuiltinEnabled(false);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(new FreemarkerException());

		MyConfiguration conf = MyConfiguration.getInstance();
		String port = System.getProperty("server.port");
		
		String localHost = NetUtils.getLocalHost();
		
		String staticServer = conf.getString("static.server");
		if(StringUtils.isEmpty(staticServer))
		{
			// => //127.0.0.1:port/static
			staticServer = "http://" + localHost + ":" + port + "/static";
		} 
		configuration.setSharedVariable("static_server", staticServer);
		
		String uploadServer = conf.getString("upload.server");
		if(StringUtils.isEmpty(staticServer))
		{
			// => //127.0.0.1:port/static
			uploadServer = "http://" + localHost + ":" + port + "/uploads";
		} 
		configuration.setSharedVariable("upload_server", uploadServer);
		
		// official server
		String officialServer = conf.getString("official.server");
		if(StringUtils.isEmpty(officialServer))
		{
			// => //127.0.0.1:port/static
			officialServer = "http://" + localHost + ":" + port;
		} 
		configuration.setSharedVariable("official_server", officialServer);
		// env variable
		configuration.setSharedVariable("env", MyEnvironment.getEnv());
		if(MyEnvironment.isDev())
		{
			configuration.setTemplateUpdateDelayMilliseconds(1);
		}
		
    }
	
}
