package com.pangugle.im.service.dao;

import java.util.Date;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.FriendRelation.MyStatus;

/**
 * 
 * @author Administrator
 *
 */
@Repository
public class FriendRelationDaoMysql extends DaoSupport implements FriendRelationDao {
	
	private static String TABLE = "pangugle_im_user_friend_relation";
	
	@Override
	public void bindRelation(String fromUserid, String toUserid, String requstUserid, MyStatus fromStatus, MyStatus toStatus, String remark)
	{
		Date date = new Date();
		
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		keyvalue.put("relation_from_userid", fromUserid);
		keyvalue.put("relation_to_userid", toUserid);
		keyvalue.put("relation_request_userid", requstUserid);
		
		keyvalue.put("relation_from_status", fromStatus.getName());
		keyvalue.put("relation_to_status", toStatus.getName());
		
		keyvalue.put("relation_createtime", date);
		keyvalue.put("relation_updatetime", date);
		
		if(!StringUtils.isEmpty(remark)) {
			keyvalue.put("relation_remark", remark);
		}
		
		persistent(TABLE, keyvalue);
	}
	
	/**
	 * 删除关系
	 * @param fromUserid
	 * @param toUserid
	 */
	@Override
	public void deleteRelation(String fromUserid, String toUserid)
	{
		String sql = "delete from " + TABLE + " where relation_from_userid = ? and relation_to_userid = ?";
		mWriterJdbcService.executeUpdate(sql, fromUserid, toUserid);
	}
	
	/**
	 * 更新好友关系状态
	 * @param fromUserid
	 * @param toUserid
	 * @param status
	 */
	@Override
	@Transactional
	public void updateFromStatus(String fromUserid, String toUserid, MyStatus status)
	{
		Date time = new Date();
		String sql = "update " + TABLE + " set relation_from_status = ?, relation_updatetime = ? where relation_from_userid = ? and relation_to_userid = ?";
		mWriterJdbcService.executeUpdate(sql, status.getName(), time, fromUserid, toUserid);
	}
	
	/**
	 * 更新好友关系状态
	 * @param fromUserid
	 * @param toUserid
	 * @param status
	 */
	@Override
	@Transactional
	public void updateToStatus(String fromUserid, String toUserid, MyStatus status)
	{
		Date time = new Date();
		String sql = "update " + TABLE + " set relation_to_status = ?, relation_updatetime = ? where relation_from_userid = ? and relation_to_userid = ?";
		mWriterJdbcService.executeUpdate(sql, status.getName(), time, fromUserid, toUserid);
	}
	
	@Override
	public FriendRelation find(String fromUserid, String toUserid)
	{
		String sql = "select * from " + TABLE + " where relation_from_userid = ? and relation_to_userid = ?";
		return mSlaveJdbcService.queryForObject(sql, FriendRelation.class, fromUserid, toUserid);
	}
	
	/**
	 * 根据用户查询指定类型关系列表
	 * @param userid
	 * @return
	 */
	public void queryStatusList(String userid, MyStatus status, Callback<String> callback)
	{
		// 自己在左边，好友右边
		String fromSql = "select relation_to_userid from " + TABLE + 
									" where relation_from_userid = ? and relation_from_status = ?";

		mSlaveJdbcService.queryAll(callback, fromSql, String.class, userid, status.getName());
		
		// 自己在右边，好友左边
		String toSql = "select relation_from_userid from " +  TABLE + 
								" where relation_to_userid = ? and relation_to_status = ?";
		mSlaveJdbcService.queryAll(callback, toSql, String.class, userid, status.getName());
	}
	
	/**
	 * 查询好友关系列表, 状态都为enable,
	 * @param userid
	 * @return
	 */
	@Override
	public void queryFriendList(String userid,  Callback<String> callback)
	{
		MyStatus status = MyStatus.enable;
		// 自己在左边，好友右边
		String fromSql = "select relation_to_userid from " + TABLE + 
									" where relation_from_userid = ? and relation_to_status = ? and relation_from_status = ? ";

		mSlaveJdbcService.queryAll(callback, fromSql, String.class, userid, status.getName(), status.getName());
		
		// 自己在右边，好友左边
		String toSql = "select relation_from_userid from " +  TABLE + 
								" where relation_to_userid = ? and relation_to_status = ? and relation_from_status = ? ";
		mSlaveJdbcService.queryAll(callback, toSql, String.class, userid, status.getName(), status.getName());
		
	}
	
	public long countFriend(String userid)
	{
		MyStatus status = MyStatus.enable;
		// 自己在左边，好友右边
		String fromSql = "select count(1) from " + TABLE + 
									" where relation_from_userid = ? and relation_to_status = ? and relation_from_status = ? ";
		
		long count = 0;
		count = mSlaveJdbcService.count(fromSql, userid, status.getName(), status.getName());
		
		// 自己在右边，好友左边
		String toSql = "select count(1) from " +  TABLE + 
								" where relation_to_userid = ? and relation_to_status = ? and relation_from_status = ? ";
		count += mSlaveJdbcService.count(toSql, userid, status.getName(), status.getName());
		
		return count;
	}
	

}
