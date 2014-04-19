/**
 * author: Simon Lee
 * Date  : Aug 29, 2013
 */
package com.icloud.framework.util;

import java.io.File;

import com.icloud.framework.util.GZIPUtil;

public class GZipUtilTest {

	public static void main(String[] args) {
		File[] sources = new File[] { new File("/tmp/students_1.xls"), new File("/tmp/students_2.xls") };
		File target = new File("/tmp/students.tar");
		GZIPUtil.compress(GZIPUtil.pack(sources, target));
	}

}
