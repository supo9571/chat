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

import com.pangugle.framework.utils.StringUtils;

public class SmsStatus {
	public static final String TYPE_MOBILE = "m";
	public static final String TYPE_IP = "i";
	public static final int VERSION = 1;

	private String key;
	private String type;

	private long startTime;
	private long lastTime;
	/*** 处罚时间 ***/
	private long delayTime;
	private int sendCount;
	private int requestCount;

	public SmsStatus(String key, String type, long defaultDelayTime) {
		this.key = key;
		this.type = type;
		this.delayTime = defaultDelayTime;
		this.startTime = System.currentTimeMillis();
	}

	public void incrRequst() {
		this.requestCount++;
		this.lastTime = System.currentTimeMillis();
	}

	public void incrSend() {
		this.sendCount++;
		this.lastTime = System.currentTimeMillis();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public void setRequestCount(int requestCount) {
		this.requestCount = requestCount;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * version=1 "1,starttime,lasttime,delay,request,send"
	 * 
	 * @return
	 */
	public String valueString() {
		StringBuffer buf = new StringBuffer();
		buf.append(VERSION).append(",").append(startTime).append(",").append(lastTime).append(",").append(delayTime)
				.append(",").append(requestCount).append(",").append(sendCount);
		return buf.toString();
	}

	public void asString(String str) {
		if (!StringUtils.isEmpty(str)) {
			String[] strs = str.split(",");
			if (strs.length == 6 && strs[0].equalsIgnoreCase("1")) {
				setStartTime(StringUtils.asLong(strs[1]));
				setLastTime(StringUtils.asLong(strs[2]));
				setDelayTime(StringUtils.asLong(strs[3]));
				setRequestCount(StringUtils.asInt(strs[4]));
				setSendCount(StringUtils.asInt(strs[5]));
			}
		} 
	}

}
