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
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.pangugle.framework.context.MyEnvironment;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.shell.Java2ShellExcutor;
import com.pangugle.framework.shell.Java2ShellExcutor.CommandResult;

public class MediaConvertUtils {
	
	private static final Log LOG = LogFactory.getLog(MediaConvertUtils.class);
	
	private static final int DEFAULT_BITRATE = 768;
	
	private static final String BIN_DIR = MyEnvironment.getHome() + "/bin/";
	private static Java2ShellExcutor mShellExcutor = new Java2ShellExcutor(10);
	
	/**
	 * 视频转码，默认 bitrate=768
	 * @param inputFile 源路径
	 * @param outputFile 目标路径
	 */
	public static boolean convertVideo(String inputFile, String outputFile)
	{
		return convertVideo(inputFile, outputFile, DEFAULT_BITRATE);
	}
	
	private static boolean convertVideo(String inputFile, String outputFile, int bitrate)
	{
		try {
			// 源文件不存在
			if(! new File(inputFile).exists()) {
				LOG.error("src file not exist error for " + inputFile);
				return false;
			}
			// 目标文件已存在，不需要转换
			File targetFile = new File(outputFile);
			if(targetFile.exists()) {
				LOG.debug("target file exist , the file is " + outputFile);
				 return true;
			}
			LOG.info("start convert file for " + inputFile);
			
			String datetime = DateUtils.convertString(DateUtils.TYPE_YYYYMMDDHHMMSS, new Date());
			String basename = FilenameUtils.getBaseName(outputFile);
			String extension = FilenameUtils.getExtension(outputFile);
			File tempfile = new File("/tmp/" + basename + datetime + "." + extension);
			
			LOG.debug("temfile = " + tempfile.getAbsolutePath());
			
			// 比特率=>768k, 格式=>libx264, 帧数=>20, 分辨率=>320*240
			String args = " video_convert " + inputFile + " " + tempfile.getAbsolutePath() + " " + bitrate;
			CommandResult result = mShellExcutor.execScript("job-media-convert.sh", args, null, BIN_DIR);
			
			// rename
			if(tempfile.length() > 0) 
			{
				FileUtils.moveFile(tempfile, targetFile);
				return targetFile.exists();
			}
			
			LOG.info("convert video result for " + inputFile + ", " + result.toString());
		} catch (Exception e) {
			LOG.error("convert error:", e);
		}
		return false;
	}

}
