package com.pangugle.passport.cache;

import com.pangugle.framework.cache.AbstractCache;
import com.pangugle.framework.cache.CacheManager;

public class UserCache extends AbstractCache{
	
	private static int DEFAULT_EXPIRES = CacheManager.EXPIRES_HOUR * 2;
	
	
	private interface UserCacheInternal {
		public UserCache mgr = new UserCache();
	}
	
	public static UserCache getIntance()
	{
		return UserCacheInternal.mgr;
	}
	
	private UserCache() {}

	@Override
	public int getDefaultExpires() {
		return DEFAULT_EXPIRES;
	}

	
	
}
