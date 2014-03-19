/**
 * Mp3Similarity.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 *
 * created by jay 2008-7-21
 */
package org.apache.lucene.search;

public class PinyinSimilarity extends DefaultSimilarity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2577257984014348346L;

	/** Implemented as <code>1/sqrt(numTerms)</code>. */
	/*public float lengthNorm(String fieldName, int numTerms) {
		return 1.0f;
	}*/

	//
	// /** Implemented as <code>1/sqrt(sumOfSquaredWeights)</code>. */
	// public float queryNorm(float sumOfSquaredWeights) {
	// return 1.0F;
	// }

	// /** Implemented as <code>sqrt(freq)</code>. */
	public float tf(float freq) {
		return 1.0f;
	}

	//
	// /** Implemented as <code>1 / (distance + 1)</code>. */
	// public float sloppyFreq(int distance) {
	// return 1.0f;
	// }

	/** Implemented as <code>log(numDocs/(docFreq+1)) + 1</code>. */
	public float idf(int docFreq, int numDocs) {
		return 1.0f;
		// return (float) (Math.log(numDocs / (double) (docFreq + 1)) + 1.0);
	}

	/** Implemented as <code>overlap / maxOverlap</code>. */
	// public float coord(int overlap, int maxOverlap) {
	// return 1.0F;
	// }
}
