package com.pangugle.im.service;

import java.util.List;

public interface GroupAdminService {
	
	public void addAdmin(String groupid, String username);
	public void deleteById(String groupid, String username);
	public boolean existAdmin(String groupid, String username);
	public List<String>queryAll(String groupid);

}
