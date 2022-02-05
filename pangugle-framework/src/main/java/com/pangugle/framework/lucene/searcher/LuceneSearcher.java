package com.pangugle.framework.lucene.searcher;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import com.pangugle.framework.conf.MyConfiguration;

/**
 * https://my.oschina.net/kxln1314/blog/1477042
 * https://wiki.apache.org/lucene-java/ImproveSearchingSpeed
 * @author Administrator
 *
 */
public class LuceneSearcher {
	
	protected Log LOG = LogFactory.getLog(this.getClass());
	
	protected static MyConfiguration conf = MyConfiguration.getInstance();
	
	private String mIndexPath;
	private IndexSearcher mySearcher;
	private IndexReaderManager mReaderManager = new IndexReaderManager();
	
	protected void open(Analyzer analyzer, String indexdir) throws IOException
	{
		this.mIndexPath = conf.getString("index.basedir") + "/" + indexdir;
		IndexReader reader = mReaderManager.create(analyzer, mIndexPath);
		this.mySearcher = new IndexSearcher(reader);
	}
	
	/**
	 * 没有效果(暂时只能重启来解决更新索引问题)
	 * @throws IOException
	 */
	protected void openIfChanged() throws IOException{
		IndexReader newReader = mReaderManager.openIfChanged(mySearcher.getIndexReader());
		IndexSearcher searcher = new IndexSearcher(newReader);
		//mySearcher.setQueryCachingPolicy(new UsageTrackingQueryCachingPolicy());
		LOG.info("lucene reopen reader " + mIndexPath);
		this.mySearcher = searcher;
	}
	
	protected IndexSearcher getSearcher() throws IOException
	{
		//openIfChanged(); 没有用
		return mySearcher;
	}
	
}
