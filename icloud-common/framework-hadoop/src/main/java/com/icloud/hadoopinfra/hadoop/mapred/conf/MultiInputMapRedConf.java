package com.icloud.hadoopinfra.hadoop.mapred.conf;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.MultipleInputs;


/**
 * Hello world!
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MultiInputMapRedConf extends MapRedConf {
	
	
	private List<MultipleSource> minputsources = new ArrayList<MultipleSource>();
	
	public void addMultipleInputSource(String path, Class formatClass, Class mapredceClass) {
		MultipleSource source = new MultipleSource(path, formatClass, mapredceClass);
		minputsources.add(source);
	}
	
	public void addMultipleInputSource(String[] paths, Class formatClass, Class mapredceClass) {
		for(String path : paths) {
			addMultipleInputSource(path, formatClass, mapredceClass);
		}
	}
	
	public void addMultipleInputSource(List<String> paths, Class formatClass, Class mapredceClass) {
		String[] temps = paths.toArray(new String[paths.size()]);
		addMultipleInputSource(temps, formatClass, mapredceClass);
	}
	
	@Override
	protected void initMapper(JobConf jobConf) {	
		for(MultipleSource source : minputsources) {
			System.out.println("inputPath = " + source.getPath());
			MultipleInputs.addInputPath(jobConf,new Path(source.getPath()), source.getFormatClass(), source.getMapredceClass()); 			
		}
	}
	
	class MultipleSource {
		
		private String path;
		
		private Class formatClass;
		
		private Class mapredceClass;
		
		public MultipleSource(String path, Class formatClass, Class mapredceClass) {
			this.path = path;
			this.formatClass = formatClass;
			this.mapredceClass = mapredceClass;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public Class getFormatClass() {
			return formatClass;
		}

		public void setFormatClass(Class formatClass) {
			this.formatClass = formatClass;
		}

		public Class getMapredceClass() {
			return mapredceClass;
		}

		public void setMapredceClass(Class mapredceClass) {
			this.mapredceClass = mapredceClass;
		}
	}
	
}
