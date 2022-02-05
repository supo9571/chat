package com.pangugle.framework.spring;

import java.util.Date;

import org.apache.commons.io.FilenameUtils;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.spring.utils.ServerUtils;
import com.pangugle.framework.utils.DateUtils;
import com.pangugle.framework.utils.ImageUtils;
import com.pangugle.framework.utils.UUIDUtils;

public class UploadManager {
	
    private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    
    private static final String FILE_TYPE_TXT = "txt";
    private static final String FILE_TYPE_PPT = "ppt";
    private static final String FILE_TYPE_DOC = "doc";
    
    private static final String DEFAULT_FILE_PATH = "file";
	
	// ==> /srv/websites/data/uploads/im/2019/09/07/img/a/aa/aaxxxxxxxxxx.png
	// ==> /srv/websites/data/uploads/im/2019/09/07/file/a/aa/aaxxxxxxxxxx.txt
	
	private String mRootUploadPath;
	
	private interface UploadManagerInternal {
		public UploadManager mgr = new UploadManager();
	}
	
	public static UploadManager getIntance()
	{
		return UploadManagerInternal.mgr;
	}
	
	private UploadManager()
	{
		MyConfiguration conf = MyConfiguration.getInstance();
		this.mRootUploadPath = conf.getString("root.upload_path");
	}
	
	public void uploadImage()
	{
		
	}
	
	public void uploadFile()
	{
		
	}
	
	public String getRootPath()
	{
		return mRootUploadPath;
	}
	
	public String createAccessPath(String relatePath)
	{
		String uploadsPath = "/uploads";
		if(ServerUtils.getUploadServer().endsWith(uploadsPath))
		{
			return ServerUtils.getUploadServer()  + relatePath;
		}
		else
		{
			return ServerUtils.getUploadServer()  + uploadsPath + relatePath;
		}
	}
	
	public String createFilePath(String prePath, String ext)
	{
		String filename = UUIDUtils.getUUID();
		String filepath =  filename.substring(0, 1) + "/" + filename.substring(0, 2) + "/" + filename;
		String timePath = DateUtils.convertString(new Date(), DEFAULT_DATE_FORMAT);
		if(ImageUtils.isPicture(ext))
		{
			return   "/" + prePath+ "/" + timePath + "/img/" + filepath + "." + ext;
		}
		else
		{
			return "/" + prePath + "/" + timePath + "/" + getFileSubffixType(ext) +"/" + filepath + "." + ext;
		}
	}
	
	public static String getFileSubffixType(String ext)
	{
		if(FILE_TYPE_TXT.equalsIgnoreCase(ext))
		{
			return FILE_TYPE_TXT;
		}
		if(ext.startsWith(FILE_TYPE_PPT))
		{
			return FILE_TYPE_PPT;
		}
		if(ext.startsWith(FILE_TYPE_DOC))
		{
			return FILE_TYPE_DOC;
		}
		return DEFAULT_FILE_PATH;
	}
	
	public static void main(String[] args)
	{
		String replate = UploadManager.getIntance().createFilePath("im", "doc");
		String url = ServerUtils.getUploadServer() + "/uploads" + replate;
		
		String filepath = UploadManager.getIntance().getRootPath() + replate;
		
		String compressFilepath = filepath.replace(FilenameUtils.getName(filepath), FilenameUtils.getBaseName(filepath) + "_25.jpg");
		
		System.out.println(compressFilepath);
		
	}

}
