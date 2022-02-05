package com.pangugle.im.service.dao;

import java.util.List;

import com.pangugle.framework.service.Callback;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupRelation;

public interface GroupRelationDao {
	
	public void bindRelation(String groupid, List<String> usernameArray);
	public void unBind(String groupid, List<String> usernameArray);
	
	public void updateNickname(String groupid, String username, String nickname);
	public GroupRelation find(String groupid, String username);

	/**
	 * 查询用户加入的群
	 */
	public List<Group> queryUserJoinGroup(String username);
	
	/**
	 * 根据群id查询群所有成员
	 * @param groupid
	 * @return
	 */
	public List<String> queryGroupMemberList(String groupid);
	
	public void queryAllGroupMember(String groupid, Callback<String> callback);
	
}
