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
 * <span>----------发送短信-------------</span>
 * </p>
 * 
 * @author LIP
 * @version 1.0.1
 */
public class SmsClientSend {
	
	private static Log LOG = LogFactory.getLog(SmsClientSend.class);

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>发送信息方法1--必须传入必填内容</span><br/>
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
	 * @param mobile
	 *            ：必填--发送的手机号码，多个可以用逗号隔比如>13512345678,13612345678
	 * @param content
	 *            ：必填--实际发送内容，
	 * @return 返回发送信息之后返回字符串
	 */
	public static String sendSms(String url, String userid, String account,
			String password, String mobile, String content) {

		return sendSms(url, userid, account, password, mobile, content, null,
				null, null, null, null, null, null, "POST", "UTF-8", "UTF-8");
	}

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>发送信息方法--暂时私有化，这里仅仅是提供用户接口而已。其实用不了那么复杂</span><br/>
	 * <span>发送信息最终的组合形如：http://118.145.30.35/sms.aspx?action=send</span>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/sms.aspx
	 * 
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @param mobile
	 *            ：必填--发送的手机号码，多个可以用逗号隔比如>13512345678,13612345678
	 * @param content
	 *            ：必填--实际发送内容，
	 * @param action
	 *            ：选填--访问的事件，默认为send
	 * @param sendTime
	 *            ：选填--定时发送时间，不填则为立即发送，时间格式如>2011-11-11 11:11:11
	 * @param checkContent
	 *            ：选填--检查是否包含非法关键字，1--表示需要检查，0--表示不检查
	 * @param taskName
	 *            ：选填--任务名称，本次任务描述，100字内
	 * @param countNumber
	 *            ：选填--提交号码总数
	 * @param mobileNumber
	 *            ：选填--手机号码总数
	 * @param telephoneNumber
	 *            ：选填--小灵通（和）或座机总数
	 * @param sendType
	 *            ：选填--发送方式，默认为POST
	 * @param codingType
	 *            ：选填--发送内容编码方式，默认为UTF-8
	 * @param backEncodType
	 *            ：选填--返回内容编码方式，默认为UTF-8
	 * @return 返回发送之后收到的信息
	 */
	private static String sendSms(String url, String userid, String account,
			String password, String mobile, String content, String action,
			String sendTime, String checkContent, String taskName,
			String countNumber, String mobileNumber, String telephoneNumber,
			String sendType, String codingType, String backEncodType) {

		try {
			if (codingType == null || codingType.equals("")) {
				codingType = "UTF-8";
			}
			if (backEncodType == null || backEncodType.equals("")) {
				backEncodType = "UTF-8";
			}
			StringBuffer send = new StringBuffer();
			if (action != null && !action.equals("")) {
				send.append("action=").append(action);
			} else {
				send.append("action=send");
			}

			send.append("&userid=").append(userid);
			send.append("&account=").append(
					URLEncoder.encode(account, codingType));
			send.append("&password=").append(
					URLEncoder.encode(password, codingType));
			send.append("&mobile=").append(mobile);
			send.append("&content=").append(
					URLEncoder.encode(content, codingType));
			if (sendTime != null && !sendTime.equals("")) {
				send.append("&sendTime=").append(
						URLEncoder.encode(sendTime, codingType));
			}
			if (checkContent != null && !checkContent.equals("")) {
				send.append("&checkContent=").append(checkContent);
			}
			if (taskName != null && !taskName.equals("")) {
				send.append("&taskName=").append(
						URLEncoder.encode(taskName, codingType));
			}
			if (countNumber != null && !countNumber.equals("")) {
				send.append("&countNumber=").append(countNumber);
			}
			if (mobileNumber != null && !mobileNumber.equals("")) {
				send.append("&mobileNumber=").append(mobileNumber);
			}
			if (telephoneNumber != null && !telephoneNumber.equals("")) {
				send.append("&telephoneNumber=").append(telephoneNumber);
			}

			if (sendType != null && (sendType.toLowerCase()).equals("get")) {
				return SmsClientAccessTool.getInstance().doAccessHTTPGet(
						url + "?" + send.toString(), backEncodType);
			} else {
				return SmsClientAccessTool.getInstance().doAccessHTTPPost(url,
						send.toString(), backEncodType);
			}
		} catch (Exception e) {
			LOG.error("send error:", e);
			return "未发送，编码异常";
		}
	}

}
