package com.icloud.stock.model.constant;

import com.icloud.framework.util.ICloudUtils;

public final class StockConstants {
	public enum StockLocation {
		SHA("sha"), //
		SZX("szx");
		private String location;

		private StockLocation(String location) {
			this.location = location;
		}

		public String getLocation() {
			return location;
		}
	}

	public enum BaseCategory {
		BASE("base", "基础分类"), XUEQIU("xueqiu", "必有分类"), ZHENGJIANHUI(
				"zhengjianhui", "精细分类");

		private String type;
		private String name;

		private BaseCategory(String type, String name) {
			this.type = type;
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return this.name;
		}

		public static BaseCategory getBaseCategory(String type) {
			if (!ICloudUtils.isNotNull(type)) {
				type = BASE.getType();
			}

			if (BASE.getType().equalsIgnoreCase(type)) {
				return BASE;
			}
			if (XUEQIU.getType().equalsIgnoreCase(type)) {
				return XUEQIU;
			}
			if (ZHENGJIANHUI.getType().equalsIgnoreCase(type)) {
				return ZHENGJIANHUI;
			}
			return BASE;
		}

	}

}
