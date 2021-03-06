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
package com.pangugle.framework.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.socketio.listener.ServerEventListener;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.socketio.model.MyProtocol;
import com.pangugle.framework.socketio.utils.LoginProtocolUtils;
import com.pangugle.framework.socketio.utils.MessageBodyFactory;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;

/**
 * 事件管理器
 * @author Administrator
 *
 */
public class ServerEventManager {
	
	public static final String DEFAULT_EVENT = "message-event";
	
	public static final String KEY_ACCESS_TOKEN = "accessToken";
	public static final String KEY_IP = "ip";
	public static final String KEY_EXTRA = "extra";
	public static final String KEY_VERSION = "version";
	
	private static Log LOG = LogFactory.getLog(ServerEventManager.class);
	
	// socket
	private ServerEventListener mServerEventListener;
	private SocketioManager mSocketManager;
	private IOClientManager mIOClientManager;
	private MessageEncrypt messageEncrypt;
	
	private interface ClientManagerInternal {
		public ServerEventManager mgr = new ServerEventManager();
	}
	
	public static ServerEventManager getIntance()
	{
		return ClientManagerInternal.mgr;
	}
	
	private ServerEventManager()
	{
		MyConfiguration conf = MyConfiguration.getInstance();
		int port = conf.getInt("socketio.port");
		this.mSocketManager = new SocketioManager(port);
		this.mIOClientManager = new IOClientManager();
	}
	
	public void registerListener(ServerEventListener listener)
	{
		if(listener == null)
		{
			throw new RuntimeException("ServerEventListener must not to set null");
		}
		this.mServerEventListener = listener;
	}
	
	public ServerEventListener getServerEventListener()
	{
		return this.mServerEventListener;
	}
	
	public IOClientManager getIOClientManager()
	{
		return mIOClientManager;
	}
	
	public void registerMessageEncrypt(MessageEncrypt encrypt)
	{
		this.messageEncrypt = encrypt;
	}
	
	public MessageEncrypt getMessageEncrypt()
	{
		return this.messageEncrypt;
	}
	
	public void start()
	{
		// connection listener
		mSocketManager.getServer().addConnectListener(new ConnectListener() 
		{
			public void onConnect(SocketIOClient client) {
				try {
					// 超过最大并发连接
					if(!mIOClientManager.isAddClient())
					{
						sendMessage(client, LoginProtocolUtils.MAX_CONN_ERR);
						client.disconnect();
						return;
					}
					String accessToken = client.getHandshakeData().getSingleUrlParam(KEY_ACCESS_TOKEN);
					String ip = client.getHandshakeData().getSingleUrlParam(KEY_IP);
					String extra = client.getHandshakeData().getSingleUrlParam(KEY_EXTRA);
					String version = client.getHandshakeData().getSingleUrlParam(KEY_VERSION);
					if(StringUtils.isEmpty(version))
					{
						sendMessage(client, LoginProtocolUtils.MULTY_CONN);
						client.disconnect();
						return;
					}
					
					String userid = null;
					try {
						// 验证异常, token 过期
						userid = mServerEventListener.onVerifyUserLogin(accessToken, ip, extra);
					} catch (Exception e) {
					}
					if(StringUtils.isEmpty(userid))
					{
						//System.out.println("=============invalid userid ===========");
						sendMessage(client, LoginProtocolUtils.INVALID_TOKEN);
						client.disconnect();
						return;
					}
					
					// 保存连接
					if(mIOClientManager.addClient(client, userid, accessToken))
					{
						sendMessage(client, LoginProtocolUtils.CONN_SUCCESS);
					}
				} catch (Exception e) {
					LOG.error("handle connection error:", e);
				}
			}
		});
		
		// disconnection listener
		mSocketManager.getServer().addDisconnectListener(new DisconnectListener() 
		{
			public void onDisconnect(SocketIOClient client) {
				try {
					mIOClientManager.removeClient(client);
				} catch (Exception e) {
					LOG.error("handle disconnection error:", e);
				}
			}
		});
		
		// data listener
		mSocketManager.getServer().addEventListener(DEFAULT_EVENT, String.class, new DataListener<String>() 
		{
			public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception
			{
				try {
					//System.out.println(data);
					if(StringUtils.isEmpty(data)) return;
					
					data = data.replaceAll("_", "+");
					//data = UrlUtils.decode(data);
					//System.out.println(data);
					
					// 消息解密
					data = decryptBody(data);
					
					MyProtocol protocol = MyProtocol.build(data);
					if(protocol == null || !protocol.verify() ) {
						return;
					}
					
					// 假冒用户，可直接封禁
					String userid = mIOClientManager.getUseridByClient(client);
					if(!protocol.getFromUserid().equalsIgnoreCase(userid))
					{
						return;
					}
					
					String accessToken = client.getHandshakeData().getSingleUrlParam(KEY_ACCESS_TOKEN);
					MessageBody messageBody = MessageBodyFactory.createBody(protocol);
					messageBody.setFromUserToken(accessToken);
					
					mServerEventListener.onTransBuffer_C2S_CallBack(messageBody);
				} catch (Exception e) {
					LOG.error("handle message event error:", e);
				}
			}
		});
		
		// start
		mSocketManager.start();
	}

	private String encryptBody(String body)
	{
		return messageEncrypt.encrypt(body);
	}
	
	private String decryptBody(String body)
	{
		return messageEncrypt.decrypt(body);
	}
	
	public void sendMessage(SocketIOClient client, MyProtocol protocol)
	{
		String bodyString = FastJsonHelper.jsonEncode(protocol);
		bodyString = encryptBody(bodyString);
		client.sendEvent(DEFAULT_EVENT, bodyString);
	}
	
	
	public void sendMessage(MessageBody body)
	{
		body.increRetry();
		if(mIOClientManager.sendMessage(body))
		{
			return;
		}
		mServerEventListener.onTransBuffer_S2C_Failure_CallBack(body);
	}
	
	public void kick(String userid, String accessToken)
	{
		mIOClientManager.kickOut(userid, accessToken);
	}
	

}

