package com.icloud.framework.dao.vo;

import java.util.List;

public class DataWrapper<E> {

	public final List<E> data;
	public final long count;

	public DataWrapper(List<E> data, long count) {
		this.data = data;
		this.count = count;
	}

}
