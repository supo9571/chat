package com.pangugle.im.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.cache.FriendCacheKeyUtils;
import com.pangugle.im.cache.IMCache;
import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.FriendRelation.MyStatus;
import com.pangugle.im.service.dao.FriendRelationDao;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class FriendRelationServiceImpl implements FriendRelationService{
	
	@Autowired
	private FriendRelationDao mRelationDao;
	
	/**
	 * 绑定好友关系
	 * @param fromUserid
	 * @param toUserid
	 */
	@Transactional
	@Override
	public void bindRelation(String fromUserid, String toUserid, String requestUserid, String remark)
	{
		// 交换用户名位置，小的是from, 大的是to
		String tmp = StringUtils.max(fromUserid, toUserid);
		if(tmp.equals(fromUserid))
		{
			fromUserid = toUserid;
			toUserid = tmp;
		}
		MyStatus fromStatus = MyStatus.enable;
		MyStatus toStatus = MyStatus.unconfirm;
		if(toUserid.equalsIgnoreCase(requestUserid))
		{
			fromStatus = MyStatus.unconfirm;
			toStatus = MyStatus.enable;
		}
		
		mRelationDao.bindRelation(fromUserid, toUserid, requestUserid, fromStatus, toStatus, remark);
		
		// 删除对方的待确认列表
		if(fromStatus == MyStatus.unconfirm)
		{
			deleteUnConfirmListCache(fromUserid);
		}
		else
		{
			deleteUnConfirmListCache(toUserid);
		}
	}
	
	/**
	 * 删除关系
	 * @param fromUserid
	 * @param toUserid
	 */
	@Transactional
	@Override
	public void deleteRelation(String fromUserid, String toUserid)
	{
		// 交换用户名位置，小的是from, 大的是to
		String tmp = StringUtils.max(fromUserid, toUserid);
		if(tmp.equals(fromUserid))
		{
			fromUserid = toUserid;
			toUserid = tmp;
		}
		
		// 删除关系
		mRelationDao.deleteRelation(fromUserid, toUserid);
				
		// 删除所有缓存
		deleteAllCache(fromUserid, toUserid);
	}
	
	/**
	 * 更新好友关系状态
	 * @param fromUserid
	 * @param toUserid
	 * @param status
	 */
	@Transactional
	@Override
	public void updateStatus(String fromUserid, String toUserid, String targetUserid, MyStatus status)
	{
		// 交换用户名位置，小的是from, 大的是to
		String tmp = StringUtils.max(fromUserid, toUserid);
		if(tmp.equals(fromUserid))
		{
			fromUserid = toUserid;
			toUserid = tmp;
		}
		
		// from userid 更新状态
		if(fromUserid.equalsIgnoreCase(targetUserid))
		{
			mRelationDao.updateFromStatus(fromUserid, toUserid, status);
		}
		else
		{
			mRelationDao.updateToStatus(fromUserid, toUserid, status);
		}
		
		// 删除所有缓存
		deleteAllCache(fromUserid, toUserid);
	}
	
	/**
	 * 查询好友关系
	 * @param fromUserid
	 * @param toUserid
	 * @return
	 */
	@Override
	public FriendRelation find(String fromUserid, String toUserid)
	{
		// 交换用户名位置，小的是from, 大的是to
		String tmp = StringUtils.max(fromUserid, toUserid);
		if(tmp.equals(fromUserid))
		{
			fromUserid = toUserid;
			toUserid = tmp;
		}
		
		String cachekey = FriendCacheKeyUtils.createUserRelation(fromUserid, toUserid);
		FriendRelation relation = IMCache.getIntance().getObject(cachekey, FriendRelation.class);
		if(relation == null)
		{
			relation = mRelationDao.find(fromUserid, toUserid);
			if(relation != null)
			{
				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(relation));
			}
		}
		return relation;
	}
	
	/**
	 * 获取 待确认列表
	 * @param userid
	 * @param page
	 * @param status
	 * @return
	 */
	public List<String> queryUnConfirmList(String userid)
	{
		MyStatus status = MyStatus.unconfirm;
		String cachekey = FriendCacheKeyUtils.createUserFriendRelationList(userid, status);
		List<String> list = IMCache.getIntance().getList(cachekey, String.class);
		if(list == null)
		{
			List<String> newList = Lists.newArrayList();
			mRelationDao.queryStatusList(userid, status, new Callback<String>() {
				public void execute(String o) {
					newList.add(o);
				}
			});
			list = newList;
			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(newList));
		}
		
		
		return list;
	}
	
	/**
	 * 查询好友列表
	 * 当page=-1，表示重新加载数据到缓存，注意这个只能内部调用，外部调用则是非法调用
	 */
	@Override
	public List<String> queryFriendList(String userid)
	{
		MyStatus status = MyStatus.enable;
		String cachekey = FriendCacheKeyUtils.createUserFriendRelationList(userid, status);
		List<String> newList = IMCache.getIntance().getList(cachekey, String.class);
		if(newList == null)
		{
			List<String> dataList = Lists.newArrayList();
			// 重新初始化数据
			mRelationDao.queryFriendList(userid, new Callback<String>() {
				public void execute(String o) {
					dataList.add(o);
				}
			});
			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(dataList));
			newList = dataList;
		}
		return newList;
	}
	
	/**
	 * 好友个数， 好友个数为-1表示没有好友
	 * @param userid
	 * @return
	 */
	@Override
	public long getUserFriendSize(String userid)
	{
		MyStatus status = MyStatus.enable;
		String cachekey = FriendCacheKeyUtils.createUserFriendRelationSize(userid, status);
		long count = IMCache.getIntance().getLong(cachekey);
		if(count == 0)
		{
			count = mRelationDao.countFriend(userid);
			if(count == 0)
			{
				count = -1;
			}
			IMCache.getIntance().setString(cachekey, count +StringUtils.getEmpty()); 
		}
		return count;
	}
	
	/**
	 * 验证是否可以添加好友
	 * @param userid
	 * @return
	 */
	@Override
	public boolean checkAdd(String userid)
	{
		long count = getUserFriendSize(userid);
		return count <= FriendRelation.DEFAULT_MAX_FRIEND_SIZE;
	}
	
	/**
	 * 删除用户对的好友列表缓存
	 * @param userid
	 */
	private void deleteFriendListCache(String userid)
	{
		MyStatus status = MyStatus.enable;
		String cachekey = FriendCacheKeyUtils.createUserFriendRelationList(userid, status);
		IMCache.getIntance().delete(cachekey);
		// 好友个数
		IMCache.getIntance().delete(FriendCacheKeyUtils.createUserFriendRelationSize(userid, status));
	}
	
	/**
	 * 删除待确认列表关系
	 * @param userid
	 */
	private void deleteUnConfirmListCache(String userid)
	{
		MyStatus status = MyStatus.unconfirm;
		String cachekey = FriendCacheKeyUtils.createUserFriendRelationList(userid, status);
		IMCache.getIntance().delete(cachekey);
	}
	
	/**
	 * 删除好友之间的关系缓存
	 * @param fromUserid
	 * @param toUserid
	 */
	private void deleteRelationCache(String fromUserid, String toUserid)
	{
		String cachekey = FriendCacheKeyUtils.createUserRelation(fromUserid, toUserid);
		IMCache.getIntance().delete(cachekey);
	}
	
	private void deleteAllCache(String fromUserid, String toUserid)
	{
		// 如果并发严重可以使用消息队列来更新缓存
		deleteFriendListCache(fromUserid);
		deleteFriendListCache(toUserid);
		
		deleteUnConfirmListCache(fromUserid);
		deleteUnConfirmListCache(toUserid);
		
		// 
		deleteRelationCache(fromUserid, toUserid);
	}
}
