package com.nicolatesser.datastructure_algorithms.patern_matching;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.nicolatesser.datastructure.pattern_matching.PatternMatchingAlgorithms;

public class PatternMatchingAlgorithmsTest {

	private String text;
	private String pattern;
	private int expected;

	@Before
	public void setUp() throws Exception {

		this.text = "aabaabaaabaab";
		this.pattern = "aaab";
		this.expected = 6;

		//this.text = "aabaabaaabaab";
		//this.pattern = "aaabc";
		//this.expected = -1;

	}

	@Test
	public void testBruteForceMatch() {

		int actual = PatternMatchingAlgorithms.bruteForceMatch(text, pattern);
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void testBoyerMooreMatch() {

		int actual = PatternMatchingAlgorithms.boyerMooreMatch(text, pattern);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testKnuthMorrisPrattMatch() {

		int actual = PatternMatchingAlgorithms.knuthMorrisPrattMatch(text,
				pattern);
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}

}
