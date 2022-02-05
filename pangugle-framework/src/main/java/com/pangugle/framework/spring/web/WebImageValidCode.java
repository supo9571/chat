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
package com.pangugle.framework.spring.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.utils.ValidateCodeUtil;

/**
 * 图片验证码工具类
 * @author Administrator
 *
 */
public class WebImageValidCode {
	
	private static Log LOG = LogFactory.getLog(WebImageValidCode.class);
	
	public static void generateAndSendImgCode(String sessionKey)
	{
		 try {
			 	String code =  ValidateCodeUtil.getRandomString();
				HttpServletRequest request = WebRequest.getHttpServletRequest();
				HttpServletResponse response = WebRequest.getHttpServletResponse();
				request.getSession().setAttribute(sessionKey, code);
				response.setContentType("image/jpeg");
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				OutputStream outputStream = response.getOutputStream();
				ValidateCodeUtil.stringToImage(code, outputStream);
				outputStream.flush();
				outputStream.close();
	        } catch (IOException e) {
	        	LOG.error("generate image valid code error:", e);
	        }
	}
	
	public static boolean checkImgCode(String code, String sessionKey)
	{
		if(StringUtils.isEmpty(code)) return false;
		HttpServletRequest request = WebRequest.getHttpServletRequest();
		String sessionImageCode = (String) request.getSession().getAttribute(sessionKey);
		return code.equalsIgnoreCase(sessionImageCode);
	}
	
	public static void removeImgCode(String sessionKey)
	{
		WebRequest.getSession().setAttribute(sessionKey, StringUtils.getEmpty());
	}
	
	public static enum ImageValidCodeType {
		NUM,
		NUM_AND_DIGIT
	}

}
