package com.pangugle.im.utils;

import com.pangugle.framework.utils.MD5;

public class GroupHelper {
	
	public static String signCreateGroupQrcodeInfo(String groupid, String createUsername, long time)
	{
		String sign = MD5.encode(groupid + createUsername + time + "fdsa;什么塔顶sfsadf");
		return sign;
	}

}
