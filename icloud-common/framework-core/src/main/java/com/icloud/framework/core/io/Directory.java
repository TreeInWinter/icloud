/**
 * 
 * Description: 目录实用工具，引用自<<Thinking In Java>>
 * Copyright: Copyright (c) 2009
 * Company:云壤
 * @author 任水
 * @version 1.0
 * @date Dec 29, 2009
 */
package com.icloud.framework.core.io;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class Directory {
	/**
	 * 
	 * description: 寻找dir中[不在子目录查找]，名字匹配regex的文件或目录
	 * @param dir
	 * @param regex
	 * @return
	 */
	public static File[] local(File dir, final String regex){
		return dir.listFiles(new FilenameFilter(){
			private Pattern pattern = Pattern.compile(regex);
			public boolean accept(File dir, String name) {
				return pattern.matcher(new File(name).getName()).matches();
			}			
		});
	}
	public static File[] local(String path, final String regex){
		return local(new File(path), regex);
	}
	public static class TreeInfo implements Iterable<File>{
		public List<File> files = new ArrayList<File>();
		public List<File> dirs = new ArrayList<File>();
		public Iterator<File> iterator() {
			return files.iterator();
		}
		void addAll(TreeInfo other){
			files.addAll(other.files);
			dirs.addAll(other.dirs);
		} 
	}
	public static TreeInfo walk(String start, String regex){
		return recurseDirs(new File(start), regex);
	}
	public static TreeInfo walk(File start, String regex){
		return recurseDirs(start, regex);
	}
	public static TreeInfo walk(String start){
		return recurseDirs(new File(start), ".*");
	}
	public static TreeInfo walk(File start){
		return recurseDirs(start, ".*");
	}
	static TreeInfo recurseDirs(File startDir, String regex){
		TreeInfo result = new TreeInfo();
		for(File item : startDir.listFiles()){
			if(item.isDirectory()){
				result.dirs.add(item);
				result.addAll(recurseDirs(item,regex));
			}else{
				if(item.getName().matches(regex))
					result.files.add(item);
			}
		}
		return result;
	} 
	public static void main(String[] args) throws IOException{
		File f = new File("我");
		System.out.println(f.getName());
		System.out.println(f.getCanonicalPath());
		System.out.println(f.getAbsolutePath());
	}
}

