package com.icloud.hadoopinfra.hadoop.mapred.conf;

import org.apache.hadoop.hbase.mapred.TableMapReduceUtil;
import org.apache.hadoop.mapred.JobConf;


/**
 * Hello world!
 *
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class HBaseMapRedConf extends MapRedConf {
	
	@Override
	protected void initMapper(JobConf jobConf) {	
		TableMapReduceUtil.initTableMapJob(params.getInputSource(), "cf", mapperClass, mapKeyClass, mapValueClass, jobConf);
	}
	
}
