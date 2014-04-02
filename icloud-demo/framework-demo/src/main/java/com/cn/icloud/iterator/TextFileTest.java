package com.cn.icloud.iterator;


public class TextFileTest {
	public static void main(String[] args) {
		TextFile textFile = new TextFile(
				"/home/jiangningcui/workspace/tz/tz-data/test/run.sh");
		// while (iterator.hasNext()) {
		// System.out.println(iterator.next());
		// }
		for (String line : textFile) {
			System.out.println(line);
		}
	}
}
