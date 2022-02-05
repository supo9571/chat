package com.pangugle.framework.lucene.searcher;

import com.pangugle.framework.log.Log;

public class LogSearcherStatus {
	
	private static final long MAX_SEARCH_DIFF_TIME = 1000; // 2s

	private long maxDiff = MAX_SEARCH_DIFF_TIME;
	private Log LOG;
	
	public LogSearcherStatus(Log log)
	{
		this.LOG = log;
	}
	
	public void logSearch(String key)
	{
		LOG.info(key);
	}
	
	public void logDuration(String key, long startTime, long endTime){
		long diff = endTime - startTime;
		if(diff > maxDiff)
		{
			LOG.warn(key + ", diff > " + diff);
		}
	}
	
}
