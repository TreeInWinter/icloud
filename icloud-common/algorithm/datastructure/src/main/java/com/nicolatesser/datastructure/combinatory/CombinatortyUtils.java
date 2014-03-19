package com.nicolatesser.datastructure.combinatory;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class CombinatortyUtils {
	
	private static final Random generator = new Random();
	
	/*
	 * There should be a method to generate all permuations
	 * (next previous permutations), using rank/unrank,
	 * every permutations have a different rank,
	 * unrank create the unique permutaions with given rank.
	 */
	
	
	public static List<String> getAllPermutations(String s)
	{
	
		return getAllPermutations("",s);
	}
	
	private static List<String> getAllPermutations (String base, String toPermute)
	{
		List<String> permutations = new Vector<String>();
		if (toPermute.length()==1)
		{
			permutations.add(base+toPermute);
			return permutations;
		}
		
		//permuatations
		for (int i=0;i<toPermute.length();i++)
		{
			char a = toPermute.charAt(i);
			StringBuffer stillToPermute = (new StringBuffer(toPermute)).delete(i, i+1);			
			List<String> smallerPermuatations = getAllPermutations(base+a,new String(stillToPermute));
			permutations.addAll(smallerPermuatations);
		}
		
		return permutations;
		
	}
	
	
	public static String getRandomPermutation(String s)
	{
		StringBuffer buffer = new StringBuffer(s);
		int n =s.length();
		for (int i=0;i<n;i++)
		{
			int random = generator.nextInt(n);
			buffer = swap(buffer, i, random);
		}
		return new String(buffer);
	}
	
	private static StringBuffer swap (StringBuffer s,int a, int b)
	{
		char t = s.charAt(a);
		s.setCharAt(a, s.charAt(b));
		s.setCharAt(b, t);
		return s;
	}
	
	

	public static List<String> getAllCombinations(String s)
	{
		List<String> combinations = new Vector<String>();

		int n = s.length();
	
		int[]scaleOfTwo = new int[n];
		int numberOfCombination = 1;
		for (int i=0;i<n;i++)
		{
			scaleOfTwo[i]=numberOfCombination;
			numberOfCombination=numberOfCombination*2;
		}
		
		for (int i=0;i<numberOfCombination;i++)
		{
			System.out.println("considering "+i+" combination.");
			StringBuffer buffer = new StringBuffer();
			for (int j=0;j<n;j++)
			{
				int considerInCombination = scaleOfTwo[j]&i;
				if (considerInCombination==scaleOfTwo[j])
				{
					buffer.append(s.charAt(j));
				}
				
			}
			System.out.println("added "+buffer+" as "+i+" combination.");
			combinations.add(new String(buffer));	
		}
		return combinations;
	}
	
	
	
	
}
