package com.pangugle.modules.boot;

import com.pangugle.framework.conf.MyConfiguration;

public class Test {
	
	
	public static void main(String[] args)
	{
		System.setProperty("env", "prod");
		String value = MyConfiguration.getInstance().getString("upload.server");
		System.out.println(value);
	}

}
