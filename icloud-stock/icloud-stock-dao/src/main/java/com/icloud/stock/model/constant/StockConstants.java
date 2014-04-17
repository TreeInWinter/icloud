package com.icloud.stock.model.constant;

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

}
