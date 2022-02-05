package com.pangugle.im.listener;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.socketio.listener.ServerEventListener;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.im.logical.HistoryManager;
import com.pangugle.im.logical.OfflineManager;
import com.pangugle.im.utils.UserStatusUtils;

/**
 * 消息事件监听器
 * @author Administrator
 *
 */
public abstract class DefaultServerEventListener implements ServerEventListener{
	
	protected  Log LOG = LogFactory.getLog(this.getClass());
	protected  HistoryManager mHistoryManager = HistoryManager.getIntance();
	
	/**
	 * 回调成功返回userid
	 */
	public String onVerifyUserLogin(String accessToken, String ip, String extra) {
		return accessToken;
	}
	
	/**
	 * 登陆成功回调
	 * @param userid
	 */
	public void onUserLoginCallback(String userid)
	{
		UserStatusUtils.online(userid);
	}
	
	@Override
	public void onUserLogoutCallback(String userid) {
		UserStatusUtils.offline(userid);
	}
	
	@Override
	public void onTransBuffer_S2C_Failure_CallBack(MessageBody body) 
	{
		if(!body.isOffline() && !body.getProtocol().isOnline())
		{
			OfflineManager.getIntance().addOfflineMessage(body.getProtocol());
		}
	}
	
	/**
	 * client to server message callback
	 * @param body
	 */
	public void onTransBuffer_C2S_CallBack(MessageBody body)
	{
		System.out.println("receiv message = " + FastJsonHelper.jsonEncode(body));
	}

}
