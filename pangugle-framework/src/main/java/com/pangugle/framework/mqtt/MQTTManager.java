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
package com.pangugle.framework.mqtt;

import java.io.IOException;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.ThreadUtils;

public class MQTTManager {
	
//	private MyMqttClient mAdminClient;
//	private MyMqttClient mWriteClient;
//	private MyMqttClient mReadClient;
	
	private MyConfiguration conf = MyConfiguration.getInstance();
	
	private interface MQTTManagerInternal {
		public MQTTManager mgr = new MQTTManager();
	}
	
	public static MQTTManager getInstanced()
	{
		return MQTTManagerInternal.mgr;
	}
	
	private MQTTManager() {
		String server = conf.getString("activemq.server");
		server = "tcp://192.168.1.171:1883";
//		this.mAdminClient = openClient(server, "admin");
//		this.mWriteClient = openClient(server, "writeuser");
//		this.mReadClient = openClient(server, "readuser");
	}
	
	private MyMqttClient openClient(String server, String username)
	{
		String password = conf.getString("activemq." + username + ".password");
		return new MyMqttClient(server, "readuser", "readuser");
	}
	
	public void send(String tp, String message)
	{
//		mAdminClient.send(tp, message);
	}
	
	public void subscribe(String tp, Callback<String> callback)
	{
//		mAdminClient.subscribe(tp, null);
//		mAdminClient.unsubscribe(tp);
//		mAdminClient.subscribe(tp, callback);
	}
	
	public static void main(String[] args) throws IOException
	{
		String queue = "mytp_queue_test";
		
		MQTTManager mqtt = new MQTTManager();
		
		mqtt.subscribe(queue, new Callback<String>() {
			public void execute(String o) {
				System.out.println("consume11111 = " + o);
			}
		});
		
		for(int i = 0; i <= 1000; i ++)
		{
			try {
				mqtt.send(queue, "i = " + i);
				ThreadUtils.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
