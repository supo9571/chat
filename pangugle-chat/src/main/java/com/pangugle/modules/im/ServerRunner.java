package   com.pangugle.modules.im;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.pangugle.framework.socketio.ServerEventManager;
import com.pangugle.framework.spring.exception.GlobalExceptionManager;
import com.pangugle.im.MyMessageEncrypt;
import com.pangugle.im.TokenErrorResult;
import com.pangugle.im.limit.MyTokenLimitException;
import com.pangugle.modules.im.listener.ChatServerEventListener;

/**
 * im 启动
 * ServerEventListener 需要自定义一个
 * @author Administrator
 *
 */
@Component
@Order(value=1)
public class ServerRunner implements CommandLineRunner {
	
    @Override
    public void run(String... args) throws Exception {
    	
    	// 启动im服务
    	ServerEventManager.getIntance().registerMessageEncrypt(new MyMessageEncrypt()); //消息加密
    	ServerEventManager.getIntance().registerListener(new ChatServerEventListener()); // 注册消息监听器
    	ServerEventManager.getIntance().getIOClientManager().build(true, 20000); // 设置client接入模式, （单点登陆|PC扫码登陆）
    	ServerEventManager.getIntance().start();
    	
    	// 异常处理
    	GlobalExceptionManager.getInstanced().addExceptionHandle(MyTokenLimitException.class, TokenErrorResult.ERR_ACCESSTOKEN_INVALID);
    }

}
