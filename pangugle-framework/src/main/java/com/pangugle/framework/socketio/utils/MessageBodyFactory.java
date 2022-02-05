package com.pangugle.framework.socketio.utils;

import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;

public class MessageBodyFactory {
	
	public static MessageBody createBody(MyProtocol protocol, boolean qos)
	{
		MessageBody messageBody = new MessageBody();
		messageBody.setProtocol(protocol);
		messageBody.setQos(qos);
		
		return messageBody;
	}
	
	public static MessageBody createBody(MyProtocol protocol)
	{
		MessageBody messageBody = new MessageBody();
		messageBody.setProtocol(protocol);
		messageBody.setQos(false);
		return messageBody;
	}

}
