package   com.pangugle.passport.service.dao;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.pangugle.framework.db.jdbc.util.SelectSQLBuilder;
import com.pangugle.framework.spring.DaoSupport;
import com.pangugle.framework.utils.StringUtils;
import   com.pangugle.passport.model.UserInfo;
import   com.pangugle.passport.model.UserInfo.UserSex;
import   com.pangugle.passport.model.UserSecret;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class UserDaoMysql extends DaoSupport implements UserDao {
	
	/*
	  user_id         int(11) UNSIGNED NOT NULL AUTO_INCREMENT ,
	  user_name       varchar(50) NOT NULL ,
	  user_phone     varchar(30) DEFAULT NULL ,
	  user_email       varchar(50) DEFAULT NULL ,
	  user_password   char(32) COLLATE utf8_bin NOT NULL ,
	  user_nickname   varchar(20) DEFAULT '' comment '用户昵称' ,
	  user_salt       char(32)  COLLATE utf8_bin NULL DEFAULT NULL ,
	  user_createtime datetime NOT NULL ,
	  user_avatar     varchar(255) DEFAULT '',
	  user_device varchar(255) DEFAULT '' comment 'wx|ios|android|pc',
	  user_registerpath varchar(15) DEFAULT '' comment '注册途径',
	  user_registerip varchar(15) NOT NULL ,
	  user_lastloginip  varchar(15) DEFAULT '' comment '最后登录IP',
	  user_lastlogintime  datetime DEFAULT NULL comment '最后登录时间',
	  user_enable_status     varchar(10) NOT NULL comment 'enable|disabled|freeze-冻结',
	  user_type       varchar(10) NOT NULL comment 'staff-员工|member-会员',*/
	
	@Override
	@Transactional
	public void addUser(String username, String password, String phone, String email, String salt, UserSex sex, String nickname, String registerpath, String registerip)
	{
		Date date = new Date();
		
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		keyvalue.put("user_name", username);
		if(!StringUtils.isEmpty(email)) {
			keyvalue.put("user_email", email);
		}
		if(!StringUtils.isEmpty(phone)) {
			keyvalue.put("user_phone", phone);
		}
		keyvalue.put("user_password", password);
		keyvalue.put("user_salt", salt);
		keyvalue.put("user_sex", sex.getKey());
		if(!StringUtils.isEmpty(nickname)) {
			keyvalue.put("user_nickname", nickname);
		}
		keyvalue.put("user_createtime", date);
		keyvalue.put("user_lastlogintime", date);
		if(!StringUtils.isEmpty(registerpath)) {
			keyvalue.put("user_registerpath", registerpath);
		}
		keyvalue.put("user_registerip", registerip);
		
		persistent("pangugle_user", keyvalue);
	}
	@Override
	public UserInfo findByUsername(String username)
	{
		String sql = "select * from pangugle_user as A where user_name = ?";
//		String sql = "select A.*, C.group_name as user_group_name from pangugle_user as A " + 
//							"left join pangugle_user_group_union as B on B.union_userid = A.user_id " + 
//							"left join pangugle_user_group as C on C.group_id = B.union_groupid " + 
//							"where A.user_name = ?";
		return mSlaveJdbcService.queryForObject(sql, UserInfo.class, username);
	}
	@Override
	public UserSecret findSecret(String username)
	{
		String sql = "select user_name, user_password, user_salt from pangugle_user as A where user_name = ?";
		return mSlaveJdbcService.queryForObject(sql, UserSecret.class, username);
	}
	@Override
	public String findNameByEmail(String email)
	{
		String sql = "select user_name from pangugle_user where user_email = ?";
		return mSlaveJdbcService.queryForObject(sql, String.class, email);
	}
	@Override
	public String findNameByPhone(String phone)
	{
		String sql = "select user_name from pangugle_user where user_phone = ?";
		return mSlaveJdbcService.queryForObject(sql, String.class, phone);
	}
	@Override
	@Transactional
	public void updateStaus(String username, boolean status)
	{
		String sql = "update pangugle_user set user_enable_status = ? where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, status, username);
	}
	@Override
	public void updateSex(String username, UserSex sex)
	{
		String sql = "update pangugle_user set user_sex = ? where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, sex.getKey(), username);
	}
	
	@Transactional
	@Override
	public void updateEmail(String username, String email)
	{
		String sql = "update pangugle_user set user_email = ? where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, email, username);
	}
	@Override
	public void updatePhone(String username, String phone)
	{
		String sql = "update pangugle_user set user_phone = ? where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, phone, username);
	}
	@Override
	@Transactional
	public void updateNickname(String username, String nickname)
	{
		String sql = "update pangugle_user set user_nickname = ? where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, nickname, username);
	}
	@Override
	@Transactional
	public void updateLastLoginIP(String username, String ip)
	{
		Date date = new Date();
		String sql = "update pangugle_user set user_lastloginip = ?, user_lastlogintime = ?  where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, ip, date, username);
	}
	
	@Override
	@Transactional
	public void updateAvatar(String username, String avatar)
	{
		Date date = new Date();
		String sql = "update pangugle_user set user_avatar = ?, user_lastlogintime = ?  where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, avatar, date, username);
	}
	
	@Override
	@Transactional
	public void updatePassword(String username, String password, String salt)
	{
		String sql = "update pangugle_user set user_password = ?, user_salt = ?  where user_name = ?";
		mWriterJdbcService.executeUpdate(sql, password, salt, username);
	}
	
	@Override
	public long count(String username,Date datemin, Date datemax)
	{
		LinkedHashMap<String, Object> keyvalue = Maps.newLinkedHashMap();
		if(!StringUtils.isEmpty(username))
		{
			keyvalue.put("user_name = ?", username);
		}
		if(datemin != null)
		{
			keyvalue.put("user_createtime >= ?", datemin);
		}
		if(datemin != null)
		{
			keyvalue.put("user_createtime < ?", datemax);
		}
		return count("pangugle_user", keyvalue);
	}
	@Override
	public List<UserInfo> queryScroll(String username, Date datemin, Date datemax, long offset, long size)
	{
		String sql = "select * from pangugle_user";
		
		
		LinkedHashMap<String, Object> whereKeyValue = Maps.newLinkedHashMap();
		if(datemin != null)
		{
			whereKeyValue.put("user_createtime >= ?", datemin);
		}
		if(datemax != null)
		{
			whereKeyValue.put("user_createtime < ? ", datemax);
		}
		if(!StringUtils.isEmpty(username))
		{
			whereKeyValue.put("user_name = ?", username);
		}
		
		return queryScrollBySQL(sql, UserInfo.class, whereKeyValue, offset, size);
	}
	
	public static void main(String[] args)
	{
		SelectSQLBuilder builder = SelectSQLBuilder.builder();
		builder.select("A.*", "C.group_name as user_group_name")
		.from("pangugle_user as A")
		.innerJoin("pangugle_user_group_union as B on B.union_userid = A.user_id")
		.innerJoin("pangugle_user_group as C on C.group_id = B.union_groupid")
		.where("");
		
		System.out.println(builder.toString());
	}

}
