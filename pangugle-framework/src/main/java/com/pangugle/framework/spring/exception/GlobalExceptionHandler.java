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
package com.pangugle.framework.spring.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.ErrorResult;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

	// 日志记录工具
	private static final Log LOG = LogFactory.getLog(GlobalExceptionHandler.class);

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public String handleException(Throwable e) {
		ApiJsonTemplate apiJsonTemplate = new ApiJsonTemplate();
		
		ErrorResult result = GlobalExceptionManager.getInstanced().getResult(e.getClass());
		if(result != null)
		{
			apiJsonTemplate.setJsonResult(result);
		}
		else
		{
			LOG.error("un handle error:", e);
			apiJsonTemplate.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
		}
		return apiJsonTemplate.toJSONString();
	}

	// @ExceptionHandler(value = Exception.class)
	// public HttpMessageConverters defaultErrorHandler(HttpServletRequest request,
	// Exception e) throws Exception {
	// LOG.error(e.toString());
	// // 1. 需要定义一个Convert转换消息的对象
	// FastJsonHttpMessageConverter fastConverter = new
	// FastJsonHttpMessageConverter();
	// // 2.添加fastjson的配置信息，比如是否要格式化返回的json数据
	// FastJsonConfig fastJsonConfig = new FastJsonConfig();
	// fastJsonConfig.setDateFormat(DateUtils.TYPE_YYYYMMDDHHMMSSSSS);
	// fastJsonConfig.setSerializerFeatures(
	// SerializerFeature.PrettyFormat,
	// SerializerFeature.WriteNullStringAsEmpty,
	// SerializerFeature.WriteNullListAsEmpty,
	// SerializerFeature.MapSortField,
	// // 循环引用
	// SerializerFeature.DisableCircularReferenceDetect);
	// // 3.在convert中添加配置信息
	// fastConverter.setFastJsonConfig(fastJsonConfig);
	// HttpMessageConverter<?> converter = fastConverter;
	// return new HttpMessageConverters(converter);
	//
	// }
}
