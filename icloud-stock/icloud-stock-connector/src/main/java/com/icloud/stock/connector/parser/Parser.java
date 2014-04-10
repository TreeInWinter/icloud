package com.icloud.stock.connector.parser;

public interface Parser<T> {
	public T parse(String content, Object... params);
}
