package com.pangugle.im.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.framework.zookeeper.DistributeLock;
import com.pangugle.framework.zookeeper.ZKClientManager;
import com.pangugle.im.GroupErrorResult;
import com.pangugle.im.MyConstants;
import com.pangugle.im.limit.MyTokenLimit;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.logical.GroupManager;
import com.pangugle.im.logical.GroupNotifyManager;
import com.pangugle.im.logical.LockManager;
import com.pangugle.im.logical.UserBlackManager;
import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupRelation;
import com.pangugle.im.model.MyUserInfo;
import com.pangugle.im.service.FriendRelationService;
import com.pangugle.im.service.GroupAdminService;
import com.pangugle.im.service.GroupRelationService;
import com.pangugle.im.service.GroupService;
import com.pangugle.im.utils.GroupHelper;

@RequestMapping(MyConstants.DEFAULT_IM_MODULE_NAME + "/groupApi")
@RestController
public class GroupApi {
	
//	private static Log LOG = LogFactory.getLog(GroupApi.class);
	
	@Autowired 
	private FriendRelationService mFriendService;

	@Autowired
	private GroupService mGroupService;
	
	@Autowired
	private GroupRelationService mGroupRelationService;
	
	@Autowired
	private GroupAdminService mGroupAdminService;
	
	private AecManager mAecManager = AecManager.getInstance();
	
	/**
	 * 创建群组
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("createGroup")
	public String createGroup()
	{
		String accessToken = WebRequest.getAccessToken();
		String holder = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		String[] memebers = WebRequest.getStrings("memebers", ",");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// 为空异常
		int len = memebers.length;
		if(memebers == null || len <= 0)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		// 一次最多20个, 大于20个非法操作
		if(len > 9)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		List<Group> groupList = mGroupRelationService.queryUserJoinGroup(holder);
		// 群组添加已到上限, 被动加入也算
		if(!CollectionUtils.isEmpty(groupList) && groupList.size() > GroupManager.DEFAULT_PER_USER_MAX_COUNT)
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_ADD_LIMIT);
			return api.toJSONString();
		}
		
		MyUserInfo holderUserInfo = mAecManager.getUserAec().findUserInfo(holder);
		
		//
		for(String username : memebers)
		{
			MyUserInfo userInfo = mAecManager.getUserAec().findUserInfo(username);
			if(userInfo == null)
			{
				UserBlackManager.getIntance().blackUser(username);
				api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
				return api.toJSONString();
			}
			
			// 拉人与被拉人是好友关系
			FriendRelation friend = mFriendService.find(holderUserInfo.getUsername(), userInfo.getUsername());
			if(friend == null || !friend.verifyFriend())
			{
				api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
				return api.toJSONString();
			}
		}
		///////////////////////////////////////////////////////////////////////////
		// 群主+其他两个人作为群名称
		StringBuffer groupAliasBuffer = new StringBuffer();
		StringBuffer groupIconBuffer = new StringBuffer();
		// 群主昵称
		groupAliasBuffer.append(holderUserInfo.getShowName());
		groupIconBuffer.append(holderUserInfo.getAvatar());
		
		// 其他两个人作为群名称
		int memberLen = memebers.length;
		int groupAliasIndex = 0;
		
		for(int i = 0; i < memberLen; i ++)
		{
			MyUserInfo memberInfo = mAecManager.getUserAec().findUserInfo(memebers[i]);
			if(memberInfo == null)
			{
				api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
				return api.toJSONString();
			}
			
			// 初始化昵称
			if(groupAliasIndex < 2)
			{
				groupAliasBuffer.append("、");
				groupAliasBuffer.append(memberInfo.getShowName());
			}
			groupAliasIndex ++;
			
			// 初始化图标
			groupIconBuffer.append(",");
			groupIconBuffer.append(memberInfo.getAvatar());
		}
		////////////////////////////////////////////////////////////////////////////////////////
		// add member opt
		List<String> memberList = Lists.newArrayList();
		// 群成员要算上群主自己
		memberList.add(holder);
		for(String username : memebers)
		{
			memberList.add(username);
		}
		String groupid = mGroupService.createGroup(holder, groupAliasBuffer.toString(), groupIconBuffer.toString(), memberList);
		//
		GroupNotifyManager.getIntance().notifyInviteMember(groupid, holder);
		
		api.setData(groupid);
		
		return api.toJSONString();
	}
	
	/**
	 * 退出或解散群组
	 * 成员表示退出群组 | 群主表示解散群组
	 * 
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("deleteGroup")
	public String deleteGroup()
	{
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		String groupid = WebRequest.getString("groupid");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// group 不存在
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			//api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		List<String> removeMemberList  = null;
		// 只有群主解散
		if(group.getHolder().equalsIgnoreCase(username))
		{
			removeMemberList = mGroupService.deleteGroup(group);
			
			GroupNotifyManager.getIntance().notifyRemoveMember(groupid, username, removeMemberList);
		}
		else
		{
			//群成员退出群组操作
			List<String> list = Lists.newArrayList();
			list.add(username);
			removeMemberList = list;
			mGroupService.updateGroupMember(group, removeMemberList, false);
			
			// 此处更新不需要加锁，后面覆盖旧的
			updateGroupIconAndAlias(group);
			
			GroupNotifyManager.getIntance().notifyRemoveMember(groupid, username, null);
		}
		
		return api.toJSONString();
	}
	
	/**
	 * 邀请好友加入群, 可能是群主，可能是群成员
	 * 群成员邀请需要群主开启邀请权限
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("inviteMembers")
	public String inviteMembers()
	{
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		String[] addMemebers = WebRequest.getStrings("addMemebers", ",");
		String groupid = WebRequest.getString("groupid");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// 为空异常
		int len = addMemebers.length;
		if(addMemebers == null || len <= 0)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		// 一次最多20个, 大于20个非法操作
		if(len > 9)
		{
			api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "一次最多只能添加9个");
			return api.toJSONString();
		}
		
		// group 不存在
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			UserBlackManager.getIntance().blackUser(username);
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		// 群已解散
		if(group.isDelete())
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_DELETE);
			return api.toJSONString();
		}
		
		// 不是管理员或群主
		if(! (username.equalsIgnoreCase(group.getHolder()) || mGroupAdminService.existAdmin(group.getId(), username)))
		{
			// 被封禁，群主禁止他人邀请
			if(!group.isEnableStatus() || !group.isEnableInvite())
			{
				api.setJsonResult(GroupErrorResult.ERR_GROUP_BLACK_OR_NOT_INVITE);
				return api.toJSONString();
			}
		}
		
		// 群组人数限制
		if(group.getCurrentCapacity() + len > group.getMaxCapacity())
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_CAPACITY_LIMIT);
			return api.toJSONString();
		}
		
		//
		for(String memeber : addMemebers)
		{
			// 不能自己邀请自己，并且不能邀请群主
			if(memeber.equalsIgnoreCase(username) || memeber.equalsIgnoreCase(group.getHolder()))
			{
				UserBlackManager.getIntance().blackUser(username);
				api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "不能邀请自己或群主!");
				return api.toJSONString();
			}
			
			if(mGroupRelationService.find(groupid, memeber) != null)
			{
				api.setJsonResult(SystemErrorResult.ERR_EXIST);
				return api.toJSONString();
			}
			
			MyUserInfo memberUserInfo = mAecManager.getUserAec().findUserInfo(memeber);
			//LOG.info("member username ========= " + memeber);
			if(memberUserInfo == null)
			{
				UserBlackManager.getIntance().blackUser(username);
				api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "非法邀请不存在的好友!");
				return api.toJSONString();
			}
			
			// 拉人与被拉人是好友关系才能建立群组关系
			FriendRelation friend = mFriendService.find(username, memberUserInfo.getUsername());
			if(friend == null || !friend.verifyFriend()) 
			{
				api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "对方不是你的好友!");
				return api.toJSONString();
			}
		}
		
		List<String> memberList = Lists.newArrayList();
		for(String member : addMemebers)
		{
			memberList.add(member);
		}
		
		String lockPath = LockManager.createGroupLockPath(groupid);
		ZKClientManager zk = ZKClientManager.getInstanced();
		DistributeLock lock = zk.createLock(lockPath);
		
		try {
			if(lock.lockAcquired(3))
			{
				mGroupService.updateGroupMember(group, memberList, true);
				
				// 更新头像和别名
				updateGroupIconAndAlias(group);
				
				//
				GroupNotifyManager.getIntance().notifyInviteMember(groupid, username);
			}
		} catch (Exception e) {
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
		} finally {
			lock.lockReleased();
		}
		
		return api.toJSONString();
	}
	
	/**
	 * 移除群成员
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("removeMembers")
	public String removeMembers()
	{
		String accessToken = WebRequest.getAccessToken();
		String holder = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		String[] removeMembers = WebRequest.getStrings("removeMembers", ",");
		String groupid = WebRequest.getString("groupid");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// 为空异常
		int len = removeMembers.length;
		if(removeMembers == null || len <= 0)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		// 一次最多20个, 大于20个非法操作
		if(len > 9)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// group 不存在
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 只有群主本人才能操作 或管理员才能操作
		if(! (group.getHolder().equalsIgnoreCase(holder) || mGroupAdminService.existAdmin(group.getId(), holder)))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		List<String> memberList = Lists.newArrayList();
		for(String username : removeMembers)
		{
			if(mGroupRelationService.find(groupid, username) == null)
			{
				api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
				return api.toJSONString();
			}
			
			// 不能删除自己
			if(username.equalsIgnoreCase(group.getHolder()))
			{
				UserBlackManager.getIntance().blackUser(username);
				api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
				return api.toJSONString();
			}
			memberList.add(username);
		}
		
		// 更新昵称
		// 群主+其他两个人作为群名称
		
		String lockPath = LockManager.createGroupLockPath(groupid);
		ZKClientManager zk = ZKClientManager.getInstanced();
		DistributeLock lock = zk.createLock(lockPath);
		
		try {
			if(lock.lockAcquired(3))
			{
				mGroupService.updateGroupMember(group, memberList, false);
				// 更新头像和别名
				updateGroupIconAndAlias(group);
				// 通知
				GroupNotifyManager.getIntance().notifyRemoveMember(groupid, holder, memberList);
			}
		} catch (Exception e) {
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
		} finally {
			lock.lockReleased();
		}
		
		return api.toJSONString();
	}
	
	/**
	 * 群二维码生成
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("createGroupQrcodeInfo")
	public String createGroupQrcodeInfo()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		// 群已解散
		if(group.isDelete())
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_DELETE);
			return api.toJSONString();
		}
		
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		if(!group.getHolder().equalsIgnoreCase(username))
		{
			// 被封禁，群主禁止他人邀请
			if(!group.isEnableStatus() || !group.isEnableInvite())
			{
				api.setJsonResult(GroupErrorResult.ERR_GROUP_BLACK_OR_NOT_INVITE);
				return api.toJSONString();
			}
		}
		
		
		long time = System.currentTimeMillis();
		String sign = GroupHelper.signCreateGroupQrcodeInfo(group.getId(), username, time);
		
		group.handleColumn();
		Map<String, Object> map = Maps.newConcurrentMap();
		map.put("groupid", group.getId());
		map.put("alias", group.getAlias());
		map.put("groupName", group.getName());
		map.put("icon", group.getIcons());
		map.put("createUsername", username);
		map.put("time", time);
		map.put("sign", sign);
		
		api.setData(map);
		return api.toJSONString();
	} 
	
	@MyTokenLimit
	@RequestMapping("joinGroup")
	public String joinGroup()
	{
		String createUsername = WebRequest.getString("createUsername");
		String groupid = WebRequest.getString("groupid");
		long time = WebRequest.getLong("time");
		String sign = WebRequest.getString("sign");
		String accessToken = WebRequest.getAccessToken();
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// 
		String tmpSign = GroupHelper.signCreateGroupQrcodeInfo(groupid, createUsername, time);
		if(!tmpSign.equalsIgnoreCase(sign))
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		// 3600 * 24 * 5 * 1000= 5day
		if(System.currentTimeMillis() - time > 431000000)
		{
			api.setJsonResult(SystemErrorResult.ERR_INVALID);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		// 群已解散
		if(group.isDelete())
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_DELETE);
			return api.toJSONString();
		}
		
		// 被封禁，群主禁止他人邀请
		if(!createUsername.equalsIgnoreCase(group.getHolder()))
		{
			if(!group.isEnableStatus() || !group.isEnableInvite())
			{
				api.setJsonResult(GroupErrorResult.ERR_GROUP_BLACK_OR_NOT_INVITE);
				return api.toJSONString();
			}
		}
		
		// 群组人数限制
		if(group.getCurrentCapacity() + 1 > group.getMaxCapacity())
		{
			api.setJsonResult(GroupErrorResult.ERR_GROUP_CAPACITY_LIMIT);
			return api.toJSONString();
		}
		
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		// 不能自己邀请自己，并且不能邀请群主
		if(createUsername.equalsIgnoreCase(username) || username.equalsIgnoreCase(group.getHolder()))
		{
			UserBlackManager.getIntance().blackUser(username);
			api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "不能邀请自己或群主!");
			return api.toJSONString();
		}
		
		if(mGroupRelationService.find(groupid, username) != null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST);
			return api.toJSONString();
		}
		
		MyUserInfo memberUserInfo = mAecManager.getUserAec().findUserInfo(username);
		//LOG.info("member username ========= " + memeber);
		if(memberUserInfo == null)
		{
			UserBlackManager.getIntance().blackUser(username);
			api.setError(SystemErrorResult.ERR_SYS_OPT_ILEGAL.getCode(), "非法邀请不存在的好友!");
			return api.toJSONString();
		}
		
		List<String> memberList = Lists.newArrayList();
		memberList.add(username);
		
		String lockPath = LockManager.createGroupLockPath(groupid);
		ZKClientManager zk = ZKClientManager.getInstanced();
		DistributeLock lock = zk.createLock(lockPath);
		
		try {
			if(lock.lockAcquired(3))
			{
				mGroupService.updateGroupMember(group, memberList, true);
				
				// 更新头像和别名
				updateGroupIconAndAlias(group);
				
				//
				GroupNotifyManager.getIntance().notifyInviteMember(groupid, username);
			}
		} catch (Exception e) {
			api.setJsonResult(SystemErrorResult.ERR_SYS_BUSY);
		} finally {
			lock.lockReleased();
		}
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getMemberList")
	public String getMemberList()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 非此群组成员不能拉取数据
		GroupRelation groupRelation = mGroupRelationService.find(groupid, username);
		if(groupRelation == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		Map<String, Object> dataMap = Maps.newHashMap();
		
		// 注意此分页配置的注释, 禁止自定义
		List<String> list = mGroupRelationService.queryScrollGroupMember(groupid);
		List<Map<String, Object>> memberList = null;
		if(CollectionUtils.isEmpty(list))
		{
			memberList = Collections.emptyList();
		}
		else
		{
			String groupidKey = "groupid";
			String usernameKey = "username";
			String groupNicknameKey = "groupNickname";
			String userNicknameKey = "userNickname";
			String userAvatarKey = "userAvatar";
			memberList = Lists.newArrayList();
			for(String memberUsername : list)
			{
				GroupRelation relation = mGroupRelationService.find(groupid, memberUsername);
				if(relation == null)
				{
					continue;
				}
				MyUserInfo userInfo = mAecManager.getUserAec().findUserInfo(relation.getUsername());
				
				Map<String, Object> model = Maps.newHashMap();
				model.put(groupidKey, relation.getGroupid());
				model.put(usernameKey, relation.getUsername());
				model.put(groupNicknameKey, relation.getNickname());
				model.put(userNicknameKey, userInfo.getNickname());
				model.put(userAvatarKey, userInfo.getShowAvatar());
				
				memberList.add(model);
			}
		}
		dataMap.put("list", memberList);
		api.setData(dataMap);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateInviteStatus")
	public String updateInviteStatus()
	{
		
		String groupid = WebRequest.getString("groupid");
		boolean inviteStatus = WebRequest.getBoolean("invite");
		
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		if(!username.equalsIgnoreCase(group.getHolder()))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(group.isEnableInvite() != inviteStatus )
		{
			mGroupService.updateInviteStatus(groupid, inviteStatus);
			
			// 提前初始化信息
			group = mGroupService.findById(groupid);
			
			// 通知所有人
			GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, username);
		}
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateAddFriendStatus")
	public String updateAddFriendStatus()
	{
		
		String groupid = WebRequest.getString("groupid");
		boolean status = WebRequest.getBoolean("status");
		
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null || group.isDelete())
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		if(!username.equalsIgnoreCase(group.getHolder()))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(group.isEnableAddFriend() != status )
		{
			mGroupService.updateAddFriendStatus(groupid, status);
			
			// 提前初始化信息
			group = mGroupService.findById(groupid);
			
			// 通知所有人
			GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, username);
		}
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateChatStatus")
	public String updateChatStatus()
	{
		
		String groupid = WebRequest.getString("groupid");
		boolean enableChat = WebRequest.getBoolean("enableChat");
		
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null || group.isDelete())
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		if(!username.equalsIgnoreCase(group.getHolder()))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(group.isEnableChat() != enableChat )
		{
			mGroupService.updateEnableChat(groupid, enableChat);
			
			// 提前初始化信息
			group = mGroupService.findById(groupid);
			
			// 通知所有人
			GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, username);
		}
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getGroupInfo")
	public String getGroupInfo()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		// 理论上要验证只有群成员才能获取群信息，如果需要更安全，则加上验证
		GroupRelation groupRelation = mGroupRelationService.find(groupid, username);
		if(groupRelation == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		Group group = mGroupService.findById(groupid);
		if(group == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		group.handleColumn();
		api.setData(group);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getGroupRelation")
	public String getGroupRelation()
	{
		String groupid = WebRequest.getString("groupid");
		String username = WebRequest.getString("username");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check groupid
		if(StringUtils.isEmpty(groupid))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		GroupRelation groupRelation = mGroupRelationService.find(groupid, username);
		if(groupRelation == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		api.setData(groupRelation);
		return api.toJSONString();
	}
	
	/**
	 * 获取用户加入的群
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("getUserGroupList")
	public String getUserGroupList()
	{
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		ApiJsonTemplate api = new ApiJsonTemplate();
		List<Group> list = mGroupRelationService.queryUserJoinGroup(username);
		if(CollectionUtils.isEmpty(list))
		{
			list = Collections.emptyList();
		}
		else
		{
			List<Group> rsList = Lists.newArrayList();
			for(Group group : list)
			{
				Group rightGroup = mGroupService.findById(group.getId());
				rsList.add(rightGroup);
			}
			// 处理字段相关, 比如icons, 添加前缀
			for(Group group : rsList)
			{
				group.handleColumn();
			}
			list = rsList;
		}
		api.setData(list);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateGroupName")
	public String updateGroupName()
	{
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		String groupid = WebRequest.getString("groupid");
		String name = WebRequest.getString("name");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(StringUtils.isEmpty(name))
		{
			api.setError(SystemErrorResult.ERR_NODATA.getCode(), "用户名不能为空!");
			return api.toString();
		}
		
		Group groupInfo = mGroupService.findById(groupid);
		if(groupInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		if(!groupInfo.getHolder().equalsIgnoreCase(username))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_DEL);
			return api.toJSONString();
		}
		
		mGroupService.updateName(groupid, name);
		
		// 提前初始化信息
		groupInfo = mGroupService.findById(groupid);
		
		// 通知所有人
		GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, username);
		
		api.setData(groupInfo);
		
		return api.toJSONString();
	}
	
	private void updateGroupIconAndAlias(Group group)
	{
			List<String> pageMemberList = mGroupRelationService.queryScrollGroupMember(group.getId());
			
			// 更新昵称
			// 群主+其他两个人作为群名称
			StringBuffer groupAliasBuffer = new StringBuffer();
			StringBuffer groupIconBuffer = new StringBuffer();
			
			MyUserInfo holderInfo = mAecManager.getUserAec().findUserInfo(group.getHolder());
			groupAliasBuffer.append(holderInfo.getShowName());
			groupIconBuffer.append(holderInfo.getAvatar());
			
			// 其他两个人作为群名称
			int topMemberLen = 0;
			if(!CollectionUtils.isEmpty(pageMemberList))
			{
				topMemberLen = pageMemberList.size();
			}
			
			int index = 0;
			for(int i = 0; i < topMemberLen; i ++)
			{
				String username = pageMemberList.get(i);
				if(username.equalsIgnoreCase(group.getHolder()))
				{
					continue;
				}
				MyUserInfo memberInfo = mAecManager.getUserAec().findUserInfo(username);
				if(memberInfo == null)
				{
					continue;
				}
				
				// 初始化昵称
				if(index < 2)
				{
					groupAliasBuffer.append("、");
					groupAliasBuffer.append(memberInfo.getShowName());
				}
				
				if(index < 9)
				{
					// 初始化图标
					groupIconBuffer.append(",");
					groupIconBuffer.append(memberInfo.getAvatar());
				}
				else
				{
					break;
				}
				
				index ++;
			}
			
			mGroupService.updateIconsAndAlias(group, groupIconBuffer.toString(), groupAliasBuffer.toString());
	}

}
