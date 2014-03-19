package com.travelzen.datastructure.heap;

/*斐波那契堆的实现和比较（相对二项堆）
Fibonacci Heap（简称F-Heap）是一种基于二项堆的非常灵活的数据结构。它与二项堆不同的地方在于：
1）root list和任何结点的child list使用双向循环链表，而且这些lists中的结点不再有先后次序（Binomial Heap中root list的根结点按degree从小到大顺序，child list的结点按degree从大到小顺序）；

2）二项堆中任何一颗Binomial Tree中根结点的degree是最大的，而F-Heap中由于decrease-key操作(cut和cascading cut)的缘故，并不能保证根结点的degree最大；

3） 二项堆中任何结点（degree等于k的）为根的子树中，结点总数为2^k；F-Heap中相应的结点总数下界为F{k+2}，
上界为2^k（如果没有 Extract-Min和Delete两类操作的话）。其中F{k+2}表示Fibonacci数列（即0,1,1,2,3,5,8,11...）中第 k+2个Fibonacci数，
第0个Fibonacci数为0，第1个Fibonacci数为1。注意不像二项堆由二项树组成那样，F-Heap的 root list中的每棵树并不是Fibonacci树（Fibonacci树属于AVL树），
而F-Heap名称的由来只是因为Fibonacci数是结点个数 的一个下界值。


4）基于上面的区别，若F-Heap中结点总数为n，那么其中任何结点（包括非根结点）的degree最大值不超过 D(n) = floor(lgn/lg1.618)，这里1.618表示黄金分割率(goldren ratio)，即方程x^2=x+1的一个解。
所以在Extract-Min的consolidate操作之后，root list中的结点最多有D(n)+1。而二项堆中degree最大值不超过floor(lgn)，从而root list中最多有floor(lgn)+1颗二项树。


5）另外一个与二项堆的最大不同之处在于：F-Heap是一种具有平摊意义上的高性能 数据结构。除了Extract-Min和Delete两类操作具有平摊复杂度O(lgn)，
其他的操作(insert，union，find- min，decrease-key）的平摊复杂度都是常数级。因此如果有一系列的操作，其中Extract-min和delete操作个数为p，
其他操作 个数为q，p<q，那么总的平摊复杂度为O(p + q.lgn)。达到这个复杂度的原因有以下几点，第一，root list和任何结点的child list中使用了双向循环链表；第二，union和insert操作的延迟合并，
从而在所有的可合并堆中，F-heap的合并开销O(1)最小的；第 三，decrease-key中cut和cascading cut的巧妙处理（即任何非根结点最多失去一个孩子）。


以下是基于CLRS第三版的伪代码的Fibonacci Heap的实现。以下几点值得注意一下：
1）Decrease-Key操作中通过添加变量cascade消除CLRS中Cascading Cut函数的tail recursion;
2）Extract-Min的consolidate函数中每处理一个root结点就将该结点从root list中删除，然后在寻找新的min结点时按照degree从小到大次序（实际上在F-Heap中不用关心节点之间的相对顺序）恢复root list；
3）consolidate函数中的link操作和二项堆中的操作类似，只不过这里不用考虑child list中结点的顺序；
4）Extract- Min中涉及mark属性的代码：在Extract-Min中将min的所有孩子结点添加到root list时不需要清除mark属性，等到consolidate的link操作以及寻找新的min结点时再分别设置：linked child的mark = false，root的mark=false。
?*/


/**
 * 
 * Fibonacci Heap   
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-09-05
 *
 */
public class FibonacciHeap {
    static class Node{
        private int key;
        private Node parent=null;
         
        private int degree=0;
        private Node child=null;
         
        private Node left;
        private Node sibling;
         
        private boolean mark=false;
          
        public Node(int key){
            this.key = key;
        }
        public String toString(){
            return this.key + "(degree=" + this.degree + ",mark=" + this.mark + ")";
        }
    }
    //the head of root list
    private Node min;
    private int n;
     
    //insert a single node
    public void insert(Node node){
        addToRootList(node);
        n++;    
    }
     
    private void addToRootList(Node node){          
        if(min==null){                  
            node.left=node.sibling=node;
             
            min = node;
        }else{
            //insert node in root list as node's left neighbor
            Node prev = min.left;
             
            prev.sibling = node;
            node.sibling = min;
             
            min.left = node;
            node.left = prev;
             
            if(node.key<min.key)
                min = node;
        }           
    }
     
    public Node findMin(){
        return this.min;
    }
     
    //rhs is the min node of H'
    public void union(FibonacciHeap h){
        Node rhs = h.min;
        if(rhs==null) return;
         
        //concatenate the root list of both heaps: insert rhs as the right neighbor of min
        if(this.min == null){
            this.min =  rhs;
        }else{
            Node nextH1 = min.sibling;
            Node lastH2 = rhs.left;
             
            min.sibling = rhs;          
            lastH2.sibling = nextH1;
             
            nextH1.left = lastH2;
            rhs.left = min;
             
            if(this.min.key>rhs.key){
                min = rhs;
            }
        }
        n += h.n;
    }
     
    public Node extractMin(){
        Node z = this.min;
        if(z!=null){
            //insert z's children in the root list
            Node x = z.child;           
            while(x!=null){
                Node next = x.sibling;
                this.addToRootList(x);
                x.parent = null;
                //x.mark = false; //delay updating mark until linking and finding new min
                x = (next != z.child)?next:null;
            }
             
            //remove z from root list
            n--;
             
            Node prev = z.left;         
            if(prev==z){
                //z is the only node in root list
                min = null;
            }else{
                Node next = z.sibling;
                prev.sibling = next;
                next.left = prev;
                //set min to a temporary value as an entry pointer of the heap, 
                //later we will update it
                this.min = next;
                consolidate();
            }           
        }
        return z;
    }
     
    private void consolidate(){
        double goldratio = (Math.sqrt(5)+1)/2.0;
        int maxdegree = (int)(Math.log(n) / Math.log(goldratio));
         
        Node[] A = new Node[maxdegree+1];
        //iterate all nodes in root list
        Node w = this.min;
        while(w!=null){
            Node prev = w.left;
            Node next = w.sibling;
             
            Node x = w;
            int d = x.degree;
            while(A[d] != null){
                Node y = A[d];
                //link x and y
                if(x.key<y.key){ //y is added as a child of x
                    x = link(x,y);                  
                }else{//x is added as a child of y                  
                    x = link(y,x);
                }               
                A[d] = null;
                d++; //when two trees with same degree are linked, the degree increases by 1.
            }
            A[d] = x;
             
            //remove w from root list
            if(prev == w){
                //w is the only node in root list
                w = null; //set condition to exit while
            }else{              
                prev.sibling = next;
                next.left = prev;
                w = next;
            }
        }
        min = null;
         
        //reconstruct root list from A[]
        for(int d=0;d<=maxdegree;d++){
            if(A[d] != null){
                A[d].mark = false; //root node is unmarked
                addToRootList(A[d]);                
            }
        }
    }
    private Node link(Node small,Node large){
        Node child = small.child;
        if(child==null){
            small.child = large;    
            large.left = large.sibling = large;
        }else{
            //make large node as a child of small node: insert large as a left neighbor of small.child          
            Node childPrev = child.left;         
             
            childPrev.sibling = large;
            large.sibling = child;
             
            child.left = large;
            large.left = childPrev;         
        }
        small.degree++;
        large.parent = small;       
        large.mark = false; //large is a child of small, so its mark is reset
        return small;
    }
     
 
    public void decreaseKey(Node x,int k) throws Exception{
        if(x.key<k) throw new Exception("key is not decreased!");
         
        x.key = k;
        Node p = x.parent;
        boolean cascade = false;
        while(p!=null){
            if(cascade || x.key<p.key){
                cut(x,p);               
                if(p.mark){ //cascading cut 
                    x = p;
                    p = x.parent;
                    cascade = true;
                }else{
                    if(p.parent != null){ //p is not root
                        p.mark = true;
                    }
                    break;
                }
            }
        }
        //if p is null, then x is root. 
        //Root node doesn't change when decreased key(thanks to the above exception checking).
         
        //update min: only x's key is changed, other's roots created via cascading cut has no effect on min
        if(k<this.min.key){
            min = x;
        }
    }
 
    private void cut(Node x,Node parent){       
        if(x == x.sibling){ //x is the only child of parent
            parent.child=null;
        }else{
            Node prev = x.left;
            Node next = x.sibling;
            //remove x from child list
            prev.sibling = next;
            next.left = prev;
             
            parent.child=next;
        }
        parent.degree--;
        x.parent = null;
        x.mark = false;
         
        //add x to root list
        addToRootList(x);
    }
    public void delete(Node x) throws Exception{
        decreaseKey(x,Integer.MIN_VALUE);
        this.extractMin();
    }
     
     
    public void print(){
        System.out.format("F-Heap(n=%d):%n",this.n);
        Node h = this.min;
        while(h!=null){
            this.print(0, h);
            h = (h.sibling!=min)?h.sibling:null;
        }
    }
     
    private void print(int level, Node node){
        for (int i = 0; i < level; i++) {
            System.out.format(" ");
        }
        System.out.format("|");
        for (int i = 0; i < level; i++) {
            System.out.format("-");
        }
        //boolean isChildLink = (node.parent!=null && node.parent.child == node);        
        System.out.format("%d%s%n", node.key,node.mark?"(x)":"");
        Node child = node.child;
        while(child!=null){         
            print(level + 1, child);
            child = (child.sibling!=node.child)?child.sibling:null;
        }           
    }
     
    public static void main(String[] args) throws Exception {
        //heap1
        FibonacciHeap fheap1 = new FibonacciHeap();
        Node node5=null,node14=null,node15=null;
        for(int i=0;i<17;i++){
            Node node = new Node(i);
            fheap1.insert(node);
            if(i==5) node5=node;
            if(i==14) node14=node;
            if(i==15) node15=node;
        }
        fheap1.extractMin();        
        //fheap1.print();       
         
        fheap1.decreaseKey(node14, 9);
        fheap1.decreaseKey(node15, 8); //cascading cut
        //fheap1.print();
                 
        fheap1.delete(node5);
        fheap1.print();
         
        Node min=fheap1.findMin();
        System.out.format("min:%d%n", min.key);
         
         
        //heap2
        System.out.println("");
        FibonacciHeap fheap2 = new FibonacciHeap();
        int[] B = new int[]{40,39,20,18,41,38,52,3};
        Node node52=null,node41=null;
        for(int i=0;i<B.length;i++){
            Node node = new Node(B[i]);
            fheap2.insert(node);
            if(i==6) node52 = node;
            if(i==4) node41 = node;
        }
        fheap2.insert(new Node(0));
        fheap2.extractMin();
        //fheap2.print();
         
        fheap2.decreaseKey(node52, 7);
        //fheap2.print();
        fheap2.decreaseKey(node41, 21);
        //fheap2.print();
         
        fheap2.extractMin();
        fheap2.print();
         
        //union heap1 and heap2 
        fheap1.union(fheap2);
        fheap1.print();
        min = fheap1.extractMin();
        while(min!=null){
            System.out.format("%d ",min.key);
            min = fheap1.extractMin();      
        }
    }
 
}
/*测试输出：
F-Heap(n=15):
|1
 |-9(x)
  |--10
  |--11
   |---12
 |-2
 |-3
  |--4
|6
|7
 |-8
|8
 |-16
 |-9
  |--13
min:1

F-Heap(n=7):
|7
 |-21
 |-20
  |--39
|18
|38
 |-40(x)
F-Heap(n=22):
|1
 |-9(x)
  |--10
  |--11
   |---12
 |-2
 |-3
  |--4
|7
 |-21
 |-20
  |--39
|18
|38
 |-40(x)
|6
|7
 |-8
|8
 |-16
 |-9
  |--13
1 2 3 4 6 7 7 8 8 9 9 10 11 12 13 16 18 20 21 38 39 40*/