package com.pangugle.modules.im.aec.http;

import com.alibaba.fastjson.JSONObject;
import com.pangugle.im.logical.aec.AuthAecSupport;
import com.pangugle.modules.im.aec.http.helper.FetchDataHelper;

//@Service
public class HttpAuthAecSupportImpl implements AuthAecSupport{
	
	@Override
	public boolean verifyAccessToken(String accessToken) {
		String url = "xxxxx/verifyAccessToken?accessToken=" + accessToken; // 地址自己定义
		// 示例 :  {"code": 200, "msg": "", data:true|false}
		JSONObject obj = FetchDataHelper.loadData(url);
		if(obj == null || obj.isEmpty())
		{
			return false;
		}
		boolean rs = obj.getBooleanValue("data");
		return rs;
	}

	@Override
	public String getAccountByAccessToken(String accessToken) {
		String url = "xxxxx/getAccountByAccessToken?accessToken=" + accessToken; // 地址自己定义
		// 示例 : {"code": 200, "msg": "success", data:"xxxxxxxxxxx"}
		JSONObject obj = FetchDataHelper.loadData(url);
		if(obj == null || obj.isEmpty())
		{
			return null;
		}
		String rs = obj.getString("data");
		return rs;
	}

}
