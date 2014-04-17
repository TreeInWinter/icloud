package com.icloud.stock.connector.model;

/**
 * 股票的基本信息
 * @author jiangningcui
 *
 */
public class StockMetaInfo {
	private String code;
	private String name;
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
