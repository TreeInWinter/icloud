package com.icloud.hadoopinfra.hadoop;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("rawtypes")
public class MapReduceLogicLoader {
	
	public static Map<String, MapReduceLogic> logics = new HashMap<String, MapReduceLogic>();
		
	public static MapReduceLogic getLogic(String className) {
		
		MapReduceLogic logic = logics.get(className);		
		if (logic == null) {
			logic = loadLogicClass(className);
		}
		return logic;
	}
	
	private static MapReduceLogic loadLogicClass(String className) {
		MapReduceLogic logic = null;
		try {
			logic = (MapReduceLogic)Class.forName(className).newInstance();
			logics.put(className, logic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logic;
	}

}
