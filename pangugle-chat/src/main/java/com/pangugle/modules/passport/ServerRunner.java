package   com.pangugle.modules.passport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.pangugle.framework.spring.exception.GlobalExceptionManager;
import com.pangugle.passport.UserErrorResult;
import com.pangugle.passport.limit.InvalidAccessTokenException;
import com.pangugle.passport.limit.InvalidLoginTokenException;

/**
 * 测试加载器,仅供参考，需要自定义
 * ServerEventListener 需要自定义一个
 * @author Administrator
 *
 */
@Component
@Order(value=1)
public class ServerRunner implements CommandLineRunner {
	
    @Override
    public void run(String... args) throws Exception {
    	GlobalExceptionManager.getInstanced().addExceptionHandle(InvalidAccessTokenException.class, UserErrorResult.ERR_ACCESSTOKEN_INVALID);
    	GlobalExceptionManager.getInstanced().addExceptionHandle(InvalidLoginTokenException.class, UserErrorResult.ERR_LOGINTOKEN_INVALID);
    }

}
