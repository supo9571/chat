package com.pangugle.im.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.MyConstants;
import com.pangugle.im.limit.MyTokenLimit;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.logical.GroupChatPermissionManager;
import com.pangugle.im.logical.GroupNotifyManager;
import com.pangugle.im.model.Group;
import com.pangugle.im.model.GroupMsgType;
import com.pangugle.im.model.GroupRelation;
import com.pangugle.im.service.GroupAdminService;
import com.pangugle.im.service.GroupRelationService;
import com.pangugle.im.service.GroupService;

@RequestMapping(MyConstants.DEFAULT_IM_MODULE_NAME + "/groupApi")
@RestController
public class GroupAdminApi {
	
//	private static Log LOG = LogFactory.getLog(GroupApi.class);
	
//	@Autowired 
//	private FriendRelationService mFriendService;

	@Autowired
	private GroupService mGroupService;
	
	@Autowired
	private GroupRelationService mGroupRelationService;
	
	@Autowired
	private GroupAdminService mGroupAdminService;
	
	private AecManager mAecManager = AecManager.getInstance();
	
		
	@MyTokenLimit
	@RequestMapping("updateGroupMemberChatDisabledTime")
	public String updateGroupMemberChatDisabledTime()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String targetUsername = WebRequest.getString("targetUsername");
		long hours = WebRequest.getLong("hours");
		hours = Math.min(hours, 24); // 最大24小时
		
		String optUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(StringUtils.isEmpty(targetUsername))
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
		
		if(groupInfo.getHolder().equalsIgnoreCase(targetUsername))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 管理员 才能操作
		if(!groupInfo.getHolder().equalsIgnoreCase(optUsername))
		{
			if(!mGroupAdminService.existAdmin(groupid, optUsername))
			{
				api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
				return api.toJSONString();
			}
		}
		
		// 群主自己操作自己，操作禁止
		if(optUsername.equalsIgnoreCase(targetUsername))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(hours == 0)
		{
			// 表示解除禁言
			GroupChatPermissionManager.getIntance().disableChat(groupid, targetUsername, 0);
		}
		else
		{
			long currentTime = System.currentTimeMillis();
			long disabledTime = currentTime + hours * 3600000;
			GroupChatPermissionManager.getIntance().disableChat(groupid, targetUsername, disabledTime);
		}
		
		
		// 通知被执行人的客户端刷新数据
		GroupNotifyManager.getIntance().send(optUsername, groupid, targetUsername, GroupMsgType.UPDATE_GROUP_INFO);
		api.setData(true);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getGroupMemberChatDisableTime")
	public String getGroupMemberChatDisableTime()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(StringUtils.isEmpty(username))
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
		
		if(groupInfo.getHolder().equalsIgnoreCase(username))
		{
			api.setData(true);
			return api.toJSONString();
		}
		
		long time = GroupChatPermissionManager.getIntance().getDisabledTime(groupid, username);
		api.setData(time);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("addGroupAdmin")
	public String actionToAddGroupAdmin()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String username = WebRequest.getString("username");
		
		String optUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(StringUtils.isEmpty(username))
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
		
		// 不能对自己操作
		if(groupInfo.getHolder().equalsIgnoreCase(username))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 群主才能操作
		if(!groupInfo.getHolder().equalsIgnoreCase(optUsername))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		List<String> adminlist = mGroupAdminService.queryAll(groupid);
		for(String admin : adminlist)
		{
			if(admin.equalsIgnoreCase(username))
			{
				api.setJsonResult(SystemErrorResult.ERR_EXIST);
				return api.toJSONString();
			}
		}
		
		if(adminlist.size() >= 5)
		{
			api.setError(SystemErrorResult.ERR_CUSTOM.getCode(), "管理员最多只能设置5个!");
			return api.toJSONString();
		}
		
		mGroupAdminService.addAdmin(groupid, username);
		
		// 通知所有人
		GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, optUsername);
		api.setData(true);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("deleteGroupAdmin")
	public String actionToDeleteGroupAdmin()
	{
		String accessToken = WebRequest.getAccessToken();
		String groupid = WebRequest.getString("groupid");
		String username = WebRequest.getString("username");
		
		String optUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		if(StringUtils.isEmpty(username))
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
		
		// 不能对自己操作
		if(groupInfo.getHolder().equalsIgnoreCase(username))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 群主才能操作
		if(!groupInfo.getHolder().equalsIgnoreCase(optUsername))
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(!mGroupAdminService.existAdmin(groupid, username))
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		mGroupAdminService.deleteById(groupid, username);
		
		// 通知所有人
		GroupNotifyManager.getIntance().notifyGroupInfoUpdate(groupid, optUsername);
		api.setData(true);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getGroupAdminList")
	public String actionToGetGroupAdminList()
	{
		String groupid = WebRequest.getString("groupid");
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		ApiJsonTemplate api = new ApiJsonTemplate();
		GroupRelation relation = mGroupRelationService.find(groupid, username);
		List<String> list = Collections.emptyList();
		if(relation != null)
		{
			list = mGroupAdminService.queryAll(groupid);
		}
		api.setData(list);
		return api.toJSONString();
	}
	
}
