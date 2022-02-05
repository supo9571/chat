package com.pangugle.im.listener.processor;

import com.pangugle.framework.socketio.model.MessageBody;

public interface MessageProcessor {
	
	public void process(MessageBody body);
	
	public String getKey();

}
