package com.pangugle.modules.im.listener;

import com.pangugle.im.listener.MyServerEventListener;
import com.pangugle.passport.logical.PCTokenManager;

public class ChatServerEventListener extends MyServerEventListener{
	private PCTokenManager mPCTokenMgr = PCTokenManager.getInstance();
	public String getFromPlatform(String accessToken) {
		if(mPCTokenMgr.isPCToken(accessToken))
		{
			return "pc";
		}
		return "app";
	}

}
