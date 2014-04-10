package com.icloud.stock.connector.parser;

public interface ParserFactory {
	public <T> Parser<T> getParser(Class<T> z);
}
