package com.pangugle.modules.passport.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.utils.ValidatorUtils;
import com.pangugle.im.MyConstants;
import com.pangugle.im.TokenErrorResult;
import com.pangugle.im.limit.MyTokenLimit;
import com.pangugle.passport.model.UserInfo;
import com.pangugle.passport.model.UserSecret;
import com.pangugle.passport.service.AuthService;
import com.pangugle.passport.service.UserService;

@RequestMapping(MyConstants.DEFAULT_IM_MODULE_NAME + "/userApi")
@RestController
public class UserApi {
	
	@Autowired
	private AuthService mAuthService;
	
	@Autowired
	private UserService mUserService;
	
	@RequestMapping("/login")
	public String login() {
		String loginname = WebRequest.getString("loginname");
		String password = WebRequest.getString("password");
		String version = WebRequest.getString("version");
		
		ApiJsonTemplate api = new ApiJsonTemplate();

		if(StringUtils.isEmpty(version))
		{
			api.setError(SystemErrorResult.ERR_CUSTOM.getCode(), "请升级最新版本!");
			return api.toJSONString();
		}
		
		// 这里暂时先放在这里 version, 应该放在其它位置
		if (StringUtils.isEmpty(loginname) || !ValidatorUtils.checkPassword(password)) {
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		UserSecret secret = mUserService.findSecret(loginname);
		if (secret == null || !secret.checkPassword(password)) {
			api.setError(SystemErrorResult.ERR_EXIST_NOT.getCode(), "用户不存在或密码错误!");
			return api.toJSONString();
		}
		String loginToken = mAuthService.createLoginTokenByAccount(loginname, secret.getPassword());
		String accessToken = mAuthService.refreshAccessToken(loginToken);
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("loginToken", loginToken);
		data.put("accessToken", accessToken);
		data.put("expires_time", mAuthService.getAccessTokenExpires());
		
		api.setData(data);
		return api.toJSONString();
	}
	
	@RequestMapping("/registerByUsername")
	public String registerByUsername()
	{
		String nickname = WebRequest.getString("nickname");
		String username = WebRequest.getString("username");
		String password = WebRequest.getString("password");
		String password2 = WebRequest.getString("password2");
		//String imgcode = WebRequest.getString("imgcode");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		if (!ValidatorUtils.checkUsername(username)) {
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "用户名为字母和数字的组合且>=6个字符!");
			return api.toJSONString();
		}
		
		if (!ValidatorUtils.checkPassword(password)) {
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "密码输入有误!");
			return api.toJSONString();
		}
		if (!ValidatorUtils.checkNickname(nickname))
		{
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "昵称不能为空!");
			return api.toJSONString();
		}
		
		if(!ValidatorUtils.checkPassword(password, password2))
		{
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "两次密码不一致!");
			return api.toJSONString();
		}
		
		
		String ipPrefixCachekey = MyConstants.DEFAULT_IM_MODULE_NAME + "_userapi_registerByUsername_ip_";
		// 60s内最多注册10个
		if(!ValidatorUtils.checkIP(ipPrefixCachekey + "second", 10, 60))
		{
			api.setJsonResult(SystemErrorResult.ERR_REQUESTS);
			return api.toJSONString();
		}
		// 10分钟内最多注册20个
		if(!ValidatorUtils.checkIP(ipPrefixCachekey + "minute", 20, 600))
		{
			api.setJsonResult(SystemErrorResult.ERR_REQUESTS);
			return api.toJSONString();
		}
		// 1小时内最多注册30个
		if(!ValidatorUtils.checkIP(ipPrefixCachekey + "hour", 30, 3600))
		{
			api.setJsonResult(SystemErrorResult.ERR_REQUESTS);
			return api.toJSONString();
		}
		
		UserInfo userInfo = mUserService.findByUsername(username);
		if(userInfo != null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST);
			return api.toJSONString();
		}
		
		mUserService.addUserByName(username, password, nickname, WebRequest.getRemoteIP());
		
		return api.toJSONString();
	}

	@MyTokenLimit
	@RequestMapping("/getUserInfo")
	public String getUserInfo() {
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();

		String username = mAuthService.getAccountByAccessToken(accessToken);
		UserInfo userInfo = mUserService.findByUsername(username);

		Map<String, Object> data = Maps.newHashMap();
		data.put("username", username);
		data.put("nickname", userInfo.getNickname());
		data.put("avatar", userInfo.getShowAvatar());

		api.setData(data);

		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("/searchUserInfo")
	public String searchUserInfo() {
		ApiJsonTemplate api = new ApiJsonTemplate();

		String username = WebRequest.getString("username");
		
		if (!ValidatorUtils.checkUsername(username)) {
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		UserInfo friendInfo = mUserService.findByUsername(username);
		
		if(friendInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("username", username);
		data.put("nickname", friendInfo.getNickname());
		data.put("avatar", friendInfo.getShowAvatar());

		api.setData(data);

		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("/updateNickname")
	public String updateNickname() {
		String nickname = WebRequest.getString("nickname");
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();

		String username = mAuthService.getAccountByAccessToken(accessToken);

		if(StringUtils.isEmpty(nickname) || nickname.length() > 20)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		mUserService.updateNickname(username, nickname);
		return api.toJSONString();
	}
	
	@RequestMapping("/refreshAccessToken")
	public String refreshAccessToken() {
		String loginToken = WebRequest.getLoginToken();

		ApiJsonTemplate api = new ApiJsonTemplate();

		if (StringUtils.isEmpty(loginToken) || !mAuthService.verifyLoginToken(loginToken)) {
			api.setJsonResult(TokenErrorResult.ERR_LOGINTOKEN_INVALID);
			return api.toJSONString();
		}

		String accessToken = mAuthService.refreshAccessToken(loginToken);

		Map<String, Object> data = Maps.newHashMap();
		data.put("accessToken", accessToken);
		data.put("expires_time", mAuthService.getAccessTokenExpires());

		api.setData(data);

		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updatePassword")
	@ResponseBody
	public String updatePassword()
	{
		String accessToken = WebRequest.getAccessToken();
		
		String username = mAuthService.getAccountByAccessToken(accessToken);
		String oldpwd = WebRequest.getString("oldpwd");
		String newpwd = WebRequest.getString("newpwd");
		String newpwd2 = WebRequest.getString("newpwd2");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(!ValidatorUtils.checkPassword(oldpwd))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if(!ValidatorUtils.checkPassword(newpwd, newpwd2))
		{
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "密码输入有误!");
			return api.toJSONString();
		}
		
		if(oldpwd.equalsIgnoreCase(newpwd))
		{
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "新密码与旧密码不能一样!");
			return api.toJSONString();
		}
		
		UserSecret secret = mUserService.findSecret(username);
		if (secret == null || !secret.checkPassword(oldpwd)) {
			api.setError(SystemErrorResult.ERR_PARAMS.getCode(), "密码不正确!");
			return api.toJSONString();
		}
		
		mUserService.updatePassword(username, newpwd);
		return api.toJSONString();
	}
	
}
