package com.pangugle.im.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.pangugle.framework.utils.StringUtils;

public class MyUserInfo {
	
	private String username;
	private String nickname;
	private String avatar;
	private String showAvatar;
	/*** 是否被禁用 ***/
	private boolean enableStatus = true;
	
	public boolean isEnableStatus() {
		return enableStatus;
	}


	public void setEnableStatus(boolean enableStatus) {
		this.enableStatus = enableStatus;
	}


	public MyUserInfo(String username, String nickname, String avatar)
	{
		this.username = username;
		this.nickname = nickname;
		this.avatar = avatar;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getShowAvatar() {
		return showAvatar;
	}
	public void setShowAvatar(String showAvatar) {
		this.showAvatar = showAvatar;
	}
	
	@JSONField(serialize = false)
	public String getShowName()
	{
		if(!StringUtils.isEmpty(nickname))
		{
			return nickname;
		}
		return username;
	}

}
