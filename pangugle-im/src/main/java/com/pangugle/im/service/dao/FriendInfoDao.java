package com.pangugle.im.service.dao;

import com.pangugle.im.model.FriendInfo;

public interface FriendInfoDao {
	
	public void add(String userid, String friendid, String alias, String mobile, String remark);
	public void black(String userid, String friendid, boolean black);
	public void update(String userid, String friendid, String alias, String mobile, String remark);
	public FriendInfo find(String userid, String friendid);
	public boolean getBlackSelfStatus(String selfUserid, String friendUserid);

}
