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
package com.pangugle.framework.sms.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.http.HttpCallback;
import com.pangugle.framework.http.HttpMediaType;
import com.pangugle.framework.http.HttpSesstionManager;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.sms.SmsHXModel;
import com.pangugle.framework.sms.SmsService;
import com.pangugle.framework.utils.MD5;
import com.pangugle.framework.utils.StringUtils;

import net.sf.json.JSONObject;
import okhttp3.Request;
import okhttp3.Response;

public class HXSmsServiceImpl implements SmsService {

    private static final String URL_FORMAT = "utf-8";

    private static final Log LOG = LogFactory.getLog(HXSmsServiceImpl.class);
    private static final HttpSesstionManager mHttpManager = new HttpSesstionManager(10, 300, 100);

    private String mAccount;
    private String mPassword;
    private String mServerURL;
    private String mContent = "(动态验证码)，请在10分钟内填写。请不要把验证码泄漏给其他人【拾七互联】";

    public HXSmsServiceImpl(MyConfiguration conf) {
        this.mAccount = conf.getString("sms.hxsoft.account");
        this.mPassword = conf.getString("sms.hxsoft.password");
        this.mServerURL = conf.getString("sms.hxsoft.server");
    }

    private static String urlEncode(String content) {
        String urlencContent = null;
        try {
            urlencContent = URLEncoder.encode(content, URL_FORMAT);
        } catch (UnsupportedEncodingException e) {
            LOG.error("encode error:", e);
        }
        return urlencContent;
    }

    private static String urlDecode(String content) {
        String urlencContent = null;
        try {
            urlencContent = URLDecoder.decode(content, URL_FORMAT);
        } catch (UnsupportedEncodingException e) {
            LOG.error("encode error:", e);
        }
        return urlencContent;
    }

    public void send(String mobiles, String content, Callback<Boolean> callback) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        String password = MD5.encode(mPassword);
        parameter.put("userid", "");
        parameter.put("account", mAccount);
        parameter.put("password", password);
        parameter.put("mobile", mobiles);
        parameter.put("content", content + mContent);
        parameter.put("sendTime", "");
        parameter.put("action", "send");
        parameter.put("extno", "");
        mHttpManager.asyncPost(mServerURL, parameter, HttpMediaType.FORM, new HttpCallback() {

            @Override
            public void onFailure(Throwable e) {
                LOG.error("send sms error:", e);
                if (callback != null) callback.execute(false);
            }

            @Override
            public void onSuccess(Request request, Response response, byte[] data) {
                String rs = urlDecode(new String(data));
                LOG.info("=====>" + rs);
                //nofiticationStatus();
                SmsHXModel smsHXModel = (SmsHXModel) JSONObject.toBean(JSONObject.fromObject(rs), SmsHXModel.class);
                if ("Success".equals(smsHXModel.getReturnstatus())) {
                    if (callback != null) callback.execute(true);
                } else {
                    if (callback != null) callback.execute(false);
                }
            }
        });
    }

    public String send(String mobiles, String content) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        String password = MD5.encode(mPassword);
        parameter.put("userid", "");
        parameter.put("account", mAccount);
        parameter.put("password", password);
        parameter.put("mobile", mobiles);
        parameter.put("content", content + mContent);
        parameter.put("sendTime", "");
        parameter.put("action", "send");
        parameter.put("extno", "");
        try {
            byte[] data = mHttpManager.syncPosts(mServerURL, parameter);
            return urlDecode(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void sendMore(String mobiles, String content, Callback<Boolean> callback) {
        send(mobiles, content, callback);
    }

    private void nofiticationStatus() {
        try {
            String tokenURL = mServerURL + "?action=GetToken&cust_code=" + mAccount;
            byte[] tokenResult = mHttpManager.syncGet(tokenURL);
            if (tokenResult == null) return;

            String[] values = StringUtils.split(new String(tokenResult), ',');
            String tokenid = values[0].substring(9);
            String token = values[1].substring(6); //5jn25hhveoxgwlu8vaz4

            String sign = MD5.encode(token + mPassword);
            String queryAccountURL = mServerURL + "?action=QueryAccount&cust_code=" + mAccount + "&token_id=" + tokenid + "&sign=" + sign;
            mHttpManager.asyncGet(queryAccountURL, new HttpCallback() {
                public void onSuccess(Request request, Response response, byte[] data) {
                    String result = urlDecode(new String(data));
                    handleStatus(result);
                }
            });
        } catch (IOException e) {
            LOG.error("", e);
        }
    }

    // cust_code:600130,status:1,sms_balance:4
    private void handleStatus(String result) {
        String content = null;
        String[] values = StringUtils.split(result, ',');
        int smsBalanceCount = StringUtils.asInt(values[2].substring(12));
        if (smsBalanceCount == 7000 ||
                smsBalanceCount == 6000 ||
                smsBalanceCount == 5000 ||
                smsBalanceCount == 4000 ||
                smsBalanceCount == 3000 ||
                smsBalanceCount == 2000 ||
                smsBalanceCount == 1000 ||
                smsBalanceCount == 500 ||
                smsBalanceCount == 100) {
            content = "易驰短信剩余条数不多了, 目前剩下  " + (smsBalanceCount - 1);
            send("13859912070", content, new Callback<Boolean>() {
                public void execute(Boolean o) {

                }
            });
        } else if (smsBalanceCount == 1) {
            content = "短信余额不足, 已经无法发送短信!";
            send("13859912070", content, new Callback<Boolean>() {
                public void execute(Boolean o) {

                }
            });
        }

    }

    public static void main(String[] args) throws IOException {
        MyConfiguration conf = MyConfiguration.getInstance();
        HXSmsServiceImpl service = new HXSmsServiceImpl(conf);
        service.send("15280219671", "153654", new Callback<Boolean>() {
            @Override
            public void execute(Boolean o) {
            }
        });
        //service.nofiticationStatus();
    }


}
