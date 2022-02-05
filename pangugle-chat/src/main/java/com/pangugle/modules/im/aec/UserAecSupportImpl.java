package com.pangugle.modules.im.aec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pangugle.im.logical.aec.UserAecSupport;
import com.pangugle.im.model.MyUserInfo;
import com.pangugle.passport.model.UserInfo;
import com.pangugle.passport.service.UserService;

@Service
public class UserAecSupportImpl implements UserAecSupport{
	
	@Autowired
	private UserService mUserService;
	
	@Override
	public MyUserInfo findUserInfo(String username) {
		UserInfo userInfo = mUserService.findByUsername(username);
		if(userInfo != null)
		{
			MyUserInfo myUserInfo = new MyUserInfo(userInfo.getName(), userInfo.getNickname(), userInfo.getAvatar());
			myUserInfo.setEnableStatus(userInfo.isEnableStatus());
			//
			myUserInfo.setShowAvatar(UserInfo.getAbsoluteAvatar(myUserInfo.getAvatar()));
			return myUserInfo;
			
		}
		return null;
	}

	@Override
	public String handleAvatar(String avatar) {
		return UserInfo.getAbsoluteAvatar(avatar);
	}

}
