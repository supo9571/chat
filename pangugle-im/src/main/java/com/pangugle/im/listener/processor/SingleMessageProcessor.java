package com.pangugle.im.listener.processor;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.logical.MessageManager;
import com.pangugle.im.model.MessageEvent;

public class SingleMessageProcessor implements MessageProcessor{

	@Override
	public void process(MessageBody body) {
		MyProtocol protocol = body.getProtocol();
		body.setToUserid(protocol.getTargetid());
		MessageManager.getIntance().sendMessage(body);
	}

	@Override
	public String getKey() {
		return MessageEvent.SINGLE.getName();
	}

}
