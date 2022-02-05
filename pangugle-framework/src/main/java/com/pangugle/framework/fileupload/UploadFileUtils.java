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
package com.pangugle.framework.fileupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.MD5;

/**
 * @Description 文件上传工具包
 * @Author LXF
 * @Create 2018-11-08 11:35
 */
public class UploadFileUtils {

	private static Log LOG = LogFactory.getLog(UploadFileUtils.class);

	private static MyConfiguration conf = MyConfiguration.getInstance();
	private static final String tempDIR = conf.getString("root.upload_tmp_path");

	public static boolean uploadFile(MultipartFile file, File targetFile) {
		try {
			if (targetFile == null || targetFile.exists())
				return false;
			String tmpPath = tempDIR + "/" + MD5.encode(targetFile.getAbsolutePath() + file.getOriginalFilename()) + "."
					+ FilenameUtils.getExtension(file.getOriginalFilename());
			File tempFile = new File(tmpPath);
			FileUtils.forceMkdirParent(tempFile);
			byte[] byteArray = IOUtils.toByteArray(file.getInputStream());
			OutputStream outStream = new FileOutputStream(tempFile);
			IOUtils.write(byteArray, outStream);
			outStream.close();
			FileUtils.moveFile(tempFile, targetFile);
			return true;
		} catch (Exception e) {
			LOG.error("upload file error:", e);
			return false;
		}
	}

	public static void main(String[] args) {
		 String filename = "sdfasdfasd.png";
		 System.out.println(FilenameUtils.getExtension(filename));
	}

}
