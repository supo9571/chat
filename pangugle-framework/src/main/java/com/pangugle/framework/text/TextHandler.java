package com.pangugle.framework.text;

import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

/**
 * https://github.com/hankcs/HanLP/tree/v1.6.6
 * @author Administrator
 *
 */
public class TextHandler {
	
	private static Suggester mSuggester = new Suggester();

	public static void segment(String text)
	{
		System.out.println(HanLP.segment(text));
	}
	
	public static void testStandardTokenizer(String key )
	{
		List<Term> termList = StandardTokenizer.segment(key);
		for(Term term : termList) {
			System.out.println(term.word);
		}
	}
	
	/**
	 * 关键字提取
	 * @param text
	 * @param size
	 * @return
	 */
	public static List<String> extractKeyword(String text, int size)
	{
		return HanLP.extractKeyword(text, size);
	}
	
	/**
	 * 短语
	 * @param text
	 * @param size
	 * @return
	 */
	public static List<String> extractPhrase(String text, int size)
	{
		return HanLP.extractPhrase(text, size);
	}
	
	/**
	 * 提取描述
	 * @param text
	 * @param size
	 * @return
	 */
	public static List<String> extractSummary(String text, int size)
	{
		return HanLP.extractSummary(text, size);
	}
	
	public static List<String> suggest(String title)
	{
		return mSuggester.suggest(title, 1);
	}
	
	private static void testIndexSegment(String text)
	{
		Segment segment = IndexTokenizer.SEGMENT.enableIndexMode(true);
		System.out.println(segment.seg(text));
	}
	
	public void printConfig()
	{
		for (String i : HanLP.Config.CustomDictionaryPath) {
	        System.out.println(i);
	    }
	}
	
	public static void testAdd()
	{
		CustomDictionary.add("涂");
	}
	
	public static void main(String[] args)
	{
		testAdd();
//		String text = "涂满章";
		//String text = "法约尔定律";
		String text = "净资产收益率又称股东权益报酬率/净值报酬率/权益报酬率/权益利润率/净资产利润率，"
				+ "是衡量上市公司盈利能力的重要指标。是指利润额与平均股东权益的比值，该指标越高，说明投资带来的收益越高；"
				+ "净资产收益率越低，说明企业所有者权益的获利能力越弱。该指标体现了自有资本获得净收益的能力。";
		
		System.out.println(extractKeyword(text, 1));
		System.out.println(extractPhrase(text, 1));
		System.out.println( extractSummary(text, 1).get(0));
		
		segment(text);
	}
	
}
