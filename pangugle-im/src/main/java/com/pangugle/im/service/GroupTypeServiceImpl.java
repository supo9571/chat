//package com.pangugle.im.service;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.pangugle.framework.cache.CacheManager;
//import com.pangugle.framework.utils.CollectionUtils;
//import com.pangugle.framework.utils.FastJsonHelper;
//import com.pangugle.im.cache.GroupCacheKeyUtils;
//import com.pangugle.im.cache.IMCache;
//import com.pangugle.im.model.GroupType;
//import com.pangugle.im.model.GroupType.DefaultConfig;
//import com.pangugle.im.service.dao.GroupTypeDao;
//
///**
// * 
// * @author Administrator
// *
// */
//@Service
//public class GroupTypeServiceImpl implements GroupTypeService{
//	
//	private static int DEFAULT_FIND_GROUP_EXPIRES = CacheManager.EXPIRES_FOREVER;
//
//	@Autowired
//	private GroupTypeDao mGroupConfigDao;
//	
//	@Override
//	@Transactional
//	public void addConfig(DefaultConfig defaultConfig)
//	{
//		mGroupConfigDao.addConfig(defaultConfig.getType(), null, defaultConfig.getCapacity());
//	}
//	@Override
//	public List<GroupType> queryAll()
//	{
//		String cachekey = GroupCacheKeyUtils.createAllGroupConfig();
//		List<GroupType> list = IMCache.getIntance().getList(cachekey, GroupType.class);
//		if(CollectionUtils.isEmpty(list))
//		{
//			list = mGroupConfigDao.queryAll();
//			if(!CollectionUtils.isEmpty(list))
//			{
//				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(list));
//			}
//		}
//		return list;
//	}
//	@Override
//	public GroupType findByType(String type)
//	{
//		String cachekey = GroupCacheKeyUtils.createFindGroupConfig(type);
//		GroupType config = IMCache.getIntance().getObject(cachekey, GroupType.class);
//		if(config == null)
//		{
//			config = mGroupConfigDao.findByType(type);
//			if(config != null)
//			{
//				IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(config), DEFAULT_FIND_GROUP_EXPIRES);
//			}
//		}
//		return config;
//	}
//	@Override
//	@Transactional
//	public void updateRemark(String type, String remark)
//	{
//		mGroupConfigDao.updateRemark(type, remark);
//		String cachekey = GroupCacheKeyUtils.createFindGroupConfig(type);
//		GroupType config = findByType(type);
//		if(config != null)
//		{
//			config.setRemark(remark);
//			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(config), DEFAULT_FIND_GROUP_EXPIRES);
//		}
//	}
//	@Override
//	@Transactional
//	public void updateCapacity(String type, long capacity)
//	{
//		mGroupConfigDao.updateCapacity(type, capacity);
//		String cachekey = GroupCacheKeyUtils.createFindGroupConfig(type);
//		GroupType config = findByType(type);
//		if(config != null)
//		{
//			config.setMaxCapacity(capacity);
//			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(config), DEFAULT_FIND_GROUP_EXPIRES);
//		}
//	}
//	
//}
