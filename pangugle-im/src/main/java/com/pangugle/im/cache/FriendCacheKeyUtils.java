package   com.pangugle.im.cache;

import com.pangugle.im.MyConstants;
import com.pangugle.im.model.FriendRelation.MyStatus;

public class FriendCacheKeyUtils {
	

	/**
	 * 用户关系
	 * @param fromUserid
	 * @param toUserid
	 * @return
	 */
	public static String createUserRelation(String fromUserid, String toUserid)
	{
		return getPrefix()+ "_createUserRelation_" + fromUserid + toUserid;
	}
	
	/**
	 * 用户好友列表
	 * @param userid
	 * @return
	 */
	public static String createUserFriendRelationList(String userid, MyStatus status)
	{
		return getPrefix()+ "_createUserFriendRelationList_" + userid + status.getName();
	}
	
	
	/**
	 * 用户好友个数
	 * @param userid
	 * @return
	 */
	public static String createUserFriendRelationSize(String userid, MyStatus status)
	{
		return getPrefix() + "_createUserFriendRelationSize_" + userid + status.getName();
	}
	
	private static String getPrefix()
	{
		return MyConstants.DEFAULT_IM_MODULE_NAME + FriendCacheKeyUtils.class.getSimpleName();
	}
	
	public static String createFriendInfo(String userid, String friendid)
	{
		return getPrefix() + "_createFriendInfo_" + userid + friendid;
	}
	
	public static String createFriendInfoForBlackself(String friendid, String userid)
	{
		// public boolean getBlackSelfStatus(String selfUserid, String friendUserid)
		return getPrefix() + "_createFriendInfoForBlackself_" +friendid + userid;
	}
}