package com.travelzen.datastructure.heap;

/*左倾堆(Leftist Heap)是一个便于merge操作的数据结构，通过左倾树(Leftist Tree)实现。左倾树是一种特殊的二叉树,树中结点除了满足普通二叉堆的key大小规定外，
 * 还要求每一个结点X的左子树的Null Path Length（NPL)值不小于右子树的NPL值，因此这也是与普通二叉堆的区别：虽然普通二叉堆也满足左倾树的条件，左倾树往往不是一棵完全二叉树(而且通常不平衡)，
 * 从而左倾树不能用数组表示了。上面提到的NPL指的是某个结点到NULL结点（总共有n+1个NULL结点）的最短路径长度，规定NULL结点本身的NPL等于-1，叶子结点的NPL等于0,非叶结点的NPL等于它的两个孩子结点的NPL最小值加1。
 * 按照左倾树的定义，它的左右子树都是左倾树, 而且有一个重要属性：一个具有n个结点的左倾堆最右边路径长度（即相当于root或树的NPL值)最多为floor(log(n+1))，利用这个特性可以在右路高效地实现下面的merge操作。

普通的二叉堆合并复杂度为O(n)，而左倾堆的合并复杂度只有O(logn)，(n是合并后的左倾堆的结点个数)，因此左倾堆的最大优势是高效的合并操作。最小左倾堆的merge操作的算法是，输入两个左倾堆h1和h2，
假设h1的根结点key值大于h2的根结点key值（如果不是这样，交换h1和h2），则将h2的根作为合并后的root结点，同时递归调用merge(h1.root，h2.root.right)，返回的结果作为h2.root的右子树，
由于merge操作保证了返回的树满足左倾树条件，因此返回的结果（现在是h2.root的右子树）也已经是一颗左倾树了。如果h2.root.right的NPL值大于h2.root.left的NPL值，
需要交换两棵子树h2.root.left和h2.root.right，最后更新h2.root的NPL。

左倾堆的Extract-Min操作先删除root结点，然后调用merge操作将左右子树合并为一颗新的左倾树；Insert操作将要插入的元素看作一个只包含root结点的左倾堆,然后调用merge操作将两个左倾堆合并。

构造左倾堆：如果还是采用Insert操作，则复杂度为O(nlogn)；如果使用普通二叉堆的构造方法，则复杂度为O(n)，但是这并不是最好的左倾堆，原因是一方面构造的左倾堆是完全二叉树，
但是另一方面又不能按照数组对它进行操作。采用FIFO队列结构可以达到O(n)复杂度，同时构造出来的左倾堆左倾效果最好，做法如下：将每个元素作为左倾堆(允许任何两个元素值相同)依次enqueue，
然后每次从队列中dequeue两个左倾堆，用merge算法合并，并将合并的左倾堆enqueue到队尾，直到队列中只剩一个左倾堆，这个左倾堆就是最终结果。


它的优势就是用在需要频繁使用合并操作的场合，例如将两个优先队列并成一个优先队列。


实现：
*/

import java.util.LinkedList;

 
/**
 * 
 * Leftist Heap   
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-08-20
 *
 */
public class LeftistMinHeap {
	static class LeftistHeapNode{
		int key;
		LeftistHeapNode left;
		LeftistHeapNode right;
		int npl; //null-path-length
		
		public LeftistHeapNode(int key){
			this.key = key;
			this.npl = 0;
		}
		
		public String toString(){
			return this.key + "(npl=" + this.npl + ")";
		}
		
		public LeftistHeapNode merge(LeftistHeapNode rhsRoot){
			if(rhsRoot==this || rhsRoot == null) return this;
										
			LeftistHeapNode root1 = null; //the root of the merged tree
			LeftistHeapNode root2 = null; 
			if(rhsRoot.key<this.key){
				//merge this with rhsRoot's right child
				root1 = rhsRoot;
				root2 = this;
			}else{
				//merge rhsRoot with this's right child
				root1 = this;
				root2 = rhsRoot;				
			}
			
			LeftistHeapNode tmpRoot = root2.merge(root1.right);
			root1.right = tmpRoot;
			if(root1.left == null){ //left can not be null unless right is null
				root1.right = null;
				root1.left = tmpRoot;
				root1.npl = 0;
			}else{
				if(root1.right.npl>root1.left.npl){
					//swap left and right child						
					root1.right = root1.left;
					root1.left = tmpRoot;					
				}
				//at this time, the right child has the shortest null-path
				root1.npl = root1.right.npl + 1;
			}
			
			return root1;
		}
	}
	
	private LeftistHeapNode root;
	
	public LeftistMinHeap(LeftistHeapNode root){
		this.root = root;
	}
	
	private static LeftistHeapNode merge(LeftistHeapNode root1,LeftistHeapNode root2){
  		if(root1 == null) return root2;
		if(root2 == null) return root1;
		return root1.merge(root2); 
	}      
	public static LeftistMinHeap merge(LeftistMinHeap h1,LeftistMinHeap h2){
		LeftistHeapNode rootNode = merge(h1.root,h2.root);
		return new LeftistMinHeap(rootNode);
	}      
	
	public static LeftistMinHeap buildHeap(int[] A){
		LinkedList<LeftistHeapNode> queue = new LinkedList<LeftistHeapNode>();
		int n = A.length;
		//init: queue all elements as a single-node tree
		for(int i=0;i<n;i++){
			LeftistHeapNode node = new LeftistHeapNode(A[i]);
			queue.add(node);
		}
		//merge adjacent heaps and enqueue the merged heap afterward
		while(queue.size()>1){
			LeftistHeapNode root1 = queue.remove(); //dequeue
			LeftistHeapNode root2 = queue.remove();
			LeftistHeapNode rootNode = merge(root1,root2);
			queue.add(rootNode);
		}
		LeftistHeapNode rootNode = queue.remove();
		return new LeftistMinHeap(rootNode);
	}
	public void insert(int x){
		this.root = LeftistMinHeap.merge(new LeftistHeapNode(x), this.root);
	}
	
	public Integer extractMin(){
		if(this.root == null) return null;
		
		int min = this.root.key;
		this.root = LeftistMinHeap.merge(this.root.left, this.root.right);
		return min;
	}
	 
	public static void main(String[] args) {
		 int[] A = new int[]{4,8,10,9,1,3,5,6,11};
		 
		 LeftistMinHeap heap = LeftistMinHeap.buildHeap(A);
		 heap.insert(7);
		 Integer min = null;
		 while((min = heap.extractMin()) != null){
			 System.out.format(" %d", min);
		 }
		 System.out.println();
		 
		 System.out.println("********************");
		 A = new int[]{3,10,8,21,14,17,23,26};
		 
		 
		 LeftistHeapNode a0 = new LeftistHeapNode(A[0]);
		 LeftistHeapNode a1 = new LeftistHeapNode(A[1]);
		 LeftistHeapNode a2 = new LeftistHeapNode(A[2]);
		 LeftistHeapNode a3 = new LeftistHeapNode(A[3]);
		 LeftistHeapNode a4 = new LeftistHeapNode(A[4]);
		 LeftistHeapNode a5 = new LeftistHeapNode(A[5]);
		 LeftistHeapNode a6 = new LeftistHeapNode(A[6]);
		 LeftistHeapNode a7 = new LeftistHeapNode(A[7]);		 
		 a0.left = a1;  a0.npl = 1;
		 a0.right = a2; 
		 a1.left = a3;  a1.npl = 1;
		 a1.right = a4;
		 a4.left = a6;
		 a2.left = a5;
		 a5.left = a7;
		 LeftistMinHeap h1 = new LeftistMinHeap(a0);
		 
		 int[] B = new int[]{6,12,7,18,24,37,18,33};
		 LeftistHeapNode b0 = new LeftistHeapNode(B[0]);
		 LeftistHeapNode b1 = new LeftistHeapNode(B[1]);
		 LeftistHeapNode b2 = new LeftistHeapNode(B[2]);
		 LeftistHeapNode b3 = new LeftistHeapNode(B[3]);
		 LeftistHeapNode b4 = new LeftistHeapNode(B[4]);
		 LeftistHeapNode b5 = new LeftistHeapNode(B[5]);
		 LeftistHeapNode b6 = new LeftistHeapNode(B[6]);
		 LeftistHeapNode b7 = new LeftistHeapNode(B[7]);
		 b0.left = b1;  b0.npl = 2;
		 b0.right = b2;
		 b1.left = b3;  b1.npl = 1;
		 b1.right = b4; 
		 b4.left = b7;
		 b2.left = b5;  b2.npl = 1;
		 b2.right = b6;
		 LeftistMinHeap h2 = new LeftistMinHeap(b0);
		 
		 heap = LeftistMinHeap.merge(h1,h2);
		 while((min = heap.extractMin()) != null){
			 System.out.format(" %d", min);
		 }
		 System.out.println(); 
		 
		 System.out.println("********************");
		 A = new int[]{1,5,7,10,15,20,25,50,99};
		 a0 = new LeftistHeapNode(A[0]);
		 a1 = new LeftistHeapNode(A[1]);
		 a2 = new LeftistHeapNode(A[2]);
		 a3 = new LeftistHeapNode(A[3]);
		 a4 = new LeftistHeapNode(A[4]);
		 a5 = new LeftistHeapNode(A[5]);
		 a6 = new LeftistHeapNode(A[6]);
		 a7 = new LeftistHeapNode(A[7]);
		 LeftistHeapNode a8 = new LeftistHeapNode(A[8]);	
		 a0.left = a1; a0.npl = 2;		 
		 a0.right = a2;
		 a1.left = a3;  a1.npl = 1;
		 a1.right = a4;
		 a2.left = a5;  a2.npl = 1;
		 a2.right = a6;
		 a5.left = a7;  a5.npl =  1;
		 a5.right = a8;
		 h1 = new LeftistMinHeap(a0);
		 
		 B = new int[]{22,75};
		 b0 = new LeftistHeapNode(B[0]);
		 b1 = new LeftistHeapNode(B[1]);
		 b0.left = b1;
		 h2 = new LeftistMinHeap(b0);
		 
		 heap = LeftistMinHeap.merge(h1,h2); 
		 while((min = heap.extractMin()) != null){
			 System.out.format(" %d", min);
		 }
		 System.out.println();
	}

}
/*

测试输出：
 1 3 4 5 6 7 8 9 10 11
********************
 3 6 7 8 10 12 14 17 18 18 21 23 24 26 33 37
********************
 1 5 7 10 15 20 22 25 50 75 99*/