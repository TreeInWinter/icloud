package com.travelzen.datastructure.heap;

/*just like leftist or skew heaps, a binomial heap is also a mergeable heap data structure.
 * 
 *  But unlike those 'lopsided' heaps that are composed of only single binary tree, a binomial heap consists of a set of trees that are called binomial trees. 
 *  
 *  Binomial trees are so called because the number of nodes at each level of the tree corresponds to the binomial coefficient.
 *   Binomial tree can be recursively defined as: a binomial tree with height k consists of two binomial trees with height k-1 that are linked together. 
 *   
 *   There are a few good properties that are characteristic of a binomial tree with height k: 
 *   
 *   1) the root node's degree in a binomial tree has the largest degree k; 
 *   
 *   2) there are 2^k number of nodes in the binomial tree; 
 *   
 *   3) the root node's children are a set of ordered binomial trees, with the following degrees respectively: k-1, k-2, ...., 1,0. 
 *   Note the degree of the root node can be called the degree of the binomial tree. The degree of the tree and the height of it have the same value k, 
 *   so they are used interchangeably. Also worth mentioning is that the children of any node in a binomial tree has order requirement, 
 *   which is no different from binary tree though a binomial tree is not binary.
 Binomial tree is just a tree structure without key constraint in each node. 
 But a binomial heap has such a constraint, i.e. the key in each node must satisfy the min-heap or max-heap property just like a binary heap. 
 In addition, the binomial heap has another constraint: there is one or zero number of binomial tree with degree k in a binomial heap. 
 Because of the latter constraint, a binomial heap's operation such as union is similar to a binary addition (see the code below for details). 
  Another important result from the constraint is there are at most floor(logn)+1 binomial trees in a binomial heap with n nodes, 
  and the max degree among those trees is floor(logn). Based on this result, it is easy to prove the operations on a binomial heap are in order of O(logn).
 Binomial heap can be represented as a root list of the binomial trees in degree's ascending order, 
 but the children of a node in a binomial tree are ordered decreasingly in their degrees. Also each binomial tree is stored in leftmost-child, 
 right-sibling form. Each node has at least five fields: parent, degree, child, sibling and key. In this way, it is easy to link two binomial trees with the same degree,
  and the cost is just O(1) (see the code below for details). Because the root of each binomial tree has no parent and sibling, 
  these two fields can be reused for another purpose in our code: the parent points to the previous node in the root list, the sibling points to the next node in the root list.
   So the root list in our implementation is actually a doubly-linked list,
  which is convenient for some operations such as Extract-Min. In Extract-Min, we first need to traverse the root list to find the node with the minimum key. 
  Afterwards, we will remove the min node from the original heap, and make another heap comprising of the children of the min node, and finally to union the two heaps. 
  To remove the min node from the original root list is very easy in a doubly-linked list. 
  On the other hand, it entails the operation Decrease-Key to be careful because of the root's repurposed parent and sibling.
   When we bubbles up the tree via the parent field of each node,
   we must ensure the root is the last node to be checked at most. Luckily, 
  the degree of a root's parent is smaller than that of the current binomial tree. So it is not a problem either.
 The basic operation of a binomial heap is to link two binomial trees of the same degree(see the link() method in the code below). 
 The most important operation is union two heaps (similar to the merge operation in a leftist heap). Most of other operations are just based on union. 
  See the code for details. 
  Compared with a binary heap or leftist/skew heap, the Find-Min operation is not constant in worst case  any more because we need to iterate the binomial trees in a heap, 
  and the the trees are numbered at O(logn). 
  But there is good news too: it can be proved that the insertion of a series of elements in a binomial heap is O(n) in worst case. In an average case, 
  the time complexity of a single insertion is constant.
 Code:
 ?*/
import java.util.Stack;

/**
 * 
 * Binomial Heap
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/) Licensed under GPL
 * (http://www.opensource.org/licenses/gpl-license.php)
 * 
 * @author ljs 2011-08-29
 * 
 */
public class BinomialHeap {
	static class Node {
		private int key;
		private int degree;

		private Node parent;
		private Node child;
		private Node sibling;

		public Node() {
			// a dummy node
		}

		public Node(int key) {
			this.key = key;
		}

		public String toString() {
			return this.key + "(degree=" + this.degree + ")";
		}
	}

	// the head of root list
	private Node head;

	// find the min key in the heap
	public Node findMin() {
		Node h = head;
		Node min = head;
		while (h != null) {
			if (h.key < min.key)
				min = h;
			h = h.sibling;
		}
		return min;
	}

	// remove the min key from the heap
	public Node extractMin() {
		Node min = findMin();
		if (min == null)
			return null;

		if (min.parent == null) {
			// remove min node from the heap: update the heap head
			this.head = this.head.sibling;
			// update parent
			if (this.head != null)
				this.head.parent = null;
		} else {
			// remove min node from the heap
			min.parent.sibling = min.sibling;
			// update parent
			if (min.sibling != null)
				min.sibling.parent = min.parent;
		}
		// reverse the children as a new heap H'
		Node child = min.child;
		if (child != null) {
			Stack<Node> children = new Stack<Node>();
			children.push(child);
			while (child.sibling != null) {
				children.push(child.sibling);
				child = child.sibling;
			}
			// headMin is the head of H'
			Node headMin = children.pop();
			Node h = headMin;
			// update parent
			h.parent = null;
			while (!children.isEmpty()) {
				Node tmp = children.pop();
				// update parent for children
				tmp.parent = h;
				h.sibling = tmp;

				h = tmp;
			}
			// Important: set the last child's sibling to null
			h.sibling = null;

			union(headMin);
		}

		return min;
	}

	// precondition: x.key > k
	public void decreaseKey(Node x, int k) throws Exception {
		if (x.key < k)
			throw new Exception("key is not decreased!");
		x.key = k;
		Node p = x.parent;
		// only adjust in a single binomial tree
		while (p != null && p.degree > x.degree && x.key < p.key) {
			// swap key
			int tmp = x.key;
			x.key = p.key;
			p.key = tmp;

			x = p;
			p = x.parent;
		}
	}

	public void delete(Node x) throws Exception {
		decreaseKey(x, Integer.MIN_VALUE);
		this.extractMin();
	}

	// insert a key into the heap (x can be duplicate key in the heap)
	public Node insert(int x) {
		Node nodex = new Node(x);
		union(nodex);
		return nodex;
	}

	// union this heap with the another heap
	// rhs: the head of the other heap
	public void union(Node rhs) {
		if (rhs == null || rhs == this.head)
			return;

		if (this.head == null) {
			this.head = rhs;
			return;
		}
		this.head = BinomialHeap.union(this.head, rhs);
	}

	// union two binomial heaps
	private static Node union(Node head1, Node head2) {
		Node prev = new Node();
		Node h = prev;

		Node carry = null;

		Node n1 = head1;
		Node n2 = head2;
		while (true) {
			// root list's back pointer
			Node parent = prev;

			int whichCase = 0;
			Node t1 = null, t2 = null;
			if (n1 == null && n2 == null) {
				if (carry != null) {
					prev.sibling = carry;
					prev = carry;
					carry = null;

					prev.parent = parent;
					parent = prev;
				}
				break; // exit while
			} else if (n2 == null) {
				t1 = n1;
				n1 = n1.sibling;
			} else if (n1 == null) {
				t2 = n2;
				n2 = n2.sibling;
			} else {
				if (n1.degree < n2.degree) {
					t1 = n1;
					n1 = n1.sibling;
				} else if (n1.degree > n2.degree) {
					t2 = n2;
					n2 = n2.sibling;
				} else {
					t1 = n1;
					t2 = n2;
					n1 = n1.sibling;
					n2 = n2.sibling;
				}
			}

			int currDeg = (t1 == null) ? t2.degree : t1.degree;
			whichCase = (t1 == null) ? 0 : 1;
			whichCase += (t2 == null) ? 0 : 2;

			if (carry != null) {
				if (carry.degree < currDeg) {
					prev.sibling = carry;
					prev = carry;
					carry = null;

					prev.parent = parent;
					parent = prev;
				} else {
					whichCase += 4;
				}
			}
			switch (whichCase) {
			case 1: // t1 != null
				prev.sibling = t1;
				prev = t1;
				prev.parent = parent;
				break;
			case 2: // t2 != null
				prev.sibling = t2;
				prev = t2;
				prev.parent = parent;
				break;
			case 3: // 1+2
				// t1 and t2 should be root
				carry = link(t1, t2);
				break;
			case 5: // 1+4
				carry = link(t1, carry);
				break;
			case 6: // 2+4
				carry = link(t2, carry);
				break;
			case 7: // 1+2+4
				prev.sibling = carry;
				prev = carry;
				prev.parent = parent;

				carry = link(t1, t2);
				break;
			}
		}
		// reset head's parent to null
		h.sibling.parent = null;
		// Important: set the last binomial tree's sibling to null
		prev.sibling = null;

		return h.sibling;
	}

	// link two binomial trees of same degree
	private static Node link(Node root1, Node root2) {
		if (root1 == null)
			return root2;
		if (root2 == null)
			return root1;

		Node smallTree = root1;
		Node bigTree = root2;
		if (root1.key > root2.key) {
			smallTree = root2;
			bigTree = root1;
		}
		// link larger to smaller tree (to maintain min-heap property)
		bigTree.sibling = smallTree.child;
		smallTree.child = bigTree;

		smallTree.degree += 1;
		bigTree.parent = smallTree;

		return smallTree;
	}

	public void printBinomialHeap() {
		System.out.println("Heap:");
		Node h = this.head;
		while (h != null) {
			this.print(0, h);
			h = h.sibling;
		}
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
		// test build heap

		BinomialHeap heap = new BinomialHeap();
		// build heap
		Node node8 = null;
		for (int k = 15; k >= 1; k--) {
			Node x = heap.insert(k);
			if (k == 8) {
				// save node
				node8 = x;
			}
		}

		heap.delete(node8);
		// after delete node8
		heap.printBinomialHeap();

		Node min = heap.findMin();
		System.out.format("min:%d%n", min.key);

		System.out.println("keys:");
		min = heap.extractMin();
		while (min != null) {
			// heap.printBinomialHeap();
			System.out.format(" %d", min.key);
			min = heap.extractMin();
		}

		System.out.format("%n****************%n");
		// test heaps union
		int[] A = new int[] { 28, 41, 15, 33, 7, 25, 12 };
		BinomialHeap h1 = new BinomialHeap();
		for (int i = 0; i < A.length; i++) {
			h1.insert(A[i]);
		}
		// h1.printBinomialHeap();

		int[] B = new int[] { 45, 55, 30, 32, 23, 24, 8, 22, 48, 50, 29, 31,
				10, 17, 6, 44, 3, 37, 18 };
		BinomialHeap h2 = new BinomialHeap();
		for (int i = 0; i < B.length; i++) {
			h2.insert(B[i]);
		}
		// h2.printBinomialHeap();

		// union h1 and h2
		h1.union(h2.head);
		h1.printBinomialHeap();

		System.out.println("keys:");
		min = h1.extractMin();
		while (min != null) {
			// h1.printBinomialHeap();
			System.out.format(" %d", min.key);
			min = h1.extractMin();
		}

		System.out.format("%n****************%n");
		// test extract min
		A = new int[] { 11, 27, 8, 17, 14, 38, 6, 29, 26, 42, 16, 23, 12, 18,
				1, 25, 28, 77, 10, 13, 37, 41 };
		heap = new BinomialHeap();
		for (int i = 0; i < A.length; i++) {
			heap.insert(A[i]);
		}
		heap.extractMin();
		heap.printBinomialHeap();

		System.out.format("%n****************%n");
		// test decrease key
		A = new int[] { 30, 42, 16, 23, 28, 77, 10, 13, 11, 27, 8, 17, 14, 38,
				6, 29, 37, 41, 12, 18, 25 };
		heap = new BinomialHeap();
		Node node0 = null;
		for (int i = 0; i < A.length; i++) {
			Node x = heap.insert(A[i]);
			if (i == 0) {
				// save node
				node0 = x;
			}
		}
		heap.decreaseKey(node0, 7);
		heap.printBinomialHeap();
	}
}
/*
 * Testing output: Heap: |1 |-9 |2 |-10 |--11 |-3 |4 |-12 |--14 |---15 |--13 |-6
 * |--7 |-5 min:1 keys: 1 2 3 4 5 6 7 9 10 11 12 13 14 15*************** Heap:
 * |12 |-18 |3 |-15 |--28 |---41 |--33 |-7 |--25 |-37 |6 |-8 |--30 |---45
 * |----55 |---32 |--23 |---24 |--22 |-29 |--48 |---50 |--31 |-10 |--17 |-44
 * keys: 3 6 7 8 10 12 15 17 18 22 23 24 25 28 29 30 31 32 33 37 41 44 45 48 50
 * 55*************** Heap: |25 |12 |-37 |--41 |-18 |6 |-10 |--16 |---26 |----42
 * |---23 |--28 |---77 |--13 |-8 |--11 |---27 |--17 |-14 |--38 |-29
 * 
 * *************** Heap: |25 |12 |-37 |--41 |-18 |6 |-7 |--10 |---16 |----42
 * |---23 |--28 |---77 |--13 |-8 |--11 |---27 |--17 |-14 |--38 |-29
 */