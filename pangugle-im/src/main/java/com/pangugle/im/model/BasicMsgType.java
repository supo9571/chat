package com.pangugle.im.model;

import com.pangugle.framework.socketio.model.IMsgType;
import com.pangugle.framework.utils.StringUtils;

/**
 * 消息类型
 * @author Administrator
 *
 */
public enum BasicMsgType implements IMsgType {
	REVOKE("revoke", "撤回消息"),
	
	TEXT("text", "文本消息"),
	IMAGE("image", "图片消息"),
	VOICE("voice", "语音消息"),
	FILE("file", "文件消息"),
	SHOURT_VIDEO("short_video", "短视频"),
	POSITION("position", "位置"),
	SHARE_POSITION("share_position", "实时共享位置"),
	SHARE_POSITION_UPDATE("share_position_update", "实时共享位置更新, 位置信息不记录"),
	CARD("card", "名片消息"),
	RED_PACKET("red_packet", "红包消息"),
	LINK("link", "链接消息");
	
	private String name;
	private String remark;
	
	private BasicMsgType(String name, String remark)
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
		BasicMsgType[] values = BasicMsgType.values();
		for(BasicMsgType value :values)
		{
			if(value.getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static BasicMsgType getType(String type)
	{
		BasicMsgType[] values = BasicMsgType.values();
		for(BasicMsgType value :values)
		{
			if(value.getName().equalsIgnoreCase(type))
			{
				return value;
			}
		}
		return null;
	}

}
