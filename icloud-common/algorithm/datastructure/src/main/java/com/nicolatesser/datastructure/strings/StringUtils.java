package com.nicolatesser.datastructure.strings;

public class StringUtils {
	
	
	public static String createString(String s)
	{
		String toReturn = s;
		return toReturn;
	}
	
	
	public static StringBuffer createStringBuffer(String s)
	{
		StringBuffer toReturn = new StringBuffer(s);
		return toReturn;
	}
	
	
	public static String append(String s, char toAppend)
	{
		return s+toAppend;
	}
	

	public static StringBuffer append(StringBuffer s, char toAppend)
	{
		return s.append(toAppend);
	}

}
