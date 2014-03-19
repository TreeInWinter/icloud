package com.travelzen.search.autocomplete.index;

import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.util.Version;
import org.joda.time.DateTime;

import com.travelzen.index.TravelIndexBuilder;
import com.travelzen.search.util.Constants;
import com.travelzen.search.util.SearchUtil;

public abstract class AutoCompleteBuilder extends TravelIndexBuilder {
	protected String indexName;

	@Override
	public void initAnalyzer() {
		// TODO Auto-generated method stub
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
		this.setAnalyzer(analyzer);
	}

	public AutoCompleteBuilder(String indexName) {
		this.indexName = indexName;
	}

	public abstract boolean makeIndex(String indexPath, DateTime startTime, boolean isAll);

	protected void generateDocument(String searchName, String key, float score, Set<String> set, List<Field> metaFieldList) {
		if (searchName == null)
			return;
		searchName = searchName.toLowerCase();
		if (SearchUtil.isEnglish(searchName)) {
			generateDocument(searchName, key, searchName, searchName, score, set, metaFieldList);
		} else {
			String[] pinyins = Constants.getPinyinAndSim(searchName);
			generateDocument(searchName, key, pinyins[0], pinyins[1], score, set, metaFieldList);

			String[] shs = pinyins[0].split(" ");
			if (shs.length > 1) {
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();
				for (String sh : shs) {
					sb.append(sh.charAt(0));
					sb2.append(sh.charAt(0) + " ");
				}
				generateDocument(searchName, key, sb.toString(), sb.toString(), score, set, metaFieldList);
				generateDocument(searchName, key, sb2.toString().trim(), sb2.toString().trim(), score * 6, set, metaFieldList);
			}
			if (pinyins[0].indexOf(" ") != -1 || pinyins[1].indexOf(" ") != -1) {
				pinyins[0] = pinyins[0].replace(" ", "");
				pinyins[1] = pinyins[1].replace(" ", "");
				generateDocument(searchName, key, pinyins[0], pinyins[1], score, set, metaFieldList);
			}

		}
	}

	/**
	 * @param wis
	 * @param sc2
	 * @return
	 */
	protected void generateDocument(String seach_name, String iatacode, String pinyin, String simplePinyin, float sc2, Set<String> set,
			List<Field> metaFieldList) {
		// TODO Auto-generated method stub
		if (set != null && !set.add(pinyin + simplePinyin)) {
			return;
		}
		set.add(pinyin + simplePinyin);
		Document doc = getAutoCompleteDocument(seach_name, iatacode, pinyin, simplePinyin, sc2);
		addMetaListToDocument(doc, metaFieldList);
		this.addDocument(doc);
	}

	/**
	 * private
	 */
	protected Document getAutoCompleteDocument(String search_name, String id, String pinyin, String simplePinyin, float sc2) {
		Document doc = new Document();
		Field search_field = new Field(AutoCompleteConstants.AUTOCOMPLETE_SEARCH_NAME, search_name, Store.YES, Index.ANALYZED);
		Field pinyin_field = new Field(AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, pinyin, Store.YES, Index.ANALYZED);
		Field simple_pinyin_field = new Field(AutoCompleteConstants.AUTOCOMPLETE_SIMPLEPINYIN_NAME, simplePinyin, Store.YES, Index.ANALYZED);
		Field address_field = new Field(AutoCompleteConstants.AUTOCOMPLETE_ID, id, Store.YES, Index.NO);
		Field real_field = new Field(AutoCompleteConstants.AUTOCOMPLETE_READL_NAME, search_name, Store.YES, Index.NO);

		search_field.setBoost(sc2);
		pinyin_field.setBoost(sc2);
		simple_pinyin_field.setBoost(sc2);

		doc.add(search_field);
		doc.add(address_field);
		doc.add(pinyin_field);
		doc.add(simple_pinyin_field);
		doc.add(real_field);
		return doc;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

}
