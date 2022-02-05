package com.pangugle.im.service.dao;

import com.pangugle.framework.service.Callback;
import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.FriendRelation.MyStatus;

public interface FriendRelationDao {
	
	/**
	 * 绑定好友关系
	 * @param fromUserid
	 * @param toUserid
	 */
	public void bindRelation(String fromUserid, String toUserid, String requstUserid, MyStatus fromStatus, MyStatus toStatus, String remark);
	
	/**
	 * 删除关系
	 * @param fromUserid
	 * @param toUserid
	 */
	public void deleteRelation(String fromUserid, String toUserid);
	
	/**
	 * 更新好友关系状态
	 * @param fromUserid
	 * @param toUserid
	 * @param status
	 */
	public void updateFromStatus(String fromUserid, String toUserid, MyStatus status);
	
	/**
	 * 更新好友关系状态
	 * @param fromUserid
	 * @param toUserid
	 * @param status
	 */
	public void updateToStatus(String fromUserid, String toUserid, MyStatus status);
	
	/**
	 * 查询好友关系
	 * @param fromUserid
	 * @param toUserid
	 * @return
	 */
	public FriendRelation find(String fromUserid, String toUserid);
	
	/**
	 * 根据用户查询指定类型关系列表
	 * @param userid
	 * @return
	 */
	public void queryStatusList(String userid, MyStatus status, Callback<String> callback);
	
	/**
	 * 查询好友关系
	 * @param userid
	 * @return
	 */
	public void queryFriendList(String userid,  Callback<String> callback);
	public long countFriend(String userid);

}
