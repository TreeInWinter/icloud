package com.travelzen.datastructure.algorithm;
/*
(原题出自微软公司面试题)问题如下：
有两个序列a,b，大小都为n,序列元素的值任意整数，无序；
要求：通过交换a,b中的元素，使[序列a元素的和]与[序列b元素的和]之间的差最小。
例如:   
var a=[100,99,98,1,2, 3];
var b=[1, 2, 3, 4,5,40];
分析：
通过交换的方式，最终的状态是在保证两个序列中元素个数相同的条件下，任何一个元素都可以位于两个序列中的任何一个。这样问题可以转化为：在一个长 度为2*n的整数序列中，如何将元素个数分成两个子集，
记每个子集的元素之和分别为S1和S2，使得|S1-S2|最小。显然这是一个最优化问题，如果用 brute-force方法，组合数是C(2n,n)=(2n)!/(2*(n!)), 如果n很大这个方法不奏效。

这里采用回溯法(backtracking)，即前序(preorder)遍历状态空间树(state-space tree)。难点在于剪枝条件的确定，下面说明如何确定剪枝条件：
注意到如果将原序列按从小到大的顺序排好序，每次从较大的元素开始取，可以得到一个这样的规律：设长度为2*n序列的元素总和为Sigma，当前集 合元素的和为S，剩下的元素之和为Sigma-S，如果二者满足S>=Sigma-S，
即Sigma<=2*S，那么在当前集合中剩下需要添 加进来的元素必须从余下的元素中取最小的那些元素，这样才能保证|S1-S2|最小。这是因为如果在下一次任意从余下的元素中取的元素分别为e和f，
那么 取e后的两个子集差为(S+e) - (Sigma-S-e) = 2S-Sigma +2e，取f后的两个子集差为2S-Sigma +2f，显然如果e>f>0, 则有前者的子集差大于后者的子集差（注意这里假设元素都为非负整数，
原序列中有负数的情况参考下面的讨论）。
如果输入序列中有负整数，可以通过平移操作转化为非负，因为每个数都平移了，它们的差值保值不变。如果不平移，结果不一定正确，比如：输入的2*n 序列为：-10,5,3,20,25,50，
平衡的对半子集应该为[-10,5,50]和[3,20,25]，差值的绝对值为3。在下面的实现中，如果不 考虑平移，得到的错误结果却是[-10,3,50]和[5,20,25]，差值的绝对值为7。
另外在状态空间树只需要考虑根节点的左枝子树，因为原问题考虑的是对半子集。
?*/
import java.util.Arrays;
import java.util.Stack;
/**
 * 
 * @author ljs 
 * 2011-05-20
 * 平衡集合问题
 *
 */
public class BalancedSet {
    //the offset to eliminate negative integers
    int OFFSET;
    int[] A;
    //the total value of the two sets
    int sigma;
    //the number of elements in each set
    int N;
    //positive value
    int minDiff=Integer.MAX_VALUE;
    Stack<Integer> tracer = new Stack<Integer>();
    Stack<Integer> bestDiffStack = new Stack<Integer>();
     
     
    public BalancedSet(int[] A) throws Exception{
        this.A = A;
        this.init();        
    }
    private void init() throws Exception{
        if(A.length % 2 != 0)
            throw new Exception();
        N = A.length / 2;   
                 
        //sort A in ascending order
        Arrays.sort(A);
         
        //offset if possible
        if(A[0]<0){
            OFFSET = -A[0];
            for(int i=0;i<A.length;i++){
                A[i] += OFFSET;
            }
        }
        //sigma is the total value after offset is done
        for(int i=0;i<A.length;i++){
            sigma += A[i];
        }           
    }
     
    private void print(){
         
        System.out.format("best partition difference is: %d%n",minDiff);
         
        //caculate the difference of two sets
        int[] P = new int[N];
        int p=0;
        int i=0,j=bestDiffStack.size()-1;
         
        //note: bestDiffStack is in descending order, we need an ascending order to compare with A      
        for(;i<A.length && j>=0;){
            if(A[i]==bestDiffStack.get(j)){
                i++;
                j--;
            }else if(A[i] < bestDiffStack.get(j)){
                P[p++] = A[i++];
            }//else: impossible case                
        }
        if(i<A.length){
            P[p++] = A[i++];
        }
         
        System.out.println("One set is: ");
        while(!bestDiffStack.isEmpty())
            System.out.format(" %2d",bestDiffStack.pop()-OFFSET);       
        System.out.println();
        System.out.println("Another set is: ");
        for(p=0;p<N;p++){
            System.out.format(" %2d",P[p]-OFFSET);      
        }
    }
     
     
     
    public void solve(int[] A){
        //the first node is not needed to analyse the include=false case
        check(A.length-1, 0, 0, true);
        print();
    }
    //A is sorted in ascending order
    //count: the searched number of elements (<=N)
    //include: is the element i included in the set
    private void check(int i,int sum,int count,boolean include){        
        if(include){
            //record the node
            tracer.push(A[i]);
             
            sum += A[i];
            count++;
        }
        if(count==N){
            int diff = Math.abs(2*sum- sigma);
            if(diff < minDiff){
                minDiff = diff;             
                //record the best nodes until now
                bestDiffStack.clear();
                for(Integer k:tracer){
                    bestDiffStack.add(k);
                }
            }//else: just throw away this combination               
        }else{
            if(sigma<=2*sum){
                //prune the tree: choose the remaining least numbers
                int remainCount = N-count;
                for(int j=0;j<remainCount;j++){
                    sum += A[j];
                }
                int diff = Math.abs(2*sum- sigma);
                if(diff < minDiff){
                    minDiff = diff;
                    //record the nodes "1...remainCount"
                    bestDiffStack.clear();
                    for(Integer k:tracer){
                        bestDiffStack.add(k);
                    }
                    for(int j=remainCount-1;j>=0;j--){
                        bestDiffStack.push(A[j]);
                    }                   
                }//else: just throw away this combination                   
            }else{
                if(i>=1){
                    //traverse the next subtrees in the state-space tree
                    check(i-1,sum,count,true);
                    check(i-1,sum,count,false);
                }//else: the check is invalid                   
            }
        }
        if(include)
            //backtracking
            tracer.pop();
    }
     
    public static void main(String[] args) throws Exception {
        int A[] = {3,5,-10,20,25,50};       
        //int A[] = {3,5,10,20,25,50};      
        //int A[] = {100,99,98,1,2,3,1,2,3,4,5,40};
        BalancedSet bs = new BalancedSet(A);
        bs.solve(A);
    }
}
