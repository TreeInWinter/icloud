/**
 * @Photo Search (PIC2U)
 * @fileName ServerUtil.java
 * @description 
 * 
 * @date 2011-6-2
 * @author Jiangning Cui
 */
package com.icloud.search.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description
 * @author Jiangning Cui
 * @date 2011-6-2
 * 
 */
public class SearchServerUtil {
	static final Logger logger = LoggerFactory
			.getLogger(SearchServerUtil.class);
	private static Searcher searcher = null;

	public static Searcher buildSearcher(String indexdir) throws IOException {
		if (searcher == null && indexdir != null) {
			String[] indexPath = indexdir.split(";");
			ArrayList<Searcher> listSearchers = new ArrayList<Searcher>();
			for (int i = 0; i < indexPath.length; i++) {
				try {
					IndexSearcher subSearcher = new IndexSearcher(
							IndexReader.open(
									FSDirectory.open(new File(indexPath[i])),
									false));
					logger.info("index " + indexPath[i] + " open succeed!"
							+ " Doc num is:"
							+ subSearcher.getIndexReader().maxDoc());
					listSearchers.add(subSearcher);
				} catch (IOException e) {
					logger.error("index " + indexPath[i] + " not found!");
				} catch (Exception e1) {
					logger.error(e1.getMessage());
				}
			}
			if (listSearchers.size() == 1) {
				searcher = (Searcher) listSearchers.get(0);
			} else {
				Searcher[] searchers = new Searcher[listSearchers.size()];
				for (int i = 0; i < listSearchers.size(); i++) {
					searchers[i] = (Searcher) listSearchers.get(i);
				}
				searcher = new MultiSearcher(searchers);
				// searcher.setSimilarity(new SimilarityDelegator(searcher
				// .getSimilarity()) {
				// public float tf(float freq) {
				// if (freq < 1.0f) {
				// return (float) Math.sqrt(freq);
				// } else {
				// return 1.0f;
				// }
				// }
				// });
			}
		}
		return searcher;
	}
}
