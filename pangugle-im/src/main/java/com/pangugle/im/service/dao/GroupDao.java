package com.pangugle.im.service.dao;

import java.util.List;

import com.pangugle.im.model.Group;

public interface GroupDao {
	
	public void addGroup(String id, String holder, String alias, String icons, long currentCapacity, long maxCapacity);
	
	public void deleteGroup(String id);
	
	public void updateName(String id, String name);
	public void updateAlias(String id, String alias);
	public void updateIcons(String id, String icons);
	public void updateInviteStatus(String id, boolean invite);
	public void updateAddFriendStatus(String id, boolean enableAddFriend);
	public void updateEnableChat(String id, boolean enableChat);
	public void updateNotice(String id, String notice);
	public void updateStatus(String id, boolean enableStatus);
	public void updateCapacity(String id, long capacity);
	public void transferHolder(String id, String newHolder);
	
	public Group findById(String id);
	
	
	public List<Group> queryAllByHolder(String holder);

}
