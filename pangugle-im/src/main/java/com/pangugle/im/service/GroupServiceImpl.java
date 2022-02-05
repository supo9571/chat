package com.pangugle.im.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.IdWorker;
import com.pangugle.im.cache.GroupCacheKeyUtils;
import com.pangugle.im.cache.IMCache;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupType;
import com.pangugle.im.service.dao.GroupDao;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class GroupServiceImpl implements GroupService {
	
	@Autowired
	private GroupDao mGroupDao;
	
	@Autowired
	private GroupRelationService mGroupRelationService;
	
	@Transactional
	public String createGroup(String holder, String alias, String icons, List<String> memberList)
	{
		String groupid = addGroup(holder, alias, icons, memberList.size());
		mGroupRelationService.bindRelation(groupid, memberList);
		return groupid;
	}
	
	@Transactional
	private String addGroup(String holder, String alias, String icons, long currentCapacity)
	{
		// 添加
		String id = IdWorker.generatorNumberId();
		mGroupDao.addGroup(id, holder, alias, icons, currentCapacity, GroupType.DefaultConfig.LOW.getCapacity());
		
		return id;
	}
	
	@Transactional
	@Override
	public List<String> deleteGroup(Group group)
	{
		// 解除所有关系
		List<String> memberList = mGroupRelationService.unBindAllRelation(group.getId());
		
		mGroupDao.deleteGroup(group.getId());
		
		// 不能直接删除缓存，会缓存并发问题，一直请求数据库
		// 所以直接更新缓存状态，标识为删除
		String cachekey = GroupCacheKeyUtils.createFindGroup(group.getId());
		group.setDelete(true);
		IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(group));
		
		return memberList;
	}
	
	@Transactional
	public void updateGroupMember(Group group, List<String> memberList, boolean isAdd)
	{
		if(isAdd)
		{
			mGroupRelationService.bindRelation(group.getId(), memberList);
			updateCapacity(group.getId(), memberList.size());
		}
		else
		{
			mGroupRelationService.unBindRelation(group.getId(), memberList);
			updateCapacity(group.getId(), -memberList.size());
		}
	}
	
	@Transactional
	@Override
	public void updateName(String id, String name)
	{
		mGroupDao.updateName(id, name);
		
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	public void updateAlias(String id, String alias)
	{
		mGroupDao.updateAlias(id, alias);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	public void updateIcons(String id, String icons)
	{
		mGroupDao.updateIcons(id, icons);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateNotice(String id, String notice)
	{
		mGroupDao.updateNotice(id, notice);
		
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	/**
	 * 
	 * @param id
	 * @param capacity 负数表示移除成员， 正数表示添加成员
	 */
	@Transactional
	public void updateCapacity(String id, long capacity)
	{
		mGroupDao.updateCapacity(id, capacity);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateStatus(String id, boolean enableStatus)
	{
		mGroupDao.updateStatus(id, enableStatus);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateInviteStatus(String id, boolean invite)
	{
		mGroupDao.updateInviteStatus(id, invite);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateAddFriendStatus(String id, boolean enableAddFriend)
	{
		mGroupDao.updateAddFriendStatus(id, enableAddFriend);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateEnableChat(String id, boolean enableChat)
	{
		mGroupDao.updateEnableChat(id, enableChat);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Transactional
	@Override
	public void updateIconsAndAlias(Group group, String icons, String alias)
	{
		updateAlias(group.getId(), alias);
		updateIcons(group.getId(), icons);
	}
	
	@Transactional
	@Override
	public void transferHolder(String id, String newHolder)
	{
		mGroupDao.transferHolder(id, newHolder);
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		IMCache.getIntance().delete(cachekey);
	}
	
	@Override
	public Group findById(String id)
	{
		String cachekey = GroupCacheKeyUtils.createFindGroup(id);
		Group group = IMCache.getIntance().getObject(cachekey, Group.class);
		if(group == null)
		{
			group = mGroupDao.findById(id);
			if(group == null)
			{
				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(group));
			}
		}
		return group;
	}
	
//	@Override
//	public List<Group> queryAllByHolder(String holder)
//	{
//		String cachekey = GroupCacheKeyUtils.createAllHolderGroup(holder);
//		List<Group> list = IMCache.getIntance().getList(cachekey, Group.class);
//		if(CollectionUtils.isEmpty(list))
//		{
//			list = mGroupDao.queryAllByHolder(holder);
//			if(!CollectionUtils.isEmpty(list))
//			{
//				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(list));
//			}
//		}
//		return list;
//	}

}
