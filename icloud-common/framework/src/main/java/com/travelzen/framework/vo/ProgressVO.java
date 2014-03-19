/**
 * 
 */
package com.travelzen.framework.vo;

/**
 */
public class ProgressVO {
	private String percent;
	private String info;
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public ProgressVO(String percent, String info) {
		super();
		this.percent = percent;
		this.info = info;
	}
	@Override
	public String toString() {
		return percent + "&" + info;
	}
	
	
}
