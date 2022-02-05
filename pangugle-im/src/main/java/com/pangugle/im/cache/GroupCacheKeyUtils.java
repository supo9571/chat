package   com.pangugle.im.cache;

import com.pangugle.im.MyConstants;

public class GroupCacheKeyUtils {
	
	private static String getPrefix()
	{
		return MyConstants.DEFAULT_IM_MODULE_NAME + GroupCacheKeyUtils.class.getSimpleName();
	}
	
	public static String createAllGroupConfig()
	{
		return getPrefix() + "_AllGroupConfig"; 
	}
	
	public static String createFindGroupConfig(String type)
	{
		return getPrefix() + "_findGroupConfig_" + type; 
	}
	
	
	public static String createFindGroup(String groupid)
	{
		return getPrefix() + "_createFindGroup_" + groupid; 
	}
	
//	public static String createAllHolderGroup(String holder)
//	{
//		return getPrefix() + "createAllHolderGroup" + holder; 
//	}
	
	//////////////////////////////////////// 我是分割线
	public static String createGroupMemberList(String groupid, long page)
	{
		return getPrefix() + "createGroupMemberList" + groupid + page; 
	}
	
//	public static String createGroupMemberListPages(String groupid)
//	{
//		return getPrefix() + "createGroupMemberListPages" + groupid; 
//	}
	
	public static String createQueryUserJoinGroup(String username)
	{
		return getPrefix() + "createQueryUserJoinGroup" + username; 
	}
	
	public static String createFindGroupRelation(String groupid, String username)
	{
		return getPrefix() + "createFindGroupRelation" + groupid + username;  
	}
	
	public static String createGroupQrcode(String reqcode)
	{
		return getPrefix() + "createFindGroupRelation" + reqcode;  
	}
	
	public static String createQueryAllGroupAdmin(String groupid)
	{
		return getPrefix() + "createQueryAllGroupAdmin" + groupid;  
	}
	
	public static String createGroupMemberEnableChat(String groupid, String username)
	{
		return getPrefix() + "createGroupMemberEnableChat" + groupid + username;  
	}
	
}