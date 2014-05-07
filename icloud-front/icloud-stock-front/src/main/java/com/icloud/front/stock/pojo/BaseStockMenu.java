package com.icloud.front.stock.pojo;

public class BaseStockMenu {
	private String code;
	private String url;
	private String name;


	public BaseStockMenu(String code, String url, String name) {
		super();
		this.code = code;
		this.url = url;
		this.name = name;
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

}
