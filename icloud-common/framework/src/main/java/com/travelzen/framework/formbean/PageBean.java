package com.travelzen.framework.formbean;

import java.util.List;

public class PageBean<T> {
	public static final int DEFAULT_PAGE_SIZE = 10;
	private int page = 1;
	private int pageSize;
	private long totalPage;
	private long totalCount;
	private List<T> data;

	public PageBean() {
		pageSize = DEFAULT_PAGE_SIZE;
	}

	public PageBean(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page > 0)
			this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		if (page > totalPage)
			page = 1;
		this.totalPage = totalPage;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		setTotalPage(totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1));
		this.totalCount = totalCount;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getStartPage() {
		return page < 3 ? 1 : page - 2;
	}

	public long getEndPage() {
		if (page < 3)
			return totalPage < 5 ? totalPage : 5;
		long endPage = page + 2;
		return endPage < totalPage ? endPage : totalPage;
	}

	public int offset() {
		return (page - 1) * pageSize;
	}

	public int limit() {
		return pageSize;
	}
}
