package com.pangugle.im.logical;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.utils.LocalSendUtils;
import com.pangugle.im.logical.push.PushManager;
import com.pangugle.im.utils.UserStatusUtils;

/**
 * 消息管理器, 其它模块不得调用
 * @author Administrator
 *
 */
public class MessageManager {
	
	
	private interface OfflineManagerInternal {
		public MessageManager mgr = new MessageManager();
	}
	
	private MessageManager()
	{
	}
	
	public static MessageManager getIntance()
	{
		return OfflineManagerInternal.mgr;
	}
	
	/**
	 * 发送消息
	 * @param body
	 */
	public void sendMessage(MessageBody body)
	{
		if(UserStatusUtils.checkOnline(body.getToUserid()))
		{
			LocalSendUtils.sendMessageToClient(body);
		}
		else
		{
			// 离线消息
			body.setOffline(true);
			OfflineManager.getIntance().addOfflineMessage(body.getProtocol());
			
			// 推送消息
			PushManager.getIntance().pushMessage(body.getProtocol());
		}
	}
	

}
