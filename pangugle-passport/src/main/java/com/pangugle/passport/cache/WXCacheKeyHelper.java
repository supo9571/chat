package com.pangugle.passport.cache;

/**
 * 微信相关的缓存
 * @author Administrator
 *
 */
public class WXCacheKeyHelper {
	
	
	public static String createAccessTokenKey()
	{
		return "passport_wxmanager_refresh_accesstoken";
	}
	
	public static String findUsernameByWXOpenidKey(String openid)
	{
		return "passport_third_login_service_find_username_by_wx_openid_"+ openid;
	}

}
