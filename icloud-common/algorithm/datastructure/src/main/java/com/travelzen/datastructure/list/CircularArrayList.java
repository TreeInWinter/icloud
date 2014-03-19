package com.travelzen.datastructure.list;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * 
 * http://www.museful.net/2011/software-development/circulararraylist-for-java
 * 
 * 
 * Java applications sometimes need a “sliding window” view on a stream/sequence of data. This can be provided by a List with
fast element insertion and removal at opposite ends (like a queue)
fast access to random elements in the list (like an array)
Unfortunately, no existing implementation of the List interface in the Java Collections Framework satisfies both of these requirements. 
ArrayList is slow at inserting/removing at the list’s head, while LinkedList is slow at accessing elements in the list’s interior.
(ArrayDeque in the Java Collections Framework almost works, but its designers did not implement the List interface, 
thereby (perhaps intentionally) hiding its interior elements.)
This problem can be solved by using a circular array (also called a circular buffer, cyclic buffer, or ring buffer)
 as the underlying data structure.
So here is the solution: a fully-fledged, ready-to-use implementation supporting generic element type and everything else you would expect
 from a standard implementation in the Java Collections Framework. By extending AbstractList and wrapping ArrayList, 
 the full implementation is accomplished in under 100 lines of code!

 * @author liangwang
 *
 * @param <E>
 */
public class CircularArrayList<E> extends AbstractList<E> implements
		RandomAccess {

	private final int n; // buffer length
	private final List<E> buf; // a List implementing RandomAccess
	private int head = 0;
	private int tail = 0;

	public CircularArrayList(int capacity) {
		n = capacity + 1;
		buf = new ArrayList<E>(Collections.nCopies(n, (E) null));
	}

	public int capacity() {
		return n - 1;
	}

	private int wrapIndex(int i) {
		int m = i % n;
		if (m < 0) { // java modulus can be negative
			m += n;
		}
		return m;
	}

	// This method is O(n) but will never be called if the
	// CircularArrayList is used in its typical/intended role.
	private void shiftBlock(int startIndex, int endIndex) {
		assert (endIndex > startIndex);
		for (int i = endIndex - 1; i >= startIndex; i--) {
			set(i + 1, get(i));
		}
	}

	@Override
	public int size() {
		return tail - head + (tail < head ? n : 0);
	}

	@Override
	public E get(int i) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return buf.get(wrapIndex(head + i));
	}

	@Override
	public E set(int i, E e) {
		if (i < 0 || i >= size()) {
			throw new IndexOutOfBoundsException();
		}
		return buf.set(wrapIndex(head + i), e);
	}

	@Override
	public boolean add(E e) {
		int s = size();
		add(s,   e);
		return true;
	}
	
	@Override
	public void add(int i, E e) {
		int s = size();
		if (s == n - 1) {
			throw new IllegalStateException("Cannot add element."
					+ " CircularArrayList is filled to capacity.");
		}
		if (i < 0 || i > s) {
			throw new IndexOutOfBoundsException();
		}
		tail = wrapIndex(tail + 1);
		if (i < s) {
			shiftBlock(i, s);
		}
		set(i, e);
	}

	@Override
	public E remove(int i) {
		int s = size();
		if (i < 0 || i >= s) {
			throw new IndexOutOfBoundsException();
		}
		E e = get(i);
		if (i > 0) {
			shiftBlock(0, i);
		}
		head = wrapIndex(head + 1);
		return e;
	}
}
