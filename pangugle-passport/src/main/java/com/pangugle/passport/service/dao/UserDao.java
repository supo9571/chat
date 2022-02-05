package   com.pangugle.passport.service.dao;

import java.util.Date;
import java.util.List;

import   com.pangugle.passport.model.UserInfo;
import   com.pangugle.passport.model.UserInfo.UserSex;
import   com.pangugle.passport.model.UserSecret;

public interface UserDao {

	public void addUser(String username, String password, String phone, String email, String salt, UserSex sex, String nickname, String registerpath, String registerip);
	
	/**
	 * 查询相关
	 * @param username
	 * @return
	 */
	public UserInfo findByUsername(String username);
	public UserSecret findSecret(String username);
	public String findNameByEmail(String email);
	public String findNameByPhone(String phone);
	
	/**
	 * 更新用户状态
	 * @param username
	 * @param status
	 */
	public void updateStaus(String username, boolean status);
	public void updateSex(String username, UserSex sex);
	public void updateEmail(String username, String email);
	public void updatePhone(String username, String phone);
	public void updateNickname(String username, String nickname);
	public void updateLastLoginIP(String username, String ip);
	public void updateAvatar(String username, String avatar);
	public void updatePassword(String username, String password, String salt);
	
	public long count(String username,Date datemin, Date datemax);
	public List<UserInfo> queryScroll(String username,Date datemin, Date datemax, long offset, long size);
	
}
