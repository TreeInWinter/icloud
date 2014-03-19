package org.apache.lucene.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;

public abstract class ExtendTopScoreDocCollector extends TopDocsCollector {

	ScoreDoc pqTop;
	int docBase = 0;
	Scorer scorer;

	public ExtendTopScoreDocCollector(int numHits) {
		super(new HitQueue(numHits, true));
		docBase = 0;
		pqTop = (ScoreDoc) pq.top();
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		return false;
	}

	@Override
	public void collect(int doc) throws IOException {
		float score = scorer.score();
		if (!isContains(doc + docBase)) {
			return;
		}
		// 额外评分
		score = extendScore(doc + docBase, score);
		// This collector cannot handle these scores:
		assert score != Float.NEGATIVE_INFINITY;
		assert !Float.isNaN(score);

		totalHits++;
		if (score <= pqTop.score) {
			// Since docs are returned in-order (i.e., increasing doc Id), a
			// document
			// with equal score to pqTop.score cannot compete since HitQueue
			// favors
			// documents with lower doc Ids. Therefore reject those docs too.
			return;
		}
		pqTop.doc = doc + docBase;
		pqTop.score = score;
		pqTop = (ScoreDoc) pq.updateTop();
	}

	protected abstract boolean isContains(int docid);

	protected abstract float extendScore(int docid, float score);

	@Override
	public void setNextReader(IndexReader reader, int base) throws IOException {
		docBase = base;
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {
		this.scorer = scorer;
	}

}
