package com.icloud.framework.core.wrapper;

import java.util.ArrayList;
import java.util.List;

// 从0开始
public class PageView {
	/**
	 * 源码显示
	 */
	private PageItem prePage = null;
	private PageItem nextPage = null;
	private PageItem firstPage = null;
	private PageItem lastPage = null;
	private List<PageItem> pageList = null;
	private int currentPageNo = -1;
	private int totalPageNum = -1;
	private int size = -1;

	public PageView(int pageNo, int totalPageNum, int size) {
		this.currentPageNo = pageNo;
		this.totalPageNum = totalPageNum;
		this.size = size;
		init();
	}

	public PageView(int pageNo, int totalPageNum) {
		this(pageNo, totalPageNum, 7);
	}

	public static PageView convertPage(Pagination pagination) {
		if (pagination != null) {
			int pageNo = pagination.getPageNo();
			int total = pagination.getTotalPageCount();
			return new PageView(pageNo, total);
		}
		return null;
	}

	public void init() {
		int total = this.totalPageNum - 1;
		int pageNo = this.currentPageNo;
		if (total < 0 || pageNo < 0 || pageNo > total)
			return;
		pageList = new ArrayList<PageItem>();
		if (pageNo > 0) {
			prePage = new PageItem(pageNo - 1, true);
		}
		if (pageNo < total) {
			nextPage = new PageItem(pageNo + 1, true);
		}
		/**
		 * 寻找5个
		 */
		int hardsize = (size - 1) / 2;
		int start = pageNo - hardsize;
		int end = pageNo + hardsize;
		if (start < 0) {
			if (end > total) {
				start = 0;
				end = total;
			} else {
				start = 0;
				end = start + size - 1;
				if (end > total) {
					end = total;
				}
			}
		} else {
			if (end > total) {
				end = total;
				start = end - (size - 1);
				if (start < 0) {
					start = 0;
				}
			} else { // start > 0 && end < total
				// 不用处理
			}
		}
		if (start != 0) {
			firstPage = new PageItem(0, true);
		}
		if (end != total) {
			lastPage = new PageItem(total, true);
		}

		for (; start <= end; start++) {
			if (start != pageNo) {
				pageList.add(new PageItem(start, true));
			} else {
				pageList.add(new PageItem(start, false));
			}
		}

	}

	public void print() {
		System.out.println("page: " + this.currentPageNo);
		System.out.println("total : " + this.totalPageNum);
		if (this.prePage != null) {
			System.out.println("prePage is " + this.prePage.getPageNo());
		}
		if (this.nextPage != null) {
			System.out.println("nextPage is " + this.nextPage.getPageNo());
		}
		if (this.firstPage != null) {
			System.out.println("firstPage is " + this.firstPage.getPageNo());
		}
		if (this.lastPage != null) {
			System.out.println("lastPage is " + this.lastPage.getPageNo());
		}

		if (this.pageList != null) {
			for (PageItem item : this.pageList) {
				System.out.println("item is " + item.getPageNo() + "   "
						+ item.isHasUrl());
			}
		}

	}

	public static void main(String[] args) {
		PageView view = new PageView(0, 1);
		view.print();
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public long getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public PageItem getPrePage() {
		return prePage;
	}

	public void setPrePage(PageItem prePage) {
		this.prePage = prePage;
	}

	public PageItem getNextPage() {
		return nextPage;
	}

	public void setNextPage(PageItem nextPage) {
		this.nextPage = nextPage;
	}

	public PageItem getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(PageItem firstPage) {
		this.firstPage = firstPage;
	}

	public PageItem getLastPage() {
		return lastPage;
	}

	public void setLastPage(PageItem lastPage) {
		this.lastPage = lastPage;
	}

	public List<PageItem> getPageList() {
		return pageList;
	}

	public void setPageList(List<PageItem> pageList) {
		this.pageList = pageList;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
