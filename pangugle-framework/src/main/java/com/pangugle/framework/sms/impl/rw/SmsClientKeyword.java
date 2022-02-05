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
package com.pangugle.framework.sms.impl.rw;

import java.net.URLEncoder;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

/**
 * <p>
 * <date>2012-03-01</date><br/>
 * <span>软维提供的JAVA接口信息（短信，彩信）调用API</span><br/>
 * <span>----------非法关键字/屏蔽字符查询--暂未提供-------------</span>
 * </p>
 * 
 * @author LIP
 * @version 1.0.1
 */
public class SmsClientKeyword {
	
	
	private static Log LOG = LogFactory.getLog(SmsClientKeyword.class);

	/**
	 * /**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>是否包含关键字获取方法1--必须传入必填内容</span><br/>
	 * <p>
	 * 其一：发送方式，默认为POST<br/>
	 * 其二：发送内容编码方式，默认为UTF-8
	 * </p>
	 * <br/>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/sms.aspx
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @param checkWord
	 *            ：必填--需要检查的字符串--比如：这个字符串中是否包含了屏蔽字
	 * @return 返回需要查询的字符串中是否包含关键字
	 */
	public static String queryKeyWord(String url, String userid,
			String account, String password, String checkWord) {

		try {
			StringBuffer sendParam = new StringBuffer();
			sendParam.append("action=checkkeyword");
			sendParam.append("&userid=").append(userid);
			sendParam.append("&account=").append(
					URLEncoder.encode(account, "UTF-8"));
			sendParam.append("&password=").append(
					URLEncoder.encode(password, "UTF-8"));
			if (checkWord != null && !checkWord.equals("")) {
				sendParam.append("&content=").append(
						URLEncoder.encode(checkWord, "UTF-8"));
			} else {
				return "需要检查的字符串不能为空";
			}
			return SmsClientAccessTool.getInstance().doAccessHTTPPost(url,
					sendParam.toString(), "UTF-8");
		} catch (Exception e) {
			LOG.error("encode error:", e);
			return "未发送，异常-->" + e.getMessage();
		}
	}
}
