package com.pangugle.im.model;

import java.util.Date;

import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.logical.AecManager;

public class Group {
	
	/*
	 * 
	 *   group_id         				int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
  group_holder						varchar(50) NOT NULL COMMENT '群主',
  group_config_id				int(11) NOT NULL COMMENT '群组配置id => pangugle_user_group_config',
  group_name      			varchar(50) NOT NULL ,
  group_notice     			varchar(100) DEFAULT '' COMMENT '公告',
  group_current_capacity      	int(11)  NOT NULL  DEFAULT 1 COMMENT '当前群总人数',
  group_createtime 			datetime NOT NULL ,
  group_status     				varchar(10) NOT NULL DEFAULT 'enable' COMMENT 'enable | disabled',
	 */
	
	private String id;
	private String holder;
	/*** 群主设置的群名称 ***/
	private String name;
	/*** 群成员前三个的名称 ***/
	private String alias;
	private String icons;
	private String notice;
	private Date createtime;
	private boolean enableInvite;
	private boolean enableAddFriend;
	private boolean enableStatus;
	private boolean enableChat;
	private long currentCapacity;
	/*** 最大上限群成员人数 ***/
	private long maxCapacity;
	/*** 非数据库字段, 缓存标识位 ***/
	private boolean delete = false;
	
	public static String getColumnPrefix(){
        return "group";
    }
	
	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	public boolean isEnableAddFriend() {
		return enableAddFriend;
	}

	public void setEnableAddFriend(boolean enableAddFriend) {
		this.enableAddFriend = enableAddFriend;
	}
	
	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public boolean isEnableInvite() {
		return enableInvite;
	}

	public void setEnableInvite(boolean enableInvite) {
		this.enableInvite = enableInvite;
	}

	public boolean isEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(boolean enableStatus) {
		this.enableStatus = enableStatus;
	}
	
	public long getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(long currentCapacity) {
		this.currentCapacity = currentCapacity;
	}
	
	public long getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(long maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
	
	public boolean isEnableChat() {
		return enableChat;
	}

	public void setEnableChat(boolean enableChat) {
		this.enableChat = enableChat;
	}
	
	public void handleColumn()
	{
		// handle icon
		if(!StringUtils.isEmpty(icons))
		{
			char split = ',' ;
			String[] iconArray = StringUtils.split(icons, split);
			int len = iconArray.length;
			for(int i = 0; i < len; i ++)
			{
				String icon = iconArray[i];
				iconArray[i] = AecManager.getInstance().getUserAec().handleAvatar(icon);
			}
			this.icons = StringUtils.join(iconArray, split);
		}
	}
	
}
