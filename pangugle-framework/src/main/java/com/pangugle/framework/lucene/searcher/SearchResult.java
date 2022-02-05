package com.pangugle.framework.lucene.searcher;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	
	private long totalRecord;
	private List<Descriptor> results = new ArrayList<Descriptor>(); 
	private int version=0;
	
	public SearchResult(long total) {
		this.totalRecord = total;
	}

	public int getCount() {
		return results.size();
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public List<Descriptor> getResults() {
		return results;
	}

	public void add(Descriptor descriptor) {
		this.results.add(descriptor);
	}
	
	
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		buffer.append("total:").append(this.totalRecord).append("\n");
		for(Descriptor d: results){
			buffer.append(d).append("\n");
		}
		return buffer.toString();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
