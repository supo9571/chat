package com.pangugle.im.logical;

import java.util.List;

import com.pangugle.framework.service.Callback;
import com.pangugle.framework.spring.SpringContextUtils;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.im.service.GroupRelationService;

public class GroupManager {
	
	// 分页提取群成员数据大小,一旦上线请勿修改，一旦修改请先清理所有缓存
//	public static final int DEFAULT_GROUP_MEMBER_PAGE_SIZE = 100;
	
	// 每个人最大可以创建多少个群
	public static final int DEFAULT_PER_USER_MAX_COUNT = 100;
	
//	private GroupService mGroupService = SpringContextUtils.getBean(GroupService.class);
	private GroupRelationService mGroupRelationService = SpringContextUtils.getBean(GroupRelationService.class);
	
	private interface GroupManagerInternal {
		public GroupManager mgr = new GroupManager();
	}
	
	public static GroupManager getIntance()
	{
		return GroupManagerInternal.mgr;
	}

	public void queryAllMembers(String groupid, Callback<String> callback)
	{
		List<String> userList = mGroupRelationService.queryScrollGroupMember(groupid);
		if(CollectionUtils.isEmpty(userList))
		{
			return;
		}
		for(String userid : userList)
		{
			callback.execute(userid);
		}
	}

}
