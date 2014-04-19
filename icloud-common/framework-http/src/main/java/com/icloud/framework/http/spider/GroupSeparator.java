package com.icloud.framework.http.spider;

public class GroupSeparator {
	
	boolean showName=true;
	
    String fieldSeparator;
    String nameSeparator;
    String lineSeparator;
	public GroupSeparator(String nameSeparator,String fieldSeparator, String lineSeparator) {
		super();
		this.nameSeparator = nameSeparator;
		// TODO Auto-generated constructor stub
		this.fieldSeparator = fieldSeparator;

		this.lineSeparator = lineSeparator;
	}
	
	
	public GroupSeparator(String fieldSeparator, String lineSeparator) {
		this.showName=false;
		this.fieldSeparator = fieldSeparator;
		this.lineSeparator = lineSeparator;
	}
	
	public String getFieldSeparator() {
		return fieldSeparator;
	}
	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}
	public String getLineSeparator() {
		return lineSeparator;
	}
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
	public String getNameSeparator() {
		return nameSeparator;
	}
	public void setNameSeparator(String nameSeparator) {
		this.nameSeparator = nameSeparator;
	}
	public boolean isShowName() {
		return showName;
	}
	public void setShowName(boolean showName) {
		this.showName = showName;
	}
}
