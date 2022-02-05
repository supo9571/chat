package com.pangugle.im.model;

import java.util.Date;

/**
 * 群与群成员关系
 * @author Administrator
 *
 */
public class GroupRelation {
	
	private String groupid;
	private String username;
	/*** 群内昵称 ***/
	private String nickname;
	/*** 免打扰配置 ***/
	private boolean free = false;
	private Date createtime;
	
	
	public static String getColumnPrefix(){
        return "relation";
    }
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

}
