package com.pangugle.framework.mq.impl;

import java.util.List;

import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.mq.MQSupport;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.framework.utils.StringUtils;

public class RocketMQImpl implements MQSupport {

	private static Log LOG = LogFactory.getLog(RocketMQImpl.class);

	private static String DEFAULT_GROUP = "pangule_default_group";
	
	private static int DEFAULT_CONSMER_THREAD_SIZE = 5;

	private static String mServer = null;
	
	private static ClientConfig mClientConfig = new ClientConfig();
	private static DefaultMQProducer mProducer;
	
	public RocketMQImpl() {
		synchronized (RocketMQImpl.class) {
			if (mProducer == null) {
				initClientConfig();
				initProducer();
			}
		}
	}

	@Override
	public void declareTopic(String queue) {
	}

	@Override
	public void deleteTopic(String queue) {

	}

	@Override
	public boolean sendMessage(String queue, String body, String tags) {
		try {
			if(StringUtils.isEmpty(tags))
			{
				tags = StringUtils.getEmpty();
			}
			Message msg = new Message(queue, tags, body.getBytes(RemotingHelper.DEFAULT_CHARSET));

			// Call send message to deliver message to one of brokers.
			SendResult sendResult = mProducer.send(msg);
			if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
				return true;
			}
			LOG.warn("send queue error : " + FastJsonHelper.jsonEncode(sendResult));
		} catch (Exception e) {
			LOG.error("send queue error:", e);
		}
		return false;
	}

	@Override
	public boolean sendMessage(String queue, String body) {
		return sendMessage(queue, body, null);
	}

	private static void initClientConfig() {
		mServer = MyConfiguration.getInstance().getString("mq.rocket.server");
		LOG.info("rocketmq.server = " + mServer);
		
		// 客户端本机 IP 地址，某些机器会发生无法识别客户端IP地址情况，需要应用在代码中强制指定
		// Name Server 地址列表，多个 NameServer 地址用分号 隔开
		mClientConfig.setNamesrvAddr(mServer);
		// 客户端实例名称，客户端创建的多个 Producer、 Consumer 实际是共用一个内部实例（这个实例包含
		// 网络连接、线程资源等）,默认值:DEFAULT
		mClientConfig.setInstanceName("DEFAULT");
		// 通信层异步回调线程数,默认值4
		mClientConfig.setClientCallbackExecutorThreads(10);
		// 轮询 Name Server 间隔时间，单位毫秒,默认：30000
		// mClientConfig.setPollNameServerInterval(30000);
		// 向 Broker 发送心跳间隔时间，单位毫秒,默认：30000
		mClientConfig.setHeartbeatBrokerInterval(30000);
		// 持久化 Consumer 消费进度间隔时间，单位毫秒,默认：5000
		mClientConfig.setPersistConsumerOffsetInterval(5000);
	}

	private static void initProducer() {
		try {
			mProducer = new DefaultMQProducer();

			mProducer.resetClientConfig(mClientConfig);
			// 在发送消息时，自动创建服务器不存在的topic，默认创建的队列数 默认值 4
			mProducer.setDefaultTopicQueueNums(4);
			// 发送消息超时时间，单位毫秒 : 默认值 10000
			mProducer.setSendMsgTimeout(10000);
			// 消息Body超过多大开始压缩（Consumer收到消息会自动解压缩）,单位字节 默认值 4096
			mProducer.setCompressMsgBodyOverHowmuch(4096);
			// 如果发送消息返回sendResult，但是sendStatus!=SEND_OK,是否重试发送 默认值 FALSE
			mProducer.setRetryAnotherBrokerWhenNotStoreOK(false);

			mProducer.setProducerGroup(DEFAULT_GROUP);
//			mProducer.setRetryTimesWhenSendAsyncFailed(3);
			mProducer.start();
		} catch (Exception e) {
			LOG.error("init producer error:", e);
		}
	}

	@Override
	public void consume(String topic, String tags, Callback<String> callback) {
		try {
			if(StringUtils.isEmpty(tags))
			{
				tags = StringUtils.getEmpty();
			}
			DefaultMQPushConsumer consumer = getConsumerInstance(topic, tags);
			consumer.setMessageModel(MessageModel.CLUSTERING);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					// TODO Auto-generated method stub
					for(MessageExt ext : msgs)
			    	{
			    		try {
			    			String body =  new String(ext.getBody());
			    			callback.execute(body);
						} catch (Exception e) {
						}
			    	}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch (MQClientException e) {
			LOG.error("consume error:", e);
		}
	}

	@Override
	public void subscribe(String topic, String tags, Callback<String> callback) {
		try {
			if(StringUtils.isEmpty(tags))
			{
				tags = StringUtils.getEmpty();
			}
			DefaultMQPushConsumer consumer = getConsumerInstance(topic, tags);
			consumer.setMessageModel(MessageModel.BROADCASTING);
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					// TODO Auto-generated method stub
					for(MessageExt ext : msgs)
			    	{
			    		try {
			    			String body =  new String(ext.getBody());
			    			callback.execute(body);
						} catch (Exception e) {
						}
			    	}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			consumer.start();
		} catch (MQClientException e) {
			LOG.error("subscrebe error:", e);
		}
	}
	
	private static DefaultMQPushConsumer getConsumerInstance(String topic, String tags) throws MQClientException
	{
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
		consumer.resetClientConfig(mClientConfig);
		consumer.setConsumerGroup(topic + tags);
		consumer.setConsumeThreadMin(DEFAULT_CONSMER_THREAD_SIZE);
		consumer.setConsumeThreadMax(DEFAULT_CONSMER_THREAD_SIZE);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		// mConsumer.subscribe(queue, "TagA || TagC || TagD");
		consumer.subscribe(topic, tags);
		return consumer;
	}

}
