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

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PageView<T> {
	
	/*** 分页导航最多5个 ***/
	private static final long DEFAULT_MAX_PAGENATION_LEN = 5;
	
	//数据总条数
	private long totalRecord;	

	//总页数
	private long totalPage;	
	//每页显示条数
	private long pagesize=20;	
	//当前页数
	private long currentPage;	
	//存一页数据
	private List<T>  list;	
	
	// 分页导航数据
	private List<Map<String, Object>> mPaginatiolList = null;
	
	//
	private long beginPage;
	private long endPage;
	
	// 上一页、下一页
	private String prePageUrl = null;
	private String nextPageUrl = null;

	public PageView(long totalRecord, long currengPage, long pageSize, List<T> dataList)
	{
		this.totalRecord = totalRecord;
		this.setPagesize(pageSize);
		this.list = dataList;
		
		// 计算总页数
		long totalPage = (totalRecord + this.getPagesize() - 1) / this.getPagesize();
		if(totalPage <= 0) totalPage = 1;
		this.totalPage = totalPage;
		
		// 设置当前页
		this.setCurrentPage(currengPage);
		
	}
	

	public void buildPaginationInfo(long paginationLen, String prefixUrl)
	{
		if(this.totalRecord <= 0)
		{
			return;
		}
		if(paginationLen <= 0)
		{
			paginationLen = DEFAULT_MAX_PAGENATION_LEN;
		}
		long stepLen = paginationLen / 2;
		// beginPage
		this.beginPage = this.currentPage - stepLen;
		if(this.beginPage <= 0)
		{
			this.beginPage = 1;
		}
		this.endPage = this.currentPage + stepLen;
		
		if(this.endPage - this.beginPage < DEFAULT_MAX_PAGENATION_LEN)
		{
			this.endPage = this.beginPage + DEFAULT_MAX_PAGENATION_LEN - 1;
		}
		if(this.endPage > this.totalPage)
		{
			this.endPage = this.totalPage;
		}
		
		
		String splitStr = "?";
		if(prefixUrl.contains(splitStr))
		{
			splitStr = "&";
		}
		buildPaginationList(prefixUrl, splitStr);
		
		//
		if(this.currentPage - 1 > this.beginPage)
		{
			this.prePageUrl = buildLink(prefixUrl, splitStr, (this.currentPage - 1));
		}
		if(this.currentPage + 1 < this.endPage)
		{
			this.nextPageUrl = buildLink(prefixUrl, splitStr, (this.currentPage + 1));
		}
	}
	
	private String buildLink(String prefixUrl, String splitStr, long pn)
	{
		if(pn <= 1)
		{
			return prefixUrl;
		}
		return prefixUrl + splitStr +  "pn=" + pn;
	}
	
	private void buildPaginationList(String prefixUrl, String splitStr)
	{
		List<Map<String, Object>> paginatiolList = Lists.newArrayList();
		for(long i = beginPage; i <= endPage; i ++)
		{
			Map<String, Object> maps = Maps.newHashMap();
			String pageUrl = buildLink(prefixUrl, splitStr, i);;
			maps.put("pageUrl", pageUrl);
			maps.put("pageIndex", i);

			// active
			if(i == this.currentPage)
			{
				maps.put("isCurrentPage", 1);
			}
			else
			{
				maps.put("isCurrentPage", 0);
			}
			paginatiolList.add(maps);
		}
		this.mPaginatiolList = paginatiolList;
	}

	
	public long getOffset()
	{
		return (this.getCurrentPage() - 1) * pagesize;
	}
	
	public long getTotalRecord() {
		return totalRecord;
	}


	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}


	public long getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}


	public long getPagesize() {
		return pagesize;
	}


	public void setPagesize(long pagesize) {
		if(pagesize <= 0) return;
		this.pagesize = pagesize;
	}

	public long getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(long currentPage) {
		if(currentPage <= 0) currentPage = 1;
		if(currentPage <= totalPage)
		{
			this.currentPage = currentPage;
		}
		else
		{
			this.currentPage = totalPage;
		}
	}

	public List<T> getList() {
		return list;
	}


	public void setList(List<T> dataList) {
		this.list = dataList;
	}
	
	public List<Map<String, Object>> getmPaginatiolList() {
		return mPaginatiolList;
	}
	
	public String getPrePageUrl() {
		return prePageUrl;
	}

	public String getNextPageUrl() {
		return nextPageUrl;
	}

}
