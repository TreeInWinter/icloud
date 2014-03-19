package com.travelzen.framework.http.encode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.travelzen.framework.core.util.TZUtil;
import com.travelzen.framework.http.HttpContent;

public class EncodingDetector {
	private Logger LOG = LoggerFactory.getLogger(EncodingDetector.class);
	public static final int NO_THRESHOLD = -1;
	public static final String MIN_CONFIDENCE_KEY = "encodingdetector.charset.min.confidence";
	private static final HashMap<String, String> ALIASES = new HashMap<String, String>();

	private static final HashSet<String> DETECTABLES = new HashSet<String>();
//	private static final int MIN_LENGTH = 4;
	private int minConfidence;
	private CharsetDetector detector;
	private List<EncodingClue> clues;

	static {
		DETECTABLES.add("text/html");
		DETECTABLES.add("text/plain");
		DETECTABLES.add("text/richtext");
		DETECTABLES.add("text/rtf");
		DETECTABLES.add("text/sgml");
		DETECTABLES.add("text/tab-separated-values");
		DETECTABLES.add("text/xml");
		DETECTABLES.add("application/rss+xml");
		DETECTABLES.add("application/xhtml+xml");

		ALIASES.put("ISO-8859-1", "windows-1252");
		ALIASES.put("EUC-KR", "x-windows-949");
		ALIASES.put("x-EUC-CN", "GB18030");
		ALIASES.put("GBK", "GB18030");
	}

	public EncodingDetector() {
		this.minConfidence = -1;
		this.detector = new CharsetDetector();
		this.clues = new ArrayList<EncodingClue>();
	}

	public void autoDetectClues(HttpContent content, boolean filter) {
		byte[] data = content.getContent();

		if (data.length > 4) {
			CharsetMatch[] matches = (CharsetMatch[]) null;
			try {
				this.detector.enableInputFilter(filter);
				if (data.length > 4) {
					this.detector.setText(data);
					matches = this.detector.detectAll();
				}
			} catch (Exception e) {
				this.LOG.debug(TZUtil.stringifyException(e));
			}

			if (matches != null) {
				for (CharsetMatch match : matches) {
					System.out.println(match.getName() + ":"
							+ match.getConfidence());
				}

			}

		}

		addClue(parseCharacterEncoding(content.getContentType()), "header");
	}

	public void addClue(String value, String source, int confidence) {
		if ((value == null) || ("".equals(value))) {
			return;
		}
		value = resolveEncodingAlias(value);
		if (value != null)
			this.clues.add(new EncodingClue(value, source, confidence));
	}

	public void addClue(String value, String source) {
		addClue(value, source, -1);
	}

	public String guessEncoding(HttpContent content, String defaultValue) {
		String base = "";

		EncodingClue defaultClue = new EncodingClue(defaultValue, "default");
		EncodingClue bestClue = defaultClue;

		for (EncodingClue clue : this.clues) {
			if (this.LOG.isTraceEnabled()) {
				this.LOG.trace(base + ": charset " + clue);
			}
			String charset = clue.value;
			if ((this.minConfidence >= 0)
					&& (clue.confidence >= this.minConfidence)) {
				if (this.LOG.isTraceEnabled()) {
					this.LOG.trace(base + ": Choosing encoding: " + charset
							+ " with confidence " + clue.confidence);
				}
				return resolveEncodingAlias(charset).toLowerCase();
			}
			if ((clue.confidence == -1) && (bestClue == defaultClue)) {
				bestClue = clue;
			}
		}

		if (this.LOG.isTraceEnabled()) {
			this.LOG.trace(base + ": Choosing encoding: " + bestClue);
		}
		return bestClue.value.toLowerCase();
	}

	public void clearClues() {
		this.clues.clear();
	}
/*
	private void findDisagreements(String url, List<EncodingClue> newClues) {
		HashSet<String> valsSeen = new HashSet<String>();
		HashSet<String> sourcesSeen = new HashSet<String>();
		boolean disagreement = false;
		for (int i = 0; i < newClues.size(); i++) {
			EncodingClue clue = newClues.get(i);
			if ((!clue.isEmpty()) && (!sourcesSeen.contains(clue.source))) {
				if ((valsSeen.size() > 0) && (!valsSeen.contains(clue.value))
						&& (clue.meetsThreshold())) {
					disagreement = true;
				}
				if (clue.meetsThreshold()) {
					valsSeen.add(clue.value);
				}
				sourcesSeen.add(clue.source);
			}
		}
		if (disagreement) {
			StringBuffer sb = new StringBuffer();
			sb.append("Disagreement: " + url + "; ");
			for (int i = 0; i < newClues.size(); i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(newClues.get(i));
			}
			this.LOG.trace(sb.toString());
		}
	}
*/
	public static String resolveEncodingAlias(String encoding) {
		if ((encoding == null) || (!Charset.isSupported(encoding)))
			return null;
		String canonicalName = new String(Charset.forName(encoding).name());
		return ALIASES.containsKey(canonicalName) ? (String) ALIASES
				.get(canonicalName) : canonicalName;
	}

	public static String parseCharacterEncoding(String contentType) {
		if (contentType == null)
			return null;
		int start = contentType.indexOf("charset=");
		if (start < 0)
			return null;
		String encoding = contentType.substring(start + 8);
		int end = encoding.indexOf(';');
		if (end >= 0)
			encoding = encoding.substring(0, end);
		encoding = encoding.trim();
		if ((encoding.length() > 2) && (encoding.startsWith("\""))
				&& (encoding.endsWith("\"")))
			encoding = encoding.substring(1, encoding.length() - 1);
		return encoding.trim();
	}

	public static void main(String[] args) throws IOException {
	}

	private class EncodingClue {
		private String value;
		private String source;
		private int confidence;

		public EncodingClue(String value, String source) {
			this(value, source, -1);
		}

		public EncodingClue(String value, String source, int confidence) {
			this.value = value.toLowerCase();
			this.source = source;
			this.confidence = confidence;
		}
/*
		public String getSource() {
			return this.source;
		}

		public String getValue() {
			return this.value;
		}
*/
		public String toString() {
			return this.value
					+ " ("
					+ this.source
					+ (this.confidence >= 0 ? ", " + this.confidence
							+ "% confidence" : "") + ")";
		}
/*
		public boolean isEmpty() {
			return (this.value == null) || ("".equals(this.value));
		}

		public boolean meetsThreshold() {
			return (this.confidence < 0)
					|| ((EncodingDetector.this.minConfidence >= 0) && (this.confidence >= EncodingDetector.this.minConfidence));
		}
*/
	}
}