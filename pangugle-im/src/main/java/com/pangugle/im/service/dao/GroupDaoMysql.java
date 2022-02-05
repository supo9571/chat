package com.pangugle.im.service.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.im.model.Group;

@Repository
public class GroupDaoMysql extends DaoSupport implements GroupDao{
	
	public static String TABLE = "pangugle_im_user_group";
	
	@Override
	public void addGroup(String id, String holder, String alias, String icons, long currentCapacity, long maxCapacity)
	{
		Date date = new Date();
		
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		keyvalue.put("group_id", id);
		keyvalue.put("group_holder", holder);
		keyvalue.put("group_alias", alias);
		keyvalue.put("group_icons", icons);
		keyvalue.put("group_current_capacity", currentCapacity);
		keyvalue.put("group_max_capacity", maxCapacity);
		keyvalue.put("group_createtime", date);
		
		persistent(TABLE, keyvalue);
	}
	
	@Override
	public void deleteGroup(String id)
	{
		String sql = "delete from " + TABLE + " where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, id);
	}
	
	@Override
	public void updateName(String id, String name)
	{
		String sql = "update " + TABLE + " set group_name = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, name, id);
	}
	public void updateAlias(String id, String alias)
	{
		String sql = "update " + TABLE + " set group_alias = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, alias, id);
	}
	public void updateIcons(String id, String icons)
	{
		String sql = "update " + TABLE + " set group_icons = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, icons, id);
	}
	public void updateInviteStatus(String id, boolean invite)
	{
		String sql = "update " + TABLE + " set group_enable_invite = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, invite, id);
	}
	
	public void updateAddFriendStatus(String id, boolean enableAddFriend)
	{
		String sql = "update " + TABLE + " set group_enable_add_friend = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, enableAddFriend, id);
	}
	
	public void updateEnableChat(String id, boolean enableChat)
	{
		String sql = "update " + TABLE + " set group_enable_chat= ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, enableChat, id);
	}
	
	@Override
	public void updateNotice(String id, String notice)
	{
		String sql = "update " + TABLE + " set group_notice = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, notice, id);
	}
	@Override
	public void updateCapacity(String id, long capacity)
	{
		String sql = "update " + TABLE + " set group_current_capacity = group_current_capacity + ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, capacity, id);
	}
	@Override
	public void updateStatus(String id, boolean enableStatus)
	{
		String sql = "update " + TABLE + " set group_status = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, enableStatus, id);
	}
	
	/**
	 * 群转让给其他人
	 * @param id
	 * @param newHolder 新群主
	 */
	@Override
	public void transferHolder(String id, String newHolder)
	{
		String sql = "update " + TABLE + " set group_holder = ? where group_id = ?";
		mWriterJdbcService.executeUpdate(sql, newHolder, id);
	}
	
	@Override
	public Group findById(String id)
	{
		String sql = "select *  from " + TABLE + " where group_id = ?";
		return mSlaveJdbcService.queryForObject(sql, Group.class, id);
	}
	
	@Override
	public List<Group> queryAllByHolder(String holder)
	{
		String sql = "select *  from " + TABLE + " where group_holder = ?";
		return mSlaveJdbcService.queryForList(sql, Group.class, holder);
	}
	

}
