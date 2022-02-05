package com.pangugle.passport.cache;

import com.pangugle.passport.MyConstants;

public class PCTokenCacheKeyUtils {
	
	public static String createReq2Token(String requestcode)
	{
		// 1分钟内有效
		String reqcode2TokenCacheKey = MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_PCAccessTokenManager_reqcode2Token_" + requestcode;
		return reqcode2TokenCacheKey;
	}
	
	public static String createToken2User(String pcToken)
	{
		String cachekey = MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_PCAccessTokenManager_token_2_user_" + pcToken;
		return cachekey;
	}
	
	public static String createUser2Token(String user)
	{
		// 1分钟内有效
		String cachekey = MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_PCAccessTokenManager_user_2_token_" + user;
		return cachekey;
	}

}
