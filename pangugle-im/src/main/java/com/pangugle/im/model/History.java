package com.pangugle.im.model;

import java.util.Date;

import com.pangugle.framework.utils.StringUtils;

public class History {

	/*** 消息唯一id ***/
	private String id;
	/***  为空表示发送给所有 ***/
	private String toUserid;
	/*** 为空表示系统发送或发送给系统 ***/
	private String fromUserid = StringUtils.getEmpty();
	//发送内容
	private String content;
	//聊天类型
	private long typeChat;
	//消息类型
	private long typeMsg;
	/*** 创建时间 ***/
	private Date createtime;
	/*** 0=未发送-就是离线消息, 1=已发送 ***/
	private long status;


	public static String getColumnPrefix(){
        return "history";
    }
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToUserid() {
		return toUserid;
	}

	public void setToUserid(String toUserid) {
		this.toUserid = toUserid;
	}

	public String getFromUserid() {
		return fromUserid;
	}

	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTypeChat() {
		return typeChat;
	}

	public void setTypeChat(long typeChat) {
		this.typeChat = typeChat;
	}

	public long getTypeMsg() {
		return typeMsg;
	}

	public void setTypeMsg(long typeMsg) {
		this.typeMsg = typeMsg;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}
	
	/**
	 * 消息发送状态
	 * @author Administrator
	 *
	 */
	public static enum Status {
		SEND(0),
		OFFLINE(1);
		private long id;
		private  Status(long id)
		{
			this.id = id;
		}
		
		public long getId()
		{
			return id;
		}
	}
	
	
}
