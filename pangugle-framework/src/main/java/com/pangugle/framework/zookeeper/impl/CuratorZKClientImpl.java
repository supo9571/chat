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

import java.util.Collections;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.zookeeper.DistributeLock;

public class CuratorZKClientImpl implements ZKClientSupport {
	
	private static final int DEFAULT_SESSION_TIMEOUT_MS = 10000;
    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 10000;
	
	private static Log LOG = LogFactory.getLog(CuratorZKClientImpl.class);

	private CuratorFramework client;
	
	public CuratorZKClientImpl(MyConfiguration conf) {
		String servers = conf.getString("zk.servers");
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		this.client = CuratorFrameworkFactory.newClient(servers, DEFAULT_SESSION_TIMEOUT_MS, DEFAULT_CONNECTION_TIMEOUT_MS, retryPolicy);
		this.client.start();
	}
	
	public void createPersistent(String path) {
		try {
			client.create().forPath(path);
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			LOG.error("createPersistent error:", e);
		}
	}

	public void createEphemeral(String path) {
		try {
			client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			LOG.error("createEphemeral error:", e);
		}
	}
	
	@Override
	public void delete(String path) {
		try {
			client.delete().forPath(path);
		} catch (NodeExistsException e) {
		} catch (Exception e) {
			LOG.error("delete error:", e);
		}
		
	}
	@Override
	public void setData(String path, String data) {
		try {
			client.setData().forPath(path, data.getBytes());
		} catch (NoNodeException e) {
		} catch (Exception e) {
			LOG.error("set data error", e);
		}
	}
	
	public String getData(String path)
	{
		try {
			byte[] data = client.getData().forPath(path);
			if(data != null) return new String(data);
		} catch (NoNodeException e) {
		} catch (Exception e) {
			LOG.error("get data error:", e);
		}
		return null;
	}

	public List<String> getChildren(String path) {
		try {
			return client.getChildren().forPath(path);
		} catch (NoNodeException e) {
		} catch (Exception e) {
			LOG.error("getChildren error:", e);
		}
		return Collections.emptyList();
	}
	
	@Override
	public DistributeLock createLock(String path) {
		return new CuratorSemaphoreLockImpl(client, path);
	}

	public boolean isConnected() {
		return client.getZookeeperClient().isConnected();
	}
	
	@Override
	public void close() {
		client.close();
	}



}
