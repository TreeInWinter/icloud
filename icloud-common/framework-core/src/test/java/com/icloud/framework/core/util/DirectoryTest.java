/**
 * 
 * Description: 
 * Copyright: Copyright (c) 2009
 * Company:联动优势
 * @author 任水
 * @version 1.0
 * @date Dec 29, 2009
 */
package com.icloud.framework.core.util;

import java.io.File;

import junit.framework.TestCase;

import com.icloud.framework.core.io.Directory;

public class DirectoryTest extends TestCase {
	public void test_local() throws Exception{
		String path = System.getProperty("user.dir");
//		System.out.println(path);
		for(File file : Directory.local(path, "^(?!(\\.|\\.\\.)).*")){
			System.out.println(file.getPath());
		}
	}
	public void test_walk() throws Exception{
		String path = System.getProperty("user.dir");
		System.out.println(path);
		
	}
}

