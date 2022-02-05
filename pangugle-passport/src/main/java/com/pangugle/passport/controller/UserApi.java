package com.pangugle.passport.controller;

import java.io.File;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.fileupload.UploadFileUtils;
import com.pangugle.framework.image.ImageCompress;
import com.pangugle.framework.spring.UploadManager;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.NetUtils;
import com.pangugle.framework.utils.RegexUtils;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.utils.ValidatorUtils;
import com.pangugle.passport.MyConstants;
import com.pangugle.passport.UserErrorResult;
import com.pangugle.passport.limit.MyLoginLimit;
import com.pangugle.passport.model.UserInfo;
import com.pangugle.passport.model.UserSecret;
import com.pangugle.passport.service.AuthService;
import com.pangugle.passport.service.UserService;

@RequestMapping(MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "/userApi")
@RestController
public class UserApi {

	@Autowired
	private UserService mUserService;

	@Autowired
	private AuthService mOauth2Service;

	@RequestMapping("/login")
	public String login() {
		String loginname = WebRequest.getString("loginname");
		String password = WebRequest.getString("password");

		ApiJsonTemplate api = new ApiJsonTemplate();

		if (StringUtils.isEmpty(loginname) || !ValidatorUtils.checkPassword(password)) {
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}

		String username = null;
		if (RegexUtils.isMobile(loginname)) {
			username = mUserService.findNameByPhone(loginname);
		} else if (RegexUtils.isEmail(loginname)) {
			username = mUserService.findNameByEmail(loginname);
		} else {
			username = loginname;
		}

		UserSecret secret = mUserService.findSecret(username);
		if (secret == null) {
			api.setJsonResult(UserErrorResult.ERR_ACCOUNT_NOT_EXIST);
			return api.toJSONString();
		}
		if (!secret.checkPassword(password)) {
			api.setJsonResult(UserErrorResult.ERR_PWD);
			return api.toJSONString();
		}

		//
		String loginToken = mOauth2Service.createLoginTokenByAccount(username, secret.getPassword());
		String accessToken = mOauth2Service.refreshAccessToken(loginToken);

		Map<String, Object> data = Maps.newHashMap();
		data.put("loginToken", loginToken);
		data.put("accessToken", accessToken);

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
		
		
		String ipPrefixCachekey = MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_userapi_registerByUsername_ip_";
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
	
	//@RequestMapping("/registerByPhone")
	public String registerByMobile()
	{
		String phone = WebRequest.getString("phone");
		String password = WebRequest.getString("password");
		String nickname = WebRequest.getString("nickname");
		String remoteip = WebRequest.getRemoteIP();
		//String imgcode = WebRequest.getString("imgcode");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if (!RegexUtils.isMobile(phone) || !ValidatorUtils.checkPassword(password)) {
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if (ValidatorUtils.checkNickname(nickname))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		String username = mUserService.findNameByPhone(phone);
		if(!StringUtils.isEmpty(username))
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST);
			return api.toJSONString();
		}
		
		mUserService.addUserByPhone(phone, password, nickname, remoteip);
		
		return api.toJSONString();
	}
	
	//@RequestMapping("/registerByEmail")
	public String registerByEmail()
	{
		String email = WebRequest.getString("email");
		String password = WebRequest.getString("password");
		String nickname = WebRequest.getString("nickname");
		String remoteip = WebRequest.getRemoteIP();
		//String imgcode = WebRequest.getString("imgcode");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if (!RegexUtils.isEmail(email) || !ValidatorUtils.checkPassword(password)) {
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if (ValidatorUtils.checkNickname(nickname))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		String username = mUserService.findNameByEmail(email);
		if(!StringUtils.isEmpty(username))
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST);
			return api.toJSONString();
		}
		
		mUserService.addUserByEmail(email, password, nickname, remoteip);
		
		return api.toJSONString();
	}

	@RequestMapping("/refreshAccessToken")
	public String refreshAccessToken() {
		String loginToken = WebRequest.getLoginToken();
		ApiJsonTemplate api = new ApiJsonTemplate();
		if (StringUtils.isEmpty(loginToken) || !mOauth2Service.verifyLoginToken(loginToken)) {
			api.setJsonResult(UserErrorResult.ERR_LOGINTOKEN_INVALID);
			return api.toJSONString();
		}
		String accessToken = mOauth2Service.refreshAccessToken(loginToken);
		Map<String, Object> data = Maps.newHashMap();
		data.put("accessToken", accessToken);
		api.setData(data);
		return api.toJSONString();
	}
	
	@MyLoginLimit
	@RequestMapping("/getUserInfo")
	public String getUserInfo() {
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();

		String username = mOauth2Service.getAccountByAccessToken(accessToken);
		UserInfo userInfo = mUserService.findByUsername(username);

		Map<String, Object> data = Maps.newHashMap();
		data.put("username", userInfo.getName());
		data.put("nickname", userInfo.getNickname());
		data.put("phone", userInfo.getPhone());
		data.put("email", userInfo.getEmail());
		data.put("avatar", userInfo.getShowAvatar());

		api.setData(data);

		return api.toJSONString();
	}
	
//	@MyLoginLimit
	//@RequestMapping("/updateNickname")
	public String updateNickname() {
		String nickname = WebRequest.getString("nickname");
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();

		String username = mOauth2Service.getAccountByAccessToken(accessToken);

		if(StringUtils.isEmpty(nickname) || nickname.length() > 20)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		mUserService.updateNickname(username, nickname);
		return api.toJSONString();
	}
	
	//@MyLoginLimit
	//@RequestMapping("/searchUserInfo")
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
		data.put("username", friendInfo.getName());
		data.put("nickname", friendInfo.getNickname());
		data.put("avatar", friendInfo.getShowAvatar());

		api.setData(data);

		return api.toJSONString();
	}
	
	@MyLoginLimit
	@RequestMapping("uploadAvatarByIM")
	@ResponseBody
	public String uploadAvatarByIM(@RequestParam("file") MultipartFile file)
	{
		String accessToken = WebRequest.getAccessToken();
		
		// 没有
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		if(StringUtils.isEmpty(ext))
		{
			ext = "png";
		}
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// verify ip
		String relatePath = UploadManager.getIntance().createFilePath(MyConstants.DEFAULT_PASSPORT_MODULE_NAME  + "/avatar", ext);
		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
		
		File targetFile = new File(filepath);
		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
		
		if(!rs)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		
//		String absolutePath = UploadManager.getIntance().createAccessPath(relatePath);
		String username = mOauth2Service.getAccountByAccessToken(accessToken);
		
		// 压缩图片
		String srcFilename = FilenameUtils.getName(filepath);
		String compressFilename = null;
		compressFilename = FilenameUtils.getBaseName(filepath) + "_25.jpg";
		
		String compressFilepath = filepath.replace(srcFilename, compressFilename);
		String compressRelatePath =  relatePath.replace(srcFilename, compressFilename);
		
		File compressFile =  new File(compressFilepath);
		ImageCompress.scale(targetFile, compressFile, 0.25f);
		// 删除旧的文件
		targetFile.delete();
		
		mUserService.updateAvatar(username, compressRelatePath);
		
		api.setData(UploadManager.getIntance().createAccessPath(compressRelatePath));
		return api.toJSONString();
	}
	
	@RequestMapping("updatePasswordByTest")
	@ResponseBody
	public String updatePasswordByTest()
	{
		String username = WebRequest.getString("username");
		String password = MD5.encode("123456");
		String ip = WebRequest.getRemoteIP();
		if(!NetUtils.isLocalHost(ip))
		{
			return "你想干啥!";
		}
		mUserService.updatePassword(username, password);
		return "ok";
	}
	
	//@MyLoginLimit
	//@RequestMapping("updatePassword")
	//@ResponseBody
	public String updatePassword()
	{
		String accessToken = WebRequest.getAccessToken();
		
		String username = mOauth2Service.getAccountByAccessToken(accessToken);
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
			api.setJsonResult(UserErrorResult.ERR_PWD);
			return api.toJSONString();
		}
		
		if(!oldpwd.equalsIgnoreCase(newpwd))
		{
			api.setJsonResult(UserErrorResult.ERR_PWD);
			return api.toJSONString();
		}
		
		UserSecret userSecret = mUserService.findSecret(username);
		if(!userSecret.checkPassword(oldpwd))
		{
			api.setJsonResult(UserErrorResult.ERR_PWD);
			return api.toJSONString();
		}
		
		mUserService.updatePassword(username, newpwd);
		return api.toJSONString();
	}
	
}
