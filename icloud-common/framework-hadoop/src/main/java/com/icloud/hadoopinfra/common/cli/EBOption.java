package com.icloud.hadoopinfra.common.cli;

import java.util.ArrayList;
import java.util.List;

public enum EBOption {
	
	inputpath("i", "inputpath", true, ""),
	outputpath("o", "outputpath", true, ""),
	mapnum("m", "mapnum", true, ""),
	reducenum("r", "reducenum", true, "");
	
	private Option option;
	
	EBOption(String opt, String longOpt, boolean hasArg, String description) {
		option = new Option(opt, longOpt, hasArg, description);
	}
	
	public Option getOption() {
		return option;
	}
	
	public static List<Option> getAllOption() {
		List<Option> options = new ArrayList<Option>();
		EBOption[] ebs = EBOption.values();
		for(EBOption eb : ebs) {
			options.add(eb.getOption());
		}
		return options;
	}
	
}