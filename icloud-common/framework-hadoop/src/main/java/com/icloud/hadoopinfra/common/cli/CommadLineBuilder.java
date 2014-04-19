package com.icloud.hadoopinfra.common.cli;

import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommadLineBuilder {
	
	Options options = new Options();
	
	CommandLineParser parser = new BasicParser();
	
	CommandLine commandLine;

    public boolean build(String[] args) {
        
        try {
			commandLine = parser.parse(options, args);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
    }
	
	public void addOption(Option option) {
		addOption(option.getOpt(), option.getLongOpt(), option.isHasArg(), option.getDescription());
	}
	
	public void addOptions(List<Option> optionList) {
		for(Option o : optionList) {
			addOption(o);
		}		
	}
	
	public void addOption(String opt, String longOpt, boolean hasArg, String description) {
		if (longOpt != null && !longOpt.equals("")) {
			options.addOption(opt, longOpt, hasArg, description);
		} else {
			options.addOption(opt, hasArg, description);
		}
	}
	
	public void addOption(String opt, boolean hasArg, String description) {
		addOption(opt, null, hasArg, description);
	}	
	
	public void addOption(String opt, String description) {
		addOption(opt, true, description);
	}
	
	public void addOption(String opt) {
		addOption(opt, true, "");
	}
	
	public String getOptionValue(String opt) {
		return commandLine.getOptionValue(opt);
	}

}
