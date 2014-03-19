package com.nicolatesser.datastructure.concurrency;

public class ConcurrencyUtils {

	private static String mutexString = "";
	
	
	synchronized public String getString()
	{
		//here wait for some seconds
		return mutexString;
	}
	
	synchronized public void setString(String s)
	{
		//here wait for some seconds
		mutexString=s;
	}
	
}
