package com.pangugle.im.logical;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.im.utils.OfflineMessageUtils;

/**
 * 离线消息管理器, 其它模块不得调用
 * @author Administrator
 *
 */
public class OfflineManager {
	
	// 并发线程数
	private static int CONCURRENT_SIZE = 10;
	private ExecutorService pool = null;
	
	private interface OfflineManagerInternal {
		public OfflineManager mgr = new OfflineManager();
	}
	
	private OfflineManager()
	{
		// 创建一个可重用固定线程数的线程池：
		// 目前给定的最大数目是10线程，可以KChat运维过程中根据综
		// 合性能和压力情况调整此值以便达到最优状态
		pool = Executors.newFixedThreadPool(CONCURRENT_SIZE);
	}
	
	public static OfflineManager getIntance()
	{
		return OfflineManagerInternal.mgr;
	}
	
	/**
	 * 添加离线消息
	 * @param userid
	 * @param body
	 */
	public void addOfflineMessage(MyProtocol body)
	{
		pool.execute(new Runnable() {
			public void run() {
				synchronized (getOfflineLockKey(body.getTargetid())) 
				{
					OfflineMessageUtils.save(body);
				}
			}
		});
	}
	
	/**
	 * 按页数获取离线消息
	 * @param userid
	 * @param page
	 * @return
	 */
	public List<MyProtocol> queryOfflineMessage(String userid, int page)
	{
		List<MyProtocol> list = OfflineMessageUtils.getPageList(userid, page);
		
		int prePage = page - 1;
		if(prePage > 0)
		{
			OfflineMessageUtils.deletePage(userid, prePage);
		}
		
//		int size = list.size();
//		if(size == 0 || size < OfflineMessageUtils.DEFAULT_PAGE_SIZE)
//		{
//			// 删除缓存
//			for(int i = 0; i < page; i ++)
//			{
//				OfflineMessageUtils.deletePage(userid, i + 1);
//			}
//		}
		return list;
	}
	
	private String getOfflineLockKey(String userid)
	{
		return userid;
	}
	

}
