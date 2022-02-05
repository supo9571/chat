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
package com.pangugle.framework.sms;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

public class SmsModel {
	public Log LOG = LogFactory.getLog(SmsModel.class);
	private static final String KEY_CACHE = "sms-bg-black-sendmobilecode2:";
	private static final int KEY_EXPIRE = 86400; //1 days
	
	public static final long DEFAULT_MOBILE_DELAY = 60000;
	public static final long DEFAULT_IP_DELAY = 10000;
	
    private SmsStatus ipStatus;
    private SmsStatus mobileStatus;
    	
    public SmsModel(String ip,String mobile)
    {
    	ipStatus = new SmsStatus(ip,SmsStatus.TYPE_IP,DEFAULT_IP_DELAY);
    	mobileStatus = new  SmsStatus(mobile,SmsStatus.TYPE_MOBILE,DEFAULT_MOBILE_DELAY);
    }
    
    public void  loadStatus(CacheManager cache)
    {
//    	String jsonString = (String)memcached.get(MemcachedManager.PASSPORT_NODE, );
//    	ipStatus.asString(jsonString);
//    	jsonString = (String)memcached.get(MemcachedManager.PASSPORT_NODE, );
//    	mobileStatus.asString(jsonString);
    	
    	String jsonString = cache.getString(KEY_CACHE + ipStatus.getKey());
    	ipStatus.asString(jsonString);
    	
    	jsonString = cache.getString(KEY_CACHE + mobileStatus.getKey());
    	mobileStatus.asString(jsonString);
    }
  
    public void  saveStatus(CacheManager cache)
    {
//    	memcached.set(MemcachedManager.PASSPORT_NODE, KEY_CACHE + ipStatus.getKey(), ipStatus.valueString(), KEY_EXPIRE);
//    	memcached.set(MemcachedManager.PASSPORT_NODE, KEY_CACHE + mobileStatus.getKey(), mobileStatus.valueString(), KEY_EXPIRE);
    	
    	cache.setString(KEY_CACHE + ipStatus.getKey(), ipStatus.valueString(), KEY_EXPIRE);
    	cache.setString(KEY_CACHE + mobileStatus.getKey(), mobileStatus.valueString(), KEY_EXPIRE);
    	
    }
    
    public void incrRequst() {
    	ipStatus.incrRequst();
    	mobileStatus.incrRequst();
	}

	public void incrSend() {
		ipStatus.incrSend();
    	mobileStatus.incrSend();
	}
   
    public boolean checkValid(){
    	boolean validStatus = false;
		long currentTime = System.currentTimeMillis();
		if(currentTime - ipStatus.getLastTime() > ipStatus.getDelayTime())
		{
			if(currentTime - mobileStatus.getLastTime() > mobileStatus.getDelayTime())
			{
				validStatus = true;
			} else
			{
				LOG.debug("mobile限制发送");
			}
		} else
		{
			LOG.debug("ip限制发送");
		}
		return validStatus;
    }
}
