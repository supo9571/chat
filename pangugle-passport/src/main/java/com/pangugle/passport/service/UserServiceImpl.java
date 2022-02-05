package   com.pangugle.passport.service;

import java.util.Date;
import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.UUIDUtils;
import com.pangugle.passport.cache.UserInfoCacheKeyUtils;
import   com.pangugle.passport.model.UserInfo;
import   com.pangugle.passport.model.UserInfo.UserSex;
import   com.pangugle.passport.model.UserSecret;
import   com.pangugle.passport.service.dao.UserDao;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao mUserDao;

	private CacheManager mCache = CacheManager.getInstance();

	@Override
	@Transactional
	public void addUserByName(String username, String password, String nickname, String registerip)
	{
		String salt = MD5.encode(password + registerip + System.currentTimeMillis());
		String encryPwd = UserSecret.encryPassword(username, password, salt);
		mUserDao.addUser(username, encryPwd, null, null, salt, UserSex.SECRET, nickname, null, registerip);
	}
	
	@Override
	@Transactional
	public void addUserByPhone(String phone, String password, String nickname, String registerip)
	{
		String username = UUIDUtils.getUUID();
		String salt = MD5.encode(password + registerip + System.currentTimeMillis());
		String encryPwd = UserSecret.encryPassword(username, password, salt);
		mUserDao.addUser(username, encryPwd, phone, null, salt, UserSex.SECRET, nickname, null, registerip);
	}
	
	@Override
	@Transactional
	public void addUserByEmail(String email, String password, String nickname, String registerip)
	{
		String username = UUIDUtils.getUUID();
		String salt = MD5.encode(password + registerip + System.currentTimeMillis());
		String encryPwd = UserSecret.encryPassword(username, password, salt);
		mUserDao.addUser(username, encryPwd, null, email, salt, UserSex.SECRET, nickname, null, registerip);
	}
	
	@Override
	public UserInfo findByUsername(String username)
	{
		String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		UserInfo userInfo = mCache.getObject(cachekey, UserInfo.class);
		if(userInfo == null)
		{
			userInfo = mUserDao.findByUsername(username);
			if(userInfo != null)
			{
				mCache.setString(cachekey, FastJsonHelper.jsonEncode(userInfo));
			}
		}
		return userInfo;
	}
	
	@Override
	public UserSecret findSecret(String username)
	{
		return mUserDao.findSecret(username);
	}
	
	@Override
	public String findNameByEmail(String email)
	{
		return mUserDao.findNameByEmail(email);
	}
	
	@Override
	public String findNameByPhone(String phone)
	{
		return mUserDao.findNameByPhone(phone);
	}
	
	@Override
	@Transactional
	public void updatePhone(String username, String phone)
	{
		mUserDao.updatePhone(username, phone);
		String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		 mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updateEmail(String username, String email)
	{
		 mUserDao.updateEmail(username, email);
		 String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		 mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updateStatus(String username, boolean status)
	{
		mUserDao.updateStaus(username, status);
		 String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		 mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updateSex(String username, UserSex sex)
	{
			mUserDao.updateSex(username, sex);
			String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
			mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updateNickname(String username, String nickname)
	{
			mUserDao.updateNickname(username, nickname);
			String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
			mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updateAvatar(String username, String avatar)
	{
		mUserDao.updateAvatar(username, avatar);
		String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		mCache.delete(cachekey);
	}
	
	@Override
	@Transactional
	public void updatePassword(String username, String password)
	{
		String salt = MD5.encode(username + System.currentTimeMillis());
		String encryPwd = UserSecret.encryPassword(username, password, salt);
		mUserDao.updatePassword(username, encryPwd, salt);
	}
	
	@Override
	public long count(String username,Date datemin, Date datemax)
	{
		return mUserDao.count(username, datemin, datemax);
	}
	
	@Override
	public List<UserInfo> queryScroll(String username,Date datemin, Date datemax, long startOffset, long endOffset)
	{
		List<UserInfo> list = mUserDao.queryScroll(username, datemin, datemax, startOffset, endOffset);
		if(list == null)
		{
			list = Lists.emptyList();
		}
		return list;
	}
	
	@Override
	public void deleteUserInfoCache(String username)
	{
		String cachekey = UserInfoCacheKeyUtils.createUserInfoKey(username);
		 mCache.delete(cachekey);
	}
	

}
