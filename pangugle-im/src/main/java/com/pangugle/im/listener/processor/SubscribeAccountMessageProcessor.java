package com.pangugle.im.listener.processor;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.im.model.MessageEvent;

/**
 * 订阅号处理器
 * @author Administrator
 *
 */
public class SubscribeAccountMessageProcessor implements MessageProcessor{

	@Override
	public void process(MessageBody body) {
	}

	@Override
	public String getKey() {
		return MessageEvent.SUBSCRIBE_ACCOUNT.getName();
	}

}
