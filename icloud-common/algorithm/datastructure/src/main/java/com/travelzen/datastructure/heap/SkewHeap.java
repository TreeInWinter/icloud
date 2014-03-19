package com.travelzen.datastructure.heap;

/*斜堆(Skew Heap)基于左倾堆的概念，也是一个用于快速合并的堆结构，但它可自我调整(self-adjusting)，每一个merge操作的平摊成本仍为 O(logN)，其中N为结点数，
 * 而且和左倾堆相比，每个结点没有npl属性，从而节省了空间。斜堆并不能保证左倾，但是每一个合并操作（也是采取右路合 并）同时需要无条件交换（而左倾堆中只是根据左右子树的npl值不符合要求时才交换）左右子树，
 * 让新合并的右子树变成左子树，原来的左子树变成新的右子树 （这有点类似于Splay Tree中的做法），从而可以达到平摊意义上的左倾效果。注意：一颗子树r和NULL子树合并时并不需要交换r子树的左右子树。

由于斜堆并不是严格的左倾堆，最坏的情况下右路长度可能为N，因此采用递归调用merge的风险是出现stack overflow。尽管如此，在下面的实现中还是提供了递归和非递归的两个版本。测试时使用的是非递归版本。

斜堆中除merge之外的其他操作跟左倾堆类似，具体情况参考以下代码，在此不再赘述。

实现：
?*/
import java.util.LinkedList;
import java.util.Stack;
 
  
 
/**
 * 
 * Skew Heap   
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-08-25
 *
 */
public class SkewHeap {
    static class Node{
        int key;
        Node left;
        Node right;
         
        public Node(int key){
            this.key = key; 
        }
         
        public String toString(){
            return String.valueOf(this.key);
        }
         
        public Node recursiveMerge(Node rhsRoot){
            if(rhsRoot==this || rhsRoot == null) {
                //Note: no need to swap the left and right children of this tree
                //Node tmp = this.left;
                //this.left = this.right;
                //this.right = tmp;
                return this;
            }
             
            Node root1 = null; //the root of the merged tree
            Node root2 = null; 
            if(rhsRoot.key<this.key){
                //merge this with rhsRoot's right child
                root1 = rhsRoot;
                root2 = this;
            }else{
                //merge rhsRoot with this's right child
                root1 = this;
                root2 = rhsRoot;                
            }
             
            Node tmpRoot = root2.recursiveMerge(root1.right);
            root1.right = root1.left;
            root1.left = tmpRoot;
            //or equivalently: we may first merge recursively, then swap left and right children.
            //Node right = root1.right;
            //root1.right = root1.left;          
            //root1.left = root2.merge(right);          
                         
            return root1;
        }
    }
    private Node root;
     
    public SkewHeap(Node root){
        this.root = root;
    }
     
    //version 1: recursive merge: not recommended because skew heap is 
    //unlike leftist heap to be deterministically leftist, so it can cause 
    //stack overflow in extreme cases.
    private static Node recursiveMerge(Node root1,Node root2){
        if(root1 == null) return root2;
        if(root2 == null) return root1;
        return root1.recursiveMerge(root2); 
    }   
 
    //version 2: non-recursive merge
    private static Node iterativeMerge(Node root1,Node root2){
        if(root1 == null) return root2;
        if(root2 == null) return root1;
         
        Stack<Node> stack = new Stack<Node>();
        Node r1 = root1;
        Node r2 = root2;
        while(r1 != null && r2 != null){
            if(r1.key<r2.key){
                stack.push(r1);
                r1 = r1.right;
            }else{
                stack.push(r2);
                r2 = r2.right;
            }           
        }
        //at this point, exactly one of r1 and r2 is null
        //Again we don't need to swap the left and right children of r
        Node r = (r1 != null)?r1:r2;
         
        //merge
        while(!stack.isEmpty()){
            Node node = stack.pop();            
            node.right = node.left;
            node.left = r;
            r = node;
        }
        return r;
    }
     
    public static SkewHeap merge(SkewHeap h1,SkewHeap h2){
        Node rootNode = iterativeMerge(h1.root,h2.root);
        return new SkewHeap(rootNode);
    }      
     
    public static SkewHeap buildHeap(int[] A){
        LinkedList<Node> queue = new LinkedList<Node>();
        int n = A.length;
        //init: queue all elements as a single-node tree
        for(int i=0;i<n;i++){
            Node node = new Node(A[i]);
            queue.add(node);
        }
        //merge adjacent heaps and enqueue the merged heap afterward
        while(queue.size()>1){
            Node root1 = queue.remove(); //dequeue
            Node root2 = queue.remove();
            Node rootNode = iterativeMerge(root1,root2);
            queue.add(rootNode);
        }
        Node rootNode = queue.remove();
        return new SkewHeap(rootNode);
    }
    public void insert(int x){
        this.root = SkewHeap.iterativeMerge(new Node(x), this.root);
    }
     
    public Integer extractMin(){
        if(this.root == null) return null;
         
        int min = this.root.key;
        this.root = SkewHeap.iterativeMerge(this.root.left, this.root.right);
        return min;
    }
      
     
    public static void main(String[] args) {
        int[] A = new int[]{4,8,10,9,1,3,5,6,11};
          
         SkewHeap heap = SkewHeap.buildHeap(A);
         heap.insert(7);
         Integer min = null;
         while((min = heap.extractMin()) != null){
             System.out.format(" %d", min);
         }
         System.out.println();
          
         System.out.println("********************");
         A = new int[]{3,10,8,21,14,17,23,26};
          
          
         Node a0 = new Node(A[0]);
         Node a1 = new Node(A[1]);
         Node a2 = new Node(A[2]);
         Node a3 = new Node(A[3]);
         Node a4 = new Node(A[4]);
         Node a5 = new Node(A[5]);
         Node a6 = new Node(A[6]);
         Node a7 = new Node(A[7]);       
         a0.left = a1;   
         a0.right = a2; 
         a1.left = a3;   
         a1.right = a4;
         a4.left = a6;
         a2.left = a5;
         a5.left = a7;
         SkewHeap h1 = new SkewHeap(a0);
          
         int[] B = new int[]{6,12,7,18,24,37,18,33};
         Node b0 = new Node(B[0]);
         Node b1 = new Node(B[1]);
         Node b2 = new Node(B[2]);
         Node b3 = new Node(B[3]);
         Node b4 = new Node(B[4]);
         Node b5 = new Node(B[5]);
         Node b6 = new Node(B[6]);
         Node b7 = new Node(B[7]);
         b0.left = b1;  
         b0.right = b2;
         b1.left = b3;  
         b1.right = b4; 
         b4.left = b7;
         b2.left = b5;  
         b2.right = b6;
         SkewHeap h2 = new SkewHeap(b0);
          
         heap = SkewHeap.merge(h1,h2);
         while((min = heap.extractMin()) != null){
             System.out.format(" %d", min);
         }
         System.out.println(); 
          
         System.out.println("********************");
         A = new int[]{1,4,23,63,24,44,28};
         a0 = new Node(A[0]);
         a1 = new Node(A[1]);
         a2 = new Node(A[2]);
         a3 = new Node(A[3]);
         a4 = new Node(A[4]);
         a5 = new Node(A[5]);
         a6 = new Node(A[6]); 
         a0.left = a1;   
         a0.right = a2;
         a1.left = a3;   
         a1.right = a4;
         a2.left = a5;  
         a2.right = a6;
         h1 = new SkewHeap(a0);
          
         B = new int[]{13,17,99,57,49,105,201};
         b0 = new Node(B[0]);
         b1 = new Node(B[1]);
         b2 = new Node(B[2]);
         b3 = new Node(B[3]);
         b4 = new Node(B[4]);
         b5 = new Node(B[5]);
         b6 = new Node(B[6]);
         b0.left = b1;
         b0.right = b2;
         b1.left = b3;
         b1.right = b4;
         b2.left = b5;
         b2.right = b6;
         h2 = new SkewHeap(b0);
          
         heap = SkewHeap.merge(h1,h2); 
         while((min = heap.extractMin()) != null){
             System.out.format(" %d", min);
         }
         System.out.println();
    }
 
}

/*测试输出：
 1 3 4 5 6 7 8 9 10 11
********************
 3 6 7 8 10 12 14 17 18 18 21 23 24 26 33 37
********************
 1 4 13 17 23 24 28 44 49 57 63 99 105 201

参考资料：
Sleator & Tarjan (1986). "Self-Adjusting Heaps"*/