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
package com.pangugle.framework.mq;

import com.pangugle.framework.service.Callback;

public  interface MQSupport{
	
	/**
	 * 对于rocketmq 没有用
	 * @param topic
	 */
	public  void declareTopic(String topic);
	public  void deleteTopic(String topic);
	
	/**
	 * 消息消息
	 * @param topic
	 * @param body
	 * @return
	 */
	public  boolean sendMessage(String topic, String body);
	public  boolean sendMessage(String topic, String body, String tags);
	
	/**
	 * 消费消息, 消息不重复消息
	 * @param tags
	 * @param callback
	 */
	public void consume(String topic, String tags, Callback<String> callback);
	
	/**
	 * 订阅消息，消息重复消费
	 * @param tags
	 * @param callback
	 */
	public void subscribe(String topic, String tags, Callback<String> callback);
}
