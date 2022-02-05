package com.pangugle.im.service;

import java.util.List;

import com.pangugle.im.model.Group;

public interface GroupService {
	
	public String createGroup(String holder, String alias, String icons, List<String> memberList);
	public List<String> deleteGroup(Group group);
	
	public void updateGroupMember(Group group, List<String> memberList, boolean isAdd);
	public void updateName(String id, String name);
	public void updateNotice(String id, String notice);
	public void updateStatus(String id, boolean enableStatus);
	public void updateInviteStatus(String id, boolean invite);
	public void updateAddFriendStatus(String id, boolean enableAddFriend);
	public void updateEnableChat(String id, boolean enableChat);
	public void updateIconsAndAlias(Group group, String icons, String alias);
	public void transferHolder(String id, String newHolder);
	
	public Group findById(String id);
	
//	public List<Group> queryAllByHolder(String holder);

}
