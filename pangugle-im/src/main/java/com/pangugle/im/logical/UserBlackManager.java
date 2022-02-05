package com.pangugle.im.logical;

public class UserBlackManager {
	
//	private UserService mUserService;
	

	private interface MyInternal {
		public UserBlackManager mgr = new UserBlackManager();
	}
	
	private UserBlackManager()
	{
//		this.mUserService = SpringContextUtils.getBean(UserService.class);
	}
	
	public static UserBlackManager getIntance()
	{
		return MyInternal.mgr;
	}
	
	/**
	 * 可以异步，这样会更好
	 * @param username
	 */
	public void blackUser(String username)
	{
//		ConcurrentManager.getIntance().execute(new Runnable() {
//			
//			@Override
//			public void run() {
//				mUserService.updateStaus(username, false);
//			}
//		});
		
	}
	
	/**
	 * 判断是否被封禁
	 * @param username
	 * @return
	 */
	public boolean isBlack(String username)
	{
		return false;
	}
	
}
