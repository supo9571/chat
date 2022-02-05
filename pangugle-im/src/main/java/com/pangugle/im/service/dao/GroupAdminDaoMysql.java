package com.pangugle.im.service.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.pangugle.framework.spring.DaoSupport;

@Repository
public class GroupAdminDaoMysql extends DaoSupport implements GroupAdminDao{
	
	private static final String TABLE = "pangugle_im_user_group_admin";
	
	public void addAdmin(String groupid, String username)
	{
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		keyvalue.put("admin_groupid", groupid);
		keyvalue.put("admin_username", username);
		
		persistent(TABLE, keyvalue);
	}
	
	public void deleteById(String groupid, String username)
	{
		String sql = "delete from " + TABLE + " where admin_groupid = ? and admin_username = ? ";
		mWriterJdbcService.executeUpdate(sql, groupid, username);
	}
	
	public List<String>queryAll(String groupid)
	{
		String sql = "select admin_username from " + TABLE + "  where admin_groupid = ?";
		return mSlaveJdbcService.queryForList(sql, String.class, groupid);
	}
	
}
