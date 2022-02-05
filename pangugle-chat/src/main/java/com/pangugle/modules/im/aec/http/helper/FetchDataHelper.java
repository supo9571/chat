package com.pangugle.modules.im.aec.http.helper;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.pangugle.framework.http.HttpSesstionManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;

public class FetchDataHelper {
	
	private static Log LOG = LogFactory.getLog(FetchDataHelper.class);
	
	private static HttpSesstionManager mHttpSesstion = HttpSesstionManager.getInstance();
	
	public static JSONObject loadData(String url)
	{
		try {
			byte[] byteRs = mHttpSesstion.syncGet(url);
			if(byteRs == null)
			{
				return null;
			}
			String rs = new String(byteRs);
			if(StringUtils.isEmpty(rs))
			{
				return null;
			}
			JSONObject obj = FastJsonHelper.toJSONObject(rs);
			if(obj == null || obj.isEmpty())
			{
				return null;
			}
			return obj;
		} catch (IOException e) {
			LOG.error("load data error:", e);
		}
		return null;
	}

}
