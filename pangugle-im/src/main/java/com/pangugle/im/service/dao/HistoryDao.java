package com.pangugle.im.service.dao;

import java.util.Date;
import java.util.List;

import com.pangugle.framework.service.Callback;
import com.pangugle.im.model.History;

public interface HistoryDao {
	
	public void save(String id, String fromUserid, String toUserid, long typeType, long typeMsg, String content, Date date);
	
	public long count(String fromUserid, String toUserid, Date mindate, Date maxdate);
	public List<History>queryScoll(String fromUserid, String toUserid, Date mindate, Date maxdate, long offset, long size);
	public void queryAll(Date mindate, Date maxdate, Callback<History> callback);
	public void delete(Date beforeTime);
}
