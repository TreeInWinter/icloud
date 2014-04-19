package com.icloud.framework.http.encode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlEncodingSniffer {
	protected static final String BALANCE_TAGS = "http://cyberneko.org/html/features/balance-tags";
//	private static final float NEW_INJECTED_PAGE_SCORE = 1.0F;
//	private static final int CHUNK_SIZE = 2000;
//	private static Pattern metaPattern = Pattern.compile(
//			"<meta\\s+([^>]*http-equiv=\"?content-type\"?[^>]*)>", 2);

	private static Pattern charsetPattern = Pattern.compile(
			"charset=\\s*([a-z][_\\-0-9a-z]*)", 2);

	private static Pattern xmlencodingPattern = Pattern.compile(
			"encoding=\"([a-z][_\\-0-9a-z]*)\"", 2);

	private static Pattern testPattern = Pattern.compile("<?xml.*?>", 2);

	public static String sniffCharacterEncoding(byte[] content) {
		
		int length = content.length < 2000 ? content.length : 2000;

//		String str = new String(content, 0, 0, length);
		String str = new String(content, 0, length);

		String encoding = null;

		Matcher metaMatcher = xmlencodingPattern.matcher(str);

		Matcher metaMatcher2 = testPattern.matcher(str);

		Matcher charsetMatcher = charsetPattern.matcher(str);

		if (metaMatcher2.find()) {
			encoding = new String(metaMatcher2.group(0));
		}

		if (metaMatcher.find()) {
			encoding = new String(metaMatcher.group(1));
		}

		if (charsetMatcher.find()) {
			encoding = new String(charsetMatcher.group(1));
		}

		return encoding;
	}
}