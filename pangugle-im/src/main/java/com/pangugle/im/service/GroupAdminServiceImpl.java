package com.pangugle.im.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangugle.framework.cache.CacheManager;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.im.cache.GroupCacheKeyUtils;
import com.pangugle.im.service.dao.GroupAdminDao;

@Service
public class GroupAdminServiceImpl implements GroupAdminService {
	
	@Autowired
	private GroupAdminDao mGroupMemberPermissionDao;
	
	@Transactional
	public void addAdmin(String groupid, String username)
	{
		mGroupMemberPermissionDao.addAdmin(groupid, username);
		deleteCache(groupid);
	}
	
	@Transactional
	public void deleteById(String groupid, String username)
	{
		mGroupMemberPermissionDao.deleteById(groupid, username);
		deleteCache(groupid);
	}
	
	public boolean existAdmin(String groupid, String username)
	{
		List<String> list = queryAll(groupid);
		for(String admin : list)
		{
			if(admin.equalsIgnoreCase(username))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<String>queryAll(String groupid)
	{
		String cachekey = GroupCacheKeyUtils.createQueryAllGroupAdmin(groupid);
		List<String> list = CacheManager.getInstance().getList(cachekey, String.class);
		if(list == null)
		{
			list = mGroupMemberPermissionDao.queryAll(groupid);
			if(CollectionUtils.isEmpty(list))
			{
				list = Collections.emptyList();
			}
			CacheManager.getInstance().setString(cachekey, FastJsonHelper.jsonEncode(list));
		}
		return list;
	}
	
	private void deleteCache(String groupid)
	{
		String cachekey = GroupCacheKeyUtils.createQueryAllGroupAdmin(groupid);
		CacheManager.getInstance().delete(cachekey);
	}

}
