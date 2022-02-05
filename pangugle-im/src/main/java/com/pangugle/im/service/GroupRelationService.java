package com.pangugle.im.service;

import java.util.List;

import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupRelation;

public interface GroupRelationService {
	
	public void bindRelation(String groupid, List<String> memberList);
	public void unBindRelation(String groupid, List<String> memberList);
	
	
	public List<String> unBindAllRelation(String groupid);
	
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
	public List<String> queryScrollGroupMember(String groupid);
	
}
