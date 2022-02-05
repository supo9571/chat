package com.pangugle.passport.service;

import org.springframework.stereotype.Service;

import com.pangugle.passport.cache.UserCache;
import com.pangugle.passport.cache.WXCacheKeyHelper;

@Service
public class ThirdLoginServiceImpl implements ThirdLoginService {
	
	
	public String findUsernameByWXOpenid(String openid)
	{
		String cachekey = WXCacheKeyHelper.findUsernameByWXOpenidKey(openid);
		String value = UserCache.getIntance().getString(cachekey);
		if(value == null)
		{
			
		}
		return null;
	}

}
