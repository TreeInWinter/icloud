/**
 * MySimilarity.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 *
 * created by jay 2010-5-4
 */
package org.apache.lucene.search;


public class MySimilarity extends DefaultSimilarity {
	private static final long serialVersionUID = -1049426611375045940L;

	@Override
	public float tf(float freq) {
		if (freq > 1.0f) {
			return 1.0f;
		} else {
			return (float) freq;
		}
	}

	@Override
	public float idf(int docFreq, int numDocs) {
		return 1.0f;
	}
}
