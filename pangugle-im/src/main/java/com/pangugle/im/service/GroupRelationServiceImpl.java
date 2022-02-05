package com.pangugle.im.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.im.cache.GroupCacheKeyUtils;
import com.pangugle.im.cache.IMCache;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupRelation;
import com.pangugle.im.service.dao.GroupRelationDao;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class GroupRelationServiceImpl implements GroupRelationService{
	
	@Autowired
	private GroupRelationDao mGroupRelationDao;
	
//	@Autowired
//	private GroupService mGroupService;;
	
	
	@Autowired
	private GroupAdminService mGroupMemberPermissionService;
	
	@Transactional
	public void bindRelation(String groupid, List<String> memberList) {
		mGroupRelationDao.bindRelation(groupid, memberList);
		
		// 删除缓存
		deleteGroupMemberCache(groupid);
		
		for(String username : memberList)
		{
			deleteUserJoinGroupCache(username);
		}
	}
	@Transactional
	public void unBindRelation(String groupid, List<String> memberList) {
		mGroupRelationDao.unBind(groupid, memberList);
		
		// 移除群成员，删除对应的权限信息
		for(String username : memberList)
		{
			mGroupMemberPermissionService.deleteById(groupid, username);
		}
		
		// 删除缓存
		deleteGroupMemberCache(groupid);
		
		for(String username : memberList)
		{
			deleteUserJoinGroupCache(username);
		}
	}
	
	@Transactional
	@Override
	public List<String> unBindAllRelation(String groupid) {
		List<String> memberList = mGroupRelationDao.queryGroupMemberList(groupid);
		if(CollectionUtils.isEmpty(memberList))
		{
			return Collections.emptyList();
		}
		unBindRelation(groupid, memberList);
		return memberList;
	}
	
	
	/**
	 * 更新用户在群里的昵称
	 */
	@Transactional
	@Override
	public void updateNickname(String groupid, String username, String nickname)
	{
		mGroupRelationDao.updateNickname(groupid, username, nickname);
		
		// 更新缓存
		String cachekey = GroupCacheKeyUtils.createFindGroupRelation(groupid, username);
		IMCache.getIntance().delete(cachekey);
	}
	
	/**
	 * 查找用户在群里的昵称
	 */
	@Override
	public GroupRelation find(String groupid, String username)
	{
		String cachekey = GroupCacheKeyUtils.createFindGroupRelation(groupid, username);
		GroupRelation model = IMCache.getIntance().getObject(cachekey, GroupRelation.class);
		if(model == null)
		{
			model = mGroupRelationDao.find(groupid, username);
			if(model != null)
			{
				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(model));
			}
		}
		return model;
	}
	
	/**
	 * 查询用户加入的群
	 */
	@Override
	public List<Group> queryUserJoinGroup(String username) {
		String cachekey = GroupCacheKeyUtils.createQueryUserJoinGroup(username);
		List<Group> list = IMCache.getIntance().getList(cachekey, Group.class);
		if(list == null)
		{
			list = mGroupRelationDao.queryUserJoinGroup(username);
			if(CollectionUtils.isEmpty(list))
			{
				list = Collections.emptyList();
			}
			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(list));
		}
		return list;
	}

	/**
	 * 根据群id分页获取群成员
	 * 此处应该使用分页
	 * @param groupid
	 * @return
	 */
	@Override
	public List<String> queryScrollGroupMember(String groupid)
	{
		int page = 1;
		String cachekey = GroupCacheKeyUtils.createGroupMemberList(groupid, page);
		List<String> list = IMCache.getIntance().getList(cachekey, String.class);
		if(list == null)
		{
			List<String> dataList = Lists.newArrayList();
			mGroupRelationDao.queryAllGroupMember(groupid, new Callback<String>() {
				@Override
				public void execute(String o) {
					dataList.add(o);
				}
			});
			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(dataList));
			list = dataList;
		}
		return list;
	}
	
	/**
	 * 如果先删除group，再删除缓存会有问题，除非在同一个事务里
	 * @param groupid
	 */
	private void deleteGroupMemberCache(String groupid)
	{
		long totalPage = 5;
		int index = 1;
		boolean stop = false;
		while(!stop)
		{
			totalPage = index * 5;
			String pageCacheKey = GroupCacheKeyUtils.createGroupMemberList(groupid, totalPage);
			if(!CacheManager.getInstance().exists(pageCacheKey))
			{
				stop = true;
				break;
			}
			index++;
		}
		// 页数是从1开始的
		for(long i = 1; i <= totalPage; i ++)
		{
			String pageCacheKey = GroupCacheKeyUtils.createGroupMemberList(groupid, i);
			IMCache.getIntance().delete(pageCacheKey);
		}
	}
	
	public void deleteUserJoinGroupCache(String username)
	{
		String cachekey = GroupCacheKeyUtils.createQueryUserJoinGroup(username);
		IMCache.getIntance().delete(cachekey);
	}
	
	public static void main(String[] args)
	{
		System.out.println((201 + 100 - 1)/100);
	}

}
