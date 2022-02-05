package com.pangugle.im.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.ffmpeg.AudioConvert;
import com.pangugle.framework.ffmpeg.VideoConvert;
import com.pangugle.framework.fileupload.UploadFileUtils;
import com.pangugle.framework.image.ImageCompress;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.spring.UploadManager;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.MyConstants;
import com.pangugle.im.concurrent.AudioConvertConcurrent;
import com.pangugle.im.limit.MyTokenLimit;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.logical.OfflineManager;

@RequestMapping("/im/api")
@RestController
public class IMApi {
	
	private static String DEFAULT_GIF_KEY = "gif";
	
	private AecManager mAecManager = AecManager.getInstance();
	
	@MyTokenLimit
	@RequestMapping("/getOfflineMessage")
	public String getOfflineMessage()
	{
		int page = WebRequest.getInt("page");
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		ApiJsonTemplate api = new ApiJsonTemplate();
		List<MyProtocol> list = OfflineManager.getIntance().queryOfflineMessage(username, page);
		
		if(CollectionUtils.isEmpty(list))
		{
			list = Lists.emptyList();
		}
		api.setData(list);
		return api.toJSONString();
	}
	
	@RequestMapping("uploadImage")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file)
	{
		// 
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// verify ip
		
		String relatePath = UploadManager.getIntance().createFilePath(MyConstants.DEFAULT_IM_MODULE_NAME + "/img", ext);
		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
		
		File targetFile = new File(filepath);
		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
		if(!rs)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		
		if(!DEFAULT_GIF_KEY.equalsIgnoreCase(ext))
		{
			// 压缩图片
			String srcFilename = FilenameUtils.getName(filepath);
			String compressFilename = null;
			compressFilename = FilenameUtils.getBaseName(filepath) + "_25.jpg";
			
			String compressFilepath = filepath.replace(srcFilename, compressFilename);
			String compressRelatePath =  relatePath.replace(srcFilename, compressFilename);
			
			File compressFile =  new File(compressFilepath);
			ImageCompress.scale(targetFile, compressFile, 0.25f);
			
			
			Map<String, String> maps = Maps.newHashMap();
			maps.put("src_path", UploadManager.getIntance().createAccessPath(relatePath));
			maps.put("compress_path", UploadManager.getIntance().createAccessPath(compressRelatePath));
			api.setData(maps);
		}
		else
		{
			String src_path = UploadManager.getIntance().createAccessPath(relatePath);
			Map<String, String> maps = Maps.newHashMap();
			maps.put("src_path", src_path);
			maps.put("compress_path", StringUtils.getEmpty());
			api.setData(maps);
		}
		
		return api.toJSONString();
	}
	
	@RequestMapping("uploadVoice")
	@ResponseBody
	public String uploadVoice(@RequestParam("file") MultipartFile file)
	{
		// 
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// verify ip
		String relatePath = UploadManager.getIntance().createFilePath(MyConstants.DEFAULT_IM_MODULE_NAME + "/audio", ext);
		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
		
		File targetFile = new File(filepath);
		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
		
		if(!rs)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		
		
		String srcFilename = FilenameUtils.getName(filepath);
		String compressFilename = null;
		compressFilename = FilenameUtils.getBaseName(filepath) + "_compress.mp3";
		
		String compressFilepath = filepath.replace(srcFilename, compressFilename);
		String compressRelatePath =  relatePath.replace(srcFilename, compressFilename);
		
		// 转换线程
		AudioConvertConcurrent.getInstance().execute(new Runnable() {
			public void run() {
				File compressFile =  new File(compressFilepath);
				AudioConvert.convertMP3(targetFile, compressFile);
				
				// 删除源文件
				targetFile.delete();
			}
		});
		
		api.setData(UploadManager.getIntance().createAccessPath(compressRelatePath));
		return api.toJSONString();
	}
	
	@RequestMapping("uploadVideo")
	@ResponseBody
	public String uploadVideo(@RequestParam("file") MultipartFile file)
	{
		// 
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// verify ip
		String relatePath = UploadManager.getIntance().createFilePath(MyConstants.DEFAULT_IM_MODULE_NAME + "/video", ext);
		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
		
		File targetFile = new File(filepath);
		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
		
		if(!rs)
		{
			targetFile.delete();
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		
		
		String srcFilename = FilenameUtils.getName(filepath);
		String compressFilename = FilenameUtils.getBaseName(filepath) + "_compress.mp4";
		
		String compressFilepath = filepath.replace(srcFilename, compressFilename);
		String compressRelatePath =  relatePath.replace(srcFilename, compressFilename);
		
		String imgPosterFilename = FilenameUtils.getBaseName(filepath) + "_poster.jpg";
		String imgPosterFilepath =  filepath.replace(srcFilename, imgPosterFilename);
		String imgPosterRelatePath =  relatePath.replace(srcFilename, imgPosterFilename);
		
//		System.out.println("imgPosterFilepath = " + imgPosterFilepath);
//		System.out.println("imgPosterRelatePath = " + imgPosterRelatePath);
		
		// 转换线程
//		AudioConvertConcurrent.getInstance().execute(new Runnable() {
//			public void run() {
//				File compressFile =  new File(compressFilepath);
//				VideoConvert.convertMP4(targetFile, compressFile, 400); // 宽度为400
				
				// 删除源文件
//				targetFile.delete();
//			}
//		});
		
		File compressFile =  new File(compressFilepath);
		rs = VideoConvert.convertMP4(targetFile, compressFile, 512000, 800); // 宽度为400
		if(!rs)
		{
			targetFile.delete();
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		rs = VideoConvert.convertVideoToPicture(compressFile, new File(imgPosterFilepath));
		if(!rs)
		{
			targetFile.delete();
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		targetFile.delete();
		
		Map<String, Object> maps = Maps.newHashMap();
		maps.put("remoteUrl", UploadManager.getIntance().createAccessPath(compressRelatePath));
		maps.put("posterUrl", UploadManager.getIntance().createAccessPath(imgPosterRelatePath));
		
		api.setData(maps);
		return api.toJSONString();
	}
	
	@RequestMapping("uploadFile")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file)
	{
		// 
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// verify ip
		String relatePath = UploadManager.getIntance().createFilePath(MyConstants.DEFAULT_IM_MODULE_NAME + "/file", ext);
		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
		
		File targetFile = new File(filepath);
		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
		if(!rs)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
			return api.toJSONString();
		} 
		api.setData(UploadManager.getIntance().createAccessPath(relatePath));
		return api.toJSONString();
	}
	
	@RequestMapping("batchUploadFile")
	@ResponseBody
	public String batchUploadFile(@RequestParam("file") MultipartFile[] files)
	{
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		// 
//		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
//		
//		
//		
//		// verify ip
//		
//		String relatePath = UploadManager.getIntance().createFilePath(Constants.MODULE_NAME, ext);
//		String filepath = UploadManager.getIntance().getRootPath() + relatePath;
//		
//		File targetFile = new File(filepath);
//		boolean rs = UploadFileUtils.uploadFile(file, targetFile);
//		
//		if(!rs)
//		{
//			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
//			return api.toJSONString();
//		} 
//		
//		String url = UploadManager.getIntance().createAccessPath(relatePath);
//		api.setData(url);
		return api.toJSONString();
	}

}
