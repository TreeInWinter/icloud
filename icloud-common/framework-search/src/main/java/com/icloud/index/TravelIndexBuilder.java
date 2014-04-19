package com.icloud.index;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.icloud.search.util.LuceneIndexManager;
import com.icloud.search.util.SerializeUtil;

public abstract class TravelIndexBuilder {
	protected static final Logger logger = LoggerFactory.getLogger(TravelIndexBuilder.class);
	private Analyzer analyzer = null;
	protected IndexWriter writer = null;
	public static final int DEFAULT_MERGE_FACTOR = 30;

	protected int count = 0;

	public void init(String indexPath, boolean isWrite) {
		// count = 0;
		// try {
		// if (this.analyzer == null) {
		// analyzer = LuceneIndexManager.getInstance().getIKAnalyzer();
		// }
		// writer = new IndexWriter(FSDirectory.open(new File(indexPath)),
		// analyzer, isWrite, MaxFieldLength.LIMITED);
		// writer.setMergeFactor(DEFAULT_MERGE_FACTOR);
		// writer.setUseCompoundFile(false);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		initAnalyzer();
		try {
			init(FSDirectory.open(new File(indexPath)), isWrite);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void initAnalyzer();

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

	public void init(Directory directory, boolean isWrite) {
		count = 0;
		if (directory == null)
			return;
		try {
			if (this.analyzer == null) {
				analyzer = LuceneIndexManager.getInstance().getIKAnalyzer();
			}
			writer = new IndexWriter(directory, analyzer, isWrite, MaxFieldLength.LIMITED);
			writer.setMergeFactor(DEFAULT_MERGE_FACTOR);
			writer.setUseCompoundFile(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void closeAndOptimize() {
		// TODO Auto-generated method stub
		logger.info("start to closeAndOptimize");
		try {
			writer.optimize();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("finish to closeAndOptimize");
	}

	public void mergeIndex(String indexPath, String indextmp) {
		// TODO Auto-generated method stub
		init(indexPath, false);

		long start = System.currentTimeMillis();

		try {
			this.writer.addIndexesNoOptimize(new Directory[] { FSDirectory.open(new File(indextmp)) });
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long time = (end - start) / 1000;
		logger.info("add indexs, costtime is  " + time + "s.");
		start = System.currentTimeMillis();

		closeAndOptimize();
		end = System.currentTimeMillis();
		time = (end - start) / 1000;
		logger.info("optimized the index, costtime is  " + time + "s.");
	}

	protected void addDocument(Document doc) {
		// TODO Auto-generated method stub
		if (doc == null)
			return;
		count++;
		if (count % 1000 == 0)
			logger.info("index Document size is " + count);
		try {
			this.writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected Field getField(String fieldName, String fieldValue, Field.Store store, Index index, float boost) {
		if (fieldName != null && fieldValue != null) {
			Field field = new Field(fieldName, fieldValue, store, index);
			field.setBoost(boost);
			return field;
		}
		return null;
	}

	protected Field getDataField(String fieldName, byte[] bytes) {
		if (fieldName != null && bytes != null) {
			Field field = new Field(fieldName, bytes);
			return field;
		}
		return null;
	}

	protected void addFieldToDocument(Document doc, Field field) {
		if (field != null && doc != null) {
			doc.add(field);
		}
	}

	protected void addFieldToDocument(String fieldName, String fieldValue, Store store, Index index, Document doc, float boost) {
		// TODO Auto-generated method stub
		addFieldToDocument(doc, getField(fieldName, fieldValue, store, index, boost));
	}

	protected void addDataFieldToDocument(String fieldName, Object obj, Document doc) {
		addFieldToDocument(doc, getDataField(fieldName, SerializeUtil.descSerialize(obj)));
	}

	protected void addFieldToList(List<Field> list, Field field) {
		if (field != null && list != null) {
			list.add(field);
		}
	}

	protected void addMetaListToDocument(Document doc, List<Field> list) {
		if (list != null && doc != null) {
			for (Field field : list) {
				addFieldToDocument(doc, field);
			}
		}
	}
}
