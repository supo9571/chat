package com.pangugle.framework.spring.exception;

import java.util.Map;

import com.google.common.collect.Maps;
import com.pangugle.framework.bean.ErrorResult;
import com.pangugle.framework.bean.SystemErrorResult;
import com.pangugle.framework.spring.limit.MyIPRateLimitException;

public class GlobalExceptionManager {
	
	private Map<Class<?>, ErrorResult> maps = Maps.newHashMap();
	
	private interface MyInternal {
		public GlobalExceptionManager mgr = new GlobalExceptionManager();
	}
	
	private GlobalExceptionManager() {
		addExceptionHandle(MyIPRateLimitException.class, SystemErrorResult.ERR_REQUESTS);
	}
	
	public static GlobalExceptionManager getInstanced()
	{
		return MyInternal.mgr;
	}
	
	public void addExceptionHandle(Class<?> clazz, ErrorResult result)
	{
		maps.put(clazz, result);
	}

	public ErrorResult getResult(Class<?> clazz)
	{
		return maps.get(clazz);
	}
	
}
