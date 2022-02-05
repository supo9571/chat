package com.pangugle.im.model;

import com.pangugle.framework.utils.StringUtils;

public enum FriendMsgType {
	
	UNCONFIRM("unconfirm", "添加朋友-待确认列表"),
	ENABLE("enable", "已确认好友"),
	BACK("black", "拉黑朋友"),
	DELETE("delete", "删除朋友"),
	;
	
	private String name;
	private String remark;
	
	private FriendMsgType(String name, String remark)
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
		FriendMsgType[] values = FriendMsgType.values();
		for(FriendMsgType value : values)
		{
			if(value.getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static FriendMsgType getType(String type)
	{
		FriendMsgType[] values = FriendMsgType.values();
		for(FriendMsgType value : values)
		{
			if(value.getName().equalsIgnoreCase(type))
			{
				return value;
			}
		}
		return null;
	}

}
