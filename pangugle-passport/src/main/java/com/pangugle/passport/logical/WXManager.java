package com.pangugle.passport.logical;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.http.HttpSesstionManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.passport.cache.UserCache;
import com.pangugle.passport.cache.WXCacheKeyHelper;
import com.pangugle.passport.model.WXCode2SessionResult;

public class WXManager {
	
	private static Log LOG = LogFactory.getLog(WXManager.class);
	
	private HttpSesstionManager mHttpSession = HttpSesstionManager.getInstance();
	
	private static final String KEY_APPID = "wx_mina_app_id";
	private static final String KEY_APP_SECRET = "wx_mina_app_secret";
	
	/*** wx token url ***/
	private String mWXTokenURL;
	
	/*** code 2 session ***/
	private String mCode2SessionURL;
	
	private interface MyInternal {
		public static WXManager mgr = new WXManager();
	}
	
	private WXManager() {
		String appid = MyConfiguration.getInstance().getString(KEY_APPID);
		String appSecret = MyConfiguration.getInstance().getString(KEY_APP_SECRET);
		this.mWXTokenURL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid +"&secret=" + appSecret;
		this.mCode2SessionURL = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appid + "&secret=" + appSecret + "&grant_type=authorization_code&js_code=";
	}
	public static WXManager getInstance()
	{
		return MyInternal.mgr;
	}
	
	/**
	 * 刷新wxToken
	 */
	public String refreshToken()
	{
		try {
			byte[] data = mHttpSession.syncGet(mWXTokenURL);
			if(data == null)
			{
				return null;
			}
			JSONObject rs = FastJsonHelper.toJSONObject(new String(data));
			if(rs == null || rs.isEmpty())
			{
				return null;
			}
			String accessToken = rs.getString("access_token");
			int expires_in = rs.getIntValue("expires_in");
			String cachekey = WXCacheKeyHelper.createAccessTokenKey();
			UserCache.getIntance().setString(cachekey, accessToken, expires_in - 100);
			return accessToken;
		} catch (IOException e) {
			LOG.error("refresh wx token error:", e);
		}
		return null;
		
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
		String accessToken = UserCache.getIntance().getString(cachekey);
		if(StringUtils.isEmpty(accessToken))
		{
			synchronized (WXManager.class)
			{
				accessToken = UserCache.getIntance().getString(cachekey);
				if(StringUtils.isEmpty(accessToken))
				{
					accessToken = refreshToken();
				}
			}
		}
		return accessToken;
	}

}
