package com.icloud.framework.core.wrapper;

public class PageItem {
	private int pageNo;
	private boolean hasUrl;

	public PageItem(int pageNo, boolean hasUrl) {
		this.pageNo = pageNo;
		this.hasUrl = hasUrl;

	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public boolean isHasUrl() {
		return hasUrl;
	}

	public void setHasUrl(boolean hasUrl) {
		this.hasUrl = hasUrl;
	}

}
