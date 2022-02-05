package   com.pangugle.passport.service;

import java.util.Date;
import java.util.List;

import   com.pangugle.passport.model.UserInfo;
import   com.pangugle.passport.model.UserInfo.UserSex;
import   com.pangugle.passport.model.UserSecret;

public interface UserService {
	

	
	public void addUserByName(String username, String password, String nickname, String registerip);
	public void addUserByPhone(String phone, String password, String nickname, String registerip);
	public void addUserByEmail(String email, String password, String nickname, String registerip);
	
	public UserInfo findByUsername(String username);
	public UserSecret findSecret(String username);
	public String findNameByEmail(String email);
	public String findNameByPhone(String phone);
	
	public void updatePhone(String username, String phone);
	public void updateEmail(String username, String email);
	
	public void updateStatus(String username, boolean status);
	public void updateSex(String username, UserSex sex);
	public void updateNickname(String username, String nickname);
	public void updateAvatar(String username, String avatar);
	public void updatePassword(String username, String password);
	
	public long count(String username,Date datemin, Date datemax);
	public List<UserInfo> queryScroll(String username,Date datemin, Date datemax, long offset, long size);

	public void deleteUserInfoCache(String username);
	
}
