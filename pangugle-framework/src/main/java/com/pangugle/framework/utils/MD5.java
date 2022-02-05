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

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5的算法在RFC1321 中定义 在RFC 1321中，给出了Test suite用来检验你的实现是否正确： MD5 ("") =
 * d41d8cd98f00b204e9800998ecf8427e MD5 ("a") = 0cc175b9c0f1b6a831c399e269772661
 * MD5 ("abc") = 900150983cd24fb0d6963f7d28e17f72 MD5 ("message digest") =
 * f96b697d7cb7938d525a2f31aaf161d0 MD5 ("abcdefghijklmnopqrstuvwxyz") =
 * c3fcd3d76192e4007dfb496cca67e13b
 * <p>
 * 传入参数：一个字节数组 传出参数：字节数组的 MD5 结果字符串
 */
public class MD5 {

	/**
	 * 32位
	 * @param input
	 * @return
	 */
	public static String encode(String input) {
		return DigestUtils.md5Hex(input);
	}
	
	public static String sha256(String input)
	{
		return DigestUtils.sha256Hex(input);
	}
	
	public static String sha384Hex(String input)
	{
		return DigestUtils.sha384Hex(input);
	}
	
	public static String sha512Hex(String input)
	{
		return DigestUtils.sha512Hex(input);
	}
	
	public static void main(String[] args)
	{
		String str = "pangugle";
//		System.out.println(encode(str));
		System.out.println("sha256 = " + MD5.sha256(str));
		System.out.println("sha384 = " + MD5.sha384Hex(str));
		System.out.println("sha512 = " + MD5.sha512Hex(str));
		
		
	}

}
