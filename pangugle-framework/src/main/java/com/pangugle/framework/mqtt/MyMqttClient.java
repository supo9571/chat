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

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.UUIDUtils;

/**
 * mqtt 发送器管理, 
 * @author Administrator
 *
 */
public class MyMqttClient {
	
	private static Log LOG = LogFactory.getLog(MyMqttClient.class);
	
	private MqttClient mClient;
	private MqttConnectOptions mConnOpts;
	
	// 设置消息的服务质量 qos至多一次（0）|| 至少一次（1）|| 只有一次（2）
	private static final int DEFAULT_QOS = 0; 
	
	public MyMqttClient(String server, String username, String password)
	{
		try {
//			MyConfiguration conf = MyConfiguration.getInstance();
//			String server = conf.getString("mqtt.activemq.server", "tcp://192.168.1.171:1883");
//			String username = conf.getString("mqtt.activemq.username", "amdin");
//			String password = conf.getString("mqtt.activemq.password", "admin");
			String clientid = "server_" + UUIDUtils.getUUID();
			
			MemoryPersistence persistence = new MemoryPersistence();
			MqttClient client = new MqttClient(server, clientid, persistence);
			mClient = client;
			
			// 创建链接参数
	        MqttConnectOptions connOpts = new MqttConnectOptions();
	        // 在重新启动和重新连接时记住状态
	        connOpts.setCleanSession(false);
	        // 设置连接的用户名
	        connOpts.setUserName(username);
	        connOpts.setPassword(password.toCharArray());
	        // 设置超时时间 单位为秒
	        connOpts.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
	        connOpts.setKeepAliveInterval(2);
	        connOpts.setAutomaticReconnect(true);
	        mConnOpts = connOpts;
	        
	        openconnect();
		} catch (MqttException e) {
			
		}
		
	}
	
	private boolean openconnect()
	{
		try {
			// 建立连接
			if(!mClient.isConnected()){
				mClient.connect(mConnOpts);
			}
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	public void send(String tp, String message)
	{
		// 创建消息
		MqttMessage body = new MqttMessage(message.getBytes());
		// 设置消息的服务质量 qos至多一次（0）|| 至少一次（1）|| 只有一次（2）
		body.setQos(DEFAULT_QOS);
		body.setRetained(false);
		if(send(tp, body)) return;
		if(send(tp, body)) return;
		if(send(tp, body)) return;
	}
	
	private boolean send(String tp, MqttMessage message)
	{
		 try {
			// 创建消息
			mClient.publish(tp, message);
			return true;
		} catch (Exception e) {
			openconnect();
		} 
		 return false;
	}
	
	
	public void subscribe(String tp, Callback<String> callback)
	{
		try {
			//订阅消息
			mClient.subscribe(tp, DEFAULT_QOS, new IMqttMessageListener() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					if(callback != null) callback.execute(new String(message.getPayload()));
				}
			});
		} catch (MqttException e) {
			LOG.error("subscribe error for " + tp, e);
		}
	}
	
	public void unsubscribe(String tp)
	{
		try {
			mClient.unsubscribe(tp);
		} catch (MqttException e) {
			LOG.error("unsubscribe error for " + tp, e);
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		String queue = "mytp.queue.test";
		
//		MyMqttClient mqttread = new MyMqttClient("readuser", "jsadlkfasdfas");
//		mqttread.subscribe(queue, new Callback<String>() {
//			public void execute(String o) {
//				System.out.println("consume11111 = " + o);
//			}
//		});
//		
//		MyMqttClient mqttwriter = new MyMqttClient("writeuser", "fdkjdfsakjdfsalkj");
//		for(int i = 0; i <= 1000; i ++)
//		{
//			mqttwriter.send(queue, "i = " + i);
//			ThreadUtils.sleep(1000);
//		}
		
		System.in.read();
	}

}
