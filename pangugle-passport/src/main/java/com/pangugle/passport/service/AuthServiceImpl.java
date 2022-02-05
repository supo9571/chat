package   com.pangugle.passport.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.passport.cache.UserCache;
import com.pangugle.passport.cache.UserInfoCacheKeyUtils;
import com.pangugle.passport.limit.InvalidLoginTokenException;
import com.pangugle.passport.logical.PCTokenManager;
import   com.pangugle.passport.model.UserSecret;

@Service
public class AuthServiceImpl implements AuthService {

    private static Log LOG = LogFactory.getLog(AuthServiceImpl.class);

    private static final int DEFAULT_LOGIN_TOKEN_EXPIRES_IN = 86400 * 30; // 30天
    // access token key, 一定要16位
//    private static final String DEFAULT_ACCESS_TOKEN_KEY = ";oqj31Gttn^/.jhf";
    // access token 有效期 1小时
    private static final int DEFAULT_ACCESS_TOKEN_EXPIRES_IN = 7200;
    
    private static final String KEY_TIME = "time";
//    private static final String KEY_SIGN = "sign";
    private static final String KEY_TOKEN = "token";
    
    private static final String DEFAULT_SALT = "sfdasdfj&67不错是乐山大佛";
    
    private UserCache mCache = UserCache.getIntance();
    
    @Autowired
    private UserService mUserService;
    
    @Override
    public String createLoginTokenByAccount(String username, String password) {
    	long expires_in = DEFAULT_LOGIN_TOKEN_EXPIRES_IN;
        long time = System.currentTimeMillis();
        String loginToken = generateLoginToken(username, password, time, expires_in);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(KEY_TOKEN, loginToken);
        map.put(KEY_TIME, time);
        String json = FastJsonHelper.jsonEncode(map);
        
        // 30天+60s
        int expires = DEFAULT_LOGIN_TOKEN_EXPIRES_IN + 60;
        
    	String userCachekey = UserInfoCacheKeyUtils.createLoginTokenToUsernameKey(loginToken);
    	mCache.setString(userCachekey, username, expires);
    	
    	String signCacheKey = UserInfoCacheKeyUtils.createLoginTokenToSignKey(username);
    	// 提前60秒失效,防止验证通过后失效
    	mCache.setString(signCacheKey, json, expires);
    	
       return loginToken;
    }

    @Override
    public boolean verifyLoginToken(String loginToken) {
    	try {
			// 获取用户名
			String username = getAccountByLoginToken(loginToken);
			if(StringUtils.isEmpty(username)) {
				//LOG.error("=====================getAccountByLoginToken InvalidLoginTokenException ========");
				return false;
			}
			
			// 获取加密信息
			String singCacheKey = UserInfoCacheKeyUtils.createLoginTokenToSignKey(username);
			String signValue = mCache.getString(singCacheKey);
			if(StringUtils.isEmpty(signValue))
			{
				//LOG.error("=====================createLoginTokenToSignKey empty InvalidLoginTokenException ========");
				return false;
			}
			
			JSONObject json = FastJsonHelper.toJSONObject(signValue);
			if(json == null || json.isEmpty()) {
				//LOG.error("=====================createLoginTokenToSignKey empty json InvalidLoginTokenException ========");
				return false;
			}
			
			long expire_in = DEFAULT_LOGIN_TOKEN_EXPIRES_IN;
			long time = json.getLong(KEY_TIME);
			String tmpLoginToken = json.getString(KEY_TOKEN);
			
			// 验证loginToken, 单点登陆
			if(!tmpLoginToken.equalsIgnoreCase(loginToken))
			{
				//LOG.error("=====================tmpLoginToken !=  loginToken, InvalidLoginTokenException ========");
				return false;
			}
			
			UserSecret secret = mUserService.findSecret(username);
			if(secret == null) {
				//LOG.error("=====================secret is empty for system error, InvalidLoginTokenException ========");
				return false;
			}
			
			// 验证密码
			tmpLoginToken = generateLoginToken(username, secret.getPassword(), time, expire_in);
			if (!loginToken.equalsIgnoreCase(tmpLoginToken)) {
				//LOG.info("=====================verifyLoginToken InvalidLoginTokenException ========");
				return false;
			}
			
			long seconds = (System.currentTimeMillis() - time) / 1000;
			if(seconds > expire_in)
			{
//				LOG.info("=====================expires login token, InvalidLoginTokenException ========");
				return false;
			}
			return true;
		} catch (Exception e) {
			LOG.error("handle verify loginToken error:", e);
		}
    	return false;
    }

    public String getUsernameByLoginToken(String loginToken) {
        String userkey = null;
        try {
        	String cacheKey = UserInfoCacheKeyUtils.createLoginTokenToUsernameKey(loginToken);
        	userkey = mCache.getString(cacheKey);
        } catch (Exception e) {
            LOG.error("getUsernameByLoginToken error:", e);
        }
        return userkey;
    }


    @Override
    public String refreshAccessToken(String loginToken) {
    	 String username = getUsernameByLoginToken(loginToken);
         long time = System.currentTimeMillis();
         String accessToken = generateAccessToken(username, time, DEFAULT_ACCESS_TOKEN_EXPIRES_IN);
         
         Map<String, Object> dataMap = Maps.newHashMap();
         dataMap.put(KEY_TOKEN, accessToken);
         dataMap.put(KEY_TIME, time);
         String json = FastJsonHelper.jsonEncode(dataMap);

         try {
         	
         	String userCacheKey = UserInfoCacheKeyUtils.createAccessTokenToUsernameKey(accessToken);
         	mCache.setString(userCacheKey, username, DEFAULT_ACCESS_TOKEN_EXPIRES_IN);
         	
         	String signCacheKey = UserInfoCacheKeyUtils.createAccessTokenToSignKey(username);
         	mCache.setString(signCacheKey, json, DEFAULT_ACCESS_TOKEN_EXPIRES_IN );
         	
            return accessToken;
         } catch (Exception e) {
             LOG.error("createAcessToken error:", e);
             throw new RuntimeException();
         }
    }
    
    public int getAccessTokenExpires()
    {
    	return DEFAULT_ACCESS_TOKEN_EXPIRES_IN;
    }

    @Override
    public boolean verifyAccessToken(String accessToken) {
    	// pc 端，设计上应该隔离, 这种写法是侵入性代码，谨慎使用
    	PCTokenManager pcTokenMgr = PCTokenManager.getInstance();
    	if(pcTokenMgr.isPCToken(accessToken))
    	{
    		return pcTokenMgr.checkToken(accessToken);
    	}
    	
    	// app
    	String username = getAccountByAccessToken(accessToken);
    	if(StringUtils.isEmpty(username))
    	{
    		return false;
    	}
    	String signCacheKey = UserInfoCacheKeyUtils.createAccessTokenToSignKey(username);
    	String signValue = mCache.getString(signCacheKey);
        JSONObject json = FastJsonHelper.toJSONObject(signValue);
        if (json == null || json.isEmpty()) {
        	return false;
        }
        
        long time = json.getLong(KEY_TIME);
        long diff = (System.currentTimeMillis() - time) / 1000;
        
        // 单点登陆, 客户并发操作导致两次刷新token, 服务端限制在10s内刷新表示同一个用户
        String tmpLoginToken = json.getString(KEY_TOKEN);
        if(!accessToken.equalsIgnoreCase(tmpLoginToken) && diff > 5)
        {
        	//System.out.println("=====================verifyAccessToken InvalidLoginTokenException ========");
        	LOG.error("=====================verifyAccessToken InvalidLoginTokenException ========");
        	//deleteLoginTokenInfo(username);
        	throw InvalidLoginTokenException.mException;
        }

        return DEFAULT_ACCESS_TOKEN_EXPIRES_IN - 10 >= diff;
    }

    @Override
    public String getAccountByAccessToken(String accessToken) {
        try {
        	// pc 端，设计上应该隔离, 这种写法是侵入性代码，谨慎使用
        	PCTokenManager pcTokenMgr = PCTokenManager.getInstance();
        	if(pcTokenMgr.isPCToken(accessToken))
        	{
        		return pcTokenMgr.getAccountByToken(accessToken);
        	}
        	// app 端
            String userCacheKey = UserInfoCacheKeyUtils.createAccessTokenToUsernameKey(accessToken);
        	return mCache.getString(userCacheKey);
        } catch (Exception e) {
            LOG.error("getAccountByAccessToken error:", e);
        }
        return null;
    }

	@Override
	public String getAccountByLoginToken(String loginToken) {
		String cacheKey = UserInfoCacheKeyUtils.createLoginTokenToUsernameKey(loginToken);
    	return mCache.getString(cacheKey);
	}
	
	/**
	 * 生成 loginToken
	 * @param username
	 * @param password
	 * @param time
	 * @param expires
	 * @return
	 */
    private static String generateLoginToken(String username, String password, long time, long expires)
    {
    	return MD5.encode(username + password + time + expires + DEFAULT_SALT);
    }
    
	/**
	 * 生成 accessToken
	 * @param username
	 * @param password
	 * @param time
	 * @param expires
	 * @return
	 */
    private static String generateAccessToken(String username, long time, long expires)
    {
    	return MD5.encode(username + time + expires + DEFAULT_SALT);
    }
    
//    private void deleteLoginTokenInfo(String username)
//    {
//    	String signCacheKey = UserInfoCacheKeyUtils.createLoginTokenToSignKey(username);
//    	mCache.delete(signCacheKey);
//    }
	
    public static void main(String[] args) {
		AuthService service = new AuthServiceImpl();
		String loginToken =service.createLoginTokenByAccount("u1", "p");
//		System.out.println(loginToken);
		
		long start = System.currentTimeMillis();
//		for(int i = 0; i < 1000; i ++)
//		{
//			service.refreshAccessToken(loginToken);
//		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		String accessToken = service.refreshAccessToken(loginToken);
		System.out.println(accessToken);
		System.out.println(service.verifyAccessToken(accessToken));
		System.out.println(service.getAccountByAccessToken(accessToken));
    }

}
