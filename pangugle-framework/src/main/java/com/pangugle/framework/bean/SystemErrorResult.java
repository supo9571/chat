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
package com.pangugle.framework.bean;

/**
 * 数据接口返回模板
 */
public enum SystemErrorResult implements ErrorResult{

	SUCCESS(200, "success"),
	ERR_SYSTEM(-1, "系统繁忙，请稍后再试"),
	ERR_PARAMS(-2, "params error"),
	ERR_NODATA(-3, "暂无数据"),
	ERR_REQUESTS(-4, "短时间内操作频繁"),
	ERR_SYS_BUSY(-5, "当前业务处理繁忙，请稍后再试"),
	ERR_SYS_FIRST(-6," 当前人数较多，请重试"),
	ERR_SYS_DATA(-7,"数据异常!"),
	ERR_SYS_GOOGLE(-8,"谷歌验证码错误"),
	ERR_SYS_MESSAGE(-9,"处理出现异常"),
	ERR_SYS_OPT_ADD(-10,  "添加失败"),
	ERR_SYS_OPT_DEL(-11,  "删除失败"),
	ERR_SYS_OPT_UPD(-12,  "更新失败"),
	ERR_SYS_OPT_ILEGAL(-13,  "非法操作"),
	ERR_SYS_OPT_FAILURE(-14,  "操作失败"),
	ERR_SYS_OPT_FORBID(-15,  "操作禁止"),
	
	ERR_EXIST(-16,  "已存在"),
	ERR_EXIST_NOT(-17,  "不存在"),
	ERR_INVALID(-18,  "已失效"),
	ERR_DISABLE(-18,  "已禁用"),
	
	ERR_IMAGE_CODE(-50,  "验证码异常"),
	
	ERR_CUSTOM(-100, ""),
	;

	
	private int code;
	private String error;
	public String getError(){return error;}
	public int getCode(){return code;}
	private SystemErrorResult(int code, String error){this.code = code;this.error = error;}
	
}
