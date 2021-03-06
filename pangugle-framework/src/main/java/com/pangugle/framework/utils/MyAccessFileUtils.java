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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.pangugle.framework.bean.BufferedRandomAccessFile;

public class MyAccessFileUtils {

	/**
	 * 通过BufferedRandomAccessFile读取文件,推荐
	 * 
	 * @param file
	 *            源文件
	 * @param encoding
	 *            文件编码
	 * @param pos
	 *            偏移量
	 * @param num
	 *            读取量
	 * @return pins文件内容，pos当前偏移量
	 */
	@SuppressWarnings("deprecation")
	public static RandomAccessFileStatus bufferedRandomAccessFileReadLine(File file, String encoding, long pos, int num) {
		RandomAccessFileStatus status = new RandomAccessFileStatus();
		BufferedRandomAccessFile reader = null;
		try {
			reader = new BufferedRandomAccessFile(file, "r");
			reader.seek(pos);
			for (int i = 0; i < num; i++) {
				String pin = reader.readLine();
				if (StringUtils.isEmpty(pin)) {
					break;
				}
				status.addLine(new String(pin.getBytes(), encoding));
			}
			status.setPos(reader.getFilePointer());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return status;
	}

	/**
	 * 通过RandomAccessFile读取文件，能出来大数据文件，效率低
	 * 
	 * @param file
	 *            源文件
	 * @param encoding
	 *            文件编码
	 * @param pos
	 *            偏移量
	 * @param num
	 *            读取量
	 * @return pins文件内容，pos当前偏移量
	 */
	@SuppressWarnings("deprecation")
	public static RandomAccessFileStatus readLine(File file, String encoding, long pos, int num) {
		RandomAccessFileStatus status = new RandomAccessFileStatus();
		
		RandomAccessFile reader = null;
		try {
			reader = new RandomAccessFile(file, "r");
			reader.seek(pos);
			for (int i = 0; i < num; i++) {
				String pin = reader.readLine();
				if (StringUtils.isEmpty(pin)) {
					break;
				}
				status.addLine(new String(pin.getBytes(), encoding));
			}
			status.setPos(reader.getFilePointer());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return status;
	}

	/**
	 * 使用LineNumberReader读取文件，1000w行比RandomAccessFile效率高，无法处理1亿条数据
	 * 
	 * @param file
	 *            源文件
	 * @param encoding
	 *            文件编码
	 * @param index
	 *            开始位置
	 * @param num
	 *            读取量
	 * @return pins文件内容
	 */
	@SuppressWarnings("deprecation")
	public static List<String> readLine(File file, String encoding, int index, int num) {
		List<String> pins = Lists.newArrayList();
		LineNumberReader reader = null;
		try {
			reader = new LineNumberReader(new InputStreamReader(new FileInputStream(file), encoding));
			int lines = 0;
			while (true) {
				String pin = reader.readLine();
				if (StringUtils.isEmpty(pin)) {
					break;
				}
				if (lines >= index) {
					if (!StringUtils.isEmpty(pin)) {
						pins.add(pin);
					}
				}
				if (num == pins.size()) {
					break;
				}
				lines++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return pins;
	}
	
	public static class RandomAccessFileStatus {
		private List<String> mLineArray = Lists.newArrayList();
		public List<String> getmLineArray() {
			return mLineArray;
		}
		public void addLine(String line) {
			this.mLineArray.add(line);
		}
		public long getPos() {
			return pos;
		}
		public void setPos(long pos) {
			this.pos = pos;
		}
		private long pos;
	}

}
