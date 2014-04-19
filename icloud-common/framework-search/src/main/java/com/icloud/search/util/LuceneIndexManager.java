package com.icloud.search.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description
 * @author Jiangning Cui
 * @date 2011-6-2
 * 
 */
public class LuceneIndexManager {
	private static final Logger logger = LoggerFactory.getLogger(LuceneIndexManager.class);
	private Map<String, IndexSearcher> searchPool = null;
	private Analyzer analyzer = null;

	// singleton
	private static class SingletonHolder {
		static final LuceneIndexManager INSTANCE = new LuceneIndexManager();
		static {
			INSTANCE.init();
		}
	}

	public static LuceneIndexManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void init() {
		// TODO Auto-generated method stub
		searchPool = new HashMap<String, IndexSearcher>();
		// analyzer = new IKAnalyzer();
		analyzer = new StandardAnalyzer(Version.LUCENE_35);
	}

	public Analyzer getIKAnalyzer() {
		return this.analyzer;
	}

	public IndexSearcher buildSearcher(String indexdir, Directory directory, IndexStaticDataInteface staticDataInterface) {
		if (indexdir != null) {
			IndexSearcher searcher = null;
			searcher = searchPool.get(indexdir);
			if (searcher != null)
				return searcher;
			try {
				if (directory == null) {
					directory = FSDirectory.open(new File(indexdir));
				}
				searcher = new IndexSearcher(IndexReader.open(directory, false));
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				searcher = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				searcher = null;
			} catch (Exception e) {
				e.printStackTrace();
				searcher = null;
			}
			if (staticDataInterface != null) {
				staticDataInterface.loadIndexData(searcher);
			}
			if (searcher != null) {
				logger.info("index " + indexdir + " open succeed!" + " Doc num is:" + searcher.getIndexReader().maxDoc());
				searchPool.put(indexdir, searcher);
			} else {
				logger.error("index " + indexdir + " open error!");
			}
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
			return searcher;
		}
		return null;
	}

	public IndexSearcher buildSearcher(String indexdir, IndexStaticDataInteface staticDataInterface) {
		return buildSearcher(indexdir, null, staticDataInterface);
	}

	// @SuppressWarnings("rawtypes")
	// public void reOpenIndex() {
	// Set keys = searchPool.keySet();
	// for (Iterator iter = keys.iterator(); iter.hasNext();) {
	// reOpenIndex((String) iter.next());
	// }
	// }

	public synchronized String reOpenIndex(String indexPath, Directory directory, IndexStaticDataInteface staticDataInterface) {
		if (indexPath == null)
			return "indexPath is null";
		StringBuffer sb = new StringBuffer();
		try {
			// 原索引searcher实例
			IndexSearcher ci = searchPool.get(indexPath);
			// 新索引目录
			if (directory == null)
				directory = FSDirectory.open(new File(indexPath));
			IndexSearcher newIndex = new IndexSearcher(directory);
			if (staticDataInterface != null) {
				staticDataInterface.loadIndexData(newIndex);
			}
			searchPool.put(indexPath, newIndex);
			Thread.sleep(100);
			if (ci != null) {
				sb.append(" old version: ").append(ci.getIndexReader().getVersion());
				ci.close();
			}
			sb.append(" new version: ").append(IndexReader.getCurrentVersion(directory)).append(" reopen succeed!\n");
			return sb.toString();
		} catch (IOException e) {
			logger.error("index " + indexPath + " not found!");
			sb.append(" not found!\n");
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
			sb.append(" open fail!\n");
		}
		return sb.toString();
	}

	public String reOpenIndex(String indexPath, IndexStaticDataInteface staticDataInterface) {
		return reOpenIndex(indexPath, null, staticDataInterface);
	}
}
