package com.icloud.framework.config.tops;

public class TopsConfEnum {
	public enum ConfScope {
		G("G", "global"), M("M", "machine"), GA("GA", "global  appid"), MA("MA", "machine appid"), U("U", "unique");

		private String scopeType;
		private String scopeName;

		ConfScope(String scopeType, String scopeName) {
			this.scopeType = scopeType;
			this.scopeName = scopeName;
		}

		public String getScopeType() {
			return scopeType;
		}

		public void setScopeType(String scopeType) {
			this.scopeType = scopeType;
		}

		public String getScopeName() {
			return scopeName;
		}

		public void setScopeName(String scopeName) {
			this.scopeName = scopeName;
		}
	}

	public enum ConfLocation {
		ZK("zookeeper"), LOCALHOST("location"), INNERAPP("innerAPP");
		private String loation;

		ConfLocation(String location) {
			this.loation = location;
		}

		public String getLoation() {
			return loation;
		}

		public void setLoation(String loation) {
			this.loation = loation;
		}

	}
}