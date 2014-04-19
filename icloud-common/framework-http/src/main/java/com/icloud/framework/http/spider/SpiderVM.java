package com.icloud.framework.http.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpiderVM {

	/**
	 * Singleton
	 */
	private static class SingletonHolder {
		static final SpiderVM INSTANCE = new SpiderVM();
	}
	public static SpiderVM getInstance() {
		return SingletonHolder.INSTANCE;
	}

	protected static final Logger logger = LoggerFactory.getLogger(SpiderVM.class);

	//needn't to use RegConfUtils
	public static List<Map<String, String>> parser(String content,
			String regexp ) {

		return SpiderVM.parser(content,  regexp,
				new SaveParam());
	}


	public static List<Map<String, String>> parser(String content,
			String regexp, SaveParam saveParam) {

		List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>();

		VarAssign varReg = null;
		VarAssign grpVarReg = null;
		Matcher grpMatch = null;

		// 组匹配模式的式子
		if (saveParam.getGroupMode() != null) {
			grpVarReg = RegularExpUtils.parseGroupMatchRegexp(regexp);

			Matcher groupMatcher = RegularExpUtils.GROUP_VAR_PATTERN
					.matcher(regexp);
			regexp = groupMatcher.replaceAll("[\\$]");

		}

		if (saveParam.getMatchMod().equals(FrameworkSpiderConstant.REGULAR)) {
			varReg = RegularExpUtils.parseMatchRegexpAdv(regexp,
					saveParam.getRegmodStr());

		} else {
			varReg = RegularExpUtils.parseMatchRegexp(regexp);
		}

		Pattern regular = varReg.getRegexp();

		boolean matched = false;

		content = content.replaceAll("&amp;", "&");
		content = content.replaceAll("&lt;", "<");
		content = content.replaceAll("&gt;", ">");
		content = content.replaceAll("&quot;", "\"");

		Matcher m = regular.matcher(content);
//		String fullExp = "";
		int last = 0;
		int itemCnt = 0;
		int matchCnt = 0;

		int grpLast = 0;
//		int grpCnt = 0;

		while (m.find(last) && matchCnt < saveParam.getMaxMatch()) {

			matched = true;

			last = m.end();

			if (saveParam.getExclude().contains(new Integer(itemCnt++))) {
				continue;
			}

			// 不包含的数据，不计算在“最多匹配{2}”中
			matchCnt++;

			Map<String, String> resultMap = new HashMap<String, String>();

			for (int i = 0; i <= m.groupCount(); i++) {
//				String resu = m.group(i);
				if (i == 0) {
//					fullExp = resu;
					// resultMap.put(ConfConstant.FULL_MATCH, fullExp);

				} else {
					resultMap.put(varReg.getVarlist().get(i - 1).getVar(),
							m.group(i));

				}
			}

			// begin add group mode
			// 单匹配模式推进一次， 组匹配模式也推进一次
			if (null != grpMatch && grpMatch.find(grpLast)) {
				grpLast = grpMatch.end();

//				Map<String, String> grpResultMap = new HashMap<String, String>();
//				String grpStr = "";
				// 对于每个组匹配
				for (int i = 0; i <= grpMatch.groupCount(); i++) {
//					String resu = grpMatch.group(i);
					if (i == 0) {
						// grpStr = res;
						// /grpResultMap.put(ConfConstant.FULL_MATCH,
						// fullExp);
					} else {

						String target = grpMatch.group(i);
						String grpName = grpVarReg.getVarlist().get(i - 1)
								.getVar();

						VarAssign va = grpVarReg.getVarlist().get(i - 1)
								.getSubVarAssign();
						Matcher tmpM = va.getRegexp().matcher(target);
						int tmpLast = 0;
//						int tmpCnt = 0;
						StringBuffer result = new StringBuffer();

						while (tmpM.find(tmpLast)) {
							tmpLast = tmpM.end();
							List<String> resultArray = new ArrayList<String>();

							for (int t = 0; t <= tmpM.groupCount(); t++) {
								if (t == 0) {
								} else {

									if (saveParam.getGroupMode().isShowName()) {

										resultArray.add(va.getVarlist()
												.get(t - 1).getVar()
												+ saveParam.getGroupMode()
														.getNameSeparator()
												+ tmpM.group(t));
									} else {
										resultArray.add(tmpM.group(t));
									}
								}
							}
							result.append(StringUtils.join(resultArray
									.iterator(), saveParam.getGroupMode()
									.getFieldSeparator()));

							result.append(saveParam.getGroupMode()
									.getLineSeparator());

						}
						resultMap.put(grpName, result.toString());
					}
				}
			}

			resultMapList.add(resultMap);

			if (last == content.length())
				break;

		}

		if (!matched) {
			// 如果匹配失败，预先打印信息，而不是留到save的时候报错，这不是必须的
			logger.info("match failed:" + regexp);
		}

		return resultMapList;
	}

}
