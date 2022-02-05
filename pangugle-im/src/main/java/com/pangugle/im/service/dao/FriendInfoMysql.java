package com.pangugle.im.service.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.im.model.FriendInfo;

/**
 * 
 * @author Administrator
 *
 */
@Repository
public class FriendInfoMysql extends DaoSupport implements FriendInfoDao{
	
	private static final String TABLE = "pangugle_im_user_friend_info";
	
	public void add(String userid, String friendid, String alias, String mobile, String remark)
	{
		Date date = new Date();
		
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		keyvalue.put("info_userid", userid);
		keyvalue.put("info_friendid", friendid);
		keyvalue.put("info_black", false);
		keyvalue.put("info_alias", alias);
		keyvalue.put("info_mobile", mobile);
		keyvalue.put("info_createtime", date);
		keyvalue.put("info_remark", remark);
		
		persistent(TABLE, keyvalue);
	}
	
	public void black(String userid, String friendid, boolean black)
	{
		String sql = "update " + TABLE + " set info_black = ? where info_userid = ? and info_friendid = ?";
		mWriterJdbcService.executeUpdate(sql, black, userid, friendid);
	}
	
	public void update(String userid, String friendid, String alias, String mobile, String remark)
	{
		String sql = "update " + TABLE + " set info_alias = ?, info_mobile = ?, info_remark = ? where info_userid = ? and info_friendid = ?";
		mWriterJdbcService.executeUpdate(sql, alias, mobile, remark, userid, friendid);
	}
	
	public FriendInfo find(String userid, String friendid)
	{
		String sql = "select * from " +TABLE + " where info_userid = ? and info_friendid = ?";
		return mSlaveJdbcService.queryForObject(sql, FriendInfo.class, userid, friendid);
	}
	
	public boolean getBlackSelfStatus(String selfUserid, String friendUserid)
	{
		String sql = "select count(1) from " + TABLE + " where info_friendid = ? and info_userid = ? and info_black = ?";
		return mSlaveJdbcService.count(sql, friendUserid, selfUserid, true) > 0;
	}
	
}
