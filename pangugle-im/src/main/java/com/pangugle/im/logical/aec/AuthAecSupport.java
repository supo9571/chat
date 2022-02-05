package com.pangugle.im.logical.aec;

public interface AuthAecSupport {
	public boolean verifyAccessToken(String accessToken); // 验证access token是否有效
	public String getAccountByAccessToken(String accessToken);
}
