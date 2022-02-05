package com.pangugle.im.logical;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.socketio.utils.MessageBodyFactory;
import com.pangugle.im.MessageApi;
import com.pangugle.im.model.FriendMsgType;

public class FriendNotifyManager {
	
	private boolean open = true;
	
	private static String EVENT_TYPE = "friend";
	
	private interface MyInternal {
		public FriendNotifyManager mgr = new FriendNotifyManager();
	}
	
	private FriendNotifyManager()
	{
	}
	
	public static FriendNotifyManager getIntance()
	{
		return MyInternal.mgr;
	}
	
	/**
	 * 
	 * @param fromUsername  添加人
	 * @param toUsername   被添加人
	 */
	public void sendUnconfirmNotification(String fromUsername, String toUsername)
	{
		send(fromUsername, toUsername, FriendMsgType.UNCONFIRM);
	}
	
	/**
	 * 
	 * @param fromUsername  添加人
	 * @param toUsername   被添加人
	 */
	public void sendEnableNotification(String fromUsername, String toUsername)
	{
		send(fromUsername, toUsername, FriendMsgType.ENABLE);
	}
	
	public void sendDeleteNotification(String fromUsername, String toUsername)
	{
		send(fromUsername, toUsername, FriendMsgType.DELETE);
	}
	
	public void sendBlackNotification(String fromUsername, String toUsername)
	{
		send(fromUsername, toUsername, FriendMsgType.BACK);
	}
	
	private void send(String fromUsername, String toUsername, FriendMsgType msgType)
	{
		if(!open) {
			return;
		}
		MyProtocol protocol = MyProtocol.build(fromUsername, toUsername, EVENT_TYPE, msgType.getName());
		MessageBody body = MessageBodyFactory.createBody(protocol);
		body.setToUserid(toUsername);
		MessageApi.getIntance().sendMessage(body);
	}

}
