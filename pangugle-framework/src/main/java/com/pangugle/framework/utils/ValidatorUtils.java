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
package com.pangugle.framework.utils;

import com.pangugle.framework.cache.CacheManager;

public class ValidatorUtils {

	private static final String REGEX_MATCH_USER_NAME_ = "[a-z0-9_]*";
	
	public static boolean checkUsername(String username)
	{
		if(StringUtils.isEmpty(username)) return false;
		int usernameLen = username.length();
		if(usernameLen >= 5 && usernameLen <= 20 && username.matches(REGEX_MATCH_USER_NAME_)) return true;
		return false;
	}
	
	public static boolean checkPassword(String password)
	{
		if(!StringUtils.isEmpty(password) && password.length() == 32) 
		{
			return true;
		}
		return false;
	}
	
	public static boolean checkPassword(String pwd1, String pwd2)
	{
		if(checkPassword(pwd1) && pwd1.equalsIgnoreCase(pwd2)) 
		{
			return true;
		}
		return false;
	}
	
	public static boolean checkNickname(String nickname)
	{
		if(!StringUtils.isEmpty(nickname)) 
		{
			int len = nickname.length();
			return len > 0 && len <= 20;
		}
		return false;
	}
	
	public static boolean checkRolename(String rolename)
	{
		if(StringUtils.isEmpty(rolename)) return false;
		int usernameLen = rolename.length();
		if(usernameLen >= 5 && usernameLen <= 20 && RegexUtils.isLetterOrDigitOrBottomLine(rolename) && !RegexUtils.isDigit(rolename)) return true;
		return false;
	}
	
	public static boolean checkGroupName(String groupname) {
		if(StringUtils.isEmpty(groupname)) return false;
		int usernameLen = groupname.length();
		if(usernameLen > 0 && usernameLen <= 20) return true;
		return false;
	}
	
	/**
	 * 验证ip
	 * @param cachekey
	 * @param maxCount
	 * @param expires
	 * @return
	 */
	public static boolean checkIP(String cachekey, int maxCount, int expires)
	{
		long currentCount = CacheManager.getInstance().getLong(cachekey);
		if(currentCount < maxCount)
		{
			currentCount ++;
			CacheManager.getInstance().setString(cachekey, currentCount + StringUtils.getEmpty(), expires);
			return true;
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		String username = "95871312";
		System.out.println(RegexUtils.isLetterDigit(username));
		System.out.println(RegexUtils.isDigit(username));
		
		//
//		String str = "7c4a8d09ca3762af61e59520943dc26494f8941b";
		String password = MD5.encode("123456");
		System.out.println(password + ", len = " + password.length());
//		System.out.println(str + ", len = " + str.length());
		System.out.println(checkPassword(password));
		
	}
	
}
