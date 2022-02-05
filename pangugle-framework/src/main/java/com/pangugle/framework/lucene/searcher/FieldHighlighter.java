package com.pangugle.framework.lucene.searcher;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.NullFragmenter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import com.pangugle.framework.utils.StringUtils;

public class FieldHighlighter {
	public static final String DEFAULT_FIELD = "default";

	private String field;

	private Highlighter highlighter;

	private int maxNumFragments = 1;

	private String fragmentSeparator;

	private Analyzer analyzer;

	private int minDocBytes = 0; // 0 is all;

	public static FieldHighlighter createDefault(Query query, Analyzer analyzer) {
		FieldHighlighter lighter = new FieldHighlighter(DEFAULT_FIELD, query, analyzer,
				new SimpleHTMLFormatter("<span class=\"searchmatch\">", "</span>"));
		lighter.setMaxNumFragments(1);
		lighter.setTextFragmenter(new SimpleFragmenter());
		lighter.setFragmentSeparator("<b>...</b>");
		lighter.setMinDocBytes(150);
		return lighter;
	}

	public FieldHighlighter(String field, Query query, Analyzer analyzer) {
		this.field = field;
		highlighter = new Highlighter(new QueryScorer(query));
		highlighter.setTextFragmenter(new NullFragmenter());
		this.analyzer = analyzer;
	}

	public FieldHighlighter(String field, Query query, Analyzer analyzer, Formatter formatter) {
		this.field = field;
		highlighter = new Highlighter(formatter, new QueryScorer(query));
		highlighter.setTextFragmenter(new NullFragmenter());
		this.analyzer = analyzer;
	}

	public Fragmenter getTextFragmenter() {
		return highlighter.getTextFragmenter();
	}

	public void setTextFragmenter(Fragmenter textFragmenter) {
		highlighter.setTextFragmenter(textFragmenter);
	}

	public String highlight(String text) throws IOException {

		TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(text));
		String result;
		try {
			result = highlighter.getBestFragments(tokenStream, text, maxNumFragments, fragmentSeparator);
			if (result == null || StringUtils.getEmpty().equals(result)) {
				if (this.minDocBytes <= 0 || text == null) {
					result = text;
				} else {
					int n = Math.min(this.minDocBytes, text.length());
					result = text.substring(0, n);
				}
			}
		} catch (InvalidTokenOffsetsException e) {
			result = text;
		}
		return result;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFragmentSeparator() {
		return fragmentSeparator;
	}

	public void setFragmentSeparator(String fragmentSeparator) {
		this.fragmentSeparator = fragmentSeparator;
	}

	public int getMaxNumFragments() {
		return maxNumFragments;
	}

	public void setMaxNumFragments(int maxNumFragments) {
		this.maxNumFragments = maxNumFragments;
	}

	public int getMinDocBytes() {
		return minDocBytes;
	}

	public void setMinDocBytes(int minDocBytes) {
		this.minDocBytes = minDocBytes;
	}

	public static void main(String[] args) throws Exception {
		// String text = "《一分钟》";
		// String term = "一分钟";
		// Analyzer analyzer = new ChineseAnalyzer();
		// QueryParser qp = new QueryParser("name", analyzer);
		// Query query = qp.parse(term);
		// FieldHighlighter lighter = new FieldHighlighter("name", query,
		// analyzer);
		// lighter.setMaxNumFragments(3);
		// System.out.println(lighter.highlight(text));
	}
}
