package com.travelzen.framework.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PageList<T> implements Serializable {
	private static final long serialVersionUID = -79015778431268087L;

	public PageList() {

	}

	public PageList(PageProperty pp, int allCount, List<T> list) {	
		if(pp.getNpage()>0){
			this.page = pp.getNpage();
		}
		if(pp.getNpagesize()>0){
			this.pageSize = pp.getNpagesize();
		}
		this.totalRecords = allCount;
		if (totalRecords % pageSize > 0) {
			this.totalPages = totalRecords / pageSize + 1;
		} else {
			this.totalPages = totalRecords / pageSize;
		}
		this.setRecords(list);
	}

	private int page = 1;

	private int totalRecords;

	private int totalPages;

	private int pageSize = 20;

	private int numbersPerBlock = 10;

	private List<T> records = new ArrayList<T>();

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1)
			page = 1;
		this.page = page;
	}

	public int getPageNumber() {
		int pageNumber = 0;
		if (totalRecords % pageSize == 0)
			pageNumber = totalRecords / pageSize;
		else
			pageNumber = 1 + totalRecords / pageSize;

		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * first row count of current page, start from 1
	 * 
	 * @return
	 */
	public int getFirstRow() {
		return (page - 1) * pageSize + 1;
	}

	/**
	 * last row count of current page
	 * 
	 * @return
	 */
	public int getLastRow() {
		return page == getPageNumber() ? getTotalRecords() : page * pageSize;
	}

	public int getPreviousPage() {
		return page > 1 ? page - 1 : page;
	}

	public int getNextPage() {
		return page < getPageNumber() ? page + 1 : page;
	}

	public int getBlocks() {
		if (this.getPageNumber() % this.numbersPerBlock == 0) {
			return this.getPageNumber() / this.numbersPerBlock;
		} else {
			return 1 + this.getPageNumber() / this.numbersPerBlock;
		}
	}

	public int getBlock() {
		if (this.getPage() % this.numbersPerBlock == 0) {
			return this.getPage() / this.numbersPerBlock;
		} else {
			return 1 + this.getPage() / this.numbersPerBlock;
		}
	}

	public int getNumbersPerBlock() {
		return numbersPerBlock;
	}

	public void setNumbersPerBlock(int numberPerBlock) {
		this.numbersPerBlock = numberPerBlock;
	}

	public int getPageOfPrevBlock() {
		if (this.getBlock() > 1) {
			return (this.getBlock() - 1) * this.getNumbersPerBlock();
		} else {
			return 1;
		}
	}

	public int getPageOfNextBlock() {
		if (this.getBlock() < this.getBlocks()) {
			return this.getBlock() * this.getNumbersPerBlock() + 1;
		} else {
			return this.getTotalRecords();
		}
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
