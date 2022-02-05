package com.pangugle.im.cache;

public class CacheKeyUtils {
	
	public static String createOfflineMessage_PageKey(String userid)
	{
		return "offline_message_page_" + userid;
	}
	
	public static String createOfflineMessage_BodyKey(String userid, int page)
	{
		return "offline_message_body_" + page + userid;
	}

}
