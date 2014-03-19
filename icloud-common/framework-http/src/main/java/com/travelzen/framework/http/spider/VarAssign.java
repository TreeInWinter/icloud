package com.travelzen.framework.http.spider;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 
 * @author jujumao
 *
 */
public class VarAssign {
	Pattern regexp;
	String 	regexpStr;
	ArrayList<Var> varlist=new ArrayList<Var>();
	
	public static class Var{
		String var;//变量
		int ids;//group�?
		VarAssign subVarAssign; //如果是组模式，则这个变量会有组模式中的子模式的信
		
		
		public Var(String var, int ids,VarAssign varAssign) {
			this.var = var;
			this.ids = ids;
			this.subVarAssign=varAssign;
		}
		
		
		public Var(String var, int ids) {
			this.var = var;
			this.ids = ids;
		}
		public int getIds() {
			return ids;
		}
		public void setIds(int ids) {
			this.ids = ids;
		}
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}


		public VarAssign getSubVarAssign() {
			return subVarAssign;
		}


		public void setSubVarAssign(VarAssign subVarAssign) {
			this.subVarAssign = subVarAssign;
		}
	}


	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("regexp:"+regexp+"\n");
		for(Var v:varlist){
			sb.append(v.getVar()+":"+v.getIds());
		}
		
		return sb.toString();
	}
	
	public Pattern getRegexp() {
		return regexp;
	}
 
	public void setRegexp(Pattern regexp) {
		this.regexp = regexp;
	}
	
	
	public VarAssign() {
	}

	public ArrayList<Var> getVarlist() {
		return varlist;
	}

	public void setVarlist(ArrayList<Var> varlist) {
		this.varlist = varlist;
	}

	public String getRegexpStr() {
		return regexpStr;
	}

	public void setRegexpStr(String regexpStr) {
		this.regexpStr = regexpStr;
	}




}
