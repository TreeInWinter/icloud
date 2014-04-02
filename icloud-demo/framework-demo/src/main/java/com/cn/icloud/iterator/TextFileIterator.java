package com.cn.icloud.iterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;


public class TextFileIterator implements Iterator<String> {
	// stream being read from

	BufferedReader in;
	// return value of next call to next()
	String nextline;

	public TextFileIterator(String filename) {
		try {
			in = new BufferedReader(new FileReader(filename));
			nextline = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return nextline != null;
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		try {
			String result = nextline;
			// if we dont have reached EOF yet
			if (nextline != null) {
				nextline = in.readLine(); // read another line
				if (nextline == null)
					in.close(); // and close on EOF
			}
			return result;
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
