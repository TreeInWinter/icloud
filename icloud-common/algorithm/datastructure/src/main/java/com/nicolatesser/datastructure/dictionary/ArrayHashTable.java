package com.nicolatesser.datastructure.dictionary;

import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class ArrayHashTable {
	
	private static final int N = 11;
	private List<String>[] hashTable;
	
	public ArrayHashTable()
	{
		 hashTable = new List[N];
			
	}
	
	
	public String get(String key) {

		int hash= hashFunction(key);
		
		List<String> list = hashTable[hash];
		
		return list.get(0);
	}

	
	public String put(String key, String value) {
		
		int hash= hashFunction(key);
		
		if (this.hashTable[hash]==null)
		{
			this.hashTable[hash]= new Vector<String>();
		}
		
		this.hashTable[hash].add(value);
		
		return key;
	}
	
	public int hashFunction(String key)
	{
		int hashCode=hashCode(key);
		return hashCode % N;
	}
	
	
	public int hashCode(String key)
	{
		int hashCode=0;
		int a=2;
		for (int i=0;i<key.length();i++)
		{
			hashCode=hashCode+a*((int)key.charAt(i));
			a=a*a;
		}
		return hashCode;
	}
	
	
	
	
	
	

	

}
