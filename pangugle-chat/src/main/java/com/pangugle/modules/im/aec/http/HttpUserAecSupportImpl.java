package com.pangugle.modules.im.aec.http;

import com.alibaba.fastjson.JSONObject;
import com.pangugle.im.logical.aec.UserAecSupport;
import com.pangugle.im.model.MyUserInfo;
import com.pangugle.modules.im.aec.http.helper.FetchDataHelper;
import com.pangugle.passport.model.UserInfo;

//@Service
public class HttpUserAecSupportImpl implements UserAecSupport{
	
	@Override
	public MyUserInfo findUserInfo(String username) {
		String url = "xxxxx/findUserInfo?username=" + username; // 地址自己定义
		// 示例 : {"code": 200, "msg": "success", data:{}}
		JSONObject obj = FetchDataHelper.loadData(url);
		if(obj == null || obj.isEmpty())
		{
			return null;
		}
		JSONObject data = obj.getJSONObject("data");
		String nickname = data.getString("nickname");
		String avatar = data.getString("avatar"); // 用户头像, 注意是绝对路径
		boolean enableStatus = data.getBooleanValue("enableStatus"); // 用户是否起用
		
		MyUserInfo myUserInfo = new MyUserInfo(username, nickname, avatar);
		myUserInfo.setEnableStatus(enableStatus);
		return myUserInfo;
	}
	
	

	/**
	 *  拼接头像地址, 一定要，因为传递进来的头像是相对路径
	 */
	@Override
	public String handleAvatar(String avatar) {
		// UserInfo getAbsoluteAvatar 是官方的实现，客户需要自己实现
		return UserInfo.getAbsoluteAvatar(avatar);
	}

}
