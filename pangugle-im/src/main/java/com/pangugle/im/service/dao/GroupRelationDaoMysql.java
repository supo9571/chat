package com.pangugle.im.service.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.pangugle.framework.db.jdbc.util.DBUtil;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupRelation;

@Repository
public class GroupRelationDaoMysql extends DaoSupport implements GroupRelationDao{
	
	public static String TABLE = "pangugle_im_user_group_relation";
	
	@Override
	public void bindRelation(String groupid, List<String> usernameArray)
	{
		java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String sql = "insert into " + TABLE + "(relation_groupid, relation_username, relation_createtime) values(?, ?, ?)";
			conn = mWriterJdbcService.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(String username : usernameArray)
			{
				ps.setString(1, groupid);
				ps.setString(2, username);
				ps.setDate(3, sqlDate);
				
				ps.addBatch();
			}
			
			ps.executeBatch();
		} catch (SQLException e) {
			LOG.error("batch sql error:", e);
		} finally {
			DBUtil.closeStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
	@Override
	public void unBind(String groupid, List<String> usernameArray)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			String sql = "delete from " + TABLE + " where relation_groupid = ? and relation_username = ?";
			conn = mWriterJdbcService.getConnection();
			ps = conn.prepareStatement(sql);
			
			for(String username : usernameArray)
			{
				ps.setString(1, groupid);
				ps.setString(2, username);
				
				ps.addBatch();
			}
			
			ps.executeBatch();
		} catch (SQLException e) {
			LOG.error("unBind batch sql error:", e);
		} finally {
			DBUtil.closeStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
	@Override
	public void updateNickname(String groupid, String username, String nickname)
	{
		String sql = "update " + TABLE + " set relation_nickname = ? where relation_groupid = ? and relation_username = ?";
		mWriterJdbcService.executeUpdate(sql, nickname, groupid, username);
	}
	@Override
	public GroupRelation find(String groupid, String username)
	{
		String sql = "select * from " + TABLE + " where relation_groupid = ? and relation_username = ? ";
		return mSlaveJdbcService.queryForObject(sql, GroupRelation.class, groupid, username);
	}
	
	/**
	 * 查询用户加入的群
	 */
	@Override
	public List<Group> queryUserJoinGroup(String username)
	{
		String sql = "select B.* from " + TABLE + " as A " +
							"inner join pangugle_im_user_group as B on B.group_id = A.relation_groupid " + 
							"where A.relation_username = ? ";
		return mSlaveJdbcService.queryForList(sql, Group.class, username);
	}
	
	/**
	 * 根据群id查询群所有成员
	 * @param groupid
	 * @return
	 */
	@Override
	public List<String> queryGroupMemberList(String groupid)
	{
		String sql = "select relation_username from " + TABLE + " where relation_groupid = ?";
		return mSlaveJdbcService.queryForList(sql, String.class, groupid);
	}
	
	@Override
	public void queryAllGroupMember(String groupid, Callback<String> callback)
	{
		String sql = "select relation_username from " + TABLE + " where relation_groupid = ?";
		mSlaveJdbcService.queryAll(callback, sql, String.class, groupid);
	}
	
}
