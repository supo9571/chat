package com.pangugle.framework.mq.impl;

import com.pangugle.framework.mq.MQSupport;
import com.pangugle.framework.redis.client.JedisManager;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisMQImpl implements MQSupport{
	
	private JedisManager mJedisClient = JedisManager.getInstanced();
	
	@Override
	public void declareTopic(String queue) {
		
	}

	@Override
	public void deleteTopic(String queue) {
		
	}

	public  boolean sendMessage(String topic, String body)
	{
		return sendMessage(topic, body, null);
	}

	@Override
	public boolean sendMessage(String queue, String body, String tags) {
		Jedis jedis =  mJedisClient.getJedis();
		try {
			jedis.publish(queue, body);
			return true;
		} catch (Exception e) {
		} finally {
			if(jedis != null)
			{
				jedis.close();
			}
		}
		return false;
	}

	@Override
	public void consume(String topic, String tags, Callback<String> callback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String topic, String tags, Callback<String> callback) {
		Jedis jedis =  mJedisClient.getJedis();
		new Thread(new Runnable() {
			public void run() {
				jedis.subscribe(new JedisPubSub() {
					public void onMessage(String channel, String message) 
					{
						try {
							if(StringUtils.isEmpty(message)) return;
							callback.execute(message);
						} catch (Exception e) {
							//LOG.error("handle message error:", e);
						}
					}
				}, topic);						
			}
		}).start();		
	}

}
