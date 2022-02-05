package com.pangugle.im.logical.push.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.dto.GtReq.NotifyInfo;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.logical.push.PushSupport;

public class GetuiPushImpl implements PushSupport{
	
    // 如果需要使用HTTPS，直接修改url即可
    //private static String url = "https://api.getui.com/apiex.htm";
    private static String url = "https://api.getui.com/apiex.htm";
    
    private boolean isInit = false;
    private  IGtPush mPushMgr;
    
 // STEP1：获取应用基本信息
    private String mAppid ;
    private String mAppKey ;
    
    private String mAppPackage; // app包名
    
    private ExecutorService mThreadPool = Executors.newFixedThreadPool(10);
    
    public GetuiPushImpl()
    {
    	MyConfiguration conf = MyConfiguration.getInstance();
    	this.mAppid = conf.getString("push.getui.appid");
    	this.mAppKey = conf.getString("push.getui.appkey");
    	String masterSecret = conf.getString("push.getui.mastersecret");
    	this.mAppPackage = conf.getString("push.getui.app_package");
    	
    	if(!StringUtils.isEmpty(this.mAppid) && !StringUtils.isEmpty(this.mAppKey))
    	{
    		this.mPushMgr = new IGtPush(url, mAppKey, masterSecret);
    		this.isInit = true;
    	}
    }

    private String buildIntent(String notifyTitle, String notifyContent, String payload)
    {
    	// S.payload=test;end
    	StringBuilder sb = new StringBuilder();
    	sb.append("intent:#Intent;action=android.intent.action.oppopush;launchFlags=0x14000000;");
    	sb.append("component=" + this.mAppPackage + "/io.dcloud.PandoraEntry;");
    	sb.append(";S.UP-OL-SU=true;S.title=" + notifyTitle);
    	sb.append(";S.content=" + notifyContent);
    	sb.append(";S.payload=" + payload + ";end");
    	return sb.toString();
    }
    
    /**
     * 发送单推消息
     * @param token
     * @param notifyTitle
     * @param notifyContent
     * @param payload
     * @param expires
     */
    public void sendSingleMessage(String token, String notifyTitle, String notifyContent, String payload, long expires) {

    	if(!this.isInit)
    	{
    		return;
    	}
    	
    	this.mThreadPool.execute(new Runnable() {
			public void run() {
				String intent = buildIntent(notifyTitle, notifyContent, payload);
		    	
		         Notify notify = new Notify();
		         notify.setTitle(notifyTitle);
		         notify.setContent(notifyContent);
		         notify.setIntent(intent);
		         notify.setType(NotifyInfo.Type._intent); 
		         
		        // STEP4：选择通知模板
		        TransmissionTemplate template = new TransmissionTemplate();
		        template.setAppId(mAppid);
		        template.setAppkey(mAppKey);
		        template.setTransmissionType(2); // 透传消息类型
		        template.setTransmissionContent(payload);  //消息内容 
		        template.set3rdNotifyInfo(notify);
		        
		        SingleMessage message = new SingleMessage();
		        message.setData(template);
		        message.setOffline(true);
		        message.setOfflineExpireTime(expires);  // 时间单位为毫秒

		        // STEP6：执行推送
		        //IPushResult ret = this.mPushMgr.pushMessageToApp(message);
		        
		        Target target = new Target();
		        target.setAppId(mAppid);
		        target.setAlias(token);
//		        target.setClientId(token);
		        
		        IPushResult ret = mPushMgr.pushMessageToSingle(message, target);
//		        this.mPushMgr.pushMessageToApp(message);
		        
		        //System.out.println(ret.getResponse().toString());
			}
		});
    	
    	
    }

}
