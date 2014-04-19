package com.icloud.search.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

public class Constants {

	private static final Log log = LogFactory.getLog(Constants.class);
	public static final String DEFAULT_STRING = "###";
	public static final String DEFAULT_SPLIT_STRING = "{]";
	public static Version LUCENE_VERSION = Version.LUCENE_33; // lucene 版本
	public static final int MAX_KEYWORDS_LENGTH = 15;// 歌曲搜索词长度
	public static final int MAX_LYRIC_KEYWORDS_LENGTH = 20;// 歌词搜索词长度
	private static Map<Character, Character> numToCnNumMap = new HashMap<Character, Character>();

	static {
		numToCnNumMap.put('0', '零');
		numToCnNumMap.put('1', '一');
		numToCnNumMap.put('2', '二');
		numToCnNumMap.put('3', '三');
		numToCnNumMap.put('4', '四');
		numToCnNumMap.put('5', '五');
		numToCnNumMap.put('6', '六');
		numToCnNumMap.put('7', '七');
		numToCnNumMap.put('8', '八');
		numToCnNumMap.put('9', '九');
	}

	/* 搜索类型日志 */

	public static final int CHANNEL_RING = 4;

	public static final int CHANNEL_MP3 = 10;

	public static final String CHANNEL_SUB_DEFAULT = "0";

	public static final String CHANNEL_SUB_MP3 = "0";

	public static final String CHANNEL_SUB_ALBUM = "1";

	public static final String CHANNEL_SUB_LYRIC = "2";

	public static final String CHANNEL_SUB_SOUND = "3";

	public static final String CHANNEL_SUB_RELATE = "5";

	// 歌手纠错
	public static final String CHANNEL_SUB_SINGER = "6";
	// 合作的子频道
	public static final String CHANNEL_SUB_COOPERATE = "7";
	// 精选集的子频道
	public static final String CHANNEL_SUB_Collect = "8";
	// 3G合作业务MP3子频道
	public static final String CHANNEL_SUB_3GMP3 = "9";
	// 3G合作业务lyric子频道
	public static final String CHANNEL_SUB_3GLYRIC = "10";
	// 3G合作业务album子频道
	public static final String CHANNEL_SUB_3GALBUM = "11";
	// 提示词子频道
	public static final String CHANNEL_SUB_PROMPT = "12";

	// 高亮替换字符
	public static final String HIGHLIGHTER_START = "#_fd_#";

	public static final String HIGHLIGHTER_END = "#_rw_#";

	public static final String HIGHLIGHTER_START_WITH_BANK = " #_fd_#";

	public static final String ENGLISH_FLAG = "#_XQ_#";

	// 频道资源的类型，则以此字段作为查询条件
	public static final String FIELD_RESOURCETYPE = "Resourcetype";

	// 频道资源的类别，如：港台，清纯等
	public static final String FIELD_SEARCHTYPE = "Searchtype";

	/* 搜索范围与排序条件 */
	public static final String CATALOG_SMALL = "small";

	public static final String CATALOG_LARGE = "large";

	public static final String CATALOG_MEDIUM = "medium";

	/* 缓存 */

	public static final int NO_CACHED = 0;

	public static final int CACHED = 1;

	/* Mp3 */
	public static final int MAX_LYRIC_LENGTH = 26;

	/* search case */
	public static final int ALBUM_SINGER_SCASE = 30;
	// 按专辑中的歌名检索
	public static final int ALBUM_SONG_SCASE = 10;

	// 需要打new，hot标记的状态
	public static final int MP3_MARK_SCASE = 2;

	private static HashSet<String> sinngers = null; // 歌手名称表

	private static HashMap<String, String> pinyin = null; // 汉字拼音对照表

	public static HashMap<String, Float> posset = new HashMap<String, Float>();

	public static final char[] INT_TO_LETTER = { 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private static StandardAnalyzer analyzer = new StandardAnalyzer(
			Version.LUCENE_30, new HashSet<String>());

	public static void init() {
		initPinyin();
		initSinger();
		// initStars();
		// initPosset();
		// initModel();
		// initMobile();
	}

	public static void initPosset() {
		posset.put("nr", new Float(2.0F));
		posset.put("n", new Float(1.5F));
		posset.put("x", new Float(0.5F));
		log.info("posset init compilite!");
	}

	/**
	 * 查歌手名
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isSinger(String key) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < key.length(); i++) {
			char c = key.charAt(i);
			if (c >= 'a' && c <= 'z') {
				c -= 32;
			}
			sb.append(c);
		}
		if (null == sinngers) {
			initSinger();
		}
		return sinngers.contains(sb.toString());
	}

	/**
	 * 获取拼音
	 * 
	 * @param tParam
	 *            输入输出参数
	 * @return sbFullPinyin 英文为用户输入原型，中文为未处理拼音,非纯英文的分开 sbSimPinyin
	 *         英文为将用户输入词串起来，中文为处理后鼻音，非纯英文的分开
	 */
	public static String[] getPinyinAndSim(String text) {
		if (null == pinyin) {
			initPinyin();
		}
		text = numToCnNum(text);

		Boolean isEnglish = isEnglish(text); // 判断是否是纯英文
		StringBuffer sbFullPinyin = new StringBuffer();
		StringBuffer sbSimPinyin = new StringBuffer();

		TokenStream ts = analyzer.tokenStream(null, new StringReader(text));
		try {
			// Token t = ts.next();
			CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);

			while (ts.incrementToken()) {
				if (isEnglish) { // 纯英文的处理
					sbFullPinyin.append(ta.toString() + " ");
					sbSimPinyin.append("" + ta.toString());
				} else { // 中文或者中英混合处理
					String py = pinyin.get(ta.toString());
					if (null == py) {
						sbFullPinyin.append(" " + ta.toString()); // 将拆分的字符分别加上新得到的
						sbSimPinyin.append(" " + ta.toString());
					} else {
						String pinyin = py.split(" ")[0];
						sbFullPinyin.append(" " + pinyin); // 将拆分的字符分别加上新得到的
						sbSimPinyin.append(" " + pinYinProcess(pinyin));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[] { sbFullPinyin.toString().trim(),
				sbSimPinyin.toString().trim() };
	}

	/**
	 * [0] full pinyin [1] sim pinyin
	 * 
	 * @param text
	 * @return
	 */
	public static String[] getRingPinyinAndSim(String text) {
		if (null == text || "".equals(text.trim())) {
			return new String[] { "", "" };
		}

		if (null == pinyin) {
			initPinyin();
		}

		text = text.trim();
		text = SearchUtil.traditionalToSimple(text);
		text = numToCnNum(text);
		String[] texts = text.split("[\\s]+");
		StringBuffer sbFullPinyin = new StringBuffer();
		StringBuffer sbSimPinyin = new StringBuffer();
		for (int i = 0; i < texts.length; i++) {
			try {
				TokenStream ts = analyzer.tokenStream(null, new StringReader(
						texts[i]));
				TermAttribute ta = ts.getAttribute(TermAttribute.class);
				while (ts.incrementToken()) {
					String py = pinyin.get(ta.term());
					if (null == py) {
						sbFullPinyin.append(ta.term());
						sbSimPinyin.append(ta.term()).append(" ");
					} else {
						String pin = py.trim().split("[\\s]+")[0];
						sbFullPinyin.append(pin);

						// 拼音处理
						pin = pinYinProcess(pin);

						sbSimPinyin.append(pin).append(" ");
					}
				}
				ts.close();
			} catch (IOException e) {
				log.info("token error: ", e);
			}
			sbFullPinyin.append(" ");
		}

		return new String[] { sbFullPinyin.toString().trim(),
				sbSimPinyin.toString().trim() };
	}

	private static String pinYinProcess(String pinYin) {
		if (null == pinYin || "".equals(pinYin)) {
			return pinYin;
		}

		// 去卷舌音
		if (pinYin.matches("^[csz]h.+")) {
			pinYin = pinYin.charAt(0) + pinYin.substring(2);
		}
		// 去后鼻音
		if ('g' == pinYin.charAt(pinYin.length() - 1)) {
			pinYin = pinYin.substring(0, pinYin.length() - 1);
		}

		return pinYin;
	}

	// 获得拼音首字母
	public static String getFirstPinYin(String pinYin) {
		if (null == pinYin || "".equals(pinYin)) {
			return pinYin;
		}
		// 去卷舌音
		if (pinYin.matches("^[csz]h.+")) {
			return pinYin.substring(0, 2);
		}
		return pinYin.substring(0, 1);
	}
	//　获得名字简称
	public static String getSimplePinyin(String pinYin){
		if (null == pinYin || "".equals(pinYin)) {
			return null;
		}
		pinYin = pinYin.toLowerCase();
		String[] pinyins = pinYin.split(" ");
		if(pinyins==null||pinyins.length<2){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(String py:pinyins){
			sb.append(getFirstPinYin(py));
		}
		return sb.toString();
	}
	
	private static String numToCnNum(String text) {
		if (null == text || "".equals(text)) {
			return text;
		}

		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < text.length(); ++i) {
			char c = text.charAt(i);
			if (numToCnNumMap.containsKey(c)) {
				sb.append(numToCnNumMap.get(c));
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/*
	 * @param text 检查是否包含汉字;若包含,返回true;否则,返回false
	 */
	public static boolean containsChinese(String text) {
		if (null == text || "".equals(text.trim())) {
			return false;
		}

		if (null == pinyin) {
			initPinyin();
		}

		try {
			TokenStream ts = analyzer.tokenStream(null, new StringReader(text));
			TermAttribute ta = ts.getAttribute(TermAttribute.class);
			while (ts.incrementToken()) {
				if (pinyin.containsKey(ta.term())) {
					return true;
				}
			}
			ts.close();
		} catch (IOException e) {
			log.info("token error: ", e);
		}

		return false;
	}

	/**
	 * 通过中文字或者混合字符取得拼音
	 * 
	 * @param word
	 * @return
	 */
	public static String getPinyinWord(String word) {
		// 去掉卷舌
		StringBuffer sb = new StringBuffer();
		if (null == pinyin) {
			initPinyin();
		}
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')) {
				sb.append(c);
			} else {
				String temp = (String) pinyin.get(c + "");
				if (temp != null) {
					String[] aTemp = temp.split(" ");
					if (aTemp[0].matches("^[zcs]h.+")) {
						sb.append(aTemp[0].charAt(0)).append(
								aTemp[0].substring(2));
					} else {
						sb.append(aTemp[0]);
					}
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString().toLowerCase().trim();
	}

	public static String getSimPinyinWordWithSpace(String word){
		StringBuffer sb = new StringBuffer();
		if (null == pinyin) {
			initPinyin();
		}
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')) {
				sb.append(c);
			} else {
				String temp = (String) pinyin.get(c + "");
				if (temp != null) {
					String[] aTemp = temp.split(" ");
					for (int j = 0; j < aTemp[0].length(); j++) {
						sb.append(aTemp[0].charAt(j)+" ");
					}
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString().toLowerCase().trim();
	}
	public static String getSimPinyinWord(String word) {
		StringBuffer sb = new StringBuffer();
		if (null == pinyin) {
			initPinyin();
		}
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')) {
				sb.append(c);
			} else {
				String temp = (String) pinyin.get(c + "");
				if (temp != null) {
					String[] aTemp = temp.split(" ");
					for (int j = 0; j < aTemp[0].length(); j++) {
						sb.append(aTemp[0].charAt(j));
					}
				} else {
					sb.append(c);
				}
			}
		}
		return sb.toString().toLowerCase().trim();
	}

	/* 从歌手库文件中读入歌手 */
	private static void initSinger() {
		sinngers = new HashSet<String>();
		try {
			InputStream is = Constants.class.getResourceAsStream("/singer.txt");
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			line = reader.readLine();
			while (line != null) {
				if (!"\n".equals(line.trim())) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < line.length(); i++) {
						char c = line.charAt(i);
						if (c >= 'a' && c <= 'z') {
							c -= 32;
						}
						sb.append(c);
					}
					sinngers.add(sb.toString().trim());
					line = reader.readLine();
				}
			}
			reader.close();
			log.info("singer.txt init compilite. total: " + sinngers.size());
		} catch (IOException e) {
			log.error("File singer.txt not found!", e);
		}
	}

	/* 从拼音对照文件中读入中文字对应的拼音 */
	private static void initPinyin() {
		try {
			pinyin = new HashMap<String, String>();
			InputStream is = Constants.class.getResourceAsStream("/pinyin.txt");
			String line; // 用来保存每行读取的内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"));
			line = reader.readLine(); // 读取第一行
			while (line != null) { // 如果 line 为空说明读完了
				// 如果系统编码不为UTF-8(文件是ANSI编码的)
				String[] myLine = line.trim().split(" ");
				if (myLine.length > 1) {
					StringBuffer sb = new StringBuffer();
					for (int i = 1; i < myLine.length; i++) {
						sb.append(myLine[i]).append(" ");
					}
					pinyin.put(myLine[0], sb.toString().trim());
				} else {
					log.error("error：" + myLine[0]);
				}
				line = reader.readLine(); // 读取下一行
			}
			reader.close();
			log.info("pinyin.txt init compilite");
		} catch (IOException e) {
			log.error("File pinyin.txt not found!");
		}
	}

	/**
	 * 判断是否是纯英文
	 * 
	 * @param word
	 * @return
	 */
	public static Boolean isEnglish(String word) {
		int charcount = 0;
		int i = 0;
		while (i < word.length()) {
			char c = word.charAt(i);
			if ((c <= 122 && c >= 97) || (c <= 90 && c >= 65)
					|| (c <= 65370 && c >= 65345) || (c <= 65338 && c >= 65313)) {
				charcount++;
				i++;
			} else if (c < 128 || (c <= 65305 && c >= 65296) || c == 65285
					|| (c <= 65295 && c >= 65291)) {
				i++;
			} else {
				return false;
			}
		}
		if (charcount == 0) {
			return false;
		}
		return true;
	}

	/* 将字符串用字分词器切分成一个字符串数组 */
	public static List<String> toWordList(final String str) {
		if (null == str || "".equals(str.trim())) {
			return new ArrayList<String>();
		}
		TokenStream ts = analyzer.tokenStream(null, new StringReader(str));
		CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);
		List<String> list = new ArrayList<String>();
		try {
			while (ts.incrementToken()) {
				list.add(ta.toString());
			}
			ts.close();
			if (list.size() > 0) {
				return list;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 是否被包含
	public static int isContains(final List<String> shortList,
			final List<String> longList) {
		ListCompare lc = new ListCompare(shortList, longList);
		return lc.isContains();
	}

	public static boolean isContainsMp3(String text, String firstName) // 包含的话返回true
	{
		HashSet<String> hsText = getMp3Token(text);
		HashSet<String> hs = getMp3Token(firstName);
		boolean isContains = true;
		for (Iterator<String> iter = hsText.iterator(); iter.hasNext();) {
			if (!hs.contains(iter.next())) {
				isContains = false;
				break;
			}
		}
		return isContains;
	}

	private static HashSet<String> getMp3Token(String text) {
		HashSet<String> hs = null;
		try {
			hs = new HashSet<String>();
			TokenStream tokenStream = analyzer.tokenStream(null,
					new StringReader(text));
			CharTermAttribute ta = tokenStream
					.getAttribute(CharTermAttribute.class);

			while (tokenStream.incrementToken()) {
				hs.add(ta.toString());
			}
			tokenStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hs;
	}

	public static void main(String[] args) {
		// init();
		// System.out.println(getPinyinWord("毛阿敏mp3"));
		String[] pinyins = getPinyinAndSim("爱的供养asdfasdf");
		System.out.println("全拼" + pinyins[0] + "\n简拼" + pinyins[1]);
		// String[] test = "中国人 nihao,ok,".split(" |,");
		// System.out.println(test[0] + "-" + test[1]);
		System.out.println(isEnglish("cai yi linａｓｄｆｇｈｊｋｌｚ"));
	}
}
