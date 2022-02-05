package com.pangugle.modules.im.aec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pangugle.im.logical.aec.AuthAecSupport;
import com.pangugle.passport.service.AuthService;

@Service
public class AuthAecSupportImpl implements AuthAecSupport{
	
	@Autowired
	private AuthService mAuthService;
	
	@Override
	public boolean verifyAccessToken(String accessToken) {
		// TODO Auto-generated method stub
		return mAuthService.verifyAccessToken(accessToken);
	}

	@Override
	public String getAccountByAccessToken(String accessToken) {
		// TODO Auto-generated method stub
		return mAuthService.getAccountByAccessToken(accessToken);
	}

}
