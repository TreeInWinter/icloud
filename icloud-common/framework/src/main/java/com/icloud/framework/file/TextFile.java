package com.icloud.framework.file;

import java.io.InputStream;
import java.util.Iterator;

public class TextFile implements Iterable<String> {
	private String filename;
	private InputStream inputStream;

	public TextFile(String filename) {
		this.filename = filename;
	}

	public TextFile(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		if (filename != null) {
			return new TextFileIterator(filename);

		}
		if (inputStream != null) {
			return new TextFileIterator(inputStream);
		}
		return null;
	}
}
