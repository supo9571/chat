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
package com.pangugle.framework.redis;

import java.util.List;
import java.util.Map;

import com.pangugle.framework.redis.impl.SharedServiceImpl;

public class PikaManager {
	
	private static RedisService mRedisService ;
	
	private interface PikaManagerInternal {
		public PikaManager mgr = new PikaManager();
	}
	
	public static PikaManager getIntance()
	{
		return PikaManagerInternal.mgr;
	}
	
	private PikaManager()
	{
		synchronized (RedisService.class) {
			if(mRedisService == null)
			{
				mRedisService = new SharedServiceImpl();
			}
		}
	}

	// ============================== get ==============================
	public String getString(String key) 
	{
		return mRedisService.getString(key);
	}
	
	public String getList(String key) 
	{
		return mRedisService.getString(key);
	}
	
	// ============================== set ==============================
	/** 
     * <p>设置key value并制定这个键值的有效期</p> 
     * @param key 
     * @param value 
     * @param seconds 单位:秒 
     * @return 成功返回OK 失败和异常返回null 
     */  
    public void setString(String key,String value, int expire)
    {  
    	mRedisService.setString(key, value, expire);  
    }  
    
	// ============================== delete ==============================
	public void delete(String key) 
	{
		mRedisService.delete(key);
	}
	
	public boolean exists(String key)
	{
		return mRedisService.exists(key);
	}
	
	public List<Object> getStringByPipeline(String... keys)
	{
		return mRedisService.getStringByPipeline(keys);
	}
	public void setStringByPipeline(Map<String, Object> keyValue)
	{
		 mRedisService.setStringByPipeline(keyValue);
	}
	
	public void test1()
	{
		setString("test", "good", 3600);
		String value = getString("test");
//		if("OK".equalsIgnoreCase(rs)) System.out.println("ok================");
		System.out.println("========> rs = " + ", value = " + value + "  ");
//		System.out.println("delete result = " + delete("test2"));
	}
	
	public void testExpire()
	{
		try {
			String key = "category_list";
			setString(key, "test3", -1);
		} catch (Exception e) {
			System.out.println("error");
		}
//		
//		String value = getString(key);
//		
//		System.out.println("========> rs = " + null + ", value = " + value + "  ");
		
//		System.out.println(delete("test3"));
	}
	
	public static void test()
	{
		PikaManager redisManager = new PikaManager();
		
		testThread(redisManager);
		testThread(redisManager);
		testThread(redisManager);
		testThread(redisManager);
		
	}
	
	private static void testThread(PikaManager redisManager)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean start = true;
				while(start)
				{
					redisManager.setString("test", "111", 100);
					System.out.println(redisManager.getString("test"));
					//ThreadUtils.sleep(50);
				}
			}
		}).start();
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		test();
//		PikaManager redisManager = new PikaManager();
////		redisManager.testExpire();
//		while(true) {
//			try {
//				redisManager.test1();
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("error");
//			}
//			Thread.sleep(1000);
//		}
		
	}
	
}
