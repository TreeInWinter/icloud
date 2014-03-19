package com.travelzen.datastructure.tree;
/*树状数组(BIT - Binary Indexed Tree)是一个用数组表示的树型数据结构，最早用于频次累计表中。树状数组中每个元素维护一个频次表A的特定部分区段的累加和。假设输入动态序列为 A，A中元素值可以被修改，如果使用直接蛮力法来查询一个区间i..j的累加和，复杂度为O(n)，而树状数组由于每个元素存储的是部分累加和，通过数组 下标的二进制索引规律可以在O(logn)时间内找到1...j或i...j的累加和。存储空间只需要维护树状数组T，A的元素值可以很容易地从T得到 （即特例i=j时的区间查询）。
数组A下标通常从0开始，而树状数组的有效下标是从1开始。下面关于lowbit的讨论假设数组A下标从1开始。
树 状数组中元素在树型结构中的位置是根据数组下标的lowbit函数值2^r来确定的。定义每一个元素T[i]的值等于A[i-2^r + 1] + ... + A[i]，即T[i]表示共2^r个元素的部分累加和，或者说T[i]元素管辖区段从i开始往前推2^r个元素。2^r的计算方法很简单，就是i & (-i)，原理是利用负数补码等于相应正数值取反加一，例如当i=6时，对应的二进制数是0110，取反得到1001，加一得到1010，从而 2^r=lowbit(i)=0110 & 1010 = 10，即2^r等于十进制值2，或者说T[6]管辖区段长度为2, 即T[6] = A[5] + A[6]。
有了lowbit值，可以高效地查询数列A从1到i的区间所有元素的累加和：即将若干个T[j]累计即可 （j>=1），设T[j]管辖的区段长度为lowbit(j)，那么T[j-1]管辖的区段的最高下标值可以通过j = j - lowbit(j)得到；也可以低代价更新数列A的某个元素，这时需要更新受影响的T中相应元素，在树型结构中，某一个结点的父结点就是受影响的结点之 一，而T[j]的父结点管辖的区段的最高下标值可以通过j = j + lowbit(j)得到 （j<=n）。查询区间可以用差分方法: 如果i<=j，那么sum(A[i..j]) = query(j) - query(i-1)。
实现：
?*/
/**
 * 
 * Binary Indexed Tree (BIT)
 * Peter M. Fenwick: A New Data Structure for Cumulative Frequency Tables
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-08-09
 * 
 */
public class BinaryIndexTree {
    private int[] T;
 
    public BinaryIndexTree(int n) {
        //each element in T is initialized to 0
        T = new int[n + 1];
    }
     
    private int lowbit(int idx){
        return idx & (-idx);
    }
 
    //idx starts with 1, not 0
    public void update(int idx, int val) {
        while (idx < T.length) {
            T[idx] += val;
            idx += lowbit(idx);
        }
    }
    //idx starts with 1, not 0
    public int query(int idx) {
        int sum = 0;
        while (idx > 0) {
            sum += T[idx];
            idx -= lowbit(idx);
        }
        return sum;
    }
    //get the sum from A[i] to A[j] inclusively
    public int queryInterval(int i,int j){
        if(j<i){
            //swap  
            int tmp=j;j=i;i=tmp;
        }
        return query(j)-query(i-1);
    }
 
    private void report(){
        int n = T.length - 1;
        //report
        System.out.print("A from tree=");
        for(int i=1;i<=n;i++){
            int ai = query(i)-query(i-1);
            System.out.format(" %d",ai);
        }
        System.out.println();
         
        //tree
        System.out.format("T[1..%d]=",n);
        for(int j=1;j<=n;j++){
            System.out.format(" %d",T[j]);           
        }
        System.out.println();
         
        //query
        System.out.print("sums = ");
        for(int j=1;j<=n;j++){
            int r = this.query(j);
            System.out.format(" %d",r);          
        }
        System.out.println();
         
         
         
    }
    public static void main(String[] args) throws Exception {
        int[] A = new int[]{2,3,4,5,6};
        int n = A.length;
        BinaryIndexTree bit = new BinaryIndexTree(n);
         
        //update all elements
        for(int i=0,j=1;i<n;i++,j++){
            bit.update(j, A[i]);
        }
        //update a single element
        int val = 4;
        int idx = 3;
        A[idx-1] += val;
        bit.update(idx, val);
         
        System.out.format("A[0..%d] =  ",n-1);
        for(int i=0;i<n;i++){
            System.out.format(" %d",A[i]);
        }
        System.out.println();
         
        bit.report();
         
        //sum query
        int q=3;
        int r = bit.query(q);
        System.out.format("sum A[%d..%d]: %d%n", 0,q-1,r);
         
        //interval query
        int p = 2;
        q=4;
        r = bit.queryInterval(p, q);
        System.out.format("sum A[%d..%d]: %d%n", p-1,q-1,r);
         
        System.out.println("*****************");
        A = new int[]{1,0,2,1,1,3,0,4,2,5,2,2,3,1,0,2};
        n = A.length;
        bit = new BinaryIndexTree(n);
         
        //update all elements
        for(int i=0,j=1;i<n;i++,j++){
            bit.update(j, A[i]);
        }
        System.out.format("A[0..%d] =  ",n-1);
        for(int i=0;i<n;i++){
            System.out.format(" %d",A[i]);
        }
        System.out.println();
         
        bit.report();
    }
}
/*输出：
A[0..4] =   2 3 8 5 6
A from tree= 2 3 8 5 6
T[1..5]= 2 5 8 18 6
sums =  2 5 13 18 24
sum A[0..2]: 13
sum A[1..3]: 16
*****************
A[0..15] =   1 0 2 1 1 3 0 4 2 5 2 2 3 1 0 2
A from tree= 1 0 2 1 1 3 0 4 2 5 2 2 3 1 0 2
T[1..16]= 1 1 2 4 1 4 0 12 2 7 2 11 3 4 0 29
sums =  1 1 3 4 5 8 8 12 14 19 21 23 26 27 27 29


参考资料：
Peter M. Fenwick (1994): A New Data Structure for Cumulative Frequency Tables
TopCoder - Binary Indexed Trees
NOCOW-树状数组*/
