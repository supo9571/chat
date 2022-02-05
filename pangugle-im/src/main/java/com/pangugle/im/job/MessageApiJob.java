package com.pangugle.im.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.mq.MQManager;
import com.pangugle.framework.mq.MQSupport;
import com.pangugle.framework.service.Callback;
import com.pangugle.framework.socketio.model.MessageBody;
import com.pangugle.framework.utils.FastJsonHelper;
import com.pangugle.im.logical.MessageManager;

/**
 * 消息队列要采用集群模式，不能用订阅模式
 * 服务端：非IM服务 发送消息job, 通过消息队列发送
 * @author Administrator
 *
 */
public class MessageApiJob implements Job{
	
	private static Log LOG = LogFactory.getLog(MessageApiJob.class);
	
	public static final String QUEUE_NAME = "im_send_message";
	
	private MQSupport mq = MQManager.getInstance().getMQ();

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		mq.consume(QUEUE_NAME, null, new Callback<String>() {
			public void execute(String bodyString)
			{
				try {
					//System.out.println("============receiv message : " + bodyString );
					MessageBody body = FastJsonHelper.jsonDecode(bodyString, MessageBody.class);
					MessageManager.getIntance().sendMessage(body);
				} catch (Exception e) {
					LOG.error("send message error:", e);
				}
			}
		});
	}

}
