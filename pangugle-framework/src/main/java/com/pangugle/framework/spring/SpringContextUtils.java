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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext mSpringContext;
	
	private static boolean isSpringEnv = false;

	public static ApplicationContext getContext() {
		return mSpringContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<?> clazz) {
		return (T) mSpringContext.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name)
	{
		return (T) mSpringContext.getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		mSpringContext = applicationContext;
		isSpringEnv = true;
	}
	
	public static boolean isSpringEnv()
	{
		return isSpringEnv;
	}

	public static void printAllBeans() {
		String[] beans = mSpringContext.getBeanDefinitionNames();
		for (String beanName : beans) {
			if(!beanName.contains("com.pangugle")) continue;
			System.out.println("===============================");
			System.out.println("BeanName:" + beanName);
			System.out.println("Bean：" + mSpringContext.getBean(beanName));
		}
	}

}
