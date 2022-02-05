package com.pangugle.framework.lucene.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.NIOFSDirectory;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

public class IndexReaderManager {
	private static Log LOG = LogFactory.getLog(IndexReaderManager.class); 
	
	private static Map<String, IndexReader> maps = new HashMap<String, IndexReader>();
	
	public IndexReader create(Analyzer analyzer, String indexPath) throws IOException
	{
		synchronized (maps) {
			IndexReader reader = maps.get(indexPath);
			if(reader == null) {
				LOG.info("lucene open reader " + indexPath);
				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
				IndexWriter indexWriter = new IndexWriter(NIOFSDirectory.open(Paths.get(indexPath)), indexWriterConfig);
				reader =  DirectoryReader.open(indexWriter, true, true);
				maps.put(indexPath, reader);
			} 
			return reader;
		}
	}
	
	public IndexReader openIfChanged(IndexReader oldReader) throws IOException
	{
		DirectoryReader reader = DirectoryReader.openIfChanged((DirectoryReader) oldReader);
		if(reader != null)
		{
			for(String key : maps.keySet())
			{
				IndexReader tempReader = maps.get(key);
				if(tempReader == oldReader)
				{
					maps.replace(key, reader);
					break;
				}
			}
			oldReader.close();
		}
		return reader;
	}
	

}
