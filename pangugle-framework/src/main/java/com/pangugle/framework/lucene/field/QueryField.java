package com.pangugle.framework.lucene.field;

import java.util.HashMap;
import java.util.Map;

public class QueryField {
	
	
	private String[] mFields;
	private Map<String, Float> mFieldBoostMaps = new HashMap<String, Float>();
	
	public static QueryField create()
	{
		QueryField info = new QueryField();
		return info;
	}
	
	private QueryField()
	{
	}
	
	public QueryField finish()
	{
		String[] values = new String[mFieldBoostMaps.size()];
		mFieldBoostMaps.keySet().toArray(values);
		this.mFields = values;
		return this;
	}
	
	public QueryField put(String field, float boost)
	{
		mFieldBoostMaps.put(field, boost);
		return this;
	}
	
	public String[] getFields()
	{
		return mFields;
	}
	
	public Map<String, Float> getBoosts()
	{
		return mFieldBoostMaps;
	}
	
	
}
