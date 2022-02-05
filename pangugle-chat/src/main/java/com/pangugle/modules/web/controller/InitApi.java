package com.pangugle.modules.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.NetUtils;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.model.FriendRelation.MyStatus;
import com.pangugle.im.service.dao.FriendRelationDao;
import com.pangugle.modules.web.fix.InitDataService;
import com.pangugle.passport.service.UserService;

@RestController
@RequestMapping("/flushApi")
public class InitApi {
	
	@Autowired
	private InitDataService mInitDataService;
	
	@Autowired
	private UserService mUserService;
	
	@Autowired
	private FriendRelationDao mFriendRelationDao;
	

	@RequestMapping("/clearAll")
	public void clearAll()
	{
		if(!NetUtils.isLocalHost(WebRequest.getRemoteIP()))
		{
			return ;
		}
		mInitDataService.clearAll();
		
	}
	
	@RequestMapping("/init")
	public String init()
	{
		if(!NetUtils.isLocalHost(WebRequest.getRemoteIP()))
		{
			return "";
		}
		String password = WebRequest.getString("password");
		
		String defaultPwd = "123456";
		String encryPwd = MD5.encode(defaultPwd);
		
		if(StringUtils.isEmpty(password))
		{
			return "password empty ...";
		}
		
		// add user
		saveUser("kefu01", encryPwd);
		for(int i = 0; i < 10; i ++)
		{
			saveUser("test0" + i, encryPwd);
		}
		
		// add relation
		bindRelation();
		
		// update pwd
		String newPwd = MD5.encode(password);
		mUserService.updatePassword("kefu01", newPwd);
		mUserService.updatePassword("test01", newPwd);
		mUserService.updatePassword("test02", newPwd);
		mUserService.updatePassword("test03", newPwd);
		return "0k";
	}
	
	private void saveUser(String username, String password)
	{
		mUserService.addUserByName(username, password, username, WebRequest.getRemoteIP());
	}
	
	private void bindRelation()
	{
		for(int i = 1; i < 10; i ++)
		{
			bindRelation("test01", "test0" + i);
		}
		
		for(int i = 2; i < 10; i ++)
		{
			bindRelation("test02", "test0" + i);
		}
		
		for(int i = 3; i < 10; i ++)
		{
			bindRelation("test03", "test0" + i);
		}
	}
	
	private void bindRelation(String fromUserid, String targetUserid)
	{
		if(fromUserid.equalsIgnoreCase(targetUserid))
		{
			return;
		}
		mFriendRelationDao.bindRelation(fromUserid, targetUserid, fromUserid, MyStatus.enable, MyStatus.enable, "我是" + fromUserid);
	}
	
}
