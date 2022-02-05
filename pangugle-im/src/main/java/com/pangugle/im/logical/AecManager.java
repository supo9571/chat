package com.pangugle.im.logical;

import com.pangugle.framework.spring.SpringContextUtils;
import com.pangugle.im.logical.aec.AuthAecSupport;
import com.pangugle.im.logical.aec.UserAecSupport;

public class AecManager{
	
	private UserAecSupport mUserSupport;
	private AuthAecSupport mAuthSupport;
	
	private static interface MyInternal {
		public AecManager mgr = new AecManager();
	}
	
	private AecManager() {
	}
	public static AecManager getInstance()
	{
		return MyInternal.mgr;
	}
	
	public UserAecSupport getUserAec()
	{
		if(this.mUserSupport == null)
		{
			this.mUserSupport = SpringContextUtils.getBean(UserAecSupport.class);
		}
		return this.mUserSupport;
	}
	
	public AuthAecSupport getAuthAec()
	{
		if(this.mAuthSupport == null)
		{
			this.mAuthSupport = SpringContextUtils.getBean(AuthAecSupport.class);
		}
		return this.mAuthSupport;
	}
	
	

}
