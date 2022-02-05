package com.pangugle.framework.lucene.field;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;

public class NumberFieldHelper {
	
	public static void buildDocument(Document doc, String fieldName, int value, boolean index, boolean docValues)
	{
		doc.add(new StoredField(fieldName, value));
		if(index) doc.add(new IntPoint(fieldName, value));
		if(docValues) doc.add(new NumericDocValuesField(fieldName, value));
	}
	
	public static void buildDocument(Document doc, String fieldName, long value, boolean index, boolean docValues)
	{
		doc.add(new StoredField(fieldName, value));
		if(index) doc.add(new LongPoint(fieldName, value));
		if(docValues) doc.add(new NumericDocValuesField(fieldName, value));
	}
	
	public static void buildDocument(Document doc, String fieldName, float value, boolean index, boolean docValues)
	{
		if(index) doc.add(new FloatPoint(fieldName, value));
		if(docValues) doc.add(new NumberSortField(fieldName, value));//store and docvalues
	}
	
	public static void buildDocument(Document doc, String fieldName, double value, boolean index, boolean docValues)
	{
		if(index) doc.add(new DoublePoint(fieldName, value));
		if(docValues) doc.add(new NumberSortField(fieldName, value)); //store and docvalues
	}

}
