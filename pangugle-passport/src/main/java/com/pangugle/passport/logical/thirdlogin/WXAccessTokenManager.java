package com.pangugle.passport.logical.thirdlogin;

import java.io.IOException;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.http.HttpCallback;
import com.pangugle.framework.http.HttpSesstionManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.passport.cache.UserCache;
import com.pangugle.passport.cache.WXCacheKeyHelper;
import com.pangugle.passport.logical.WXManager;
import com.pangugle.passport.model.WXCode2SessionResult;

import okhttp3.Request;
import okhttp3.Response;

public class WXAccessTokenManager {
	
private static Log LOG = LogFactory.getLog(WXManager.class);
	
	private static int DEFAULT_ACCESS_TOKEN = 7200 - 300;
	
	private HttpSesstionManager mHttpSession = HttpSesstionManager.getInstance();
	
	private static final String KEY_APPID = "wx_mina_app_id";
	private static final String KEY_APP_SECRET = "wx_mina_app_secret";
	
	/*** wx token url ***/
	private String mWXTokenURL;
	/*** code 2 session ***/
	private String mCode2SessionURL;
	
	private interface MyInternal {
		public static WXAccessTokenManager mgr = new WXAccessTokenManager();
	}
	
	private WXAccessTokenManager() {
		String appid = MyConfiguration.getInstance().getString(KEY_APPID);
		String appSecret = MyConfiguration.getInstance().getString(KEY_APP_SECRET);
		this.mWXTokenURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid +"&secret=" + appSecret;
		this.mCode2SessionURL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&grant_type=authorization_code&js_code=";
	}
	public static WXAccessTokenManager getInstance()
	{
		return MyInternal.mgr;
	}
	
	/**
	 * 刷新wxToken
	 */
	public void refreshToken()
	{
		mHttpSession.asyncGet(mWXTokenURL, new HttpCallback() {
			public void onFailure(Throwable e) {
				LOG.error("refresh wx accessToken error:", e);
			}

			public void onSuccess(Request request, Response response, byte[] data) {
				String accessToken = new String(data);
				if(StringUtils.isEmpty(accessToken))
				{
					LOG.error("refresh wx accessToken error, the result is empty");
					return;
				}
				String cachekey = WXCacheKeyHelper.createAccessTokenKey();
				UserCache.getIntance().setString(cachekey, accessToken, DEFAULT_ACCESS_TOKEN);
			}
		});
	}
	
	/**
	 * 登录凭证校验
	 * @param code
	 * @return
	 */
	public WXCode2SessionResult code2Session(String code)
	{
		try {
			byte[] rs = mHttpSession.syncGet(mCode2SessionURL + code);
			if(rs != null) {
				return FastJsonHelper.jsonDecode(new String(rs), WXCode2SessionResult.class);
			}
		} catch (IOException e) {
			LOG.error("code2Session error:", e);
		}
		return null;
	}
	
	/**
	 * 从缓存直接获取token
	 * @return
	 */
	public String getAccessToken()
	{
		String cachekey = WXCacheKeyHelper.createAccessTokenKey();
		return UserCache.getIntance().getString(cachekey);
	}

}
