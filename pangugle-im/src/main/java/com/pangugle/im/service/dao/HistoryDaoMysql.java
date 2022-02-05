package com.pangugle.im.service.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.model.History;

@Repository
public class HistoryDaoMysql extends DaoSupport implements HistoryDao{
	
	private static String HISTORY_TABLE_NAME = "pangugle_im_history";
	
	public void save(String id, String fromUserid, String toUserid, long typeType, long typeMsg, String content, Date date)
	{
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		
		keyvalue.put("history_id", id);
		keyvalue.put("history_from_userid", fromUserid);
		keyvalue.put("history_to_userid", toUserid);
		keyvalue.put("history_type_chat", typeType);
		keyvalue.put("history_type_msg", typeMsg);
		keyvalue.put("history_content", content);
		keyvalue.put("history_createtime", date);
		
		persistent(HISTORY_TABLE_NAME, keyvalue);
	}
	
	public List<History>queryScoll(String fromUserid, String toUserid, Date mindate, Date maxdate, long offset, long size)
	{
		LinkedHashMap<String, Object> whereKeyValue = Maps.newLinkedHashMap();
		if(mindate != null)
		{
			whereKeyValue.put("history_createtime >= ?", mindate);
		}
		if(maxdate != null)
		{
			whereKeyValue.put("history_createtime < ?", maxdate);
		}
		if(!StringUtils.isEmpty(fromUserid))
		{
			whereKeyValue.put("history_from_userid = ?", fromUserid);
		}
		if(!StringUtils.isEmpty(fromUserid))
		{
			whereKeyValue.put("history_to_userid = ?", toUserid);
		}
		
		return queryScrollByName(HISTORY_TABLE_NAME, History.class, whereKeyValue, offset, size);
	}
	
	public long count(String fromUserid, String toUserid, Date mindate, Date maxdate)
	{
		LinkedHashMap<String, Object> whereKeyValue = Maps.newLinkedHashMap();
		if(mindate != null)
		{
			whereKeyValue.put("history_createtime >= ?", mindate);
		}
		if(maxdate != null)
		{
			whereKeyValue.put("history_createtime < ?", maxdate);
		}
		if(!StringUtils.isEmpty(fromUserid))
		{
			whereKeyValue.put("history_from_userid = ?", fromUserid);
		}
		if(!StringUtils.isEmpty(fromUserid))
		{
			whereKeyValue.put("history_to_userid = ?", toUserid);
		}
		
		return count(HISTORY_TABLE_NAME, whereKeyValue);
	}
	
	public void queryAll(Date mindate, Date maxdate, Callback<History> callback)
	{
		String sql = "select * from " + HISTORY_TABLE_NAME + " where history_createtime >= ? and history_createtime < ?";
		mSlaveJdbcService.queryAll(callback, sql, History.class, mindate, maxdate);
	}

	@Override
	public void delete(Date beforeTime) {
		String sql = "delete from " + HISTORY_TABLE_NAME + " where history_createtime <= ?";
		mWriterJdbcService.executeUpdate(sql, beforeTime);
	}
	

}
