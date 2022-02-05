/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.cache;

import java.util.List;
import java.util.Map;

import com.beust.jcommander.internal.Maps;
import com.pangugle.framework.cache.impl.PikaCacheImpl;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;

public class CacheManager {

	public static final int EXPIRES_FOREVER = -1;
    public static final int EXPIRES_HOUR = 3600;
    public static final int EXPIRES_DAY = 86400;
    public static final int EXPIRES_WEEK = 86400 * 7;
    public static final int EXPIRES_MONTH = 86400 * 30;
    public static final int EXPIRES_FIVE_MINUTES = 300;
    //	private static final CacheSupport support = MyBeanFactory.getInstance(LocalMemoryImpl.class);
    private CacheService support;

    private interface CacheManagerInternal {
        public CacheManager mgr = new CacheManager();
    }

    public static CacheManager getInstance() {
        return CacheManagerInternal.mgr;
    }

    private CacheManager() {
    	support = new PikaCacheImpl();
    }

    public void setString(String key, String value, int seconds) {
        support.setString(key, value, seconds);
    }
    
    public void setString(String key, String value) {
        support.setString(key, value, EXPIRES_HOUR);
    }

    public String getString(String key) {
        return support.getString(key);
    }
    
    public boolean exists(String key)
    {
    	return support.exists(key);
    }
    
    public <T> T getObject(String key, Class<T> clazz)
    {
    	String value = getString(key);
    	//if(StringUtils.isEmpty(value)) return null;
    	return FastJsonHelper.jsonDecode(value, clazz);
    }
    
    public long getLong(String key)
    {
    	String value = getString(key);
    	return StringUtils.asLong(value);
    }
    
    public <T> List<T> getList(String key, Class<T> clazz)
    {
    	List<T> list = null;
    	String value = getString(key);
    	if(!StringUtils.isEmpty(value)) {
    		list = FastJsonHelper.parseArray(value, clazz);
    	}
    	return list;
    }

    public void delete(String key) {
        support.delete(key);
    }
    
	public <T> List<T> getMultiString(Class<T> clazz, String... keys)
	{
		return support.getMultiString(clazz, keys);
	}
	public void setMultiKeys(Map<String, Object> keyValue)
	{
		support.setMultiKeys(keyValue);
	}

    public static void main(String[] args) {
        CacheManager manager = CacheManager.getInstance();
        
        Map<String, Object> keyvalue = Maps.newHashMap();
        
        
        for(int i = 0; i < 3; i ++)
        {
        	
        	keyvalue.put("test" + i, "value = " + i);
        }
        manager.setMultiKeys(keyvalue);
        List<String> list = manager.getMultiString(String.class, "a0", "test1", "test2");
        System.out.println(FastJsonHelper.jsonEncode(list));
        
        
        manager.setString("test1111", "fdsfasdf");
        
        String rs = manager.getString("test1111");
        System.out.println(rs);
    }

}
