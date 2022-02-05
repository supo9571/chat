package com.pangugle.im.listener.processor;

import com.pangugle.framework.service.Callback;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.socketio.utils.MessageBodyFactory;
import com.pangugle.im.logical.GroupManager;
import com.pangugle.im.logical.MessageManager;
import com.pangugle.im.model.MessageEvent;

public class GroupMessageProcessor implements MessageProcessor{

	@Override
	public void process(MessageBody body) {
		MyProtocol protocol = body.getProtocol();
		String groupid = protocol.getTargetid();
		GroupManager.getIntance().queryAllMembers(groupid, new Callback<String>() {
			@Override
			public void execute(String toUserid) {
				// 过虑自己
				if(toUserid.equalsIgnoreCase(protocol.getFromUserid())) {
					return;
				}
				
				// 目标人
				MyProtocol targetProtocal = protocol.copy();
				targetProtocal.setTargetid(groupid);
				
				// 发送
				MessageBody targetBody = MessageBodyFactory.createBody(targetProtocal);
				targetBody.setToUserid(toUserid);
				MessageManager.getIntance().sendMessage(targetBody);
			}
		});
	}

	@Override
	public String getKey() {
		return MessageEvent.GROUP.getName();
	}

}
