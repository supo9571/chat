package com.pangugle.im.service;

import java.util.List;

import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.FriendRelation.MyStatus;

/**
 * 用户好友关系业务层
 * @author Administrator
 *
 */
public interface FriendRelationService {
	
	/**
	 * 绑定好友关系
	 * @param fromUserid
	 * @param toUserid
	 */
	public void bindRelation(String fromUserid, String toUserid, String requestUserid, String remark);
	
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
	public void updateStatus(String fromUserid, String toUserid, String targetUserid, MyStatus status);
	
	/**
	 * 查询好友关系
	 * @param fromUserid
	 * @param toUserid
	 * @return
	 */
	public FriendRelation find(String fromUserid, String toUserid);
	
	/**
	 * 黑名单，待确认列表，拒绝列表，接口只能提供待确认列表，
	 * @param userid
	 * @param page
	 * @param status
	 * @return
	 */
	public List<String> queryUnConfirmList(String userid);
	
	/**
	 * 查询好友关系
	 * @param userid
	 * @return
	 */
	public List<String> queryFriendList(String userid);
	
	/**
	 * 好友个数
	 * @param userid
	 * @return
	 */
	public long getUserFriendSize(String userid);
	
	/**
	 * 验证是否可以添加好友
	 * @param userid
	 * @return
	 */
	public boolean checkAdd(String userid);

}
