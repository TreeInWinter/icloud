package com.nicolatesser.datastructure.pattern_matching;

public class PatternMatchingAlgorithms {
	
	
	public static int bruteForceMatch(String text, String pattern)
	{
		for (int i=0;i<text.length();i++)
		{
			for (int j=0;j<pattern.length();j++)
			{
				if (text.charAt(i+j)!=pattern.charAt(j)) break;
				if (j==pattern.length()-1) return i;
			}
		}
		
		
		return -1;
	}
	
	
	public static int boyerMooreMatch(String text, String pattern)
	{
		//calculate the last function, parsing the chars of the text, and compiling a hash map...
		
		//initialize indexes i und j as m-1
		
		//while true
		
		//if the char is the same continue
		
		//if j=0 is a match
		
		//otherwise take 1 away from indexes
		
		//otherwise update the index with the last function, i=i+m-last(text[i])... or i=i+m-j
		
		return -1;
	}
	
	public static int knuthMorrisPrattMatch(String text, String pattern)
	{
		int[]f = computeFailureFunction(pattern);
		int i=0;
		int j=0;
		int n=text.length();
		int m=pattern.length();
		
		while (i<n)
		{
			if (text.charAt(i)==pattern.charAt(j))
			{
				if (j==m-1) return i-m+1;
				i++;
				j++;
			}
			else if (j>0)
			{
				j=f[j-1];
				
			}
			else
			{
				i++;
			}
		}
		
		
		return -1;
	}
	
	private static int[] computeFailureFunction(String pattern)
	{
		int[]f = new int[pattern.length()];
		f[0]=0;
		int i=1;
		int j=0;
		
		while (i<pattern.length())
		{
			if (pattern.charAt(i)==pattern.charAt(j))
			{
				f[i]=j+1;
				i++;
				j++;
			}
			else if (j>0)
			{
				j=f[j-1];
			}
			else
			{
				f[i]=0;
				i++;
			}
		}
		
		
		return f;
	}
	

}
