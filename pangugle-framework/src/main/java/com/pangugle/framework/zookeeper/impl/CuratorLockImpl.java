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
package com.pangugle.framework.zookeeper.impl;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.zookeeper.DistributeLock;

/**
 * 可重入锁
 * @author Administrator
 *
 */
public class CuratorLockImpl implements DistributeLock{
	
	private static final Log LOG = LogFactory.getLog(CuratorLockImpl.class);
	
	private InterProcessMutex mLock;
	
	public CuratorLockImpl(CuratorFramework client, String lockPath)
	{
		this.mLock = new InterProcessMutex(client, lockPath);
	}
	
	public boolean lockAcquired(long waitSeconds) {
		boolean rs = false;
		try {
			rs = mLock.acquire(waitSeconds, TimeUnit.SECONDS);
		} catch (Exception e) {
			LOG.error("lock error:", e);
		}
		return rs;
	}
	
	public boolean lockAcquired()
	{
		return lockAcquired(5);
	}
	
	public boolean isLockAcquiredInThisProcess()
	{
		return mLock.isAcquiredInThisProcess();
	}

	public void lockReleased() {
		try {
			mLock.release();
		} catch (Exception e) {
			LOG.error("unlock error:", e);
		}
	}
	
}
