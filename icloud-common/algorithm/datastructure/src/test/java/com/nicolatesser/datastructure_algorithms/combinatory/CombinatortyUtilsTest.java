package com.nicolatesser.datastructure_algorithms.combinatory;


import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.combinatory.CombinatortyUtils;

public class CombinatortyUtilsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	
	
	@Test
	public void testGetAllPermutations()
	{
		//Prepare
		String s ="abc";
		//Execute
		List<String> allPermutations = CombinatortyUtils.getAllPermutations(s);
		
		//Verify
		int i=1;
		for (String permutation : allPermutations)
		{
			System.out.println((i++)+" permutation= "+permutation);
		}
	}
	
	
	@Test
	public void testGetRandomPermutation()
	{
		//Prepare
		String s ="0123456789";
		//Execute
		String randomPermutation = CombinatortyUtils.getRandomPermutation(s);
		//Verify
		System.out.println("random permutation is "+randomPermutation);
	}
	
	
	@Test
	public void testGetAllCombinations()
	{
		//Prepare
		String s ="abc";
		//Execute
		List<String> allCombinations = CombinatortyUtils.getAllCombinations(s);
		
		//Verify
		int i=1;
		for (String combination : allCombinations)
		{
			System.out.println((i++)+" allCombinations= "+combination);
		}
	}
}
