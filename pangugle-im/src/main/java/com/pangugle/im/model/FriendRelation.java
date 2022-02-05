package com.pangugle.im.model;

import java.util.Date;

import com.pangugle.framework.utils.StringUtils;

public class FriendRelation {
	
	// 分页提取好友成员数据大小,一旦上线请勿修改，一旦修改请先清理所有缓存
//	public static final long DEFAULT_FRIEND_PAGE_SIZE = 100;
	// 最多5000个好友
	public static final long DEFAULT_MAX_FRIEND_SIZE = 200;
	// 最多分页提取好友页数
//	public static final long DEFAULT_TOTAL_PAGE = DEFAULT_MAX_FRIEND_SIZE / DEFAULT_FRIEND_PAGE_SIZE;
	
	private String fromUserid;
	private String toUserid;
	private String requestUserid;
	private String fromStatus;
	private String toStatus;
	private Date createtime;
	private Date updatetime;
	private String remark = StringUtils.getEmpty();
	
	public String getRequestUserid() {
		return requestUserid;
	}

	public void setRequestUserid(String requestUserid) {
		this.requestUserid = requestUserid;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static String getColumnPrefix(){
        return "relation";
    }
	
	public String getFromUserid() {
		return fromUserid;
	}
	public void setFromUserid(String fromUserid) {
		this.fromUserid = fromUserid;
	}
	public String getToUserid() {
		return toUserid;
	}
	public void setToUserid(String toUserid) {
		this.toUserid = toUserid;
	}
	public String getFromStatus() {
		return fromStatus;
	}

	public void setFromStatus(String fromStatus) {
		this.fromStatus = fromStatus;
	}

	public String getToStatus() {
		return toStatus;
	}

	public void setToStatus(String toStatus) {
		this.toStatus = toStatus;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	public boolean verifyFriend()
	{
		if(MyStatus.enable.getName().equalsIgnoreCase(fromStatus) && MyStatus.enable.getName().equalsIgnoreCase(toStatus))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 好友关系状态
	 * @author Administrator
	 *
	 */
	public static enum MyStatus {
		unconfirm("unconfirm", "待确认"),
		enable("enable", "确认-为好友"),
		;
		
		
		private String name;
		private String remark;
		
		private MyStatus(String name, String remark)
		{
			this.remark = remark;
			this.name = name;
		}
		
		public String getRemark()
		{
			return remark;
		}
		
		public String getName()
		{
			return name;
		}
	}

}
