/**
 * CFieldDoc.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 *
 * created by jay 2010-5-13
 */
package org.apache.lucene.search;

public class CFieldDoc extends FieldDoc {

	private static final long serialVersionUID = -3959246845681715679L;

	public float luceneScore;

	public float businessScore;
	
	//description
//	public String descritopin;

	/** Expert: Creates one of these objects with empty sort information. */
	public CFieldDoc(int doc, float score) {
		super(doc, score);
	}

	/** Expert: Creates one of these objects with the given sort information. */
	public CFieldDoc(int doc, float score, Comparable[] fields) {
		super(doc, score, fields);
	}
}
