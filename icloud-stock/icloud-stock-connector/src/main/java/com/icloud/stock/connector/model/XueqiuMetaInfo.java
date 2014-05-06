package com.icloud.stock.connector.model;

import java.util.List;

public class XueqiuMetaInfo {
	private int count;
	private List<Stock> data;

	public static class Stock {
		private String symbol;
		private String code;
		private String name;

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

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

		@Override
		public String toString() {
			return "Stock [symbol=" + symbol + ", code=" + code + ", name="
					+ name + "]";
		}

	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Stock> getData() {
		return data;
	}

	public void setData(List<Stock> data) {
		this.data = data;
	}


}
