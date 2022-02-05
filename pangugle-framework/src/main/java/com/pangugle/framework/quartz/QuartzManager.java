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

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class QuartzManager {

	public static final Log LOG = LogFactory.getLog(QuartzManager.class);
	private Scheduler mScheduler = null;
	
	private static QuartzManager manager;
	
	public static QuartzManager getInstance()
	{
		synchronized (QuartzManager.class) {
			if(manager == null)
			{
				manager = new QuartzManager();
			}
		}
		return manager;
	}

	private QuartzManager() {
		init();
	}

	private void init() {
		try {
			SchedulerFactory factory = new StdSchedulerFactory("config/quartz.properties");
			mScheduler = factory.getScheduler();
			mScheduler.start();
		} catch (SchedulerException e) {
			LOG.error("init quartz error", e);
		}
	}

	public void deleteJobByGroup(String group)
	{
		try {
			for (String groupName : mScheduler.getJobGroupNames()) {
				for (JobKey jobKey : mScheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
					String jobName = jobKey.getName();
					String jobGroup = jobKey.getGroup();
					deleteJob(jobName, jobGroup);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交任务
	 * 
	 * @param taskClass
	 * @param dataMap
	 * @param cronExpression
	 */
	public void submitJob(JobModel jobmodel) {
		try {
			// 构建任务信息
			JobBuilder jobBuilder = JobBuilder.newJob();
			if (jobmodel.getJobId() != null) {
				JobKey jobKey = JobKey.jobKey(jobmodel.getJobId(), jobmodel.getJobGroup());
				jobBuilder.withIdentity(jobKey);
			}

			JobDetail job = jobBuilder.ofType(jobmodel.getJobClass()).usingJobData(jobmodel.getJobParams()).storeDurably().build();
			
			// 构建触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().forJob(job);
			if (JobModel.TIGGER_TYPE_DATE.equalsIgnoreCase(jobmodel.getTriggerType())) {
				if(jobmodel.getDate() != null) 
				{
					triggerBuilder.startAt(jobmodel.getDate());
				} else
				{
					triggerBuilder.startNow();
				}
			} else if (JobModel.TIGGER_TYPE_CRON.equalsIgnoreCase(jobmodel.getTriggerType())) {
				triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(jobmodel.getCronExpression()));
			} 
			Trigger trigger = triggerBuilder.build();

			mScheduler.addJob(job, true);
			mScheduler.scheduleJob(trigger);
		} catch (SchedulerException e) {
			LOG.error("schedule submitTask error:", e);
		}
	}

	public void submitCronJob(Class<? extends Job> taskClass, JobDataMap dataMap, String cronExpression, String mJobID, String mJobGroup) {
		JobModel jobModel = new JobModel(mJobID, mJobGroup, dataMap);
		jobModel.setJobClass(taskClass);
		jobModel.setTriggerType(JobModel.TIGGER_TYPE_CRON);
		jobModel.setCronExpression(cronExpression);
		submitJob(jobModel);
	}

	/**
	 * 提交指定时间相关的任务
	 * 
	 * @param taskClass
	 */
	public void submitDateJob(Class<? extends Job> taskClass, JobDataMap dataMap, Date date, String mJobID, String mJobGroup) {
		JobModel jobModel = new JobModel(mJobID, mJobGroup, dataMap);
		jobModel.setJobClass(taskClass);
		jobModel.setTriggerType(JobModel.TIGGER_TYPE_DATE);
		jobModel.setDate(date);
		submitJob(jobModel);
	}
	
	/**
	 * interrupt
	 */
	public void interruptJob(String jobID)
	{
		interruptJob(jobID, null);
	}
	
	public void interruptJob(String mJobID, String mJobGroup) {
		try {
			JobKey jobKey = JobKey.jobKey(mJobID, mJobGroup);
			mScheduler.interrupt(jobKey);
		} catch (Exception e) {
			LOG.error("interrupt job error:", e);
		}
	}

	/**
	 * 
	 * @param mJobID
	 */
	public void deleteJob(String mJobID) {
		deleteJob(mJobID, null);
	}

	public void deleteJob(String mJobID, String mJobGroup) {
		try {
			JobKey jobKey = JobKey.jobKey(mJobID, mJobGroup);
			mScheduler.deleteJob(jobKey);
		} catch (Exception e) {
			LOG.error("delete job error:", e);
		}
	}

	public void deleteJob(JobKey jobKey) {
		try {
			mScheduler.deleteJob(jobKey);
		} catch (Exception e) {
			LOG.error("delete job error:", e);
		}
	}
	
	public boolean checkExist(String mJobID, String mJobGroup)
	{
		boolean exist = false;
		try {
			JobKey jobKey = JobKey.jobKey(mJobID, mJobGroup);
			exist = mScheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			LOG.error("checkExist job error:", e);
		}
		return exist;
	}

	public void stop(boolean waitForJobsToComplete) {
		try {
			if (mScheduler != null) {
				mScheduler.shutdown(waitForJobsToComplete);
			}
		} catch (SchedulerException e) {
		}
		mScheduler = null;
	}
}
