package com.pangugle.im.utils;

import java.util.List;

import com.google.common.collect.Lists;
import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.cache.CacheKeyUtils;
import com.pangugle.im.cache.IMCache;

public class OfflineMessageUtils {
	
	public static final int DEFAULT_PAGE_SIZE = 100;
	
	public static void save(MyProtocol protocal)
	{
		String userid = protocal.getTargetid();
		String pageKey = CacheKeyUtils.createOfflineMessage_PageKey(userid);
		int page = StringUtils.asInt(IMCache.getIntance().getString(pageKey));
		
		if(page <= 0)
		{
			// 表示未有任何离线消息, 初始化为1
			page = 1;
		}
		
		boolean stop = false;
		int currentPage = page;
		while(!stop)
		{
			List<MyProtocol> dataList = getPageList(userid, currentPage);
			// 每page 最大100条数据
			if(dataList.size() <= DEFAULT_PAGE_SIZE)
			{
				dataList.add(protocal);
				String bodyKey = CacheKeyUtils.createOfflineMessage_BodyKey(userid, currentPage);
				IMCache.getIntance().setString(bodyKey, FastJsonHelper.jsonEncode(dataList), CacheManager.EXPIRES_WEEK);
				stop = true;
				break;
			}
			
			currentPage ++;
		}
		
		// 页数更新了，需要重新初始化
		if(currentPage != page)
		{
			// 离线消息最多保持7天
			IMCache.getIntance().setString(pageKey, currentPage + StringUtils.getEmpty(), CacheManager.EXPIRES_WEEK);
		}
	}
	
	public static List<MyProtocol> getPageList(String userid, int page)
	{
		String bodyKey = CacheKeyUtils.createOfflineMessage_BodyKey(userid, page);
		List<MyProtocol> dataList = IMCache.getIntance().getList(bodyKey, MyProtocol.class);
		if(CollectionUtils.isEmpty(dataList))
		{
			dataList = Lists.newArrayList();
		}
		return dataList;
	}
	
	public static void deletePage(String userid, int page)
	{
		String bodyKey = CacheKeyUtils.createOfflineMessage_BodyKey(userid, page);
		IMCache.getIntance().delete(bodyKey);
	}

}
