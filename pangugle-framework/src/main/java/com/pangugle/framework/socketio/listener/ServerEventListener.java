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
package com.pangugle.framework.socketio.listener;

import com.pangugle.framework.socketio.model.MessageBody;

/**
 * 事件回调
 * @author Administrator
 *
 */
public interface ServerEventListener {
	
	/**
	 * 登陆成功回调
	 * @param userid
	 */
	public String onVerifyUserLogin(String accessToken, String ip, String extra);
	
	public String getFromPlatform(String accessToken);
	
	/**
	 * 登陆成功回调
	 * @param userid
	 */
	public void onUserLoginCallback(String userid);
	
	/**
	 * 用户退出回调
	 * @param userid
	 */
	public void onUserLogoutCallback(String userid);
	
	/**
	 * 客户端向服务端发送消息成功回调
	 * @param body
	 */
	public void onTransBuffer_C2S_CallBack(MessageBody body);
	
	/**
	 * 客户端向服务端发送消息成功回调
	 * @param body
	 */
	public void onTransBuffer_S2C_Failure_CallBack(MessageBody body);
	
}
