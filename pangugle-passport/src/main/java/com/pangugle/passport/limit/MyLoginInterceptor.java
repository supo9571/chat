package com.pangugle.passport.limit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.pangugle.framework.spring.web.WebRequest;
import com.pangugle.framework.utils.StringUtils;
import com.pangugle.passport.service.AuthService;

@Aspect
@Configuration
public class MyLoginInterceptor {
	@Autowired
	private AuthService mAuthService;

	@Around("execution(public * *(..)) && @annotation(com.pangugle.passport.limit.MyLoginLimit)")
	public Object handleLimit(ProceedingJoinPoint pjp) throws Throwable {
		String accessToken = WebRequest.getAccessToken();
		if (StringUtils.isEmpty(accessToken) || !mAuthService.verifyAccessToken(accessToken)) {
			throw new InvalidAccessTokenException();
		}
		Object obj = pjp.proceed();
		return obj;
	}

}
