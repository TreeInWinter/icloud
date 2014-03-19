package com.travelzen.hadoopinfra.hadoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.travelzen.hadoopinfra.common.cli.CommadLineBuilder;
import com.travelzen.hadoopinfra.common.cli.EBOption;
import com.travelzen.hadoopinfra.common.cli.Option;


public class XParams {
	
	private Map<String, String> params = new HashMap<String, String>();
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public String getParam(String key) {
		return params.get(key);
	}

	public void setParam(String key, String value) {
		this.params.put(key, value);
	}

	public static XParams getXParams(List<Option> options, String[] args) {
			
		CommadLineBuilder builder = new CommadLineBuilder();
		builder.addOptions(options);
		builder.build(args);
		
		XParams xparams = new XParams();		
		for(Option o : options) {
			xparams.setParam(o.getOpt(), builder.getOptionValue(o.getOpt()));
		}
		
		return xparams;
	}
	
	public static XParams getXParams(String[] opts, String[] args) {
		
		List<Option> options = new ArrayList<Option>();
		for(String opt : opts) {
			Option option = new Option();
			option.setOpt(opt);
			option.setHasArg(true);
			options.add(option);
		}
		return getXParams(options, args);
	}
	
	public static Params getXParams(String[] args) {
				
		CommadLineBuilder builder = new CommadLineBuilder();
		builder.addOptions(EBOption.getAllOption());
		builder.build(args);
		
		Params params = new Params();
		
		params.setInputSource(builder.getOptionValue(EBOption.inputpath.getOption().getOpt()));
		params.setOutputSource(builder.getOptionValue(EBOption.outputpath.getOption().getOpt()));
		
		int mapnum = Integer.valueOf(builder.getOptionValue(EBOption.mapnum.getOption().getOpt()));
		if (mapnum != -1) {
			params.setMapNum(mapnum);
		}

		int reducenum = Integer.valueOf(builder.getOptionValue(EBOption.reducenum.getOption().getOpt()));
		if (reducenum != -1) {
			params.setReduceNum(reducenum);
		}
		
		return params;		
	}

}
