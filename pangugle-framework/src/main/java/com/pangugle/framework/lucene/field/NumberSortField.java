package com.pangugle.framework.lucene.field;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DocValuesType;

public class NumberSortField extends Field {

	/**
	   * Type for numeric DocValues.
	   */
	  private static final FieldType TYPE = new FieldType();
	  static {
	    TYPE.setDocValuesType(DocValuesType.NUMERIC);
	    TYPE.setStored(true);
	    TYPE.freeze();
	  }

	  /** 
	   * Creates a new DocValues field with the specified 64-bit long value 
	   * @param name field name
	   * @param value 64-bit long value
	   * @throws IllegalArgumentException if the field name is null
	   */
	  public NumberSortField(String name, long value) {
	    this(name, Long.valueOf(value));
	  }
	  
	  /**
	   * Creates a new DocValues field with the specified 64-bit long value
	   * @param name field name
	   * @param value 64-bit long value or <code>null</code> if the existing fields value should be removed on update
	   * @throws IllegalArgumentException if the field name is null
	   */
	  public NumberSortField(String name, Long value) {
	    super(name, TYPE);
	    fieldsData = value;
	  }
	  
	  public NumberSortField(String name, int value) {
		    super(name, TYPE);
		    fieldsData = value;
		  }
	  
	  public NumberSortField(String name, float value) {
		   super(name, TYPE);
		   fieldsData = value;
	  }
	  
	  public NumberSortField(String name, double value) {
		   super(name, TYPE);
		   fieldsData = value;
	  }

	  

}
