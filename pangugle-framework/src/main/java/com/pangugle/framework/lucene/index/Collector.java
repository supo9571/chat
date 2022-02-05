package com.pangugle.framework.lucene.index;

import com.pangugle.framework.lucene.searcher.Documentation;

public interface Collector {
	public void collect(Documentation doc);

}
