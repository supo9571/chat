package com.pangugle.modules.passport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.MessageApi;
import com.pangugle.passport.limit.MyLoginLimit;
import com.pangugle.passport.logical.PCTokenManager;
import com.pangugle.passport.service.AuthService;

@RequestMapping("/passport/pcTokenApi")
@RestController
public class PCTokenApi {
	
	private PCTokenManager mPCTokenManager = PCTokenManager.getInstance();
	
	@Autowired
	private AuthService mAuthService;
	
	@MyLoginLimit
	@RequestMapping("/createPCAccessToken")
	public String createPCAccessToken() {
		String reqcode = WebRequest.getString("reqcode");
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();
		if(StringUtils.isEmpty(reqcode) || mPCTokenManager.isPCToken(accessToken))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		String username = mAuthService.getAccountByAccessToken(accessToken);
		mPCTokenManager.createToken(reqcode, username);
		return api.toJSONString();
	}
	
	@MyLoginLimit
	@RequestMapping("/kickPCClient")
	public String kickPCClient() {
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(mPCTokenManager.isPCToken(accessToken))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		String username =mAuthService.getAccountByAccessToken(accessToken);
		String pcToken = mPCTokenManager.getTokenByUserid(username);
		
		if(StringUtils.isEmpty(pcToken))
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		else
		{
			mPCTokenManager.deleteToken(username);
			MessageApi.getIntance().kick(username, pcToken);
		}
		return api.toJSONString();
	}
	
	/**
	 * pc 端登陆请求码
	 * @return
	 */
	@RequestMapping("/getReqcode")
	public String getReqcode() {
		String ip = WebRequest.getRemoteIP();
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		String reqcode = mPCTokenManager.createRequestCode(ip);
		if(!StringUtils.isEmpty(reqcode))
		{
			api.setData(reqcode);
		}
		else
		{
			api.setJsonResult(SystemErrorResult.ERR_NODATA);
		}
		return api.toJSONString();
	}

}
