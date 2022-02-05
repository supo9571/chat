//package com.pangugle.im.service.dao;
//
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import com.google.common.collect.Maps;
//import com.pangugle.framework.spring.DaoSupport;
//import com.pangugle.framework.utils.StringUtils;
//import com.pangugle.im.model.GroupType;
//
///**
// * 
// * @author Administrator
// *
// */
//@Repository
//public class GroupTypeDaoMysql extends DaoSupport implements GroupTypeDao {
//	
//	public static String TABLE = "im_user_group_type";
//	
//	@Override
//	public void addConfig(String type, String remark, long maxCapacity)
//	{
//		Date date = new Date();
//		
//		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
//		keyvalue.put("type_name", type);
//		if(!StringUtils.isEmpty(remark)) {
//			keyvalue.put("type_remark", remark);
//		}
//		keyvalue.put("type_max_capacity", maxCapacity);
//		keyvalue.put("type_createtime", date);
//		
//		persistent(TABLE, keyvalue);
//	}
//	@Override
//	public List<GroupType> queryAll()
//	{
//		String sql = "select * from " + TABLE;
//		return mSlaveJdbcService.queryForList(sql, GroupType.class);
//	}
//	@Override
//	public GroupType findByType(String type)
//	{
//		String sql = "select * from " + TABLE + " where type_name = ?";
//		return mSlaveJdbcService.queryForObject(sql, GroupType.class, type);
//	}
//	@Override
//	public void updateRemark(String type, String remark)
//	{
//		String sql = "update " + TABLE + " set type_remark = ? where type_name = ?";
//		mWriterJdbcService.executeUpdate(sql, remark);
//	}
//	@Override
//	public void updateCapacity(String type, long capacity)
//	{
//		String sql = "update " + TABLE + " set type_max_capacity = ? where type_name = ?";
//		mWriterJdbcService.executeUpdate(sql, capacity);
//	}
//	
//
//}
