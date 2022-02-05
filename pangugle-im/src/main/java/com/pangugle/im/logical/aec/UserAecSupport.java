package com.pangugle.im.logical.aec;

import com.pangugle.im.model.MyUserInfo;

public interface UserAecSupport {
	
	/**
	 * 用户头像信息
	 * @param username
	 * @return
	 */
	public MyUserInfo findUserInfo(String username);
	
	/**
	 * 用户头像地址
	 * @param avatar
	 * @return
	 */
	public String handleAvatar(String avatar);
}
