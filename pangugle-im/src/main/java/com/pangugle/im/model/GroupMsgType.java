package com.pangugle.im.model;

import com.pangugle.framework.utils.StringUtils;

public enum GroupMsgType {
	
	INVITE_MEMBER("invite_member", "邀请成员"),
	REMOVE_MEMBER("remove_member", "移除成员"),
	UPDATE_GROUP_INFO("update_group_info", "更新群信息");
	
	private String name;
	private String remark;
	
	private GroupMsgType(String name, String remark)
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
		GroupMsgType[] values = GroupMsgType.values();
		for(GroupMsgType value : values)
		{
			if(value.getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public static GroupMsgType getType(String type)
	{
		GroupMsgType[] values = GroupMsgType.values();
		for(GroupMsgType value : values)
		{
			if(value.getName().equalsIgnoreCase(type))
			{
				return value;
			}
		}
		return null;
	}

}
