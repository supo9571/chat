package   com.pangugle.passport.cache;

import com.pangugle.passport.MyConstants;

public class UserInfoCacheKeyUtils {
	
	public static String createUserInfoKey(String username)
	{
		return MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_findByUsername_" + username;
	}
	
	public static String createAccessTokenToUsernameKey(String accessToken)
	{
		return MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_accessToken_" + accessToken;
	}
	
	public static String createAccessTokenToSignKey(String username)
	{
		return MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_accessToken_2_sign_" + username;
	}
	
	public static String createLoginTokenToUsernameKey(String loginToken)
	{
		return MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_loginToken_2_username_" + loginToken;
	}
	
	public static String createLoginTokenToSignKey(String username)
	{
		return MyConstants.DEFAULT_PASSPORT_MODULE_NAME + "_loginToken_2_sign_" + username;
	}
	
	
}