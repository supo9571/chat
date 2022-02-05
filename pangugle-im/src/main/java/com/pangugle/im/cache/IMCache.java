package com.pangugle.im.cache;

import java.util.List;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;

public class IMCache {
	
	private static String CACHE_KEY_PREFIX = "im_";
	
	private CacheManager cache = CacheManager.getInstance();
	
	private interface MyInternal {
		public IMCache mgr = new IMCache();
	}
	
	public static IMCache getIntance()
	{
		return MyInternal.mgr;
	}
	
	private IMCache() {}

	
	public void setString(String key, String value, int seconds) {
		cache.setString(getCacheKey(key), value, seconds);
    }
    
    public void setString(String key, String value) {
    	cache.setString(getCacheKey(key), value);
    }

    public String getString(String key) {
        return cache.getString(getCacheKey(key));
    }
    
    public long getLong(String key)
    {
    	return cache.getLong(key);
    }
    
    public <T> T getObject(String key, Class<T> clazz)
    {
    	String value = getString(getCacheKey(key));
    	if(StringUtils.isEmpty(value)) return null;
    	return FastJsonHelper.jsonDecode(value, clazz);
    }
    
    public <T> List<T> getList(String key, Class<T> clazz)
    {
    	return cache.getList(getCacheKey(key), clazz);
    }

    public void delete(String key)
    {
    	cache.delete(getCacheKey(key));
    }
    
    private String getCacheKey(String key)
    {
    	return CACHE_KEY_PREFIX + key;
    }

}
