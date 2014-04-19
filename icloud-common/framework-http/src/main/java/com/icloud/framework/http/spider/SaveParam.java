package com.icloud.framework.http.spider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class SaveParam {

	// 供次级页面入口反引用的名字
	private String deferName = "";

	// 地址转换规则
	private String postRule = "";
	
	
//	 需要包含的条目，优先
	private Set<Integer> include = new TreeSet<Integer>();

	// 需要排除的条目
	private Set<Integer> exclude = new TreeSet<Integer>();

	private Map<String, String> translateRules = new HashMap<String, String>();

	private int maxMatch = Integer.MAX_VALUE;

	// 识别式名
	private String recognizeName = "";

	// 是否启用正则模式
	private String matchMod = "";

	// 正则的修饰符
	private String regmodStr = "";

	private String saveStat = "";

	private GroupSeparator groupMode;
	
	public String getDeferName() {
		return deferName;
	}

	public void setDeferName(String deferName) {
		this.deferName = deferName;
	}

	public Set<Integer> getExclude() {
		return exclude;
	}

	public void setExclude(Set<Integer> exclude) {
		this.exclude = exclude;
	}

	public String getMatchMod() {
		return matchMod;
	}

	public void setMatchMod(String matchMod) {
		this.matchMod = matchMod;
	}

	public int getMaxMatch() {
		return maxMatch;
	}

	public void setMaxMatch(int maxMatch) {
		this.maxMatch = maxMatch;
	}

	public String getPostRule() {
		return postRule;
	}

	public void setPostRule(String postRule) {
		this.postRule = postRule;
	}

	public String getRecognizeName() {
		return recognizeName;
	}

	public void setRecognizeName(String recognizeName) {
		this.recognizeName = recognizeName;
	}

	public String getRegmodStr() {
		return regmodStr;
	}

	public void setRegmodStr(String regmodStr) {
		this.regmodStr = regmodStr;
	}

	public String getSaveStat() {
		return saveStat;
	}

	public void setSaveStat(String saveStat) {
		this.saveStat = saveStat;
	}

	public Map<String, String> getTranslateRules() {
		return translateRules;
	}

	public void setTranslateRules(Map<String, String> translateRules) {
		this.translateRules = translateRules;
	}

	public GroupSeparator getGroupMode() {
		return groupMode;
	}

	public void setGroupMode(GroupSeparator groupMode) {
		this.groupMode = groupMode;
	}

	public Set<Integer> getInclude() {
		return include;
	}

	public void setInclude(Set<Integer> include) {
		this.include = include;
	}

	
}
