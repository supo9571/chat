package com.pangugle.passport.logical;


import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.utils.UUIDUtils;
import com.pangugle.passport.cache.PCTokenCacheKeyUtils;
import com.pangugle.passport.cache.UserCache;

public class PCTokenManager {
	
	private static int DEFAULT_REQUEST_CODE_EXPIRES = 60;
	private static int DEFAULT_TOKEN_EXPIRES = 3600 * 5;
	
	private interface MyInternal {
		public static PCTokenManager mgr = new PCTokenManager();
	}
	
	private PCTokenManager() {
	}
	public static PCTokenManager getInstance()
	{
		return MyInternal.mgr;
	}
	
	public boolean isPCToken(String token)
	{
		// 因为pctoken是 md5 再加一个1，所以是33位，
		return token.length() == 33;
	}
	
	/**
	 * 创建请求code, 一次调用
	 * @param ip
	 * @return
	 */
	public String createRequestCode(String ip)
	{
		return MD5.encode(UUIDUtils.getUUID() + ip + System.currentTimeMillis());
	}
	
	public void createToken(String requestcode, String username)
	{
		long time = System.currentTimeMillis();
		// 1分钟内有效
		String reqcode2TokenCacheKey = PCTokenCacheKeyUtils.createReq2Token(requestcode);
		// 加个1表示来自pc端
		String pcToken = MD5.encode(requestcode + username + time) + 1;
		UserCache.getIntance().setString(reqcode2TokenCacheKey, pcToken, DEFAULT_REQUEST_CODE_EXPIRES);
		
		//
		String token2UserCacheKey = PCTokenCacheKeyUtils.createToken2User(pcToken);
		UserCache.getIntance().setString(token2UserCacheKey, username + StringUtils.getBottomDividerLine() + time, DEFAULT_TOKEN_EXPIRES);
		
		// 
		String user2TokenCackeKey = PCTokenCacheKeyUtils.createUser2Token(username);
		UserCache.getIntance().setString(user2TokenCackeKey, pcToken, DEFAULT_TOKEN_EXPIRES);
	}
	
	public boolean checkToken(String token)
	{
		String cacheKey = PCTokenCacheKeyUtils.createToken2User(token);
		String value = UserCache.getIntance().getString(cacheKey);
		if(!StringUtils.isEmpty(value))
		{
			String[] arr = value.split(StringUtils.getBottomDividerLine());
			if(arr == null || arr.length != 2)
			{
				return false;
			}
			long time = StringUtils.asLong(arr[1]);
			if((System.currentTimeMillis() - time) / 1000 > DEFAULT_TOKEN_EXPIRES - 10000)
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 一次性调用
	 * @param requestcode
	 * @return
	 */
	public String getTokenByReqcode(String requestcode)
	{
		String reqcode2TokenCacheKey = PCTokenCacheKeyUtils.createReq2Token(requestcode);
		String rs = UserCache.getIntance().getString(reqcode2TokenCacheKey);
		if(!StringUtils.isEmpty(rs))
		{
			UserCache.getIntance().delete(reqcode2TokenCacheKey);
		}
		return rs;
	}
	
	public String getTokenByUserid(String userid)
	{
		String cachekey = PCTokenCacheKeyUtils.createUser2Token(userid);
		String rs = UserCache.getIntance().getString(cachekey);
		return rs;
	}
	
	public String getAccountByToken(String token)
	{
		String cacheKey = PCTokenCacheKeyUtils.createToken2User(token);
		String rs = UserCache.getIntance().getString(cacheKey);
		return rs;
	}
	
	public void deleteToken(String username)
	{
		String user2TokenCackeKey = PCTokenCacheKeyUtils.createUser2Token(username);
		UserCache.getIntance().delete(user2TokenCackeKey);
	}
	
	public static void main(String[] args)
	{
		PCTokenManager mgr = PCTokenManager.getInstance();
		String reqCode = mgr.createRequestCode("192.168.1.1");
		System.out.println(reqCode);
		
		String username = "test111111111111";
		
		mgr.createToken(reqCode, username);
		
		String token = mgr.getTokenByReqcode(reqCode);
		
		System.out.println("===========> token = " + token);
		System.out.println("===========> check token = " + mgr.checkToken(token));
	}
	
}
