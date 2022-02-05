package com.pangugle.framework.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.springframework.util.StringUtils;

import com.pangugle.framework.lucene.field.QueryField;

public class MyQueryParseFactory {
	
	private static int SLOP = Integer.MAX_VALUE;
	
	private Analyzer mAnalyzer;
	
	public MyQueryParseFactory(Analyzer analyzer)
	{
		this.mAnalyzer = analyzer;
	}
	
	
	public void buildMultyQueryParser(BooleanQuery.Builder build, QueryField filedInfo, String queryString) throws ParseException
	{
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		if(!StringUtils.isEmpty(queryString)) 
		{
			queryString = QueryParser.escape(queryString);
			QueryParser qp = new MultiFieldQueryParser(filedInfo.getFields(), mAnalyzer, filedInfo.getBoosts());
			qp.setPhraseSlop(SLOP);
			Query q = qp.parse(queryString);
			builder.add(q, BooleanClause.Occur.SHOULD);
		}
	}
	


}
