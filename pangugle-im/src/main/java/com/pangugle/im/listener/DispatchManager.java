package com.pangugle.im.listener;

import java.util.Map;

import com.google.common.collect.Maps;
import com.pangugle.im.listener.processor.GroupMessageProcessor;
import com.pangugle.im.listener.processor.KefuMessageProcessor;
import com.pangugle.im.listener.processor.MessageProcessor;
import com.pangugle.im.listener.processor.SingleMessageProcessor;

/**
 * 消息分发器
 * @author Administrator
 *
 */
public class DispatchManager {
	
	private Map<String, MessageProcessor> mProcessorMaps = Maps.newConcurrentMap();
	
	private static interface MyInternal {
		public DispatchManager mgr = new DispatchManager();
	}
	
	private DispatchManager() {
		addHandle(new SingleMessageProcessor()); // 单聊
		addHandle(new GroupMessageProcessor()); // 群聊
		addHandle(new KefuMessageProcessor()); // 在线客服
		
//		addHandle(new SubscribeAccountMessageProcessor());
//		addHandle(new OfficialAccountMessageProcessor());
	}
	public static DispatchManager getInstanced()
	{
		return MyInternal.mgr;
	}
	
	
	private void addHandle(MessageProcessor processor)
	{
		mProcessorMaps.put(processor.getKey(), processor);
	}
	
	public MessageProcessor getProcessor(String key)
	{
		return mProcessorMaps.get(key);
	}

}
