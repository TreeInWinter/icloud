package com.icloud.hadoopinfra.common.cli;

public class Option {
	
	private String opt;
	
	private String longOpt;
	
	private boolean hasArg;
	
	private String description;
	
	public Option() {
	}
	
	public Option(String opt, String longOpt, boolean hasArg, String description) {
		this.opt = opt;
		this.longOpt = longOpt;
		this.hasArg = hasArg;
		this.description = description;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getLongOpt() {
		return longOpt;
	}

	public void setLongOpt(String longOpt) {
		this.longOpt = longOpt;
	}

	public boolean isHasArg() {
		return hasArg;
	}

	public void setHasArg(boolean hasArg) {
		this.hasArg = hasArg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}