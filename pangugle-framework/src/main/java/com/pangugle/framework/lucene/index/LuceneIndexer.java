package com.pangugle.framework.lucene.index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import com.pangugle.framework.conf.MyConfiguration;
import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.lucene.AnalyzerFactory;
import com.pangugle.framework.lucene.searcher.Documentation;



public class LuceneIndexer {
	
	private static final Log LOG = LogFactory.getLog(LuceneIndexer.class);

	private MyConfiguration conf = MyConfiguration.getInstance();
	private IndexWriter writer;
	private  Analyzer analyzer;
	

	public void setAnalyzer(Analyzer analyzer)
	{
		this.analyzer = analyzer;
	}
	
	public void open(String pathdir) {
		try {
			String baseindexdir = conf.getString("index.basedir");
			pathdir = baseindexdir + "/" + pathdir;
			LOG.info("index dir => " + pathdir);
			
			File srcFile = new File(pathdir);
			if(srcFile.exists()) FileUtils.deleteDirectory(srcFile); // 删除旧索引数据，防止重复索引
			srcFile.mkdirs();
			
			// 设置默认分词器
			if(analyzer == null) this.analyzer = AnalyzerFactory.createDefaultAnalyzer();
			Directory d = NIOFSDirectory.open(Paths.get(pathdir));
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			config.setMaxBufferedDocs(1000);
			config.setUseCompoundFile(false);
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			writer = new IndexWriter(d, config);
//			writer.forceMerge(100);
		} catch (IOException e) {
			LOG.error("open index dir error:", e);
		}
	}

	public void index(Documentation doc) throws IOException {
		if (writer != null) {
			writer.addDocument(doc.buildDocument(conf));
		}
	}
	
	public void index(Document doc) throws IOException {
		if (writer != null) {
			writer.addDocument(doc);
		}
	}

	public void close() throws IOException {
		if (writer != null) {
			writer.close();
		}
		LOG.info("index finished.");
	}
	
	
	
}
