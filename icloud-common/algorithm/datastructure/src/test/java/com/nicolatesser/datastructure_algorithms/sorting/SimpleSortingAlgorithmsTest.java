package com.nicolatesser.datastructure_algorithms.sorting;

import org.junit.Before;
import org.junit.Test;

import com.nicolatesser.datastructure.sorting.SimpleSortingAlgorithms;

import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleSortingAlgorithmsTest extends TestCase {

	private int[] array;

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		this.array = new int[] { 7, 8, 5, 2, 3, 1, 4, 0, 9, 6 };
		//this.array = new int[] { 2,1,3,4,5,6,7,8,9 };
		
	}

	public void assertArrayIsOrdered(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i] > array[i + 1])
				fail("the element at position " + (i)
						+ " is bigger than the element at position " + (i + 1)
						+ ". (" + array[i] + " > " + array[i + 1] + ").");
		}
	}
	
	public void printArray(int[]array)
	{
		for (int i=0;i<array.length;i++)
		{
			System.out.println(array[i]);
		}
	}

	@Test
	public final void testBubbleSort() {

		int[] orderedArray = SimpleSortingAlgorithms.bubbleSort(this.array);
		assertArrayIsOrdered(orderedArray);

	}
	
	@Test
	public final void testBubbleSortOptimized() {

		int[] orderedArray = SimpleSortingAlgorithms.bubbleSortOptimized(this.array);
		assertArrayIsOrdered(orderedArray);

	}

	@Test
	public final void testInsertionSort() {
		int[] orderedArray = SimpleSortingAlgorithms.insertionSort(this.array);
		System.out.println("hey");
		printArray(orderedArray);
		System.out.println("hey");

		assertArrayIsOrdered(orderedArray);
	}
	
	@Test
	public final void testSelectionSort() {
		int[] orderedArray = SimpleSortingAlgorithms.selectionSort(this.array);
		printArray(orderedArray);
		assertArrayIsOrdered(orderedArray);
	}

	@Test
	public final void testMergeSort() {
		int[] orderedArray = SimpleSortingAlgorithms.mergeSort(this.array);
		assertArrayIsOrdered(orderedArray);
	}

	@Test
	public final void testQuickSort() {
		int[] orderedArray = SimpleSortingAlgorithms.quickSort(this.array);
		
		printArray(orderedArray);
		
		assertArrayIsOrdered(orderedArray);

	}

	@Test
	public final void testHeapSort() {
		int[] orderedArray = SimpleSortingAlgorithms.heapSort(this.array);
		assertArrayIsOrdered(orderedArray);

	}

	@Test
	public final void testRandomizedQuickSort() {
		int[] orderedArray = SimpleSortingAlgorithms.randomizedQuickSort(this.array);
		System.out.println("ord");
		printArray(orderedArray);
		System.out.println("ord");

		assertArrayIsOrdered(orderedArray);

	}
	
	@Test
	public final void testSelect() {
//		int elementAtRank = SimpleSortingAlgorithms.select(this.array, 4);
//		Assert.assertEquals(4, elementAtRank);
		
	}

}
