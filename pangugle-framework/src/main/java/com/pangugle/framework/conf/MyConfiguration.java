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
package com.pangugle.framework.conf;

import java.util.Properties;
import java.util.Set;

import com.pangugle.framework.context.MyEnvironment;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.StringUtils;

public class MyConfiguration {
	
	private static Log LOG = LogFactory.getLog(MyConfiguration.class);

    private static final Properties src = new Properties();

    private interface MyConfigurationInternal {
        public static MyConfiguration conf = new MyConfiguration();
    }

    public static MyConfiguration getInstance() {
        return MyConfigurationInternal.conf;
    }

    private MyConfiguration() {
        synchronized (MyConfiguration.class) {
            if (src.isEmpty()) {
                MyEnvironment.loadConf(src, "site-default.cfg");
                MyEnvironment.loadConf(src, "site-" + MyEnvironment.getEnv() + ".cfg");
                
                MyEnvironment.loadConf(src, "/etc/mywg/custom-site-prod.cfg");
                
                String cfgFile = System.getProperty("site_config_file");
                if(!StringUtils.isEmpty(cfgFile))
                {
                	MyEnvironment.loadConf(src, cfgFile);
                }
            }
        }
    }

    public String getString(String key) {
        return src.getProperty(key);
    }

    public String[] getStrings(String key) {
        return src.getProperty(key).split(",");
    }

    public String getString(String key, String def) {
        return src.getProperty(key, def);
    }

    public boolean getBoolean(String key) {
        String value = src.getProperty(key);
        return StringUtils.asBoolean(value);
    }

    public boolean getBoolean(String key, boolean def) {
        String value = src.getProperty(key);
        return StringUtils.isEmpty(value) ? def : StringUtils.asBoolean(value);
    }

    public int getInt(String key, int def) {
        String value = src.getProperty(key);
        return StringUtils.isEmpty(value) ? def : StringUtils.asInt(value);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    public float getFloat(String key, float def) {
        String value = src.getProperty(key);
        return StringUtils.isEmpty(value) ? def : StringUtils.asFloat(value);
    }

    public void test()
    {
    	Set<Object> keyset = src.keySet();
    	for(Object key : keyset)
    	{
    		Object value = src.get(key);
    		System.out.println(key + "=" + value);
    	}
    }
    
    public static void main(String[] args) {
    	
    	MyConfiguration conf = MyConfiguration.getInstance();
    	String port = conf.getString("bootstrap.global.admin.server.port");
    	System.out.println("aaaaaaaaaaaaaaaa" + port);
    	
    	LOG.error("============");
    }


}
