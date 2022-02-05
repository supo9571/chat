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

import java.awt.image.RenderedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.collect.Maps;

public class ImageUtils 
{
	private static Map<String, Integer> maps = Maps.newHashMap();
	
	static {
		synchronized (maps) {
			maps.put("bmp", 1);
			maps.put("dib", 1);
			maps.put("gif", 1);
			maps.put("jfif", 1);
			maps.put("jpe", 1);
			maps.put("jpeg", 1);
			maps.put("jpg", 1);
			maps.put("png", 1);
			maps.put("tif", 1);
			maps.put("tiff", 1);
			maps.put("ico", 1);
		}
	}
	

	public static void saveImage(RenderedImage img, String path) 
    {  
    	try {
			OutputStream sos = new FileOutputStream(path);
			ImageIO.write(img, "png", sos);  
			sos.close();
		} catch (Exception e) {
		}
    }
	
//	public static boolean isBlack(String filename) {
//        org.eclipse.swt.graphics.ImageLoader loader = new org.eclipse.swt.graphics.ImageLoader();
//        loader.load(filename);
//        org.eclipse.swt.graphics.ImageData data = loader.data[0];
//        byte[] bytes = data.data;
//        for (byte b : bytes) {
//            if (b != 0) {
//                return false;
//            }
//        }
//        return true;
//    }
	
	public static boolean isPicture(String ext)
	{
		if(StringUtils.isEmpty(ext)) return false;
		return maps.containsKey(ext);
	}
	
}
