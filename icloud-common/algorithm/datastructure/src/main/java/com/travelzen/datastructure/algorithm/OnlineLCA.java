package com.travelzen.datastructure.algorithm;

/*Tarjan算法解决LCA查询要求事先知道全部查询提问，如果LCA要求即时询问即时回答，就需要用到下面介绍的在线算法。在线算法需要对任意树进行预处理，设输入树的结点个数为n，该算法的预处理时间和空间复杂度都是O(n)，查询复杂度为O(1)。

 本算法的基本思想是将对一棵树T做预处理，生成每个结点的额外信息，然后利用结点的额外信息将T映射到一颗完全二叉树B，最后利用完全二叉树的特性，通过二进制位运算可以在常数时间内查询出任何两个结点x和y的LCA。

 *****************预处理*****************
 预处理T需要生成每个结点的inlabel值，ascendant值，parent和level值，以及一张大小为O(n)的HEAD表。

 计算inlabel值的方法是：对T做先序(preorder)深度优先遍历，从1开始对每一个结点编号，由于是先序遍历，每一棵子树的根结点（设为v）的编号在该子树中是最小的，如果同时记录该子树的结点个数size(v)，可以得出以v为根的子树的编号范围是闭区间：[v, v+size(v)-1]。（这里同时用v表示它的编号，注意不要混淆）。将这个区间中每一个编号转换成二进制表示（二进制位数l+1=floor(logn)+1）之后，inlabel(v)就是这些二进制数中右边0最多的那个编号。显然叶结点的inlabel(v)就是v的编号。

 inlabel值具有两个重要的性质：1）inlabel(v)相同的结点在T中构成一条简单路径，叫做inlabel(v)路径。2）将完全二叉树B按照中序(inorder)遍历对它的每一个结点编号，然后将T中每一个结点的inlabel值映射到B中编号相同的结点，得到一个多对一映射(n:1)，但不是满射(surjective)。显然inlabel path上的点映射为B中的同一个结点，不在同一个inlabel path上的点映射为B中不同的结点。采用这样的映射方法之后，在T中，如果结点q是结点p祖先，那么在B中inlabel(q)也是inlabel(p)的祖先。为方便起见，这里认为每一个结点也是自身的祖先结点（下面的分析中也用到这一规定）。

 计算ascendant值的方法是：ascendant(v)记录的是v的所有从根开始的祖先结点中inlabel值的最右边的"1"的索引下标。例如, 假设v(它的inlabel值等于0011)的祖先的inlabel值依次为：1000,0100,0011（注意v也是自身的祖先），那么ascendant(v)=1101。ascendant值的二进制位数也是l+1=floor(logn)+1。因为ascendant值需要inlabel值，因此计算ascendant需要在另一个遍历（以下实现中使用层序遍历）中完成。

 计算level值（从根的level=0开始,每增加一层level加1）和parent值很简单，在遍历时附带添加就可以了。HEAD表记录的是每一个inlabel路径的head，所谓head就是inlabel路径上与root最近的那个结点，它的parent的inlabel值和它的label值不同。由于inlabel值上界值为n,因此在以下实现中设置HEAD表的大小为n+1（下标从0开始），HEAD的每一个下标等于对应的inlabel值，由于并不是每一个下标都有相应的inlabel值，因此HEAD是一个稀疏表，但是这里还是采用数组来处理。

 *****************查询：LCA(x,y)*****************
 查询分两种情况：
 1）如果inlabel(x)等于inlabel(y)，即x和y在一条inlabel路径上，那么LCA(x,y)等于x和y中level值最小的那个结点。
 2) 如果inlabel(x)不等于inlabel(y)，即x和y不在同一条inlabel路径上。在树T中，令z=LCA(x,y)，在树B中，令b=LCA(inlabel(x),inlabel(y))。设b的二进制表示中最右边的"1"索引值为i, inlabel(z)的最右边的“1”索引值为j。

 按以下步骤计算z：
 a) 计算i: 设inlabel(x)和inlabel(y)中从左到右第一个不同的bit所在索引值为(1),inlabel(x)的最右边的"1"的索引值为(2)，inlabel(y)的最右边的"1"的索引值为(3)，那么i=max((1),(2),(3))；

 b) 根据i，计算j和inlabel(z): 由于z是x和y的最近公共祖先，因此在B中inlabel(z)也是inlabel(x)和inlabel(y)的祖先，而且是b的最近祖先(b也可能等于inlabel(z))。j的计算如下：ascendant(x)和ascendant(y)的二进制数从右边第i位开始一直往左扫描，找到的第一个"1"所在索引值就是j。inlabel(z)根据inlabel(x)或inlabel(y)以及j值很容易得出(参考以下实现中的方法findInlabel)：左边l-j位与inlabel(x)或inlabel(y)相应位相同，然后右边添加1，最后右边补上j个"0"。

 c) 根据j，计算z: 现在已经找到inlabel(z)，目标是找到inlabel(z)路径上x的最近祖先x_hat以及y的最近祖先y_hat，然后根据x_hat和y_hat的level值大小按照情况1)中的方法确定z的值。下面只介绍计算x_hat的方法，计算y_hat的方法类似。如果inlabel(x)等于inlabel(z)，那么x_hat就是x。否则，计算x_hat可以先计算x_hat的儿子w, w同时也要求是x的祖先（w可以等于x）。
 w不能直接计算出来，需要先计算出w的二进制表示中最右边的"1"的索引值k，然后根据以下实现中的方法findInlabel找到inlabel(w)，最后根据inlabel(w)得出w。w计算出来之后，可以计算出x_hat=w.parent。下面只说明两个关键步骤：k和w如何计算。

 k的计算方法：k等于ascendant(x)的二进制表示中最右边j位数中最左边的"1"的索引值。这是因为w离inlabel(z)最近,而且k小于j。

 根据inlabel(w)得出w: 由于x_hat是inlabel(z)路径上x的最近祖先，也就是说从x到x_hat的搜索路径上，当遇到inlabel(z)路径时，这个结点就是x_hat， 因此x_hat的儿子w不会在inlabel(z)路径上，而且又由于w的parent就是x_hat，它们的inlabel值不同，所以w就是它所在的inlabel path的head。所以可以得到w=HEAD(inlabel(w))。

 实现：
 ?*/
import java.util.LinkedList;

/**
 * 
 * Online LCA algorithm complexity: <O(n),O(1)>
 * 
 * see also: B. Schieber & U. Vishkin (1988)
 * "On finding lowest common ancestors - Simplification and parallelization"
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/) Licensed under GPL
 * (http://www.opensource.org/licenses/gpl-license.php)
 * 
 * 
 * @author ljs 2011-08-23
 * 
 */
public class OnlineLCA {
	static class Node {
		int key;
		Node[] children;

		Node parent;
		int inlabel;
		int ascendant;
		int level;

		public Node(int key) {
			this.key = key;
		}

		public String toString() {
			return String.valueOf(key) + " parent:"
					+ (this.parent == null ? "" : this.parent.key)
					+ " inlabel:" + inlabel + " level:" + level + " ascendant:"
					+ ascendant;
		}
	}

	// the leaders of each inlabel path
	private Node[] HEAD;
	// the bits of inlabel and ascendent
	private int l;
	private Node root;

	public OnlineLCA(int n, Node root) {
		HEAD = new Node[n + 1];
		this.root = root;
		l = (int) (Math.log(n) / Math.log(2));
	}

	// preprocess tree T
	public void preprocess() {
		this.root.level = 0;
		doInOrderTraverse(this.root);

		doLevelOrderTraverse();
	}

	// return the LCA node of x and y
	public Node query(Node x, Node y) {
		Node lca = null;
		if (x.inlabel == y.inlabel) {
			if (x.level < y.level)
				lca = x;
			else
				lca = y;
		} else {
			// find i (lca of x.inlabel and y.inlabel in complete binary tree B)
			int i = findLSBOne(x.inlabel, 0);
			int tmp = findLSBOne(y.inlabel, 0);
			if (tmp > i)
				i = tmp;
			int diff = x.inlabel ^ y.inlabel;
			tmp = (int) (Math.log(diff) / Math.log(2));
			if (tmp > i)
				i = tmp;

			// find j and z.inlabel (z is lca of x and y)
			int ascand = x.ascendant & y.ascendant;
			int j = findLSBOne(ascand, i);
			int inlabelz = findInlabel(x, j);

			// find x-hat and y-hat
			Node x_hat = findHat(x, inlabelz, j);
			Node y_hat = findHat(y, inlabelz, j);
			if (x_hat.level < y_hat.level)
				lca = x_hat;
			else
				lca = y_hat;
		}
		return lca;
	}

	private Node findHat(Node x, int inlabelz, int j) {
		Node x_hat = null;
		if (x.inlabel == inlabelz) {
			x_hat = x;
		} else {
			int ascsub = x.ascendant & ((1 << j) - 1); // 000.A{j-1}...A{0}
			int k = (int) (Math.log(ascsub) / Math.log(2));
			// compute inlabel(w) path
			int inlabelw = findInlabel(x, k);
			Node w = HEAD[inlabelw];
			x_hat = w.parent;
		}
		return x_hat;
	}

	private int findInlabel(Node x, int j) {
		int mask = ((1 << (l - j)) - 1) << (j + 1); // e.g. 1110000
		int inlabel = (x.inlabel & mask) + (1 << j); // 1111000
		return inlabel;
	}

	// find the index of rightmost "1" in num's binary form
	private int findLSBOne(int num, int rightStart) {
		int i = rightStart;
		num >>= rightStart;
		while ((num & 0x01) != 0x01) {
			num >>= 1;
			i++;
		}
		return i;
	}

	// calculate ascendants and head based on inlabel numbers
	// so this method must be called after inlabels are determined.
	private void doLevelOrderTraverse() {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(this.root);
		HEAD[this.root.inlabel] = this.root;
		this.root.ascendant = (1 << l); // 2^l
		while (!queue.isEmpty()) {
			Node parent = queue.remove();
			// enqueue children
			if (parent.children != null) {
				for (Node child : parent.children) {
					if (child.inlabel != parent.inlabel) {
						HEAD[child.inlabel] = child;

						// calculate ascendant
						int i = findLSBOne(child.inlabel, 0);
						child.ascendant = parent.ascendant + (1 << i);
					} else {
						child.ascendant = parent.ascendant;
					}
					queue.add(child);
				}
			}
		}
	}

	private int nr = 1;

	private int doInOrderTraverse(Node node) {
		// visit node
		int preorder = nr;
		nr++;

		int size = 1;
		if (node.children != null) {
			for (Node child : node.children) {
				child.parent = node; // update parent
				child.level = node.level + 1; // calculate level values
				size += doInOrderTraverse(child);
			}
		}
		// calculate inlabel(node)
		int intervalMax = preorder + size - 1;
		int diff = (preorder - 1) ^ intervalMax;
		int i = (int) (Math.log(diff) / Math.log(2));
		int inlabel = intervalMax & ~((1 << i) - 1);
		node.inlabel = inlabel;

		return size;
	}

	public void lcaReport(Node x, Node y) {
		Node lca = this.query(x, y);
		System.out.format("LCA of %d and %d is: %d%n", x.key, y.key, lca.key);
	}

	public static void main(String[] args) {
		int[] A = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
		Node a0 = new Node(A[0]);
		Node a1 = new Node(A[1]);
		Node a2 = new Node(A[2]);
		Node a3 = new Node(A[3]);
		Node a4 = new Node(A[4]);
		Node a5 = new Node(A[5]);
		Node a6 = new Node(A[6]);
		Node a7 = new Node(A[7]);
		Node a8 = new Node(A[8]);
		Node a9 = new Node(A[9]);

		a0.children = new Node[] { a1, a4 };
		a1.children = new Node[] { a2, a3 };
		a4.children = new Node[] { a5, a6, a7 };
		a7.children = new Node[] { a8, a9 };

		OnlineLCA onlineLCA = new OnlineLCA(A.length, a0);

		onlineLCA.preprocess();

		// queries
		onlineLCA.lcaReport(a2, a3);
		onlineLCA.lcaReport(a2, a9);
		onlineLCA.lcaReport(a6, a9);
		onlineLCA.lcaReport(a7, a8);
		onlineLCA.lcaReport(a6, a7);
		onlineLCA.lcaReport(a9, a8);
		onlineLCA.lcaReport(a4, a9);

		System.out.println("************************");
		int[] B = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110,
				120, 130, 140, 150, 160, 170, 180, 190, 200, 210, 220 };
		Node b0 = new Node(B[0]);
		Node b1 = new Node(B[1]);
		Node b2 = new Node(B[2]);
		Node b3 = new Node(B[3]);
		Node b4 = new Node(B[4]);
		Node b5 = new Node(B[5]);
		Node b6 = new Node(B[6]);
		Node b7 = new Node(B[7]);
		Node b8 = new Node(B[8]);
		Node b9 = new Node(B[9]);
		Node b10 = new Node(B[10]);
		Node b11 = new Node(B[11]);
		Node b12 = new Node(B[12]);
		Node b13 = new Node(B[13]);
		Node b14 = new Node(B[14]);
		Node b15 = new Node(B[15]);
		Node b16 = new Node(B[16]);
		Node b17 = new Node(B[17]);
		Node b18 = new Node(B[18]);
		Node b19 = new Node(B[19]);
		Node b20 = new Node(B[20]);
		Node b21 = new Node(B[21]);

		b0.children = new Node[] { b1, b10, b12 };
		b1.children = new Node[] { b2 };
		b2.children = new Node[] { b3, b6, b9 };
		b3.children = new Node[] { b4, b5 };
		b6.children = new Node[] { b7 };
		b7.children = new Node[] { b8 };

		b10.children = new Node[] { b11 };
		b12.children = new Node[] { b13 };
		b13.children = new Node[] { b14 };
		b14.children = new Node[] { b15, b16, b17, b21 };
		b17.children = new Node[] { b18 };
		b18.children = new Node[] { b19, b20 };

		onlineLCA = new OnlineLCA(B.length, b0);

		onlineLCA.preprocess();

		// queries
		onlineLCA.lcaReport(b3, b6);
		onlineLCA.lcaReport(b16, b17);
		onlineLCA.lcaReport(b8, b21);
		onlineLCA.lcaReport(b12, b19);
		onlineLCA.lcaReport(b15, b21);
		onlineLCA.lcaReport(b19, b20);
		onlineLCA.lcaReport(b11, b18);
		onlineLCA.lcaReport(b4, b7);
		onlineLCA.lcaReport(b3, b10);
		onlineLCA.lcaReport(b5, b9);
		onlineLCA.lcaReport(b2, b19);
		onlineLCA.lcaReport(b5, b18);
		onlineLCA.lcaReport(b0, b4);
		onlineLCA.lcaReport(b1, b4);
		onlineLCA.lcaReport(b4, b8);
		onlineLCA.lcaReport(b2, b14);
	}

}

/*
 * 测试：
 * 
 * LCA of 30 and 40 is: 20 LCA of 30 and 100 is: 10 LCA of 70 and 100 is: 50 LCA
 * of 80 and 90 is: 80 LCA of 70 and 80 is: 50 LCA of 100 and 90 is: 80 LCA of
 * 50 and 100 is: 50*********************** LCA of 40 and 70 is: 30 LCA of 170
 * and 180 is: 150 LCA of 90 and 220 is: 10 LCA of 130 and 200 is: 130 LCA of
 * 160 and 220 is: 150 LCA of 200 and 210 is: 190 LCA of 120 and 190 is: 10 LCA
 * of 50 and 80 is: 30 LCA of 40 and 110 is: 10 LCA of 60 and 100 is: 30 LCA of
 * 30 and 200 is: 10 LCA of 60 and 190 is: 10 LCA of 10 and 50 is: 10 LCA of 20
 * and 50 is: 20 LCA of 50 and 90 is: 30 LCA of 30 and 150 is: 10
 */
