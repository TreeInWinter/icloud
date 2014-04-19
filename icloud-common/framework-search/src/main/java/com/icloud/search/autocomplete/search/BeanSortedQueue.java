package com.icloud.search.autocomplete.search;

import org.apache.lucene.util.PriorityQueue;

public abstract class BeanSortedQueue<T> extends PriorityQueue<T> {

	public BeanSortedQueue(int size) {
		initialize(size);
	}

	public void insertBean(T bean) {
		insertWithOverflow(bean);
	}

}
