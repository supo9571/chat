package com.pangugle.im.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangugle.framework.service.Callback;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.model.History;
import com.pangugle.im.service.dao.HistoryDao;

@Service
public class HistoryServiceImpl implements HistoryService {

	@Autowired
	private HistoryDao mHistoryDao;

	@Transactional
	public void save(MyProtocol body) {
		try {
//			Date date = new Date(body.getTime());
//			mHistoryDao.save(body.getId(), body.getFromUserid(), body.getTargetid(), body.getTypeChat(), body.getTypeMsg(), body.getDataContent(), date);
		} catch (Exception e) {
			// 可能消息主键id冲突了
			// LOG.error("save history msg error:", e);
		}
	}
	
	public List<History>queryScoll(String fromUserid, String toUserid, Date mindate, Date maxdate, long offset, long size)
	{
		return mHistoryDao.queryScoll(fromUserid, toUserid, mindate, maxdate, offset, size);
	}
	
	public long count(String fromUserid, String toUserid, Date mindate, Date maxdate)
	{
		return mHistoryDao.count(fromUserid, toUserid, mindate, maxdate);
	}
	
	public void queryAll(Date mindate, Date maxdate, Callback<History> callback)
	{
		mHistoryDao.queryAll(mindate, maxdate, callback);
	}

	@Transactional
	public void delete(Date beforeTime) {
		mHistoryDao.delete(beforeTime);
	}
	

}
