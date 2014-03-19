package com.nicolatesser.datastructure.number_theroretic;

public class NumberTheoreticAlgorithms {

	public static boolean isPrime(long a)
	{
		long sqrtA = (long) Math.floor(Math.sqrt(a));
		
		if ((a % 2)==0) return false;
		
		for (int i=3;i<=sqrtA;i=i+2)
		{
			if ((a % i)==0) return false;
		}
		return true;
		
	}
	
	public static boolean isPseudoPrime(long a)
	{
		long pow = (long)Math.pow(2, a-1);
		long rem = pow % a ;
		
		if (rem == 1)
			return true;
		else
			return false;
		
	}
	
	//gcd
	//there is an algorithm (maybe euclide) that recoursively look for the mod of a division
	// gcd (a,b)
	// gcd (b,a % b)
	// ...
	//till x % y == 0 , then y is the gcd.
	
	
	public static int gcd(int a,int b)
	{
		if (b>a){
			int t=a;
			a=b;
			b=t;
		}
		
		int mod = a % b;
		
		if (mod == 0) return b;
		else
			return gcd(b,mod);
	}
	
	
	public static int gcd_non_recursive(int a,int b)
	{
		if (b>a){
			int t=a;
			a=b;
			b=t;
		}
		
		int mod = a % b;
		
		while (mod != 0)
		{
			a=b;
			b=mod;
			mod = a%b;
		}
		
		return b;
	}
	
	
	
	
	
	
}
