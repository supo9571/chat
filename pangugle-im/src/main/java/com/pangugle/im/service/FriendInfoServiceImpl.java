package com.pangugle.im.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.cache.FriendCacheKeyUtils;
import com.pangugle.im.cache.IMCache;
import com.pangugle.im.model.FriendInfo;
import com.pangugle.im.service.dao.FriendInfoDao;

@Service
public class FriendInfoServiceImpl implements FriendInfoService{

	@Autowired
	private FriendInfoDao mFriendInfoDao;
	
	@Override
	@Transactional
	public void add(String userid, String friendid, String alias, String mobile, String remark) {
		if(StringUtils.isEmpty(alias))
		{
			alias = StringUtils.getEmpty();
		}
		if(StringUtils.isEmpty(mobile))
		{
			mobile = StringUtils.getEmpty();
		}
		if(StringUtils.isEmpty(remark))
		{
			remark = StringUtils.getEmpty();
		}
		mFriendInfoDao.add(userid, friendid, alias, mobile, remark);
	}
	
	@Transactional
	public boolean black(String userid, String friendid, boolean black)
	{
		
		mFriendInfoDao.black(userid, friendid, black);
		// delete cache
		String cachekey = FriendCacheKeyUtils.createFriendInfo(userid, friendid);
		IMCache.getIntance().delete(cachekey);
		
		// 删除被拉黑人的缓存
		String cachekey2 = FriendCacheKeyUtils.createFriendInfoForBlackself(friendid, userid);
		IMCache.getIntance().delete(cachekey2);
		
		return true;
	}

	@Override
	@Transactional
	public void update(String userid, String friendid, String alias, String mobile, String remark) {
		if(StringUtils.isEmpty(alias))
		{
			alias = StringUtils.getEmpty();
		}
		if(StringUtils.isEmpty(mobile))
		{
			mobile = StringUtils.getEmpty();
		}
		if(StringUtils.isEmpty(remark))
		{
			remark = StringUtils.getEmpty();
		}
		mFriendInfoDao.update(userid, friendid, alias, mobile, remark);
		
		// delete cache
		String cachekey = FriendCacheKeyUtils.createFriendInfo(userid, friendid);
		IMCache.getIntance().delete(cachekey);
	}

	@Override
	public FriendInfo find(String userid, String friendid) {
		String cachekey = FriendCacheKeyUtils.createFriendInfo(userid, friendid);
		FriendInfo value = IMCache.getIntance().getObject(cachekey, FriendInfo.class);
		if(value == null)
		{
			value = mFriendInfoDao.find(userid, friendid);
			if(value == null)
			{
				add(userid, friendid, StringUtils.getEmpty(), StringUtils.getEmpty(), StringUtils.getEmpty());
				value = new FriendInfo();
				value.setFriendid(friendid);
				value.setBlack(false);
				value.setMobile(StringUtils.EMPTY);
				value.setAlias(StringUtils.EMPTY);
				value.setRemark(StringUtils.EMPTY);
			}
			IMCache.getIntance().setString(cachekey, FastJsonHelper.jsonEncode(value));
		}
		return value;
	}
	
	public boolean getBlackSelfStatus(String selfUserid, String friendUserid)
	{
		boolean rs = false;
		String cachekey = FriendCacheKeyUtils.createFriendInfoForBlackself(friendUserid, selfUserid);
		String value = IMCache.getIntance().getString(cachekey);
		if(StringUtils.isEmpty(value))
		{
			rs = mFriendInfoDao.getBlackSelfStatus(selfUserid, friendUserid);
			IMCache.getIntance().setString(cachekey, rs +StringUtils.getEmpty());
		}
		else
		{
			rs = StringUtils.asBoolean(value);
		}
		return rs;
	}

}
