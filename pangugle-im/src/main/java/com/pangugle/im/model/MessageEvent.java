package com.pangugle.im.model;

import com.pangugle.framework.socketio.model.IEventType;
import com.pangugle.framework.utils.StringUtils;

/**
 * 聊天类型定义
 * @author Administrator
 *
 */
public enum MessageEvent implements IEventType{
	LOGIN("login", "登陆操作相关"),
	FRIEND("friend", "好友操作相关"),
	SINGLE("single", "单聊"),
	GROUP("group", "群聊"),
	KEFU("kefu", "客服"),
	GROUP_OPT("group_opt", "群组操作"),
	SUBSCRIBE_ACCOUNT("subscribe_account", "订阅号"),
	OFFICAL_ACCOUNT("offical_account", "公众号");
	
	private String name;
	private String remark;
	
	private MessageEvent(String name, String remark)
	{
		this.name = name;
		this.remark = remark;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getRemark()
	{
		return this.remark;
	}
	
	public static boolean support(String name)
	{
		if(StringUtils.isEmpty(name)) return false;
		MessageEvent[] values = MessageEvent.values();
		for(MessageEvent value : values)
		{
			if(value.getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static MessageEvent getType(String type)
	{
		MessageEvent[] values = MessageEvent.values();
		for(MessageEvent value : values)
		{
			if(value.getName().equalsIgnoreCase(type))
			{
				return value;
			}
		}
		return null;
	}

}
