package com.icloud.front.stock.pojo;

public class BaseStockMenu {
	private String code;
	private String url;
	private String name;
	private String fatherName;

	public BaseStockMenu(String code, String url, String name,
			String fatcherName) {
		super();
		this.code = code;
		this.url = url;
		this.name = name;
		this.fatherName = fatcherName;
	}

	public static BaseStockMenu getDefaultStockMenu() {
		return new BaseStockMenu("-1", "/", "股票列表", "深沪股市");
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

}
