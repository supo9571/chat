package com.pangugle.framework.lucene;

import org.apache.lucene.analysis.Analyzer;

import com.hankcs.lucene.HanLPAnalyzer;

public class AnalyzerFactory {

	public static Analyzer createDefaultAnalyzer()
	{
		//http://hanlp.linrunsoft.com/services.html
		// https://github.com/hankcs/HanLP/releases
//		result = new StandardFilter(result);  
//        result = new LowerCaseFilter(result);  
//        result = new StopFilter(result, stopSet);  
//		StandardAnalyzer();
		return new HanLPAnalyzer(true);
//		return new ComplexAnalyzer();
	}
	
//	public static Analyzer createMmsegAnalyzer()
//	{
//		return new ComplexAnalyzer();
//	} 
	
}
