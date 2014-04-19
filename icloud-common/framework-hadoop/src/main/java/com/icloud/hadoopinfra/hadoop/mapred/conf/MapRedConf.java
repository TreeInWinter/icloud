package com.icloud.hadoopinfra.hadoop.mapred.conf;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapred.Partitioner;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.lib.MultipleInputs;

import com.icloud.hadoopinfra.hadoop.Context;
import com.icloud.hadoopinfra.hadoop.MapReduceLogic;
import com.icloud.hadoopinfra.hadoop.MapReduceLogicLoader;
import com.icloud.hadoopinfra.hadoop.Params;
import com.icloud.hadoopinfra.hadoop.SetEnv;


/**
 * Hello world!
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MapRedConf {

	protected Params params;

	private Class inputFormatClass;

	private Class outputFormatClass;

	protected Class mapperClass;

	private Class reducerClass;

	private Class combinerClass;

	protected Class mapKeyClass;

	protected Class mapValueClass;

	protected Class partitionerClass;

	private String[] sourcepaths;

	private Map<String, String> properties = new HashMap<String, String>();

	public MapRedConf() {
		SetEnv.set();
	}


	public void setParams(Params params) {
		this.params = params;
		System.out.println(params.toString());
	}

	public void set(String name, String value) {
		properties.put(name, value);
	}

	public void setMapOutputKeyValueClass(Class mapKeyClass, Class mapValueClass) {
		this.mapKeyClass = mapKeyClass;
		this.mapValueClass = mapValueClass;
	}

	public void setMapper(Class<? extends InputFormat> inputFormatClass, Class<? extends Mapper> mapperClass) {
		this.inputFormatClass = inputFormatClass;
		this.mapperClass = mapperClass;
	}

	public void setReducer(Class<? extends OutputFormat> outputFormatClass, Class<? extends Reducer> reducerClass) {
		this.outputFormatClass = outputFormatClass;
		this.reducerClass = reducerClass;
	}

	public void setCombiner(Class<? extends Reducer> combinerClass) {
		this.combinerClass = combinerClass;
	}

	public void setPartitioner(Class<? extends Partitioner> partitionerClass) {
		this.partitionerClass = partitionerClass;
	}

	protected void initMapper(JobConf jobConf) {

		for(String p : sourcepaths) {
			System.out.println("inputPath = " + p);
			if (mapperClass != null) {
				MultipleInputs.addInputPath(jobConf,new Path(p), inputFormatClass, mapperClass);
			}
		}
	}

	private void initProperties(JobConf jobConf) {

		for(Entry<String, String> entry : properties.entrySet()) {
			jobConf.set(entry.getKey(), entry.getValue());
		}

		int size = properties.size();
		String[] propertykeys = new String[size];
		properties.keySet().toArray(propertykeys);

		jobConf.setStrings(Context.PROPERTIES, propertykeys);
	}

	public void runJob(String jobName, String strategyClass) {

		if (jobName == null || jobName.trim().length() == 0) {
			jobName = "wjjob";
		}

		String sourcepath = params.getInputSource();
		String outpath = params.getOutputSource();

		sourcepaths = sourcepath.split(",");

		Configuration conf = HBaseConfiguration.create();
		JobConf jobConf = new JobConf(conf, new MapRedConf().getClass());
		jobConf.setJobName(jobName);

		// about mapOut Keyvalue
		if (mapKeyClass == null) {
			mapKeyClass = Text.class;
		}
		if (mapValueClass == null) {
			mapValueClass = BytesWritable.class;
		}

		// init mapper
		initMapper(jobConf);

		// about job
		FileOutputFormat.setOutputPath(jobConf, new Path(outpath));
		jobConf.setOutputFormat(outputFormatClass);

		if (reducerClass != null) {
			jobConf.setReducerClass(reducerClass);
		}

		if (combinerClass != null) {
			jobConf.setCombinerClass(combinerClass);
		}

		jobConf.setMapOutputKeyClass(mapKeyClass);
		jobConf.setMapOutputValueClass(mapValueClass);

		if (params.getMapNum() > 0) {
			jobConf.setNumMapTasks(params.getMapNum());
		}

		if (params.getReduceNum() > 0) {
			jobConf.setNumReduceTasks(params.getReduceNum());
		}

		if (partitionerClass != null) {
			jobConf.setPartitionerClass(partitionerClass);
		}

		jobConf.set("input.sstable.strategy", "simple");
		jobConf.set(MapReduceLogic.LOGIC_CLASS, strategyClass);

		initProperties(jobConf);

		try {
			JobClient.runJob(jobConf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MapReduceLogic configure(JobConf job) {
		MapReduceLogic logic;

		// context
		Context ctx = new Context();

		// properties
		String[] propertykeys = job.getStrings(Context.PROPERTIES);
		if (propertykeys != null) {
			for(String key : propertykeys) {
				ctx.setProperty(key, job.get(key));
			}
		}

		// logic-init
		String className = job.get(MapReduceLogic.LOGIC_CLASS);
		logic = MapReduceLogicLoader.getLogic(className);
		logic.setContext(ctx);

		return logic;
	}

}
