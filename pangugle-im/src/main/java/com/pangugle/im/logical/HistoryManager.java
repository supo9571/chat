package com.pangugle.im.logical;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.concurrent.HistoryConcurrent;
import com.pangugle.im.service.HistoryService;

/**
 * 历史消息管理器, 记录聊天记录
 * @author Administrator
 *
 */
public class HistoryManager {
	
	public static Log LOG = LogFactory.getLog(HistoryManager.class);
	
	private HistoryService mHistoryService;

	private interface MessageManagerInternal {
		public HistoryManager mgr = new HistoryManager();
	}
	
	private HistoryManager()
	{
	}
	
	public void setHistoryService(HistoryService service)
	{
		this.mHistoryService = service;
	}
	
	public static HistoryManager getIntance()
	{
		return MessageManagerInternal.mgr;
	}
	
	public void addMessage(MyProtocol body)
	{
		// 消息体，或
		if(body == null || mHistoryService == null) return;
		
		//
		HistoryConcurrent.getInstance().execute(new Runnable()
		{
			public void run() {
				mHistoryService.save(body);
			}
		});
	}

}
