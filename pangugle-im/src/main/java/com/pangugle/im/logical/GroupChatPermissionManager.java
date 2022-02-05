package com.pangugle.im.logical;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.cache.GroupCacheKeyUtils;

/**
 * 群组成员禁言管理器，针对单个群成员
 * @author Administrator
 *
 */
public class GroupChatPermissionManager {
	
	private interface MyInternal {
		public static GroupChatPermissionManager mgr = new GroupChatPermissionManager();
	}
	
	public static GroupChatPermissionManager getIntance()
	{
		return MyInternal.mgr;
	}
	
	private GroupChatPermissionManager()
	{
	}
	
	public long getDisabledTime(String groupid, String username)
	{
		String cachekey = GroupCacheKeyUtils.createGroupMemberEnableChat(groupid, username);
		String value = CacheManager.getInstance().getString(cachekey);
		// 默认为true, 可以聊天
		return StringUtils.asLong(value);
	}
	
	public void disableChat(String groupid, String username, long time)
	{
		String cachekey = GroupCacheKeyUtils.createGroupMemberEnableChat(groupid, username);
		CacheManager.getInstance().setString(cachekey, time + StringUtils.getEmpty());
	}
	

}
