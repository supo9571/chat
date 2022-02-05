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
import java.util.ArrayList;

/**
 * <p>
 * Title: 多页查询页面控制器
 * </p>
 * <p>
 * Description:total,TotalPages,StartRow,EndRow,
 * PageSize,CurrentPage,PreviousPage,NextPage,FirstPage,LastPage,
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * 
 * @author wab
 * @version 1.0
 */
public class RowPager<T> {
	private long total;

	private long startRow, endRow;

	private long pageSize;

	private long currentPage, firstPage, lastPage;

	private List<T> list = new ArrayList<T>();


	public long getTotal() {
		return getTotalRows();
	}
	
	public long getTotalRows() {
		return total;
	}

	public long getPageSize() {
		return pageSize;
	}

	public long getStartRow() {
		return startRow;
	}

	public long getPreviousPage() {
		return this.validPage(this.currentPage - 1);
	}

	public long getNextPage() {
		return this.validPage(currentPage + 1);
	}

	public long getEndRow() {
		return endRow;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getLastPage() {
		return lastPage;
	}

	public long getFirstPage() {
		return firstPage;
	}

	public long getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param total
	 *            int
	 * @param pageno
	 *            int
	 * @param pagesize
	 *            int
	 */
	public RowPager(long total, long pageno, long pagesize) {
		if(pagesize <= 0) pagesize = 10;
		this.total = total;
		this.pageSize = pagesize;
		this.firstPage = 1;
		
		long totalPage = (total + pagesize - 1) / pagesize;
		if(totalPage <= 0) totalPage = firstPage;
//		this.total

		this.currentPage = validPage(pageno);
		// 计算当前页的开始记录号,结束记录号;
		this.startRow = (this.currentPage - 1) * this.pageSize + 1;
		this.endRow = (this.currentPage < this.lastPage ? this.currentPage
				* this.pageSize
				: this.total);

	}

	private long validPage(long pageno) {
		if(pageno < this.lastPage)
		{
			if(pageno > this.firstPage)
			{
				return pageno;
			}
			else
			{
				return this.firstPage;
			}
		} else
		{
			return this.lastPage;
		}
	}

	public void addRow(T row) {
		this.list.add(row);
	}
	
	public void addRows(List<T> rows)
	{
		this.list.addAll(rows);
	}
	
	
	public static void main(String[] args)
	{
		int totalPage = (1 + 10 - 1) / 10;
		System.out.println(totalPage);
	}
}
