package com.pangugle.framework.lucene.searcher;

import org.apache.lucene.document.Document;

import com.pangugle.framework.conf.MyConfiguration;

public interface Documentation {
	public Document buildDocument(MyConfiguration conf);
}
