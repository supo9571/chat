package com.pangugle.im.utils;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.utils.StringUtils;

/**
 * 用户状态管理器
 * @author Administrator
 *
 */
public class UserStatusUtils {
	
	private static CacheManager mCache = CacheManager.getInstance();

	public static void online(String userid)
	{
		String cachekey = cachekey(userid);
		// 不下线就永久缓存
		mCache.setString(cachekey, 1 + StringUtils.getEmpty(), -1);
	}
	
	public static boolean checkOnline(String userid)
	{
		String cachekey = cachekey(userid);
		return mCache.exists(cachekey);
	}
	
	public static void offline(String userid)
	{
		String cachekey = cachekey(userid);
		mCache.delete(cachekey);
	}
	
	private static String cachekey(String userid)
	{
		return "im_" + UserStatusUtils.class.getName() + "_user_status_" + userid;
	}
	
}

