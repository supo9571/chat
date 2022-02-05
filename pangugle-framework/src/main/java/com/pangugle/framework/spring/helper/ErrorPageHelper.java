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
package com.pangugle.framework.spring.helper;

import org.springframework.ui.Model;

import com.pangugle.framework.bean.ErrorResult;
import com.pangugle.framework.utils.StringUtils;

/**
 * 公共错误页面帮助类
 * @author Administrator
 *
 */
public class ErrorPageHelper {
	
	
	public static String toError(Model model, int code, String msg)
	{
		if(StringUtils.isEmpty(msg))
		{
			msg = "非法操作";
		}
		model.addAttribute("code", code);
		model.addAttribute("msg", msg);
		return "common/error";
	}
	
	public static String toError(Model model, String msg)
	{
		return toError(model, 0, msg);
	}
	
	public static String toError(Model model)
	{
		return toError(model, 0, null);
	}

	public static String toError(Model model, ErrorResult result)
	{
		return toError(model, result.getCode(), result.getError());
	}
}
