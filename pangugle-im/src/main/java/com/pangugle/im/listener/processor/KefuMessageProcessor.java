package com.pangugle.im.listener.processor;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.logical.MessageManager;
import com.pangugle.im.model.MessageEvent;

public class KefuMessageProcessor implements MessageProcessor{

	@Override
	public void process(MessageBody body) {
		// TODO Auto-generated method stub
		MyProtocol protocol = body.getProtocol();
		body.setToUserid(protocol.getTargetid());
		MessageManager.getIntance().sendMessage(body);
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return MessageEvent.KEFU.getName();
	}

}
