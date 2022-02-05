package   com.pangugle.passport.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.pangugle.framework.spring.utils.ServerUtils;
import com.pangugle.framework.utils.StringUtils;

public class UserInfo {
	
	private static final String DEFAULT_AVATAR = "/static/passport/img/pg_def_avatar.png";
	
	/*
	  user_id         int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
	  user_name       varchar(50) NOT NULL ,
	  user_phone     varchar(30) DEFAULT NULL ,
	  user_email       varchar(50) DEFAULT NULL ,
	  user_nickname   varchar(20) DEFAULT '' comment '用户昵称' ,
	  user_createtime datetime NOT NULL ,
	  user_avatar     varchar(255) DEFAULT '',
	  user_registerpath varchar(15) DEFAULT '' comment 'wx|ios|android|pc',
	  user_registerip varchar(15) NOT NULL ,
	  user_lastloginip  varchar(15) DEFAULT '' comment '最后登录IP',
	  user_lastlogintime  datetime DEFAULT NULL comment '最后登录时间',
	  user_status     varchar(10) NOT NULL comment 'enable|disabled|freeze-冻结',
	  user_type       varchar(10) NOT NULL comment 'staff-员工|member-会员',*/
	
	private String sex;
	private String name;
	private String phone;
	private String email;
	private String nickname;
	private Date createtime;
	private String avatar = StringUtils.getEmpty();
	private String registerpath;
	private String registerip;
	private String lastloginip;
	private Date lastlogintime;
	private boolean enableStatus;
	
	// groupname
	private String groupName;
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public static String getColumnPrefix(){
        return "user";
    }
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JSONField(serialize = false)
	public String getShowName()
	{
		if(!StringUtils.isEmpty(nickname))
		{
			return nickname;
		}
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		if(StringUtils.isEmpty(email)) {
			return StringUtils.getEmpty();
		}
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public String getAvatar() {
		if(!StringUtils.isEmpty(avatar))
		{
			return avatar;
		}
		return DEFAULT_AVATAR;
	}
	
	@JSONField(serialize = false)
	public String getShowAvatar()
	{
		String rs = getAvatar();
		return getAbsoluteAvatar(rs);
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRegisterpath() {
		return registerpath;
	}

	public void setRegisterpath(String registerpath) {
		this.registerpath = registerpath;
	}

	public String getRegisterip() {
		return registerip;
	}

	public void setRegisterip(String registerip) {
		this.registerip = registerip;
	}


	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public boolean isEnableStatus() {
		return enableStatus;
	}

	public void setEnableStatus(boolean enableStatus) {
		this.enableStatus = enableStatus;
	}
	
	public static String getAbsoluteAvatar(String avatar)
	{
		if(StringUtils.isEmpty(avatar))
		{
			return ServerUtils.getStaticServer() + DEFAULT_AVATAR;
		}
		else if(DEFAULT_AVATAR.equalsIgnoreCase(avatar))
		{
			return ServerUtils.getStaticServer()  +  DEFAULT_AVATAR;
		}
		else
		{
			return ServerUtils.getUploadServer()+ "/uploads" + avatar;
		}
	}
	
	public static UserSex getSex(String key)
	{
		if(UserSex.MAN.getKey().equalsIgnoreCase(key))
		{
			return UserSex.MAN;
		}
		
		if(UserSex.GIRL.getKey().equalsIgnoreCase(key))
		{
			return UserSex.GIRL;
		}
		
		return  UserSex.SECRET;
	}
	
	public static enum UserSex {
		MAN("man", "男"),
		GIRL("girl", "女"),
		SECRET("secret", "保密"),
		;
		
		private String key;
		private String name;
		
		private UserSex(String key, String name)
		{
			this.key = key;
			this.name = name;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public String getName()
		{
			return name;
		}
	}
	
	
}
