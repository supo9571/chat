package com.pangugle.passport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.passport.MyConstants;
import com.pangugle.passport.limit.MyLoginLimit;
import com.pangugle.passport.logical.PCTokenManager;
import com.pangugle.passport.service.AuthService;

@RequestMapping(MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "/tokenApi")
@RestController
public class TokenApi {
	
	@Autowired
	private AuthService mOauth2Service;
	
	@MyLoginLimit
	@RequestMapping("/createPCAccessToken")
	public String createPCAccessToken() {
		String reqcode = WebRequest.getString("reqcode");
		ApiJsonTemplate api = new ApiJsonTemplate();
		if(StringUtils.isEmpty(reqcode) || reqcode.length() != 32)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		String accessToken = WebRequest.getAccessToken();
		String username = mOauth2Service.getAccountByAccessToken(accessToken);
		PCTokenManager.getInstance().createToken(reqcode, username);
		return api.toJSONString();
	}
	
	@MyLoginLimit
	@RequestMapping("/kickPCClient")
	public String kickPCClient() {
		String accessToken = WebRequest.getAccessToken();
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		PCTokenManager mgr = PCTokenManager.getInstance();
		if(mgr.isPCToken(accessToken))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		String username = mOauth2Service.getAccountByAccessToken(accessToken);
		String pcToken = mgr.getTokenByUserid(username);
		
		if(StringUtils.isEmpty(pcToken))
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		else
		{
			mgr.deleteToken(username);
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
		String reqcode = PCTokenManager.getInstance().createRequestCode(ip);
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
