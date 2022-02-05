package com.pangugle.framework.image;

import java.io.File;
import java.io.IOException;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

import net.coobird.thumbnailator.Thumbnails;

public class ImageCompress {
	
	private static Log LOG = LogFactory.getLog(ImageCompress.class);
	
	/**
	 * 压缩图片
	 * @param srcFile
	 * @param targetFile
	 * @param scale 0 ~ 1，越小压缩越高
	 * @return
	 */
	public static boolean scale(File srcFile, File targetFile, float scale)
	{
		boolean rs = false;
		try {
			if(srcFile == null || !srcFile.exists())
			{
				return rs;
			}
			if(scale > 1 || scale < 0)
			{
				scale = 0.5f;
			}
			
			Thumbnails.of(srcFile).scale(scale).toFile(targetFile);
			rs = true;
		} catch (IOException e) {
			LOG.error("compress image error:", e);
		}  
		return rs;
	}
	
	public static boolean scale(String srcFilePath, String targetFilePath, float scale) 
	{
		File srcFile = new File(srcFilePath);
		File targetFile = new File(targetFilePath);
		return scale(srcFile, targetFile, scale);
	}
	
	public static void main(String[] args)
	{
		String srcFilePath = "C:/Users/Administrator/Desktop/chat/intro/group_info.jpg";
		String targetFilePath = "C:/Users/Administrator/Desktop/chat/intro/group_info.png";
		scale(srcFilePath, targetFilePath, 0.5f);
	}

}
