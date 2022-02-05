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

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Utils {

	public static String encode(String str){
		String destr="";
		try{  
		      byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
		      destr= new String(encodeBase64,"utf-8");
		} catch(UnsupportedEncodingException e){  
			//
	    }  
		return destr;
	}
	
	public static byte[] decode(byte[] input)
	{
		return Base64.decodeBase64(input);
	}
	
	public static byte[] encode(byte[] input)
	{
		return Base64.encodeBase64(input);
	}
	
	public static String decode(String str){
		String destr="";
		try{  
		      byte[] decodeBase64 = Base64.decodeBase64(str.getBytes("UTF-8"));
		      destr= new String(decodeBase64,"utf-8");
		} catch(UnsupportedEncodingException e){  
			//
	    }  
		return destr;
	}
	
	public static void main(String[] args)
	{
		String str = "pangugle";
		System.out.println("原始字符串 = " + str);
		
		String encryptStr = Base64Utils.encode(str);
		System.out.println("加密 = " + encryptStr);
		System.out.println("解密 = " + Base64Utils.decode(encryptStr));
	}
	
}
