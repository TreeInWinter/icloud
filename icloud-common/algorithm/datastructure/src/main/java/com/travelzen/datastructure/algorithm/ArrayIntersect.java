package com.travelzen.datastructure.algorithm;

import java.util.Arrays;

public class ArrayIntersect {
	
	
	public static int[] intersectSortedArrays(int[] a, int[] b){
	    int[] c = new int[Math.min(a.length, b.length)]; 
	    int ai = 0, bi = 0, ci = 0;
	    while (ai < a.length && bi < b.length) {
	        if (a[ai] < b[bi]) {
	            ai++;
	        } else if (a[ai] > b[bi]) {
	            bi++;
	        } else {
	            if (ci == 0 || a[ai] != c[ci - 1]) {
	                c[ci++] = a[ai];
	            }
	            ai++; bi++;
	        }
	    }
	    return Arrays.copyOfRange(c, 0, ci); 
	}

}
