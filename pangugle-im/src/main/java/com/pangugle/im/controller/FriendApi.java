package com.pangugle.im.controller;

import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.CollectionUtils;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.FriendErrorResult;
import com.pangugle.im.MyConstants;
import com.pangugle.im.limit.MyTokenLimit;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.logical.FriendNotifyManager;
import com.pangugle.im.model.FriendInfo;
import com.pangugle.im.model.FriendRelation;
import com.pangugle.im.model.FriendRelation.MyStatus;
import com.pangugle.im.model.MyUserInfo;
import com.pangugle.im.service.FriendInfoService;
import com.pangugle.im.service.FriendRelationService;

@RequestMapping(MyConstants.DEFAULT_IM_MODULE_NAME + "/friendApi")
@RestController
public class FriendApi {
	
	private static Log LOG = LogFactory.getLog(FriendApi.class);
	
	@Autowired 
	private FriendRelationService mFriendRelationService;
	
	@Autowired
	private FriendInfoService mFriendInfoService;
	
	private AecManager mAecManager = AecManager.getInstance();

	@MyTokenLimit
	@RequestMapping("addFriend")
	public String addFriendRequest()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		String remark = WebRequest.getString("remark");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		// check friend
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			// 这里可认为添加人是异常操作，可封禁添加人
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 被添加人被封号
		if(!friendUserInfo.isEnableStatus())
		{
			api.setJsonResult(FriendErrorResult.ERR_ACCOUNT_DISABLED);
			return api.toJSONString();
		}
		
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		MyUserInfo selfUserInfo = mAecManager.getUserAec().findUserInfo(selfUsername);
		if(!mFriendRelationService.checkAdd(selfUserInfo.getUsername()))
		{
			// 好友数已上限
			api.setJsonResult(FriendErrorResult.ERR_FRIEND_ADD_MAX_LIMIT_SELF);
			return api.toJSONString();
		}
		
		// 对方好友数已上限
		if(!mFriendRelationService.checkAdd(friendUsername))
		{
			api.setJsonResult(FriendErrorResult.ERR_FRIEND_ADD_MAX_LIMIT_TARGET);
			return api.toJSONString();
		}
		
		// 对方待确认列表超过上限
		List<String> unconfirmList = mFriendRelationService.queryUnConfirmList(friendUsername);
		if(unconfirmList.size() > 10)
		{
			api.setJsonResult(FriendErrorResult.ERR_FRIEND_ADD_MAX_LIMIT_TARGET);
			return api.toJSONString();
		}
		
		// 获取对好友的设置信息
		FriendInfo friendInfo = mFriendInfoService.find(selfUsername, friendUsername);
		if(friendInfo != null && friendInfo.isBlack())
		{
			api.setJsonResult(FriendErrorResult.ERR_FRIEND_ADD_BY_LIMIT);
			return api.toJSONString();
		}
		
		boolean isError = false;
		FriendRelation friend = mFriendRelationService.find(selfUserInfo.getUsername(), friendUserInfo.getUsername());
		if(friend != null)
		{
			if(friend.verifyFriend())
			{
				// 已添加为好友
				api.setJsonResult(SystemErrorResult.ERR_EXIST);
				return api.toJSONString();
			}
			// 自己是from
			if(friend.getFromUserid().equalsIgnoreCase(selfUsername))
			{
				// 对比已确认
				if(FriendRelation.MyStatus.enable.getName().equalsIgnoreCase(friend.getToStatus()))
				{
					// 立即成为好友
					mFriendRelationService.updateStatus(selfUsername, friendUserInfo.getUsername(), selfUsername, MyStatus.enable);
					
					// 已添加为好友
					api.setJsonResult(SystemErrorResult.ERR_EXIST);
					//return api.toJSONString();
					isError = true;
				}
			}
			// 自己在to部分
			else
			{
				if(FriendRelation.MyStatus.enable.getName().equalsIgnoreCase(friend.getFromStatus()))
				{
					// 立即成为好友
					mFriendRelationService.updateStatus(selfUsername, friendUserInfo.getUsername(), selfUsername, MyStatus.enable);
					
					// 已添加为好友
					api.setJsonResult(SystemErrorResult.ERR_EXIST);
					isError = true;
					//return api.toJSONString();
				}
			}
			
			// 请求已添加
			FriendNotifyManager.getIntance().sendUnconfirmNotification(selfUsername, friendUsername);
			if(!isError)
			{
				api.setJsonResult(SystemErrorResult.SUCCESS);
			}
			return api.toJSONString();
		}
		
		// 添加好友请求
		mFriendRelationService.bindRelation(selfUserInfo.getUsername(), friendUserInfo.getUsername(), selfUserInfo.getUsername(), remark);
		
		// 通知被添加人
		FriendNotifyManager.getIntance().sendUnconfirmNotification(selfUsername, friendUsername);
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("enableFriend")
	public String enableFriend()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		// check friend
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			// 这里可认为添加人是异常操作，可封禁添加人
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		MyUserInfo selfUserInfo = mAecManager.getUserAec().findUserInfo(selfUsername);
		
	
		
		FriendRelation friend = mFriendRelationService.find(selfUserInfo.getUsername(), friendUserInfo.getUsername());
		if(friend == null)
		{
			// 没有朋友关系，
			// 这里可认为添加人是异常操作，可封禁添加人
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		if(friend.verifyFriend())
		{
			// 已添加为好友
			api.setJsonResult(SystemErrorResult.ERR_EXIST);
			return api.toJSONString();
		}
		
		// 自己是from
		if(friend.getFromUserid().equalsIgnoreCase(selfUsername))
		{
			if(!FriendRelation.MyStatus.enable.getName().equalsIgnoreCase(friend.getFromStatus()))
			{
				mFriendRelationService.updateStatus(selfUserInfo.getUsername(), friendUserInfo.getUsername(), selfUserInfo.getUsername(), FriendRelation.MyStatus.enable);
				
				// 通知被添加人
				FriendNotifyManager.getIntance().sendEnableNotification(selfUsername, friendUsername);
			}
		}
		// 自己在to部分
		else
		{
			if(!FriendRelation.MyStatus.enable.getName().equalsIgnoreCase(friend.getToStatus()))
			{
				mFriendRelationService.updateStatus(selfUserInfo.getUsername(), friendUserInfo.getUsername(), selfUserInfo.getUsername(), FriendRelation.MyStatus.enable);
				
				// 通知被添加人
				FriendNotifyManager.getIntance().sendEnableNotification(selfUsername, friendUsername);
			}
		}
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("deleteFriend")
	public String deleteFriend()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		// check friend
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			// 这里可认为添加人是异常操作，可封禁添加人
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		MyUserInfo selfUserInfo = mAecManager.getUserAec().findUserInfo(selfUsername);
		
		
		FriendRelation friend = mFriendRelationService.find(selfUserInfo.getUsername(), friendUserInfo.getUsername());
		if(friend == null)
		{
			// 没有朋友关系，
			// 这里可认为添加人是异常操作，可封禁添加人
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		// 删除
		mFriendRelationService.deleteRelation(selfUserInfo.getUsername(), friendUserInfo.getUsername());
		
		FriendNotifyManager.getIntance().sendDeleteNotification(selfUserInfo.getUsername(), friendUserInfo.getUsername());
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("queryUnConfirmList")
	public String queryUnConfirmList()
	{
		//WebRequest.logForm();
		String accessToken = WebRequest.getAccessToken();
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// check self
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		// fetch data list
		List<String> list = mFriendRelationService.queryUnConfirmList(username);
		if(CollectionUtils.isEmpty(list))
		{
			list = Lists.emptyList();
			
			Map<String, Object> dataMap = Maps.newHashMap();
			dataMap.put("list", list);
			api.setData(dataMap);
			
			return api.toJSONString();
		}
		
		List<JSONObject> rsList = Lists.newArrayList();
		
		// 提取数据要控制在ms内
		String usernameKey = "username";
		String nicknameKey = "nickname";
		String remarkKey = "remark";
		String avatarKey = "avatar";
		for(String friendUserName : list)
		{
			// 添加好友请求时，要提取备注信息，如我是谁谁谁。。。
			FriendRelation friend = mFriendRelationService.find(username, friendUserName);
			if(friend == null)
			{
				LOG.warn("un not relation for " + friendUserName);
				continue;
			}
						
			MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUserName);
			
			JSONObject json = new JSONObject();
			json.put(usernameKey, friendUserName);
			json.put(nicknameKey, friendUserInfo.getNickname());
			json.put(avatarKey, friendUserInfo.getShowAvatar());
			
			
			json.put(remarkKey, friend.getRemark());
			
			rsList.add(json);
		}
		
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("list", rsList);
		api.setData(dataMap);
		return api.toJSONString();
	}
	
	/**
	 * 获取好友列表
	 */
	@MyTokenLimit
	@RequestMapping("queryFriendList")
	public String queryFriendList()
	{
		//WebRequest.logForm();
		String accessToken = WebRequest.getAccessToken();
//		long page = WebRequest.getLong("page");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		// 
//		if(page <= 0 || page > FriendRelation.DEFAULT_TOTAL_PAGE)
//		{
//			// 状态不对，就是非法操作
//			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
//			return api.toJSONString();
//		}
		
		// check self
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		MyUserInfo userInfo = mAecManager.getUserAec().findUserInfo(username);
		
		// fetch data list
		List<String> list = mFriendRelationService.queryFriendList(userInfo.getUsername());
		if(CollectionUtils.isEmpty(list))
		{
			list = Lists.emptyList();
			
			Map<String, Object> dataMap = Maps.newHashMap();
			dataMap.put("list", list);
//			dataMap.put("pagesize", GroupManager.DEFAULT_GROUP_MEMBER_PAGE_SIZE);
			api.setData(dataMap);
			
			return api.toJSONString();
		}
		
		List<JSONObject> rsList = Lists.newArrayList();
		
		// 提取数据要控制在ms内
		String usernameKey = "username";
		String nicknameKey = "nickname";
		String avatarKey = "avatar";
		
		String aliasKey = "alias";
		String blackKey = "black";
		String mobileKey = "mobile";
		String remarkKey = "remark";
		
		for(String friendUserName : list)
		{
			MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUserName);
			
			JSONObject json = new JSONObject();
			json.put(usernameKey, friendUserName);
			json.put(nicknameKey, friendUserInfo.getNickname());
			json.put(avatarKey, friendUserInfo.getShowAvatar());
			
			// get friendinfo
			FriendInfo myFriendInfo = mFriendInfoService.find(username, friendUserName);
			json.put(blackKey, myFriendInfo.isBlack());
			json.put(aliasKey, myFriendInfo.getAlias());
			json.put(mobileKey, myFriendInfo.getMobile());
			json.put(remarkKey, myFriendInfo.getRemark());
			
			rsList.add(json);
		}
		
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("list", rsList);
		api.setData(dataMap);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getRelation")
	public String getRelation()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		FriendRelation relation = mFriendRelationService.find(selfUsername, friendUsername);
		if(relation == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		Map<String, Object> dataMap = Maps.newHashMap();
		dataMap.put("fromUserid", relation.getFromUserid());
		dataMap.put("fromStatus", relation.getFromStatus());
		dataMap.put("toUserid", relation.getFromUserid());
		dataMap.put("toStatus", relation.getToStatus());
		dataMap.put("remark", relation.getRemark());
		
		api.setData(dataMap);
		
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateFriendInfo")
	public String updateFriendInfo()
	{
		String friendUsername = WebRequest.getString("friendUsername");
		String alias = WebRequest.getString("alias");
		String mobile = WebRequest.getString("mobile");
		String remark = WebRequest.getString("remark");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if(!StringUtils.isEmpty(alias) && alias.length() > 50)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if(!StringUtils.isEmpty(mobile) && mobile.length() > 20)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		if(!StringUtils.isEmpty(remark) && remark.length() > 100)
		{
			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
			return api.toJSONString();
		}
		
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		FriendInfo friendInfo = mFriendInfoService.find(username, friendUsername);
		if(friendInfo == null)
		{
			mFriendInfoService.add(username, friendUsername, alias, mobile, remark);
		}
		else
		{
			mFriendInfoService.update(username, friendUsername, alias, mobile, remark);
		}
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("getFriendInfo")
	public String getFriendInfo()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		// get user info from user
		MyUserInfo userInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(userInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
				
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		Map<String, Object> data = Maps.newHashMap();
		data.put("username", userInfo.getUsername());
		data.put("nickname", userInfo.getNickname());
		data.put("avatar", userInfo.getShowAvatar());
		
		// get friendinfo
		FriendInfo friendInfo = mFriendInfoService.find(selfUsername, friendUsername);
		data.put("black", friendInfo.isBlack());
		data.put("alias", friendInfo.getAlias());
		data.put("mobile", friendInfo.getMobile());
		data.put("remark", friendInfo.getRemark());
		
		api.setData(data);
		return api.toJSONString();
	}
	
	@MyTokenLimit
	@RequestMapping("updateFriendInfoBlack")
	public String updateFriendInfoBlack()
	{
		String accessToken = WebRequest.getAccessToken();
		String friendUsername = WebRequest.getString("friendUsername");
		boolean black = WebRequest.getBoolean("black");
		
		ApiJsonTemplate api = new ApiJsonTemplate();
		
//		if(!mAecManager.getUserRegexAec().checkUsername(friendUsername))
//		{
//			api.setJsonResult(SystemErrorResult.ERR_PARAMS);
//			return api.toJSONString();
//		}
		
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		if(!friendUserInfo.isEnableStatus())
		{
			api.setJsonResult(FriendErrorResult.ERR_ACCOUNT_DISABLED);
			return api.toJSONString();
		}
		
		// check self
		String selfUsername = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		
		// 获取对好友的设置信息
		FriendInfo friendInfo = mFriendInfoService.find(selfUsername, friendUsername);
		if(friendInfo != null && friendInfo.isBlack() == black)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_ILEGAL);
			return api.toJSONString();
		}
		
		boolean rs = mFriendInfoService.black(selfUsername, friendUsername, black);
		if(!rs)
		{
			api.setJsonResult(SystemErrorResult.ERR_SYS_OPT_FAILURE);
			return api.toJSONString();
		}
		FriendNotifyManager.getIntance().sendBlackNotification(selfUsername, friendUsername);
		return api.toJSONString();
	}
	
	
	/**
	 * 我被哪些人拉的列表
	 * @return
	 */
	@MyTokenLimit
	@RequestMapping("getBlackSelfStatus")
	public String getBlackSelfStatus()
	{
		String friendUsername = WebRequest.getString("friendUsername");
		String accessToken = WebRequest.getAccessToken();
		String username = mAecManager.getAuthAec().getAccountByAccessToken(accessToken);
		ApiJsonTemplate api = new ApiJsonTemplate();
		
		MyUserInfo friendUserInfo = mAecManager.getUserAec().findUserInfo(friendUsername);
		if(friendUserInfo == null)
		{
			api.setJsonResult(SystemErrorResult.ERR_EXIST_NOT);
			return api.toJSONString();
		}
		
		boolean rs = mFriendInfoService.getBlackSelfStatus(friendUsername, username);
		api.setData(rs);
		return api.toJSONString();
	}
}
