//package com.bc.framework.log.log4j;
//
//import org.apache.log4j.LogManager;
//
//import com.pangugle.framework.log.Log;
//import com.pangugle.framework.log.LogAdapter;
//
//public class Log4jAdapter implements LogAdapter{
//
//
//	public Log getLog(Class<?> key) {
//		return new Log4jLog(LogManager.getLogger(key));
//	}
//
//	public Log getLog(String key) {
//		return new Log4jLog(LogManager.getLogger(key));
//	}
//
//
//}
