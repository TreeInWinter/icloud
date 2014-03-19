package com.travelzen.framework.core.util;

import java.util.Comparator;
import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

public class SearchUtil {

	public static enum SearchResultType {
		SMALLER_THAN_LOW, BIGGER_THAN_HIGH, NORMAL, FOUND,

		// BIGGER_THAN_LOW,
		// SMALLER_THAN_HITH

	}

	public static <T> Triplet<Integer, Integer, SearchResultType> typedBinarySearch(
			List<? extends Comparable<? super T>> l, T key) {
		int low = 0;
		int high = l.size() - 1;

		int cmp = l.get(high).compareTo(key);
		if (cmp < 0) {
			return Triplet.with(high, null, SearchResultType.BIGGER_THAN_HIGH);
		}

		cmp = l.get(low).compareTo(key);

		if (cmp > 0) {
			return Triplet.with(null, low, SearchResultType.SMALLER_THAN_LOW);
		}

		int lowbound = low;
		int highbound = high;

		while (low <= high) {

			int mid = (low + high) >>> 1;

			cmp = l.get(mid).compareTo(key);

			if (cmp < 0) {
				lowbound = mid;
				low = mid + 1;
			} else if (cmp > 0) {
				highbound = mid;
				high = mid - 1;
			} else
				return Triplet.with(mid, mid, SearchResultType.FOUND); // key
																		// found
		}

		return Triplet.with(lowbound, highbound, SearchResultType.NORMAL); // key
																			// not
																			// found

	}

	/**
	 * this this function, the "low" is the last position which is less than key
	 * so we
	 * 
	 * @param l
	 * @param key
	 * @param c
	 * @return
	 */
	public static <T> Pair<Integer, SearchResultType> typedBinarySearch(List<? extends T> l, T key,
			Comparator<? super T> c) {
		int low = 0;
		int high = l.size() - 1;

		int cmp = c.compare(l.get(high), key);
		if (cmp < 0) {
			return Pair.with(high, SearchResultType.BIGGER_THAN_HIGH);
		}

		cmp = c.compare(l.get(low), key);
		if (cmp > 0) {
			return Pair.with(low, SearchResultType.SMALLER_THAN_LOW);
		}

		while (low <= high) {
			int mid = (low + high) >>> 1;
			T midVal = l.get(mid);
			cmp = c.compare(midVal, key);

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return Pair.with(mid, SearchResultType.FOUND); // key found
		}

		return Pair.with(low + 1, SearchResultType.NORMAL); // key not found
	}
}
