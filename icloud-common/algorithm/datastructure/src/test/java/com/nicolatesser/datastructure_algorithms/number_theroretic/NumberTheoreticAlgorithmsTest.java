package com.nicolatesser.datastructure_algorithms.number_theroretic;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.number_theroretic.NumberTheoreticAlgorithms;

public class NumberTheoreticAlgorithmsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsPrime_Prime() {
		
		int a = 3041;
		
		boolean prime = NumberTheoreticAlgorithms.isPrime(a);
		
		Assert.assertTrue(prime);
	}
	
	@Test
	public void testIsPrime_NonPrime() {
		
		int a = 2597;
		
		boolean prime = NumberTheoreticAlgorithms.isPrime(a);
		
		Assert.assertFalse(prime);
	}
	
	@Test
	public void testIsPseudoPrime_Prime() {
		
		int a = 53;
		
		boolean prime = NumberTheoreticAlgorithms.isPseudoPrime(a);
		
		Assert.assertTrue(prime);
	}
	
	@Test
	public void testIsPseudoPrime_NonPrime() {
		
		int a = 2597;
		
		boolean prime = NumberTheoreticAlgorithms.isPseudoPrime(a);
		
		Assert.assertFalse(prime);
	}
	
	@Test
	public void testGcd_13_29()
	{
		int gcd = NumberTheoreticAlgorithms.gcd(13, 29);
		Assert.assertEquals(1, gcd);
	}
	
	@Test
	public void testGcd_9_12()
	{
		int gcd = NumberTheoreticAlgorithms.gcd(9, 12);
		Assert.assertEquals(3, gcd);
	}
	
	
	@Test
	public void testGcdNonRecursive_13_29()
	{
		int gcd = NumberTheoreticAlgorithms.gcd_non_recursive(13, 29);
		Assert.assertEquals(1, gcd);
	}
	
	@Test
	public void testGcdNonRecursive_9_12()
	{
		int gcd = NumberTheoreticAlgorithms.gcd_non_recursive(9, 12);
		Assert.assertEquals(3, gcd);
	}
	
	

}
