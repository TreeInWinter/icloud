package com.travelzen.hadoopinfra.hadoop;

public class SetEnv {
	
	public static void set() {
		
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",  
				"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");  
		System.setProperty("javax.xml.parsers.SAXParserFactory",  
				"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
	}

}
