package com.nicolatesser.datastructure_algorithms.bit_arithmetic;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

import com.nicolatesser.datastructure.bit_arithmetic.BitArithmeticAlgorithms;

public class BitArithmeticAlgorithmsTest {

	@Test
	public void testSum() {
		int a = 3;
		int b = 5;
		
		int sum = BitArithmeticAlgorithms.sum(a, b);
		
		Assert.assertEquals(a+b, sum);
	}
	
	@Test
	public void testSum2() {
		int a = 1;
		int b = 0;
		
		int sum = BitArithmeticAlgorithms.sum(a, b);
		
		Assert.assertEquals(a+b, sum);
	}
	
	@Test
	public void testSum3() {
		int a = 7;
		int b = 7;
		
		int sum = BitArithmeticAlgorithms.sum(a, b);
		
		Assert.assertEquals(a+b, sum);
	}

}
