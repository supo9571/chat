//package com.pangugle.framework.lucene;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.apache.commons.io.FileUtils;
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.CharArraySet;
//import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.Tokenizer;
//import org.apache.lucene.analysis.core.LowerCaseFilter;
//import org.apache.lucene.analysis.standard.StandardFilter;
//
//import com.hankcs.hanlp.tokenizer.IndexTokenizer;
//import com.hankcs.lucene.HanLPAnalyzer;
//import com.hankcs.lucene.HanLPTokenizer;
//import com.pangugle.framework.log.Log;
//import com.pangugle.framework.log.LogFactory;
//
//public class MyHanLPAnalyzer extends Analyzer {
//	
//	private static final String DEFAULT_HANLP_STOPWORDS = "/srv/bgdata/hanlp/hanlp_v166/data/dictionary/stopwords.txt";
//	
//	private static Log LOG = LogFactory.getLog(MyHanLPAnalyzer.class);
//	
//	private static CharArraySet ENGLISH_STOP_WORDS_SET;
//	private static Set<String> DEFAULT_STOP_FILTERS;
//	static {
//		try {
////			List<String> stopWordsList = Arrays.asList(
////			  "a", "an", "and", "are", "as", "at", "be", "but", "by",
////			  "for", "if", "in", "into", "is", "it",
////			  "no", "not", "of", "on", "or", "such",
////			  "that", "the", "their", "then", "there", "these",
////			  "they", "this", "to", "was", "will", "with"
////			);
//			// 2018-7-17 更新使用词
//			LOG.debug("根据hanlp更新, stopwords path = " + DEFAULT_HANLP_STOPWORDS);
//			String stopwordstring = FileUtils.readFileToString(new File(DEFAULT_HANLP_STOPWORDS), "UTF-8");
//			String[] stowordsArray = stopwordstring.split("\n");
//			List<String> stopWordsList = Arrays.asList(stowordsArray);
//			
//			DEFAULT_STOP_FILTERS = new HashSet<>(stopWordsList);
//			CharArraySet stopSet = new CharArraySet(stopWordsList, false);
//			ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	  }
//	
//	/*** 是否分析词干.进行单复数,时态的转换 ***/
//	private boolean enablePorterStemming = true;
//    private Set<String> filter = DEFAULT_STOP_FILTERS;
//
//
//    public MyHanLPAnalyzer()
//    {
//        super();
//    }
//    
//    /**
//     * 重载Analyzer接口，构造分词组件
//     */
//    @Override
//    protected TokenStreamComponents createComponents(String fieldName)
//    {
//    	//IndexTokenizer.SEGMENT.enableOffset(true)
////        Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment().enableOffset(true), filter, enablePorterStemming);
//        Tokenizer tokenizer = new HanLPTokenizer(IndexTokenizer.SEGMENT.enableOffset(true), filter, enablePorterStemming);
//        TokenStream tok = new StandardFilter(tokenizer);
//        tok = new LowerCaseFilter(tok);
//       // tok = new StopFilter(tok, ENGLISH_STOP_WORDS_SET);
//        return new TokenStreamComponents(tokenizer, tok);
//    }
//    
//    @Override
//    protected TokenStream normalize(String fieldName, TokenStream in) {
//      TokenStream result = new StandardFilter(in);
//      result = new LowerCaseFilter(result);
//      result = new StandardFilter(result);
//      //result = new StopFilter(result, ENGLISH_STOP_WORDS_SET); 
//      return result;
//    }
//    
//    public static void main(String[] args) throws IOException
//    {
//    	HanLPAnalyzer a;
//    	String path = MyHanLPAnalyzer.class.getResource("/stopwords.txt").getPath();
//		String stopwordstring = FileUtils.readFileToString(new File(path), "UTF-8");
//		String[] stowordsArray = stopwordstring.split("\n");
//		
//		String path2 = "C:/Users/Administrator/Desktop/stowords.txt";
//		String stopwordstring2 = FileUtils.readFileToString(new File(path2), "UTF-8");
//		String[] stowordsArray2 = stopwordstring2.split("\n");
//		
//		int count = 0;
//		for(String stopword : stowordsArray)
//		{
//			stopword = stopword.trim();
//			for(String tmp : stowordsArray2)
//			{
//				tmp = tmp.trim();
//				if(stopword.equalsIgnoreCase(tmp)) {
//					count ++;
//					System.out.println(tmp);
//				}
//			}
//		}
//		System.out.println(count);
//    }
//}
