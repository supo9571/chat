package com.pangugle.im.limit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.im.logical.AecManager;

@Aspect
@Configuration
public class MyTokenInterceptor {
	
	private AecManager mAecManager = AecManager.getInstance();
	
	@Around("execution(public * *(..)) && @annotation(com.pangugle.im.limit.MyTokenLimit)")
	public Object handleLimit(ProceedingJoinPoint pjp) throws Throwable {
		String accessToken = WebRequest.getAccessToken();
		if (StringUtils.isEmpty(accessToken) || !mAecManager.getAuthAec().verifyAccessToken(accessToken)) {
			throw new MyTokenLimitException();
		}
		Object obj = pjp.proceed();
		return obj;
	}

}
