/*
 * Copyright (C) 2019  即时通讯网(www.pangugle.com) & Jack Pangugle.
 * The pangugle project. All rights reserved.
 * 
 * 【本产品为著作权产品，合法授权后请放心使用，禁止外传！】
 * 【本次授权给：<xxx网络科技有限公司>，授权编号：<授权编号-xxx>】
 * 
 * 本系列产品在国家版权局的著作权登记信息如下：
 * 1）国家版权局登记名（简称）和证书号：Project_name（软著登字第xxxxx号）
 * 著作权所有人：厦门盘古网络科技有限公司
 * 
 * 违法或违规使用投诉和举报方式：
 * 联系邮件：2624342267@qq.com
 * 联系微信：pangugle
 * 联系QQ：2624342267
 * 官方社区：http://www.pangugle.com
 */
package com.pangugle.framework.bean;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import com.pangugle.framework.context.MyEnvironment;

public class OrderProperties extends Properties{
	
	private static final long serialVersionUID = 1L;
	
	
	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();
 
    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }
 
    public Object put(Object key, Object value) {
    	if(key == null) return null;
        keys.add(key);
        return super.put(key, value);
    }
 
    public Set<Object> keySet() {
        return keys;
    }
 
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();
 
        for (Object key : this.keys) {
            set.add((String) key);
        }
 
        return set;
    }
    
    public static void main(String[] args)
    {
    	OrderProperties config = new OrderProperties();
    	MyEnvironment.loadConf(config, "menu.cfg");
    	
		Set<String> keys = config.stringPropertyNames();
		String rootKey = null;
		for(String key : keys)
		{
			if(key.startsWith("root_"))
			{
				rootKey = key.split("_")[1];
				String name = config.getProperty(key);
				System.out.println(key + " = " + name);
			}
			else if(key.startsWith(rootKey))
			{
				String name = config.getProperty(key);
				String url = "/admin/" + key;
				String permission = key + "_list";
				System.out.println(key + " = " + name + ", url = " + url + ", permission = " + permission);
			}
			else
			{
				throw new RuntimeException("菜单配置顺序异常,请先配置一级菜单,再配置二级菜单, for " + key);
			}
		}
    }


}
