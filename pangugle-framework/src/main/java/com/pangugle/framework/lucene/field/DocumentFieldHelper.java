package com.pangugle.framework.lucene.field;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.pangugle.framework.utils.StringUtils;


public class DocumentFieldHelper {

	
	public static void buildStringDocument(Document doc, String fieldName, String value, Store store)
	{
		if(StringUtils.isEmpty(value)) return;
		
		StringField field = new StringField(fieldName, value, store);
		doc.add(field);
	}
	
	public static void buildTextDocument(Document doc, String fieldName, String value, Store store)
	{
		if(StringUtils.isEmpty(value)) return;
		
		TextField field = new TextField(fieldName, value, store);
		doc.add(field);
	}
}
