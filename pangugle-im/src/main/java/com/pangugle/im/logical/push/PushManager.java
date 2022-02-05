package com.pangugle.im.logical.push;

import com.alibaba.fastjson.JSONObject;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.logical.push.impl.GetuiPushImpl;
import com.pangugle.im.model.BasicMsgType;

/**
 * 推送管理器
 * @author Administrator
 *
 */
public class PushManager {
	
//	private APNSService mAPNSService = new APNSServiceImpl();
	
	private PushSupport mPushSupport = new GetuiPushImpl();
	
	private interface APNSManagerInternal {
		public PushManager mgr = new PushManager();
	}
	
	private PushManager()
	{
		
	}
	
	public static PushManager getIntance()
	{
		return APNSManagerInternal.mgr;
	}
	
	public void pushMessage(MyProtocol protocol)
	{
		String token = MD5.encode(protocol.getTargetid());
//		String token = "388056311e1e449f5a7148fbc418bb40";
		String payload = StringUtils.getEmpty(); // 直接跳转到首页
		JSONObject dataJson = (JSONObject) protocol.getData();
		String notifyContent = null;
		if(BasicMsgType.TEXT.getName().equalsIgnoreCase(protocol.getMsgType()))
		{
			notifyContent = dataJson.getString("content");
		}
		else 
		{
			notifyContent = dataJson.getString("convText");
		}
		mPushSupport.sendSingleMessage(token, "新消息", notifyContent, payload, 3600 * 1000);
	}
	
	public void pushMessage(String deviceid, String notificationTitle, String msgTitle, String msgContent, String extra, int badge)
	{
		
	}

}
