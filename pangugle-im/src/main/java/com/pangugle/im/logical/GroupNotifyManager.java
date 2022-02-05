package com.pangugle.im.logical;

import java.util.List;

import com.pangugle.framework.service.Callback;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.socketio.utils.MessageBodyFactory;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.im.MessageApi;
import com.pangugle.im.concurrent.GroupConcurrent;
import com.pangugle.im.model.GroupMsgType;
import com.pangugle.im.model.MessageEvent;


public class GroupNotifyManager {
	
	private boolean open = true;
	
	private interface MyInternal {
		public GroupNotifyManager mgr = new GroupNotifyManager();
	}
	
	private GroupNotifyManager()
	{
	}
	
	public static GroupNotifyManager getIntance()
	{
		return MyInternal.mgr;
	}
	
	public void notifyInviteMember(String groupid, String fromUserid)
	{
		GroupConcurrent.getInstance().execute(new Runnable() {
			public void run() {
				GroupManager.getIntance().queryAllMembers(groupid, new Callback<String>() {
					@Override
					public void execute(String toUserid) {
						if(fromUserid.equalsIgnoreCase(toUserid))
						{
							return;
						}
						send(fromUserid, groupid, toUserid, GroupMsgType.INVITE_MEMBER);
					}
				});
			}
		});
	}
	
	public void notifyRemoveMember(String groupid, String fromUserid, List<String> removeMemberList)
	{
		GroupConcurrent.getInstance().execute(new Runnable() {
			public void run() {
				// 发送给被移除成员
				if(!CollectionUtils.isEmpty(removeMemberList))
				{
					for(String toUserid : removeMemberList)
					{
						send(fromUserid, groupid, toUserid, GroupMsgType.REMOVE_MEMBER);
					}
				}
				
				// 发送给现在群里的成员
				GroupManager.getIntance().queryAllMembers(groupid, new Callback<String>() {
					@Override
					public void execute(String toUserid) {
						if(fromUserid.equalsIgnoreCase(toUserid))
						{
							return;
						}
						send(fromUserid, groupid, toUserid, GroupMsgType.REMOVE_MEMBER);
					}
				});
			}
		});
	}
	
	public void notifyGroupInfoUpdate(String groupid, String fromUserid)
	{
		GroupConcurrent.getInstance().execute(new Runnable() {
			public void run() {
				GroupManager.getIntance().queryAllMembers(groupid, new Callback<String>() {
					@Override
					public void execute(String toUserid) {
						if(fromUserid.equalsIgnoreCase(toUserid))
						{
							return;
						}
						send(fromUserid, groupid, toUserid, GroupMsgType.UPDATE_GROUP_INFO);
					}
				});
			}
		});
	}
	
	public void send(String fromUsername, String groupid, String toUserid , GroupMsgType msgType)
	{
		if(!open) {
			return;
		}
		MyProtocol protocol = MyProtocol.build(fromUsername, groupid, MessageEvent.GROUP_OPT.getName(), msgType.getName());
		MessageBody body = MessageBodyFactory.createBody(protocol);
		body.setToUserid(toUserid);
		MessageApi.getIntance().sendMessage(body);
	}

}
