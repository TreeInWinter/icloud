package com.travelzen.datastructure.heap;

/*配对堆(Pairing Heap)是一个简单实用的min-heap结构（当然也可以做成max-heap）。它是一颗多路树(multiway tree)，
 * 类似于Leftist Heap和Skew Heap，但是与Binomial Tree和Fibonacci Heap不一样。它的基本操作是两个多路树的连接(link)，
 * 所以取名叫Pairing Heap。连接操作(参考以下实现中的方法linkPair)类似于Binomial Tree和Fibonacci Heap中的link操作，
 * 即将root key值最大的树作为key值最小的树的孩子（一般作为最左边的孩子，特别是Binomial Heap必须这样做），其复杂度是常数级。因为Pairing Heap只有一棵树，
 * 所以它的merge操作(类似于Fibonacci Heap中的union)也很简单，只需要link两棵树就可以了，平摊复杂度与Fibonacci Heap类似，都是常数级操作，
 * 而在Binomial Heap中需要union两个root lists，所以复杂度为O(logn)。在算法分析中，往往有很多数据结构实现起来比较简单，但是分析起来很复杂，例如快速排序 (Quicksort)，
 * 配对堆也是一个典型例子。配对堆的merge，insert和findMin的平摊复杂度都是O(1)，extract-min 的平摊复杂度是O(logn)，这与Fibonacci Heap中的相应操作的复杂度相当。
 * 但是，decrease-key的平摊复杂度比Fibonacci Heap大，后者的decrease-key的平摊复杂度是O(1)。
 * 关于配对堆的decrease-key操作的平摊复杂度结果可以参 考：http://en.wikipedia.org/wiki/Pairing_heap。

 在以下实现中，Pairing Heap采用“leftmost child,right sibling”（左孩子，右兄弟）方式表示，而且每一个结点还有一个left属性：对于第一个孩子，left属性表示该孩子的父结点；
 对于其他结 点，left属性表示该结点的左兄弟。Extract-Min操作比较有意思，首先采用类似Binomial Heap和Fibonacci Heap中做法，
 即先删除root结点，然后得到root的孩子结点双向链表，链表中每一个结点对应一个子堆(subheap)；接下来考虑如何将子堆合 并到原来的堆中，在这里可以比较一下二项堆，
 Fibonacci堆和配对堆的合并做法：在Binomial Heap中将孩子结点倒排，生成按degree从小到大顺序的单向链表，然后将该单链表跟原来剩余的堆结点root list链表作union操作。
 在Fibonacci Heap中的做法是，将孩子结点依次添加到root list中（不用考虑先后次序），然后通过consolidate生成degree唯一的双向循环链表。
 二者都是在Extract-min时让每个堆结构 变得更加紧凑，恢复成理想的状态，同时Extract-min的操作成本也相对比较高。
 在Pairing Heap中做法类似：如果没有Extract-min操作，其他的操作（比如insert,merge，decrease-key）势必使得root结点 的孩子链表变得很长，通过Extract-Min两两合并，让Pairing Heap变得更加有序。
 Extract-Min两两合并做法是：先从左到右将相邻的孩子结点两两link，生成一个缩减的双向链表，然后对该新的双向链表从右到左link（上一次合并的结果作为下一次link中的右兄弟结点）。
 实现：
 ?*/
/**
 * 
 * Pairing Heap
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/) Licensed under GPL
 * (http://www.opensource.org/licenses/gpl-license.php)
 * 
 * @author ljs 2011-09-06
 * 
 */
public class PairingHeap {
	// left-child, right-sibling representation
	static class Node {
		private int key;

		// left is the parent for first child; is the left sibling for other
		// children
		private Node left;

		private Node sibling;

		// child points to the leftmost-child
		private Node child;

		public Node(int key) {
			this.key = key;
		}

		public String toString() {
			return String.valueOf(this.key);
		}
	}

	private Node root;

	private Node linkPair(Node first, Node second) {
		if (second == null)
			return first;
		if (first == null)
			return second;

		if (first.key < second.key) {
			// second is linked to first as a child

			// retain the sibling relation
			Node secondzSibling = second.sibling;
			first.sibling = secondzSibling;
			if (secondzSibling != null)
				secondzSibling.left = first;

			Node firstzChild = first.child;

			// update second's left and sibling pointers
			second.left = first;
			second.sibling = firstzChild;

			// update first.child's pointer
			if (firstzChild != null)
				firstzChild.left = second;

			// update first's child
			first.child = second;
			return first;
		} else {
			// first is linked to second as a child

			// retain the sibling relation
			Node firstzLeft = first.left;
			second.left = firstzLeft;
			if (firstzLeft != null) {
				if (firstzLeft.child == first) {
					// firstzLeft is first's parent
					firstzLeft.child = second;
				} else {
					// firstzLeft is first's left sibling
					firstzLeft.sibling = second;
				}
			}

			Node secondzChild = second.child;
			// update first's left and sibling pointers
			first.left = second;
			first.sibling = secondzChild;

			// update second's child pointer
			if (secondzChild != null)
				secondzChild.left = first;

			// update second's child
			second.child = first;
			return second;
		}
	}

	public Node insert(Node node) {
		if (root == null)
			root = node;
		else
			root = linkPair(node, root);
		return node;
	}

	public void decreaseKey(Node x, int k) throws Exception {
		if (x.key < k)
			throw new Exception("key is not decreased!");
		x.key = k;
		if (x != root) {
			// cut x subtree from its siblings
			Node xzLeft = x.left;
			// if x is not root, its left (i.e. xzLeft) can never be null
			if (xzLeft.child == x) {// xzLeft is x's parent
				xzLeft.child = x.sibling;
			} else {// xzLeft is x's left sibling
				xzLeft.sibling = x.sibling;
			}
			if (x.sibling != null) {
				x.sibling.left = xzLeft;
			}

			// merge this tree with x subtree
			x.left = null;
			x.sibling = null;
			root = this.linkPair(x, root);
		}
	}

	public void merge(Node rhs) {
		if (this.root == null) {
			this.root = rhs;
			return;
		}
		if (rhs == null)
			return;

		this.root = this.linkPair(this.root, rhs);
	}

	public Node findMin() {
		return this.root;
	}

	public Node extractMin() {
		Node z = this.root;
		if (z != null) {
			if (z.child == null)
				root = null;
			else {
				Node firstSibling = z.child;
				firstSibling.left = null;
				root = mergeSubHeaps(firstSibling);
			}
		}
		return z;
	}

	private Node mergeSubHeaps(Node firstSibling) {
		// the 1st pass: merge pairs from left side
		Node first = firstSibling;
		Node second = first.sibling;

		Node tail = first;
		if (second != null) {
			tail = this.linkPair(first, second);
			first = tail.sibling;
			if (first != null)
				second = first.sibling;
			else
				second = null;
		}
		while (first != null && second != null) {
			tail = this.linkPair(first, second);
			first = tail.sibling;
			if (first != null)
				second = first.sibling;
			else
				second = null;
		}

		// the 2nd pass: merge pairs from right side
		if (first != null) {
			tail = first;
		}

		Node prev = tail.left;
		while (prev != null) {
			tail = this.linkPair(prev, tail);
			prev = tail.left;
		}
		return tail;
	}

	public void print() {
		System.out.println("Pairing Heap:");
		this.print(0, this.root);
	}

	private void print(int level, Node node) {
		for (int i = 0; i < level; i++) {
			System.out.format(" ");
		}
		System.out.format("|");
		for (int i = 0; i < level; i++) {
			System.out.format("-");
		}
		System.out.format("%d%n", node.key);
		Node child = node.child;
		while (child != null) {
			print(level + 1, child);
			child = child.sibling;
		}
	}

	public static void main(String[] args) throws Exception {
		PairingHeap pheap = new PairingHeap();
		Node node7 = pheap.insert(new Node(7));
		pheap.insert(new Node(19));
		Node node2 = pheap.insert(new Node(2));

		PairingHeap pheap2 = new PairingHeap();
		pheap2.insert(new Node(9));
		pheap2.insert(new Node(17));
		pheap2.insert(new Node(12));
		pheap2.insert(new Node(14));
		pheap.merge(pheap2.root);

		pheap2 = new PairingHeap();
		pheap2.insert(new Node(15));
		pheap2.insert(new Node(18));
		pheap2.insert(new Node(16));
		pheap2.insert(new Node(5));
		Node node11 = pheap2.insert(new Node(11));
		pheap.merge(pheap2.root);

		pheap2 = new PairingHeap();
		pheap2.insert(new Node(4));
		pheap2.insert(new Node(8));
		pheap.merge(pheap2.root);

		pheap2 = new PairingHeap();
		Node node3 = pheap2.insert(new Node(3));
		pheap2.insert(new Node(13));
		pheap2.insert(new Node(10));
		pheap.merge(pheap2.root);

		pheap.insert(new Node(6));

		pheap.print();

		Node min = pheap.findMin();
		System.out.format("min: %d%n", min.key);

		pheap.decreaseKey(node11, 0);
		pheap.decreaseKey(node7, 4);
		pheap.decreaseKey(node2, 1);
		pheap.decreaseKey(node3, 2);

		min = pheap.extractMin();
		while (min != null) {
			System.out.format("%d ", min.key);
			min = pheap.extractMin();
		}
	}

}
/*
 * 测试输出： Pairing Heap: |2 |-6 |-3 |--10 |--13 |-4 |--8 |-5 |--11 |--15 |---16
 * |---18 |-9 |--14 |--12 |--17 |-7 |--19 min: 2 0 1 2 4 4 5 6 8 9 10 12 13 14
 * 15 16 17 18 19
 */