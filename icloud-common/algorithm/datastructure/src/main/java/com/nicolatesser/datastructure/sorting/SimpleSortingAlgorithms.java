package com.nicolatesser.datastructure.sorting;

import java.util.Random;

public class SimpleSortingAlgorithms {

	public static int[] bubbleSort(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] > array[j]) {
					int t = array[i];
					array[i] = array[j];
					array[j] = t;
				}
			}
		}
		return array;
	}

	/**
	 * If no substitution is made in one cycle then the array is returned.
	 * 
	 * @param array
	 * @return
	 */
	public static int[] bubbleSortOptimized(int[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			boolean substitutionMade = false;
			for (int j = i + 1; j < array.length; j++) {

				if (array[i] > array[j]) {
					int t = array[i];
					array[i] = array[j];
					array[j] = t;
					substitutionMade = true;
				}

			}
			if (!substitutionMade) {
				System.out
						.println("bubbleSortOptimized: returned because no substitution has been made");
				return array;
			}
		}
		return array;
	}

	public static int[] insertionSort(int[] array) {
		for (int i = 1; i < array.length; i++) {
			int j = i - 1;
			while ((j >= 0) && (array[i] < array[j])) {
				int t = array[i];
				array[i] = array[j];
				array[j] = t;
				j--;
				i--;
			}
		}

		return array;
	}
	
	
	public static int[] selectionSort(int[] array) {
		for (int i=0;i<array.length-1;i++)
		{
			int min=Integer.MAX_VALUE;
			int min_index=i;
			for (int j=i+1;j<array.length;j++)
			{
				if (array[j]<min)
				{
					min =array[j];
					min_index=j;
				}	
			}
			if (min<array[i])
			{
				int t=array[i];
				array[i]=array[min_index];
				array[min_index]=t;
			}
		}
		
		return array;
	}
	
	

	public static int[] mergeSort(int[] array) {
		mergeSort(array, 0, array.length);
		return array;
	}

	/**
	 * Implemented in place, with indexes
	 * 
	 * @param array
	 * @param a
	 * @param b
	 */
	private static void mergeSort(int[] array, int a, int b) {
		if (b - a == 1)
			return;

		int i = b - ((b - a) / 2);

		mergeSort(array, a, i);
		mergeSort(array, i, b);

		merge(a, i, i, b, array);
	}

	private static void merge(int i1, int i2, int j1, int j2, int[] array) {

		// int[]array = new int[array1.length+array2.length];

		int i = 0;
		int j = 0;

		while ((i + i1 < i2) && (j + j1 < j2)) {
			if (array[i + i1] <= array[j + j1]) {
				array[i + j + i1] = array[i + i1];
				i++;
			} else {
				array[i + j + i1] = array[j + j1];
				j++;
			}
		}

		while (i + i1 < i2) {
			array[i + j + i1] = array[i + i1];
			i++;
		}

		while (j + j1 < j2) {
			array[i + j + i1] = array[j + j1];
			j++;
		}
		// return array;
	}

	
	public static int[] quickSort(int[] array) {
		quickSort(array, 0, array.length - 1);
		return array;
	}
	
	public static void quickSort(int[] array, int a, int b)
	{
		//base
		if (a>=b) return;
		
		int pivot = array[b];
		int i=a;
		int j=b-1;
		
		while (i<=j)
		{
			while ((i<=j)&&(array[i]<=pivot))
			{
				i++;
			}
			while ((i<=j)&&(array[j]>=pivot))
			{
				j--;
			}
			
			if (i<j)
			{
				int t=array[i];
				array[i]=array[j];
				array[j]=t;
			}
		}
		
		array[b]=array[i];
		array[i]=pivot;
		
		
		
		//recursion
		quickSort(array,a,i-1);
		quickSort(array,i+1,b);
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	/*private static void quickSort(int[] array, int a, int b) {
		// base
		if (b - a < 1)
			return;

		// sorting
		int pivot = array[b];

		// scanning the array from the beginning looking for something bigger
		// than pivot, and from the end looking for something smaller than the
		// pivot
		int i = 0;
		int j = b - 1;
		while (i < j) {
			while ((array[i] < pivot) && (i < j)) {
				i++;
			}
			while ((array[j] > pivot) && (i < j)) {
				j--;
			}
			if (i < j) {
				int t = array[i];
				array[i] = array[j];
				array[j] = t;
			}
		}
		// change j and pivot
		array[b] = array[j];
		array[j] = pivot;

		// recursive call
		quickSort(array, a, j - 1);
		quickSort(array, j + 1, b);

	}*/

	
	
	public static int[] heapSort(int[] array) {
		// from sequence to heap (insertItem)

		for (int i = 0; i < array.length; i++) {
			int j = i;
			int k = i;

			while (j > 0) {
				j = (j - 1) / 2;
				if (array[k] > array[j]) {
					int t = array[k];
					array[k] = array[j];
					array[j] = t;
					k = j;
				} else
					break;

			}

		}

		// from heap to ordered sequence (removeMax)
		for (int i = 0; i < array.length; i++) {
			// move the head (max) at the end, k is last index of head
			int k = array.length - 1 - i;
			int t = array[0];
			array[0] = array[k];
			array[k] = t;

			int j = 0;

			// if at least the left children is still inside the heap
			if ((j * 2 + 1) < k) {

				//the value of the node to pop down
				int a = array[j];
				
				while ((j * 2 + 1) < k) {

					// the 2 children	
					int b = array[j * 2 + 1];
					// c may be not present, initialize to 0
					int c = Integer.MIN_VALUE;
					if ((j * 2 + 2) < k) {
						c = array[j * 2 + 2];
					}
					if ((a > b) && (a > c))
						break;
					else {
						if (b > c) {
							array[j] = b;
							array[j * 2 + 1] = a;
							j = j * 2 + 1;
						} else {
							array[j] = c;
							array[j * 2 + 2] = a;
							j = j * 2 + 2;
						}
					}

				}
			}
		}

		return array;
	}

	public static int[] randomizedQuickSort(int[] array) {
		randomizedQuickSort(array, 0, array.length-1);
		return array;
	}
	
	public static void randomizedQuickSort(int[] array, int a, int b)
	{
		//base
		if (a>=b) return;
		
		Random r = new Random();
		int random = r.nextInt(b-a);
		int pivotIndex = a + random;
		int pivot = array[pivotIndex];
		//substitute random with last
		array[pivotIndex]=array[b];
		array[b]=pivot;
		
		int i=a;
		int j=b-1;
		
		while (i<=j)
		{
			while ((i<=j)&&(array[i]<=pivot))
			{
				i++;
			}
			while ((i<=j)&&(array[j]>=pivot))
			{
				j--;
			}
			
			if (i<j)
			{
				int t=array[i];
				array[i]=array[j];
				array[j]=t;
			}
		}
		
		array[b]=array[i];
		array[i]=pivot;
		
		
		
		//recursion
		randomizedQuickSort(array,a,i-1);
		randomizedQuickSort(array,i+1,b);
		
		
	}

}
