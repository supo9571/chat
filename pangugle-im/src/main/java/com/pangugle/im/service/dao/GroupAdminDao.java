package com.pangugle.im.service.dao;

import java.util.List;

public interface GroupAdminDao {
	
	public void addAdmin(String groupid, String username);
	public void deleteById(String groupid, String username);
	public List<String>queryAll(String groupid);

}
