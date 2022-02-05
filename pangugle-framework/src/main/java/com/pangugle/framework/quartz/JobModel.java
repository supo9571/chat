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
package com.pangugle.framework.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;

import com.pangugle.framework.utils.DateUtils;
import com.pangugle.framework.utils.StringUtils;

public class JobModel {
	public final static String TIGGER_TYPE_NOW="now";
	public final static String TIGGER_TYPE_DATE="date";
	public final static String TIGGER_TYPE_CRON="cron";
	private String  jobId;
	private String  jobGroup;
	private Class<? extends Job> jobClass;
	private String triggerType=TIGGER_TYPE_NOW;  //
	private Date date;
	private String cronExpression;
	private JobDataMap jobParams;
	
	public JobModel(){
		this(null,null);
	}
	public JobModel(String jobId){
		 this(jobId,null);
	}
	public JobModel(String jobId,String jobGroup){
		this(jobId, jobGroup, new JobDataMap());
	}
	public JobModel(String jobId,String jobGroup, JobDataMap dataMap){
		this.jobId = jobId;
		this.jobGroup = jobGroup;
		this.jobParams = dataMap;
	}
	
	//
	
	public String getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(String triggerType) {
		if(triggerType != null) {
			this.triggerType = triggerType;
		}
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public Class<? extends Job> getJobClass() {
		return jobClass;
	}
	public void setJobClass(Class<? extends Job> jobClass) {
		this.jobClass = jobClass;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDateString(String dateString) {
//		this.date = date;
		if(!StringUtils.isEmpty(dateString)) {
			this.date = DateUtils.convertDate("yyyyMMddHHmmss", dateString);
		}
		
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public JobDataMap getJobParams() {
		return jobParams;
	}

	
	
	
	
}
