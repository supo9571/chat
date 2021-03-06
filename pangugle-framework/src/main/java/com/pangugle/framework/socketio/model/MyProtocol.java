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
package com.pangugle.framework.socketio.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.pangugle.framework.utils.DateUtils;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.utils.UUIDUtils;

/**
 * 消息协议体, 各个端约定
 * 1~100 为系统相关消息 - 保留消息段
 * （此类消息通常由服务器即fromUserid="sys"的用户发出）,
 * @author Administrator
 *
 */
public class MyProtocol {
	
	public static final String DEFAULT_SYSTEM_USERID = "";
	
	/*** 消息唯一id ***/
	private String id;
	/*** 为空表示系统发送或发送给系统 ***/
	private String fromUserid = DEFAULT_SYSTEM_USERID;
	/***  为空表示发送给所有 ***/
	private String targetid;
	//发送内容
	private Object data;
	/*** 事件类型 single|group|friend ***/
	private String event;
	//消息类型
	private String msgType;
	/*** 创建时间-时间戳 ***/
	private long time;
	/*** 是否是在线消息 ***/
	private boolean online = false;
	/*** @人 ***/
	private String at;
	
	
	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUserid() {
		return fromUserid;
	}

	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}
	
	public String getTargetid() {
		return targetid;
	}

	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@JSONField(serialize = false)
	public String getDataContent()
	{
		if(data != null )
		{
			if(data instanceof String)
			{
				return (String) data;
			}else
			{
				return FastJsonHelper.jsonEncode(data);
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@JSONField(serialize = false)
	public <T> T getDataModel()
	{
		try {
			if(data != null )
			{
				return (T) getData();
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	@JSONField(serialize = false)
	public boolean verify()
	{
		if(StringUtils.isEmpty(id))
		{
			return false;
		}
		if(StringUtils.isEmpty(fromUserid))
		{
			return false;
		}
		if(StringUtils.isEmpty(targetid))
		{
			return false;
		}
		if(StringUtils.isEmpty(event))
		{
			return false;
		}
		if(StringUtils.isEmpty(msgType))
		{
			return false;
		}
		return true;
	}
	
	public String toString()
	{
		return FastJsonHelper.jsonEncode(this);
	}
	
	public static String getNowTimeString()
	{
		return DateUtils.convertString(new Date());
	}
	
	public static MyProtocol build(String targetId, String event, String msgType)
	{
		return build(DEFAULT_SYSTEM_USERID, targetId, event, msgType);
	}
	public static MyProtocol build(String fromUserid, String targetId, String event, String msgType)
	{
		MyProtocol protocol = new MyProtocol();
		protocol.setId(UUIDUtils.getUUID());
		protocol.setFromUserid(fromUserid);
		protocol.setTargetid(targetId);
		protocol.setEvent(event);
		protocol.setMsgType(msgType);
		protocol.setTime(System.currentTimeMillis());
		return protocol;
	}
	
	public MyProtocol copy()
	{
		String jsonString = FastJsonHelper.jsonEncode(this);
		MyProtocol body = FastJsonHelper.jsonDecode(jsonString, MyProtocol.class);
		return body;
	}
	
	public static MyProtocol build(String jsonString)
	{
		MyProtocol body = FastJsonHelper.jsonDecode(jsonString, MyProtocol.class);
		body.setTime(System.currentTimeMillis()); // 以服务器时间为主
		return body;
	}
	
}
