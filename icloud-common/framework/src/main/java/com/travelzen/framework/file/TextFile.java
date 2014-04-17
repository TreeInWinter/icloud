package com.travelzen.framework.file;

import java.util.Iterator;

public class TextFile implements Iterable<String> {
	private String filename;

	public TextFile(String filename) {
		this.filename = filename;
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return new TextFileIterator(filename);
	}
}
