package com.pangugle.im;

import com.pangugle.framework.socketio.ServerEventManager;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.im.concurrent.SendConcurrent;
import com.pangugle.im.logical.MessageManager;

/**
 * 服务端服务发送消息接口
 * @author Administrator
 *
 */
public class MessageApi {
	
//	private MQSupport mq = MQManager.getInstance().getMQ();
	
	private interface MyInternal {
		public MessageApi mgr = new MessageApi();
	}
	
	private MessageApi()
	{
	}
	
	public static MessageApi getIntance()
	{
		return MyInternal.mgr;
	}
	
	public void sendMessage(MessageBody body)
	{
		SendConcurrent.getInstance().execute(new Runnable() {
			public void run() {
				MessageManager.getIntance().sendMessage(body);
			}
		});
		
//		if(body == null) return;
//		String bodyString = FastJsonHelper.jsonEncode(body);
//		// 先暂时使用topic, 后续改成queue
//		mq.sendQueue(MessageApiJob.QUEUE_NAME, bodyString);
	}
	
	public void kick(String userid, String accessToken)
	{
		ServerEventManager.getIntance().kick(userid, accessToken);
	}

}
