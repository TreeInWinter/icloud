package com.travelzen.framework.http.spider;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RegularExpUtils {

	final static HashMap<String, Integer> modifierMap = new HashMap<String, Integer>();
	static {
		modifierMap.put("i", Pattern.CASE_INSENSITIVE);
		modifierMap.put("m", Pattern.MULTILINE);
		modifierMap.put("s", Pattern.DOTALL);

	}

//	private static String specialString = ".?*()[]^+|$\\ ";

	// 前面不是'\'的[%... %],代表组模式，如果加了'\'则不是
	public static Pattern GROUP_VAR_PATTERN = Pattern.compile(
			"((?<!\\\\)\\[%(\\w+)=(.*?)%\\])", Pattern.CASE_INSENSITIVE
					| Pattern.MULTILINE | Pattern.DOTALL);

	public static Pattern UNGREED_PLACEHOLDER_PATTERN = Pattern.compile(
			"\\(\\.\\*\\?\\)", Pattern.LITERAL);

	public static Pattern UNGREED_VAR_PLACEHOLDER_PATTERN = Pattern
			.compile("\\\\\\[\\\\\\$\\w+?\\\\\\]");

	static Pattern REG_PATTERN = Pattern.compile(
			"\\.|\\?|\\*|\\(|\\)|\\[|\\]|\\^|\\+|\\||\\$|\\\\|\\{",
			Pattern.CASE_INSENSITIVE);

	static Pattern BLANK_PLACEHOLDER_PATTERN = Pattern.compile("[$]",
			Pattern.LITERAL);

	static Pattern GREED_BLANK_PLACEHOLDER_PATTERN = Pattern.compile("[#]",
			Pattern.LITERAL);

	public static Pattern VAR_PATTERN = Pattern.compile(
			"\\[(\\$|#)(.*?)(\\((.*?)\\))?\\]", Pattern.CASE_INSENSITIVE);

	// static Pattern UNFREED_VAR_PATTERN = Pattern.compile("\\[\\#(.*?)\\]",
	// Pattern.CASE_INSENSITIVE);

	static Pattern FULL_PATTERN = Pattern.compile("^\\[\\$(.*?)\\]$",
			Pattern.CASE_INSENSITIVE);

	// 定制正则的高级匹配模式， 主要问题是，需要判断前面要跳过几个group
	// 需要对每个变量所在的group做 name->int 的hash
	static public VarAssign parseMatchRegexpAdv(String exp, String modStr) {
		VarAssign varExp = new VarAssign();

		// 去掉空的占位符
		exp = BLANK_PLACEHOLDER_PATTERN.matcher(exp).replaceAll(".*?");
		exp = GREED_BLANK_PLACEHOLDER_PATTERN.matcher(exp).replaceAll(".*");

		Matcher matcher = VAR_PATTERN.matcher(exp);

		int last = 0;
		int itemCnt = 0;
		while (matcher.find(last)) {
			last = matcher.end();

//			StringBuffer sbuf = new StringBuffer();
			if (matcher.groupCount() > 0) {

				String var = matcher.group(2);

				if (StringUtils.isEmpty(var))
					continue;

				String needle = matcher.group(4);
				if (StringUtils.isEmpty(needle)) {

					exp = Pattern.compile("[$" + var + "]", Pattern.LITERAL)
							.matcher(exp).replaceAll("(.*?)");

					exp = Pattern.compile("[#" + var + "]", Pattern.LITERAL)
							.matcher(exp).replaceAll("(.*)");
				} else {

					String rep = escapeRegular(needle);

					exp = Pattern.compile("[$" + var + "(" + needle + ")]",
							Pattern.LITERAL).matcher(exp).replaceFirst(
							"(" + rep + "*?)");

					exp = Pattern.compile("[#" + var + "(" + needle + ")]",
							Pattern.LITERAL).matcher(exp).replaceAll(
							"(" + rep + "*)");
				}

				itemCnt++;

				varExp.getVarlist().add(new VarAssign.Var(var, itemCnt));

			}
		}

		int mod = 0;

		String[] ms = modStr.split("");
		for (String m : ms) {
			Integer i = modifierMap.get(m);
			if (null != i) {
				mod |= i.intValue();
			}
		}

		varExp.setRegexp(Pattern.compile(exp, mod));
		varExp.setRegexpStr(exp);
		return varExp;
	}

	static public VarAssign parseMatchRegexp(String exp) {

		VarAssign varExp = new VarAssign();

		Matcher fullMatch = FULL_PATTERN.matcher(exp);
		if (fullMatch.find()) {
			String var = fullMatch.group(1);
			varExp.getVarlist().add(new VarAssign.Var(var, 1));
			String regexp = "(.*)";
			varExp.setRegexpStr(regexp);
			varExp.setRegexp(Pattern.compile(regexp, Pattern.MULTILINE
					| Pattern.DOTALL));
			return varExp;
		}

		Matcher matcher = VAR_PATTERN.matcher(exp);

		// 转义正则元字符
		Matcher m = REG_PATTERN.matcher(exp);
		StringBuffer expBuf = new StringBuffer();

		// 转义正则元字符
		while (m.find()) {
			m.appendReplacement(expBuf, "\\\\$0");
		}
		m.appendTail(expBuf);

		exp = expBuf.toString();

		// 去掉空的占位符
		exp = Pattern.compile("\\[\\$\\]", Pattern.LITERAL).matcher(exp)
				.replaceAll(".*?");
		exp = Pattern.compile("\\[#\\]", Pattern.LITERAL).matcher(exp)
				.replaceAll(".*");

		exp = exp.replaceAll("\\s+", "\\\\s+");

		// 找到每一个替换变量
		int last = 0;

		int itemCnt = 0;
		while (matcher.find(last)) {
			last = matcher.end();

//			StringBuffer sbuf = new StringBuffer();
			// 匹配到[.+?]的话，记录各个变量的group值， 只能是扁平结构
			if (matcher.groupCount() > 0) {

				String var = matcher.group(2);

				if (StringUtils.isEmpty(var))
					continue;

				// String needle=matcher.group(3);
				// if(StringUtils.isEmpty(needle)){
				// needle=".*";
				// }

				String needle = matcher.group(4);
				if (StringUtils.isEmpty(needle)) {

					exp = Pattern.compile("\\[\\$" + var + "\\]",
							Pattern.LITERAL).matcher(exp).replaceAll("(.*?)");

					exp = Pattern
							.compile("\\[#" + var + "\\]", Pattern.LITERAL)
							.matcher(exp).replaceAll("(.*)");
				} else {

					needle = escapeRegular(needle);

					exp = Pattern.compile(
							"\\[\\$" + var + "\\(" + needle + "\\)\\]",
							Pattern.LITERAL).matcher(exp).replaceAll(
							"(" + needle + "*?)");

					exp = Pattern.compile("\\[#" + var + "(" + needle + ")\\]",
							Pattern.LITERAL).matcher(exp).replaceAll(
							"(" + needle + "*)");
				}

				itemCnt++;
				// 把占位符替换为正则式,暂时不考虑

				varExp.getVarlist().add(new VarAssign.Var(var, itemCnt));

			}
		}

		varExp.setRegexp(Pattern.compile(exp, Pattern.MULTILINE
				| Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		varExp.setRegexpStr(exp);
		return varExp;
	}

	// 开启组匹配模式,暂时组模式里只能再包含一个变量，因为一个组最终匹配出来，比如
	// [%actor=<a href="/person/905359/" title="塞吉·卡斯特里图/Sergio
	// Castellitto">塞吉·卡斯特里图 Sergio Castellitto</a>]
	// </p><p><a href="/person/957004/" title="/Tiziana Lodato"> Tiziana
	// Lodato</a></p>
	// <p><a href="/person/936223/" title="/Franco Scaldati"> Franco
	// Scaldati</a> 
	// <a href="/movie/10000/fullcredits.html" title="更多
	/**
	 * [%film=<a href="[$acturl]">[$actor]</a>%]
	 */
	// 组模式必须要和原来的模式分两次处理
	static public VarAssign parseGroupMatchRegexp(String exp) {

		VarAssign varExp = new VarAssign();

		// 去掉所有单个模式的匹配

		// 找出表达式中的组模式
		Matcher groupMatcher = GROUP_VAR_PATTERN.matcher(exp);
		String expStr = groupMatcher.replaceAll("(.*?)");

		// 转义正则元字符
		Matcher regm = REG_PATTERN.matcher(expStr);
		StringBuffer sBuf = new StringBuffer();

		// 转义正则元字符
		while (regm.find()) {
			regm.appendReplacement(sBuf, "\\\\$0");
		}
		regm.appendTail(sBuf);

		expStr = sBuf.toString();

		expStr = UNGREED_PLACEHOLDER_PATTERN.matcher(expStr)
				.replaceAll("(.*?)");

		expStr = expStr.replaceAll("\\s+", "\\\\s+");

		// 去掉空的占位符
		expStr = Pattern.compile("\\[\\$\\]", Pattern.LITERAL).matcher(expStr)
				.replaceAll(".*?");
		expStr = Pattern.compile("\\[#\\]", Pattern.LITERAL).matcher(expStr)
				.replaceAll(".*");

		int last = 0;
		int itemCnt = 0;

		while (groupMatcher.find(last)) {

			VarAssign grpVarExp = new VarAssign();

			last = groupMatcher.end();

//			StringBuffer sbuf = new StringBuffer();
			// 匹配到[.+?]的话，记录各个变量的group值， 只能是扁平结构
			if (groupMatcher.groupCount() > 0) {

				// film
				String gpName = groupMatcher.group(2);

				// <a href="[$acturl]">[$actor]</a>
				String gpStr = groupMatcher.group(3);

				// var=$actor
				if (StringUtils.isEmpty(gpStr))
					continue;

				Matcher varMatcher = VAR_PATTERN.matcher(gpStr);

				//
				// Matcher m = REG_PATTERN.matcher(gpStr);
				// StringBuffer expBuf = new StringBuffer();
				//
				//
				// m.appendTail(expBuf);
				//
				// gpStr = expBuf.toString();

				int varLast = 0;
				int varItemCnt = 0;
				while (varMatcher.find(varLast)) {
					varLast = varMatcher.end();

//					StringBuffer varSbuf = new StringBuffer();
					if (varMatcher.groupCount() > 0) {
						String var = varMatcher.group(2);
						if (StringUtils.isEmpty(var))
							continue;

						gpStr = Pattern.compile("[$" + var + "]",
								Pattern.LITERAL).matcher(gpStr).replaceAll(
								"(.*?)");

						gpStr = Pattern.compile("[#" + var + "]",
								Pattern.LITERAL).matcher(gpStr).replaceAll(
								"(.*)");

						// 去掉空的占位符
						gpStr = Pattern.compile("[$]", Pattern.LITERAL)
								.matcher(gpStr).replaceAll(".*?");
						gpStr = Pattern.compile("[#]", Pattern.LITERAL)
								.matcher(gpStr).replaceAll(".*");

						varItemCnt++;
						// 把占位符替换为正则式,暂时不考虑

						// 这里还缺少一步，就是先把\\换成\,再把\[换成[
						// gpStr=gpStr.replaceAll("\\\\\\\\","\\\\");
						// gpStr=gpStr.replaceAll("\\\\\\[","[");

						grpVarExp.getVarlist().add(
								new VarAssign.Var(var, varItemCnt));

					}
					;
				}

				grpVarExp.setRegexpStr(gpStr);
				grpVarExp.setRegexp(Pattern.compile(gpStr, Pattern.MULTILINE
						| Pattern.DOTALL | Pattern.CASE_INSENSITIVE));

				varExp.getVarlist().add(
						new VarAssign.Var(gpName, itemCnt, grpVarExp));
				itemCnt++;

			}
		}

		expStr = VAR_PATTERN.matcher(expStr).replaceAll(".*?");

		// 这里还缺少一步，就是先把\\换成\,再把\[换成[
		// expStr=expStr.replaceAll("\\\\\\\\","\\\\");
		// expStr=expStr.replaceAll("\\\\\\[","[");

//		int a;

		// Matcher groupMatcher =
		// RegularExpUtils.GROUP_VAR_PATTERN.matcher(regexp);
		// regexp=groupMatcher.replaceAll("[\\$]");
		expStr = UNGREED_VAR_PLACEHOLDER_PATTERN.matcher(expStr).replaceAll(
				".*?");
		// expStr=Pattern.compile("\\\\[(\\\\$|#)(\\w+?)(\\((.*?)\\))?\\\\]").matcher(expStr).replaceAll("[\\$]");

		varExp.setRegexp(Pattern.compile(expStr, Pattern.MULTILINE
				| Pattern.DOTALL | Pattern.CASE_INSENSITIVE));
		varExp.setRegexpStr(expStr);
		return varExp;

	}

	// static public Outlink[] getNamedMatchUrl(Outlink[] outLinks, String
	// pattern) {
	//
	// //先取出完全符合模式的links
	// ArrayList<Outlink> result = new ArrayList();
	// Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	// for (Outlink ol : outLinks) {
	// if (p.matcher(ol.getToUrl()).find()) {
	// result.add(ol);
	// }
	// }
	//
	//
	//
	// int i = pattern.indexOf("\"");
	// int j = pattern.lastIndexOf("\"");
	// // 取出两个双引号之间的内容，来匹配outlink
	// pattern = pattern.substring(i + 1, j - 1).trim();
	//
	// p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	//
	// ArrayList<Outlink> finalResult = new ArrayList();
	//
	// for (Outlink ol : finalResult) {
	// if (p.matcher(ol.getToUrl()).find()) {
	// result.add(ol);
	// }
	// }
	//
	// return result.toArray(new Outlink[0]);
	// }

	static public String escapeRegular(String str) {
		Matcher m = REG_PATTERN.matcher(str);
		StringBuffer expBuf = new StringBuffer();

		// 转义正则元字符
		while (m.find()) {
			m.appendReplacement(expBuf, "\\\\$0");
		}
		m.appendTail(expBuf);

		str = expBuf.toString();

		return str;
	}

	// static public Outlink[] getNormalMatchUrl(Outlink[] outLinks, String
	// pattern) {
	//
	// // sql语法
	// ArrayList<Outlink> result = new ArrayList();
	//
	// // 不支持单个的匹配，只支持非贪婪匹配
	// pattern = pattern.replaceAll("\\.", "\\\\.");
	// pattern = pattern.replaceAll("\\?", "\\\\?");
	// pattern = pattern.replaceAll("\\*", ".*?");
	//
	// Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	//
	// for (Outlink ol : outLinks) {
	// if (p.matcher(ol.getToUrl()).find()) {
	// result.add(ol);
	// }
	// }
	//
	// return result.toArray(new Outlink[0]);
	// }
}
