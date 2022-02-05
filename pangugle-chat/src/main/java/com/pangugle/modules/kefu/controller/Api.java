package com.pangugle.modules.kefu.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ApiJsonTemplate;
import com.pangugle.im.logical.AecManager;
import com.pangugle.im.model.MyUserInfo;

@RestController
@RequestMapping("/kefu/api")
public class Api {
	
	@RequestMapping("/getKefuList")
	public String getKefuList()
	{
		ApiJsonTemplate template = new ApiJsonTemplate();
		
		List<Map<String, Object>> list = Lists.newArrayList();
		for(int i = 1; i <= 1; i ++)
		{
			String username = "kefu0" + i;
			MyUserInfo userInfo = AecManager.getInstance().getUserAec().findUserInfo(username);
			if(userInfo == null)
			{
				continue;
			}
			Map<String, Object> data = Maps.newHashMap();
			data.put("username", userInfo.getUsername());
			data.put("nickname", userInfo.getNickname());
			data.put("avatar", userInfo.getShowAvatar());
			list.add(data);
		}
		template.setData(list);
		return template.toJSONString();
	}
	

}
