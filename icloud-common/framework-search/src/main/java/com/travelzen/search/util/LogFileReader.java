package com.travelzen.search.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LogFileReader {
	private BufferedReader br = null;

	public LogFileReader(String file, String code) {
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LogFileReader(InputStream stream) {
		if (stream != null) {
			br = new BufferedReader(new InputStreamReader(stream));
		}
	}

	public LogFileReader(String file) {
		this(file, "UTF-8");
	}

	public List<String> reader() {
		if (br != null) {
			List<String> list = new ArrayList<String>();
			String line = readLine();
			while (line != null) {
				list.add(line);
				line = readLine();
			}
			return list;
		}
		return null;
	}

	public String readLine() {
		String line = null;
		try {
			line = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			line = null;
		}
		return line;
	}

	public void close() {
		try {
			this.br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
