/**
 * @author jn
 * @date
 * @version
 */
package com.travelzen.search.autocomplete.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.search.autocomplete.index.AutoCompleteConstants;
import com.travelzen.search.util.Constants;
import com.travelzen.search.util.LuceneIndexManager;
import com.travelzen.search.util.PDTokenizor;
import com.travelzen.search.util.SearchUtil;
import com.travelzen.tops.constants.flight.FlightAirLineConstants;

/**
 * @description
 * @author Jiangning Cui
 * @date 2011-6-2
 * 
 */
public abstract class AutoCompleteSearchServer<T> {
	static final Logger logger = LoggerFactory.getLogger(AutoCompleteSearchServer.class);
	private static final float RESULT_RATE = 0.7f;
	private static final float LONG_RESULT_RATE = 0.6f;
	// private static AutoCompleteSearchServer server;
	// protected Searcher searcher;
	// protected String indexPath;
	protected Analyzer analyzer;
	private static final int preelectionCount = 50;// 纠错预选集数

	// singleton
	// private static class SingletonHolder {
	// static final AutoCompleteSearchServer INSTANCE = new
	// AutoCompleteSearchServer();
	// static {
	// INSTANCE.init();
	// }
	// }
	// public static AutoCompleteSearchServer getInstance() {
	// return SingletonHolder.INSTANCE;
	// }

	protected abstract void init();

	protected abstract T convertDocToSearchBean(Document doc, int docid, float score);

	// {
	//
	// analyzer = new IKAnalyzer();
	// try {
	// searcher = SearchServerUtil.buildSearcher(Config
	// .getPathItem("dir.flight.autocomplete.index"));
	// } catch (IOException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }

	public BooleanQuery createFuzzyQuery(String searchName) {
		if (searchName == null)
			return null;
		searchName = searchName.toLowerCase();
		Term t = new Term(AutoCompleteConstants.AUTOCOMPLETE_SIMPLEPINYIN_NAME, searchName);
		// 字符数<4不能错
		// 字符数==4 :1
		// 字符数5,6,7 :2
		// 字符数8,9,...13:3
		// >13 5/length
		// 设置最小相似度依据
		float minimumSimilarity = (float) 0.6;
		int minLength = 0, maxLength = 0;
		if (searchName.length() < 4) {
			minimumSimilarity = (float) 0.99;
			minLength = maxLength = searchName.length();
		} else if (searchName.length() == 4) {
			minimumSimilarity = (float) (1.0 - 1.0 / searchName.length() - 0.1);
			minLength = searchName.length() - 1;
			maxLength = searchName.length() + 1;
		} else if (searchName.length() > 4 && searchName.length() < 8) {
			minimumSimilarity = (float) (1.0 - 2.0 / searchName.length());
			minLength = searchName.length() - 2;
			maxLength = searchName.length() + 2;
		} else if (searchName.length() > 7 && searchName.length() < 14) {
			minimumSimilarity = (float) (1.0 - 3.0 / searchName.length());
			minLength = searchName.length() - 3;
			maxLength = searchName.length() + 3;
		} else if (searchName.length() > 13) {
			minimumSimilarity = (float) ((searchName.length() - 3) / (searchName.length() * 3.0));
			minLength = searchName.length() - 3;
			maxLength = searchName.length() * 3;
		}
		int prefixLength = 1;// 首字母必须匹配

		// 模糊query
		FuzzyQuery fuzzyQuery = new FuzzyQuery(t, minimumSimilarity, prefixLength);
		BooleanQuery allBooleanQuery = new BooleanQuery();// 我们的业务逻辑查询
		fuzzyQuery.setBoost((float) 0.2);
		allBooleanQuery.add(fuzzyQuery, BooleanClause.Occur.MUST);
		return allBooleanQuery;
	}

	public List<T> prefixSearch(String query, int number, String indexPath, int type) {
		if (query == null || query.length() == 0)
			return null;
		Searcher searcher = LuceneIndexManager.getInstance().buildSearcher(indexPath, null);
		if (searcher == null)
			return null;
		query = SearchUtil.traditionalToSimple(query);
		String[] temp = Constants.getPinyinAndSim(query);
		query = temp[0].replace(" ", "");
		String prequery = query;
		if (number <= 0)
			number = 6;
		Term t = new Term(AutoCompleteConstants.AUTOCOMPLETE_SIMPLEPINYIN_NAME, prequery);
		Term t2 = new Term(AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, prequery);
		PrefixQuery pQuery = new PrefixQuery(t);
		PrefixQuery pQuery2 = new PrefixQuery(t2);
		BooleanQuery tmpBooleanQuery = new BooleanQuery();
		tmpBooleanQuery.add(pQuery, BooleanClause.Occur.SHOULD);
		tmpBooleanQuery.add(pQuery2, BooleanClause.Occur.SHOULD);

		BooleanQuery allBooleanQuery = new BooleanQuery();
		allBooleanQuery.add(tmpBooleanQuery, BooleanClause.Occur.MUST);
		allBooleanQuery.add(tmpBooleanQuery, BooleanClause.Occur.MUST);

		BooleanQuery typeBooleanQuery = new BooleanQuery(true);
		typeBooleanQuery.add(new TermQuery(new Term(FlightAirLineConstants.META_TYPE, type + "")), BooleanClause.Occur.MUST);
		allBooleanQuery.add(typeBooleanQuery, BooleanClause.Occur.MUST);
		// BooleanQuery pQuery = createFuzzyQuery(prequery);
		// System.out.println("query : " + pQuery);
		TopDocs hits = null;
		try {
			hits = searcher.search(allBooleanQuery, number * 4);
		} catch (IOException e) {
			hits = null;
			allBooleanQuery = null;
			return null;
		}
		int len = hits.scoreDocs.length;
		List<T> list = new ArrayList<T>();
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < len; i++) {
			Document doc = null;
			try {
				doc = searcher.doc(hits.scoreDocs[i].doc);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				continue;
			} catch (IOException e) {
				continue;
			}
			if (doc != null) {
				String term = doc.get(AutoCompleteConstants.AUTOCOMPLETE_ID);
				if (term != null) {
					if (!set.contains(term)) {
						if (term.length() > 1) {
							set.add(term);
							T bean = convertDocToSearchBean(doc, hits.scoreDocs[i].doc, hits.scoreDocs[i].score);
							if (bean != null) {
								list.add(bean);
								// if (list.size() == number) {
								// break;
								// }
							}
						}
					}
				}
			}
		}
		set.clear();
		set = null;
		BeanSortedQueue<T> queue = getAutoCompleteBeanSortedQueue(number);
		boolean isEnglish = SearchUtil.isEnglish(query);
		hotQueueSort(queue, list, query, isEnglish);
		int size = queue.size();
		// T[] result = new T[size];

		T[] result = createArray(size);

		for (int i = result.length - 1; i >= 0; i--) {
			T bean = queue.pop();
			result[i] = bean;
		}
		List<T> beans = new ArrayList<T>();
		for (T bean : result) {
			beans.add(bean);
		}
		return beans;
	}

	protected abstract BeanSortedQueue<T> getAutoCompleteBeanSortedQueue(int limit);

	public List<T> autoComplete(String query, int limit, int type, String indexPath) {
		if (query == null || query.length() == 0)
			return null;
		Searcher searcher = LuceneIndexManager.getInstance().buildSearcher(indexPath, null);
		if (searcher == null)
			return null;
		if (limit < 1)
			limit = 4;
		query = SearchUtil.traditionalToSimple(query);
		BeanSortedQueue<T> queue = getAutoCompleteBeanSortedQueue(limit);

		List<T> list = null;
		List<String> words = null;
		try {
			words = PDTokenizor.getTokens(analyzer, query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		boolean isEnglish = SearchUtil.isEnglish(query);
		if (isEnglish) {
			// list = pinyinSearch(query, words, type, searcher);
			// list = fuzzySearch(query, words, type, searcher);// 模糊搜索
			// if (list == null || list.size() == 0) {
			list = fuzzyEnglishSearch(query, words, type, searcher);
			// }
		} else if (SearchUtil.containsEnglisht(query)) {
			list = prefixSearch(query, limit, indexPath, type);// 如果英文一块
		} else {
			// list = fuzzySearch(query, words, type, searcher);// 模糊搜索
			if (type == AutoCompleteConstants.META_HOTEL_NAME) {
				list = fuzzySearch(query, words, type, searcher);// 模糊搜索
			} else {
				list = pinyinSearch(query, words, type, searcher);// 模糊搜索
			}
		}

		hotQueueSort(queue, list, query, isEnglish);
		// T[] result = new T[queue.size()];
		int size = queue.size();
		// T[] result = new T[size];

		T[] result = createArray(size);

		for (int i = result.length - 1; i >= 0; i--) {
			T bean = queue.pop();
			result[i] = bean;
		}
		List<T> beans = new ArrayList<T>();
		for (T bean : result) {
			beans.add(bean);
		}
		// List<T> beanstmp2 = prefixSearch(query, limit, indexPath);
		// if(true)
		// return beanstmp2;
		if (beans.size() < limit / 2) {
			List<T> beanstmp = prefixSearch(query, limit, indexPath, type);
			if (beanstmp != null && beanstmp.size() > beans.size())
				return beanstmp;
			return beans;
		}
		return beans;
	}

	protected abstract T[] createArray(int size);

	/**
	 * 去掉重复的汉字
	 * 
	 * @param searchName
	 * @return
	 */
	private String modifiedSearchName(String searchName) {
		// TODO Auto-generated method stub
		if (searchName != null && searchName.length() > 0) {
			StringBuffer sb = new StringBuffer();
			int len = searchName.length();
			char lastChar = 0;
			for (int i = 0; i < len; i++) {
				char c = searchName.charAt(i);
				if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
				} else {
					if (lastChar != 0 && lastChar == c) {
						continue;
					}
				}
				sb.append(c);
				lastChar = c;
			}
			return sb.toString();
		}
		return searchName;
	}

	private List<T> pinyinSearch(String searchName, List<String> words, int type, Searcher searcher) {
		// TODO Auto-generated method stub
		searchName = modifiedSearchName(searchName);
		try {
			List<BooleanQuery> listBooleanQuery = new ArrayList<BooleanQuery>();
			// 全拼query
			String[] temp = Constants.getPinyinAndSim(searchName);
			String fullPinyin = temp[0];// 全拼
			getPinyinQueryList(fullPinyin, AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, listBooleanQuery);
			// 2)简拼
			String simplifiedPinyin = temp[1];
			getPinyinQueryList(simplifiedPinyin, AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, listBooleanQuery);
			// 组装查询
			BooleanQuery allBooleanQuery = new BooleanQuery();
			BooleanQuery ourOperationBooleanQuery = new BooleanQuery(true);
			for (int i = 0; i < listBooleanQuery.size(); i++) {
				ourOperationBooleanQuery.add(listBooleanQuery.get(i), BooleanClause.Occur.SHOULD);
			}
			allBooleanQuery.add(ourOperationBooleanQuery, BooleanClause.Occur.SHOULD);
			// 检索
			return getSearchResult(allBooleanQuery, preelectionCount, type, searcher);
		} catch (Exception e) {

		}
		return null;
	}

	protected abstract int hotQueueSort(BeanSortedQueue<T> queue, List<T> listPinyinBean, String searchWord, Boolean isEnglish);

	/**
	 * 热度队列排序
	 * 
	 * @param queue
	 * @param tParam
	 * @return
	 */
	// private int hotQueueSort(AutoCompleteBeanSortedQueue<T> queue, List<T>
	// listPinyinBean,
	// String searchWord, Boolean isEnglish) {
	// String searchName = searchWord.toLowerCase();
	// // 判断searchName是否有连续的叠字（3个字）
	// boolean flag = checkIsOverlap(searchName);
	// int len = searchName.length();
	// for (T pb : listPinyinBean) {
	// if (pb.getIndexName() != null && pb.getPinyin() != null) {
	// String realChinese = SearchUtil.getNoStopWord(pb.getIndexName()
	// .toLowerCase());
	// String chinese = SearchUtil.getNoStopWord(pb.getPinyin())
	// .toLowerCase();
	// double stime = pb.getStatWeight();
	// int searchwordSize = Math.max(searchWord.length(),
	// chinese.length()); // 与纠错歌手比较的最大值
	// int realLd = StringUtil.LD(searchName, realChinese); // 检索词与真实纠错词的编辑距离
	// int ld = StringUtil.LD(searchName, chinese); // 检索词与纠错词的编辑距离
	// if (isEnglish) {
	// // stime += (1 - (float) ld / searchwordSize) * 10;
	// if (realLd == 0) {
	// // 有命中结果
	// stime += 10.0f;
	// pb.setIsMatch(true);
	// }
	// pb.setStatWeight(stime);
	// queue.insertWithOverflow(pb);
	//
	// } else {
	// stime += (1 - (float) ld / searchwordSize) * 10;
	// if (realLd == 0) {
	// // 有命中结果
	// pb.setIsMatch(true);
	// stime += 10.0f;
	// }
	//
	// // 对于一些ld可能过低，但是其互相包含的那种，情况，此对其进行了加分4f
	// int chineselen = chinese.length();
	// if (len > 2 && chineselen > 2) {
	// if (chinese.indexOf(searchName) != -1
	// || searchName.indexOf(chinese) != -1) {
	// stime += 4.0f;
	// }
	// }
	// // 对于连续有3个叠字的的情况，需要判断编辑距离和searchwordSize的关系
	// // 普遍认为当ld>1的时候，都不满足条件了
	// if (flag) {//
	// if (ld > 2 && realLd > 0) {
	// continue;
	// }
	// }
	// pb.setStatWeight(stime);
	// queue.insertWithOverflow(pb);
	// }
	// }
	// }
	// return queue.size();
	// }

	/**
	 * 判断是否有重叠的叠字 条件： 连续有3个字重叠
	 * 
	 * @param searchName
	 * @return
	 */
	protected boolean checkIsOverlap(String searchName) {
		// TODO Auto-generated method stub
		if (searchName != null && searchName.length() > 0) {
			int len = searchName.length();
			char lastChar = searchName.charAt(0);
			int count = 1;
			for (int i = 1; i < len; i++) {
				char ch = searchName.charAt(i);
				if (ch != lastChar) {
					lastChar = ch;
					count = 1;
				} else {
					count++;
					if (count >= 3)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取拼音查询query
	 * 
	 * @param pinyin
	 *            拼音串
	 * @param field
	 *            查询域
	 * @return 匹配结果的最小，最大字数限制
	 */
	private void getPinyinQueryList(String pinyin, String field, List<BooleanQuery> listBooleanQuery) {
		String fullPinyin = pinyin;
		String[] arrayPinyin = fullPinyin.split(";");
		for (int i = 0; i < arrayPinyin.length; i++) {
			String[] arrayTmp = arrayPinyin[i].split(",");
			BooleanQuery tmpBooleanQuery = new BooleanQuery();
			for (int j = 0; j < arrayTmp.length; j++) {
				String[] arrayWord = arrayTmp[j].trim().split(" ");
				PhraseQuery tmpPhraseQuery = new PhraseQuery();
				for (int k = 0; k < arrayWord.length; k++)
					tmpPhraseQuery.add(new Term(field, arrayWord[k]));
				tmpBooleanQuery.add(tmpPhraseQuery, BooleanClause.Occur.MUST);
			}
			listBooleanQuery.add(tmpBooleanQuery);
		}

	}

	/**
	 * 英文模糊搜索
	 * 
	 * @param tParam
	 *            输入输出参数
	 * @return >=0 成功 ; <0 失败
	 */
	private List<T> fuzzyEnglishSearch(String searchName, List<String> words, int type, Searcher searcher) {
		long times = System.currentTimeMillis();
		try {
			// 标准搜索
			BooleanQuery wordsBooleanQuery = new BooleanQuery();
			BooleanQuery normalBooleanQuery = new BooleanQuery();
			normalBooleanQuery.setMinimumNumberShouldMatch(getFuzzyEnglishMinMatchToken(words));

			for (int i = 0; i < words.size(); i++) {
				normalBooleanQuery.add(new TermQuery(new Term(AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, words.get(i))), BooleanClause.Occur.SHOULD);
			}
			wordsBooleanQuery.add(normalBooleanQuery, BooleanClause.Occur.MUST);

			// 模糊
			StringBuilder bufferCharacterString = new StringBuilder();
			for (int i = 0; i < searchName.length(); i++) {
				if (searchName.charAt(i) != ' ')
					bufferCharacterString.append(searchName.charAt(i));
			}
			Term t = new Term(AutoCompleteConstants.AUTOCOMPLETE_SIMPLEPINYIN_NAME, bufferCharacterString.toString().toLowerCase());
			Term t2 = new Term(AutoCompleteConstants.AUTOCOMPLETE_PINYIN_NAME, bufferCharacterString.toString().toLowerCase());
			// 字符数<4不能错
			// 字符数==4 :1
			// 字符数5,6,7 :2
			// 字符数8,9,...13:3
			// >13 5/length
			// 设置最小相似度依据
			float minimumSimilarity = (float) 0.6;
			int minLength = 0, maxLength = 0;
			if (bufferCharacterString.length() < 4) {
				minimumSimilarity = (float) 0.66;
				minLength = maxLength = searchName.length();
			} else if (bufferCharacterString.length() == 4) {
				minimumSimilarity = (float) (1.0 - 1.0 / bufferCharacterString.length() - 0.15);
				minLength = searchName.length() - 1;
				maxLength = searchName.length() + 1;
			} else if (bufferCharacterString.length() > 4 && bufferCharacterString.length() < 8) {
				minimumSimilarity = (float) (1.0 - 2.0 / bufferCharacterString.length() - 0.2);
				minLength = searchName.length() - 2;
				maxLength = searchName.length() + 2;
			} else if (bufferCharacterString.length() > 7 && bufferCharacterString.length() < 14) {
				minimumSimilarity = (float) (1.0 - 3.0 / bufferCharacterString.length() - 0.1);
				minLength = searchName.length() - 3;
				maxLength = searchName.length() + 3;
			} else if (bufferCharacterString.length() > 13) {
				minimumSimilarity = (float) ((bufferCharacterString.length() - 3) / (bufferCharacterString.length() * 3.0));
				minLength = bufferCharacterString.length() - 3;
				maxLength = bufferCharacterString.length() * 3;
			}
			int prefixLength = 1;// 首字母必须匹配
			// System.out.println("min: " + minimumSimilarity);
			// minimumSimilarity = 0.01f;
			// 模糊query
			FuzzyQuery fuzzyQuery = new FuzzyQuery(t, minimumSimilarity, prefixLength);
			// FuzzyQuery fuzzyQuery2 = new FuzzyQuery(t2, minimumSimilarity,
			// prefixLength);

			BooleanQuery allBooleanQuery = new BooleanQuery();// 我们的业务逻辑查询
			BooleanQuery tmpBooleanQuery = new BooleanQuery();
			fuzzyQuery.setBoost((float) 0.2);
			tmpBooleanQuery.add(fuzzyQuery, BooleanClause.Occur.SHOULD);
			// tmpBooleanQuery.add(fuzzyQuery2, BooleanClause.Occur.SHOULD);
			tmpBooleanQuery.add(wordsBooleanQuery, BooleanClause.Occur.SHOULD);
			allBooleanQuery.add(tmpBooleanQuery, BooleanClause.Occur.MUST);
			return getSearchResult(allBooleanQuery, preelectionCount, type, searcher);
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 英文最小匹配单词数
	 * 
	 * @param lst
	 * @return
	 */
	private int getFuzzyEnglishMinMatchToken(List<String> lst) {
		int iRet = lst.size() - 1;
		if (lst.size() > 8)
			iRet = lst.size() - 3;
		if (lst.size() > 5)
			iRet = lst.size() - 2;
		return iRet;
	}

	private List<T> fuzzySearch(String searchKeyword, List<String> words, int type, Searcher searcher) {
		// TODO Auto-generated method stub
		long times = System.currentTimeMillis();

		String[] allPinyinWords = null; // 模糊拼音
		BooleanQuery chineseBooleanQuery = new BooleanQuery();
		// BooleanQuery ourOperationBooleanQuery = new BooleanQuery();//
		// 我们的业务逻辑查询
		BooleanQuery ourOperationBooleanQuery = new BooleanQuery(true);
		BooleanQuery allBooleanQuery = new BooleanQuery();// 最后组合查询query
		int countQuery = 2;
		try {
			String[] temp = Constants.getPinyinAndSim(searchKeyword);
			String allPinyin = temp[1];
			allPinyinWords = allPinyin.trim().split(";");
			// 汉字单字匹配
			for (int i = 0; i < words.size(); i++) {
				chineseBooleanQuery.add(new TermQuery(new Term(AutoCompleteConstants.AUTOCOMPLETE_SEARCH_NAME, words.get(i))), BooleanClause.Occur.SHOULD);
				countQuery++;
			}
			// 最小匹配限制
			if (words.size() > 9) {
				chineseBooleanQuery.setMinimumNumberShouldMatch(Math.round(words.size() * LONG_RESULT_RATE));
			} else if (words.size() > 2) {
				chineseBooleanQuery.setMinimumNumberShouldMatch(Math.round(words.size() * RESULT_RATE));
			}
			// 拼音单字匹配
			for (int i = 0; i < allPinyinWords.length; i++) {
				String[] arrayTmp = allPinyinWords[i].split(" |,");
				BooleanQuery tmpBooleanQuery = new BooleanQuery();
				countQuery++;
				for (int j = 0; j < arrayTmp.length; j++) {
					tmpBooleanQuery.add(new TermQuery(new Term(AutoCompleteConstants.AUTOCOMPLETE_SIMPLEPINYIN_NAME, arrayTmp[j])), BooleanClause.Occur.SHOULD);
					countQuery++;
				}
				if (words.size() > 9) {
					tmpBooleanQuery.setMinimumNumberShouldMatch(Math.round(words.size() * LONG_RESULT_RATE));
				} else if (words.size() > 2) {
					tmpBooleanQuery.setMinimumNumberShouldMatch(Math.round(words.size() * RESULT_RATE));
				}
				ourOperationBooleanQuery.add(tmpBooleanQuery, BooleanClause.Occur.SHOULD);

			}
			// 汉字单字的 查询 权重
			chineseBooleanQuery.setBoost(2.2f);
			ourOperationBooleanQuery.add(chineseBooleanQuery, BooleanClause.Occur.SHOULD);
			allBooleanQuery.add(ourOperationBooleanQuery, BooleanClause.Occur.MUST);
			countQuery++;
			return getSearchResult(allBooleanQuery, preelectionCount, type, searcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected abstract void addOtherQuery(BooleanQuery query, int type);

	/**
	 * 根据查询条件query和获取的记录数，获取搜索结果到tParam.list
	 * 
	 * @param query
	 *            :查询query;recordCount 返回最多纪录数；tParam 结果集参数
	 * @return >=0 成功 ; <0 失败
	 */
	private List<T> getSearchResult(BooleanQuery query, int recordCount, int type, Searcher searcher) {
		if (query == null) {
			return null;
		}
		addOtherQuery(query, type);
		// BooleanQuery typeBooleanQuery = new BooleanQuery(true);
		// typeBooleanQuery.add(new TermQuery(new Term(
		// AutoCompleteConstants.META_TYPE, type + "")),
		// BooleanClause.Occur.MUST);
		// query.add(typeBooleanQuery, BooleanClause.Occur.MUST);
		List<T> list = new ArrayList<T>();
		try {
			int size = recordCount * 4;
			TopDocs top = searcher.search(query, null, size);
			// System.out.println("top size is " + top.totalHits);
			int count = size > top.totalHits ? top.totalHits : size;
			HashSet<String> hash = new HashSet<String>();
			int searchTimes = 0;
			float maxScore = top.getMaxScore();
			for (int i = 0, j = 0; i < count && j <= recordCount; i++) {
				Document doc = searcher.doc(top.scoreDocs[i].doc);
				// System.out.println(doc);
				T bean = this.convertDocToSearchBean(doc, top.scoreDocs[i].doc, top.scoreDocs[i].score);
				if (bean != null) {
					if (!beanIsOverLop(hash, bean)) {
						list.add(bean);
						j++;
					}
				}
			}
			return list;
		} catch (Exception e) {
		}
		return list;
	}

	protected abstract boolean beanIsOverLop(HashSet<String> set, T bean);

	/**
	 * 根据搜索词长度获取 长度限制范围
	 * 
	 * @param wordCount
	 *            搜索词字数
	 * @return 匹配结果的最小，最大字数限制
	 */
	private int[] getLengthRang(int wordCount) {
		// 构造长度限制query
		int minWordLenth = 0;// tParam.minWordLenth;
		int maxWordLenth = 0;// tParam.maxWordLenth;
		if (wordCount <= 2) {
			minWordLenth = 1;
			maxWordLenth = 4;
		} else if (wordCount >= 3 && wordCount <= 6) {
			minWordLenth = wordCount - 2;
			maxWordLenth = wordCount + 3;
		} else if (wordCount >= 7 && wordCount <= 10) {
			minWordLenth = wordCount - 4;
			maxWordLenth = wordCount + 4;
		} else if (wordCount >= 11) {
			minWordLenth = wordCount - 4;
			maxWordLenth = wordCount + 4;
		}
		return new int[] { minWordLenth, maxWordLenth };
	}
}
