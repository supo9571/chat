package com.pangugle.framework.lucene.searcher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public abstract class ResultParser {

	protected IndexSearcher searcher;

	private String encoding = "UTF-8";

	private String type;

	private Map<String, FieldHighlighter> fieldHighlighters = new HashMap<String, FieldHighlighter>();

	public ResultParser(IndexSearcher searcher) {
		this.searcher = searcher;
	}

	public FieldHighlighter getHighlighter(String field) {
		FieldHighlighter lighter = (FieldHighlighter) fieldHighlighters.get(field);
		if (lighter == null) {
			lighter = (FieldHighlighter) fieldHighlighters.get(FieldHighlighter.DEFAULT_FIELD);
		}
		return lighter;
	}

	public void setDefaultHighlighter(FieldHighlighter highlighter) {
		fieldHighlighters.put(FieldHighlighter.DEFAULT_FIELD, highlighter);
	}

	public void addHighlighter(FieldHighlighter highlighter) {
		this.fieldHighlighters.put(highlighter.getField(), highlighter);
	}

	protected String highlight(String field, String text) throws IOException {
		FieldHighlighter fieldHighlighter = this.getHighlighter(field);
		if (fieldHighlighter != null && text != null) {
			text = fieldHighlighter.highlight(text);
		}
		return text;
	}

	protected String[] highlight(String field, String[] texts) throws IOException {
		FieldHighlighter fieldHighlighter = this.getHighlighter(field);

		if (fieldHighlighter != null && texts != null) {
			String[] showTexts = new String[texts.length];
			for (int i = 0; i < texts.length; i++) {
				showTexts[i] = fieldHighlighter.highlight(texts[i]);
			}
			return showTexts;
		} else {
			return texts;
		}

	}

	protected String encodeURL(String s) {
		String es = s;
		if (s != null) {
			try {
				es = URLEncoder.encode(s, encoding);
				es = es.replace("%2F", "/");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return es;
	}

	protected String[] encodeURL(String[] s) {
		if (s != null) {
			String[] es = new String[s.length];
			for (int i = 0; i < s.length; i++) {
				es[i] = encodeURL(s[i]);
			}
			return es;
		}
		return null;
	}

	protected String parseDate(String date) {
		if (date != null && date.length() > 8) {
			date = date.substring(0, date.length() - 8);
		}
		return date;
	}

	protected int parseInt(String s) {
		int n = 0;
		if (s != null && !"".equals(s)) {
			n = Integer.parseInt(s);
		}
		return n;
	}

	protected double parseDouble(String s) {
		double n = 0;
		if (s != null && !"".equals(s)) {
			n = Double.parseDouble(s);
		}
		return n;
	}
	
	public SearchResult renderSearchResult(ScoreDoc[] dataArray, int start, int num) throws IOException {
		int len = dataArray.length;
		SearchResult result = new SearchResult(len);
		for (int i = start; i < len && i < start + num; i++) {
			int docId = dataArray[i].doc;
			Document d = searcher.doc(docId);
			result.add(parseDocument(d, docId));
		}
		return result;
	}

	public SearchResult renderSearchResult(TopDocs topDocs, int start, int num) throws IOException {
		return renderSearchResult(topDocs,start,num,0);
	}

	public SearchResult renderSearchResult(TopDocs topDocs, int start, int num, float minScore) throws IOException {
		SearchResult result = new SearchResult(topDocs.totalHits.value);
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		if (minScore > 0) {
			int ncount = 0;
			int npos = 0;
			for (int i = 0; i < scoreDocs.length; i++) {
				int docId = scoreDocs[i].doc;
				float score = scoreDocs[i].score;
				// System.out.println(docId+"-"+score + ">" +minScore);
				if (score > minScore) {
					if (npos++ >= start) {
						Document d = searcher.doc(docId);
						result.add(parseDocument(d, docId));
						if (ncount++ >= num)
							break;
					}
				}
			}
		} else {
			for (int i = start; i < scoreDocs.length && i < start + num; i++) {
				int docId = scoreDocs[i].doc;
				// if (docId <= maxDoc){
				Document d = searcher.doc(docId);
				result.add(parseDocument(d, docId));
				// }
			}
		}
		return result;
	}

	public abstract Descriptor parseDocument(Document doc, int docId) throws IOException;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
