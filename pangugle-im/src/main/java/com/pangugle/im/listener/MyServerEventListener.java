package com.pangugle.im.listener;

import com.pangugle.framework.context.MyEnvironment;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.im.listener.processor.MessageProcessor;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.model.BasicMsgType;

public abstract class MyServerEventListener extends DefaultServerEventListener {

	protected static Log LOG = LogFactory.getLog(MyServerEventListener.class);

	//private HistoryManager mHistoryManager = HistoryManager.getIntance();
	private AecManager mAecManager = AecManager.getInstance();
	
	@Override
	public String onVerifyUserLogin(String accessToken, String ip, String extra) {
		if(mAecManager.getAuthAec().verifyAccessToken(accessToken))
		{
			return mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		}
		return null;
	}

	/**
	 * client to server message callback
	 * 
	 * @param body
	 */
	public void onTransBuffer_C2S_CallBack(MessageBody body) {
		if(MyEnvironment.isDev() || "test02".equalsIgnoreCase(body.getProtocol().getFromUserid()) || "test03".equalsIgnoreCase(body.getProtocol().getFromUserid()))
		{
			LOG.info("receive C2C message for " + FastJsonHelper.jsonEncode(body.getProtocol()));
		}
		
		// 记录消息
		// mHistoryManager.recordMsg(body);

		MyProtocol protocol = body.getProtocol();
		
		if(!BasicMsgType.support(protocol.getMsgType()))
		{
			// 非法消息, 改造成过滤器
			return;
		}
		
		String key = protocol.getEvent();
		MessageProcessor processor = DispatchManager.getInstanced().getProcessor(key);
		if(processor != null)
		{
			processor.process(body);
		}
			

	}

//	@Override
//	public String getFromPlatform(String accessToken) {
//		if(mAecManager.getPCTokenAecSupport().isPCToken(accessToken))
//		{
//			return "pc";
//		}
//		return "app";
//	}


}
