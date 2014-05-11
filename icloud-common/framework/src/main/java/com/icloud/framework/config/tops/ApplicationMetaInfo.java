package com.icloud.framework.config.tops;

import com.icloud.framework.core.util.TZUtil;

public class ApplicationMetaInfo {
	private String appid;
	private String port;
	private String seq;

	private String confirmId;

	public ApplicationMetaInfo(String appid, String port, String seq) {
		this.appid = appid;
		this.port = port;
		this.seq = seq;
	}

	private String getAppid() {
		if (!TZUtil.isEmpty(appid))
			return appid;
		if (!TZUtil.isEmpty(port))
			return port;
		if (!TZUtil.isEmpty(seq))
			return seq;
		return null;
	}

	public String getConfirmId() {
		if (!TZUtil.isEmpty(confirmId))
			return confirmId;
		return getAppid();
	}

	public void setConfirmId(String confirmId) {
		this.confirmId = confirmId;
	}

}
