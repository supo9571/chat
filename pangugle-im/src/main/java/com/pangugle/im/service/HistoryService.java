package com.pangugle.im.service;

import java.util.Date;
import java.util.List;

import com.pangugle.framework.service.Callback;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.model.History;

/**
 * 用户聊天历史信息
 * @author Administrator
 *
 */
public interface HistoryService {
	
	public void save(MyProtocol root);
	public long count(String fromUserid, String toUserid, Date mindate, Date maxdate);
	public List<History>queryScoll(String fromUserid, String toUserid, Date mindate, Date maxdate, long offset, long size);
	public void queryAll(Date mindate, Date maxdate, Callback<History> callback);
	public void delete(Date beforeTime);
}
