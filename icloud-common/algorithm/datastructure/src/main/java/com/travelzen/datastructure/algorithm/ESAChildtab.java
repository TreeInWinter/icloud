package com.travelzen.datastructure.algorithm;
/*
相比后缀树，后缀数组的优势是存储空间小，相关算法效率高。但是若存放childtab还是使用up，down和nextLIndex三个属性，这显然不 符合后缀数组节省空间的"第一原则"。幸运的是，可以压缩存储childtab，将三个属性up，down和nextLIndex变成一个一维数组。

后缀数组childtab压缩存储的基本思路是保留所有的nextLIndex值（因为nextLIndex没有冗余），将大部分冗余的down值剔除（剩下部分的down值存放到空白的nextLIndex位置中），然后将up值放到空白的nextLIndex位置。
首先给出lcp-interval和childtab的定义：
lcp区间的定义：
lcp-interval[i..j] (0<=i<j<=n)的lcp值为l，那么满足：
1) lcptab[i]<l,
2) lcptab[k]>=l for all k with i+1<=k<=j,
3) lcptab[k]=l for at least one k with i+1<=k<=j,
4) lcptab[j+1]<l
其中满足3)的k是区间的一个l-index。
childtab定义：

********************如何压缩存储down值？***************************
对任何一个childtab[i].down值，它一定等于某个lcp-interval t1-[i..j]的第一个l-index。根据t1是否有右兄弟区间，可以分以下两种情况来考虑如何压缩存储childtab[i].down值：

case 1) 如果t1有右兄弟t2-[p..q]，即p=j+1，那么根据up的定义，childtab[p+1].up等于childtab[i].down；因此 childtab[i].down是冗余的：可以用childtab[p+1].up代替。所以这种情况下的做法是：不存储 childtab[i].down，如果要找t1的第一个l-index，可以通过childtab[p+1].up得到。

另外，因为t1有右兄弟，所以childtab[i].nextLIndex有值而且等于p。

case 2) 如果t1没有右兄弟，那么childtab[i].down是没有对应的up值，因此不再冗余。另外，因为t1没有右兄弟，所以childtab[i].nextLIndex没有值。

所以这种情况下的做法是：需要存储childtab[i].down值，将这个down值存入childtab[i].nextLIndex位置。

具 体实现时可以这样做：先在第一个for循环中将所有的childtab[i].down值（包括上面case 1和case 2的down值）放到childtab[i].nextLIndex位置（childtab[i].nextLIndex实际上是一个一维数组），然后在 第二个for循环中用childtab[i].nextLIndex值覆盖上面第一种情况下的childtab[i].down值（这是因为case 1中一定存在childtab[i].nextLIndex值用来覆盖; case 2中没有对应位置的nextLIndex值，所以可以确保childtab[i].down值不被覆盖）。

********************如何压缩存储up值？***************************
对 任何一个childtab[j+1].up值，它一定等于某个lcp-interval t1-[i..j]的第一个l-index(即lcptab[childtab[j+1].up]==t1)。因此根据上面lcp-interval的定 义第2)和第4)条，lcptab[j+1]<t1<=lcptab[j]。因此根据childtab[j]的next l-index定义（即使能找到一个最小的q满足lcptab[q]=lcptab[j]，但是当k等于j+1时，不满足lcptab[k]> lcptab[q]=lcptab[j]），childtab[j].nextLIndex没有值。而且childtab[j].down也没有值，这也 是根据down值的定义得出的：即使能找到一个最大的q满足lcptab[q]>lcptab[j]，但是当k等于j+1时，不满足 lcptab[k]>lcptab[q]>lcptab[j]。

由于上述推导得出如果 childtab[j+1].up有值，那么childtab[j].nextLIndex和childtab[j].down都没有值。因此可以将 childtab[j+1].up值存放到childtab[j].nextLIndex位置，可以确保up值不会被down和nextLIndex值覆 盖。
*********************如何区分up，down和nextLIndex值？*********************************
现在三个值共用一个数组childtab，如何区分每一个元素是up，还是down还是nextLIndex值？
对于压缩存储的数组元素childtab[i]，
1）如果lcptab[i]>lcptab[i+1]，那么childtab[i]存储的是childtab[i+1].up值；
理 由是：如果lcptab[j]>lcptab[j+1]，那么根据nextLIndex和down定 义，childtab[j].nextLIndex和childtab[j].down都没有值（题外话:单从 lcptab[j]>lcptab[j+1],可以肯定lcptab[j+1].up一定有值，因为lcptab[j+1].up至少可以等于j， 但是还可能有更小的q值）。 所以存储的一定是childtab[j+1].up值。
2）如果lcptab[childtab[i]] > lcptab[i]，那么childtab[i]存储的是childtab[i].down值；
证明（用反正法）：若childtab[i]存储的不是childtab[i].down值，那么childtab[i]存储的值只有childtab[i+1].up和childtab[i].nextLIndex。分以下两种情况讨论：
case 1) 如果是childtab[i].nextLIndex，那么根据nextLIndex定义：lcptab[i] = lcptab[childtab[i]]，与假设矛盾。所以存储的不是childtab[i].nextLIndex；
case 2) 如果childtab[i]存储的是childtab[i+1].up，因为i是i+1左边相邻的一个后缀，所以childtab[i+1].up<=i：若childtab[i+1].up=i，有lcptab[childtab[i+1].up]=lcptab[childtab[i]]=lcptab[i]，与假设矛盾；若childtab[i+1].up<i，那么根据up定义(i位于q和i+1之间)，有lcptab[childtab[i]]=lcptab[childtab[i+1].up]<=lcptab[i]，也与假设矛盾。
所以可以确信, 如果lcptab[childtab[i]] > lcptab[i]，那么childtab[i]存储的一定是childtab[i].down值。
3）如果上述两种情况都不是，那么childtab[i]存储的是i的next l-index值。
*********************实现*********************************
因 为最后一个后缀既没有down值，也没有nextLIndex值，而且下一个i不存在更没有下一个i的up值，所以childtab[len- 1]=-1，这样方法getLIndexValue可以简化: 只需判断一个分支；另外，因为冗余的down值已经被剔除，因此在获取某个lcp-interval的第一个l-index时，应该先用 childtab[j+1].up值判断是否在i..j范围之内，如果不在i..j范围之内，再用childtab[i].down值判断，大多数情况下 up值的判断能得出正确结果。
以下实现以top-down遍历作为childtab的一个应用：
?*/
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
  
/**
 * 
 * 
 * Suffix Array: space-efficient childtab derivation from lcptab
 * (The suffix array is constructed with DC3 algorithm)
 * 
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-07-26
 *
 */
public class ESAChildtab {
    private String text;
    private int[] lcptable;
    private int[] childtab;
    private int len;
     
    public ESAChildtab(String text){
        this.text = text;
        this.len = text.length();
         
        childtab=new int[len];  
        for(int i=0;i<len;i++){
            childtab[i] = -1;
        }
    }
     
    public ESAChildtab(int len){    
        this.len = len;
         
        childtab=new int[len];  
        for(int i=0;i<len;i++){
            childtab[i] = -1;
        }
    }
     
    public static final char MAX_CHAR = '\u00FF';
 
    class Suffix{
        int[] sa;  
        //Note: the p-th suffix in sa: SA[rank[p]-1]];
        //p is the index of the array "rank", start with 0;
        //a text S's p-th suffix is S[p..n], n=S.length-1.
        int[] rank; 
        boolean done;
          
        public Suffix(int[] sa,int[] rank){
            this.sa = sa;
            this.rank = rank;
        }
    }
     
 
    //a prefix of suffix[isuffix] represented with digits
    class Tuple{
        int isuffix; //the p-th suffix
        int[] digits;
        public Tuple(int suffix,int[] digits){
            this.isuffix = suffix;
            this.digits = digits;           
        }
        public String toString(){
            StringBuffer sb = new StringBuffer();           
            sb.append(isuffix);
            sb.append("(");
            for(int i=0;i<digits.length;i++){
                sb.append(digits[i]);
                if(i<digits.length-1)
                    sb.append("-");
            }
            sb.append(")");
            return sb.toString();
        }
    }
     
    //d: the digit to do countingsort
    //max: A value's range is 0...max
    private void countingSort(int d,Tuple[] tA,Tuple[] tB,int max){
        //init the counter array
        int[] C = new int[max+1];
        for(int i=0;i<=max;i++){
            C[i] = 0;
        }
        //stat the count
        for(int j=0;j<tA.length;j++){
            C[tA[j].digits[d]]++;
        }
        //process the counter array C
        for(int i=1;i<=max;i++){
            C[i]+=C[i-1];
        }
        //distribute the values  
        for(int j=tA.length-1;j>=0;j--){
            //C[A[j]] <= A.length 
            tB[--C[tA[j].digits[d]]]=tA[j];         
        }
    }
     
    //tA: input
    //tB: output for rank caculation
    private void radixSort(Tuple[] tA,Tuple[] tB,int max,int digitsLen){
        int len = tA.length;
        int digitsTotalLen = tA[0].digits.length;
             
        for(int d=digitsTotalLen-1,j=0;j<digitsLen;d--,j++){
            this.countingSort(d, tA, tB, max);
            //assign tB to tA
            if(j<digitsLen-1){
                for(int i=0;i<len;i++){
                    tA[i] = tB[i];
                }       
            }
        }
    }
     
    //max is the maximum value in any digit of TA.digits[], used for counting sort
    //tA: input
    //tB: the place holder, reused between iterations
    private Suffix rank(Tuple[] tA,Tuple[] tB,int max,int digitsLen){       
        int len = tA.length;        
        radixSort(tA,tB,max,digitsLen); 
         
        int digitsTotalLen = tA[0].digits.length;
         
        //caculate rank and sa  
        int[] sa = new int[len];
        sa[0] = tB[0].isuffix;  
         
        int[] rank = new int[len+2]; //add 2 for sentinel   
        rank[len]=1;rank[len+1] = 1;
        int r = 1; //rank starts with 1
        rank[tB[0].isuffix] = r;        
        for(int i=1;i<len;i++){
            sa[i] = tB[i].isuffix;  
             
            boolean equalLast = true;
            for(int j=digitsTotalLen-digitsLen;j<digitsTotalLen;j++){
                if(tB[i].digits[j]!=tB[i-1].digits[j]){
                    equalLast = false;
                    break;
                }
            }
            if(!equalLast){
                r++;
            }
            rank[tB[i].isuffix] = r;    
        }
                  
        Suffix suffix = new Suffix(sa,rank);
        //judge if we are done
        if(r==len){
            suffix.done = true;
        }else{
            suffix.done = false;
        }
        return suffix;
         
    }
     
    private int[] orderSuffixes(Tuple[] tA,Tuple[] tB,int max,int digitsLen){       
        int len = tA.length;        
        radixSort(tA,tB,max,digitsLen);         
        //caculate rank and sa  
        int[] sa = new int[len];
        for(int i=0;i<len;i++){
            sa[i] = tB[i].isuffix;              
        }
        return sa;       
    }
     
    //rank needs sentinel: len+2
    private Suffix reduce(int[] rank,int max){
        int len = rank.length - 2;
         
        int n1 = (len+1)/3;
        int n2 = len/3;
        Tuple[] tA = new Tuple[n1+n2];
        Tuple[] tB = new Tuple[n1+n2];
         
        for(int i=0,j=1;i<n1;i++,j+=3){
            int r1 =  rank[j];
            int r2 =  rank[j+1];
            int r3 =  rank[j+2];
            tA[i] = new Tuple(i,new int[]{r1,r2,r3});
        }
        for(int i=n1,j=2;i<n1+n2;i++,j+=3){
            int r1 =  rank[j];
            int r2 =  rank[j+1];
            int r3 =  rank[j+2];     
            tA[i] = new Tuple(i,new int[]{r1,r2,r3});
        }
          
        return rank(tA,tB,max,3);       
    }
     
     
    private int[] skew(int[] rank,int max){
        int len = rank.length - 2;
         
        //step 1: caculate sa12
        Suffix suffixT12 = reduce(rank,max);
          
         
        int[] sa12 = null;
        if(!suffixT12.done){
            int[] rankT12 = suffixT12.rank;
            int maxT12 = rankT12[suffixT12.sa[suffixT12.sa.length-1]];
            sa12 = skew(rankT12,maxT12);
            // debug for string: GACCCACCACC#
            //s12 = new Suffix();
            //s12.rank = new int[]{3,6,5,4,7,2,1,1,1};
            //s12.sa = new int[]{7,6,5,0,3,2,1,4};
            //s12.done =true;                       
        }else{
            sa12 = suffixT12.sa;            
        }
         
        //index conversion for sa12
        int n1 = (len+1)/3;
        for(int j=0;j<sa12.length;j++){
            if(sa12[j]<n1){
                sa12[j] = 1 + 3*sa12[j];
            }else{
                sa12[j] = 2 + 3*(sa12[j]-n1);
            }               
        }
        //recaculate rank for sa12
        int[] rank12 = new int[len+2];
        rank12[len] = 1;rank12[len+1] = 1;
        for(int k=0;k<sa12.length;k++){
            rank12[sa12[k]] = k+1;
        }
          
           
         
        //step 2: caculate sa0      
        int n0=(len+2)/3;
        Tuple[] tA = new Tuple[n0];
        Tuple[] tB = new Tuple[n0];
        for(int i=0,j=0;i<n0;i++,j+=3){
            int r1 =  rank[j];
            int r2 =  rank12[j+1]; 
            tA[i] = new Tuple(i,new int[]{r1,r2});
        }
        int max12 = rank12[sa12[sa12.length-1]];        
        int[] sa0 = orderSuffixes(tA,tB,max<max12?max12:max,2);
        //index conversion for sa0
        for(int j=0;j<n0;j++){
            sa0[j] = 3*sa0[j];                  
        }        
         
        //step 3: merge sa12 and sa0
        int[] sa = new int[len];
        int i=0,j=0;
        int k=0;
        while(i<sa12.length && j<sa0.length){
            int p = sa12[i];
            int q = sa0[j];
            if(p%3==1){
                //case 1
                if(rank[p]<rank[q]){
                    sa[k++] = p;i++;
                }else if(rank[p]>rank[q]){
                    sa[k++] = q;j++;
                }else{
                    if(rank12[p+1]<rank12[q+1]){
                        sa[k++] = p;i++;
                    }else{
                        sa[k++] = q;j++;
                    }                   
                }
            }else{
                //case 2
                if(rank[p]<rank[q]){
                    sa[k++] = p;i++;
                }else if(rank[p]>rank[q]){
                    sa[k++] = q;j++;
                }else{
                    if(rank[p+1]<rank[q+1]){
                        sa[k++] = p;i++;
                    }else if(rank[p+1]>rank[q+1]){
                        sa[k++] = q;j++;
                    }else{
                        if(rank12[p+2]<rank12[q+2]){
                            sa[k++] = p;i++;
                        }else{
                            sa[k++] = q;j++;
                        }           
                    }
                }
            }           
        }
        for(int m=i;m<sa12.length;m++){
            sa[k++] = sa12[m];
        }
        for(int m=j;m<sa0.length;m++){
            sa[k++] = sa0[m];
        }       
         
        return sa;      
    }
    //Precondition: the last char in text must be less than other chars.
    private Suffix DC3(String text){
        if(text == null)return null;
        int len = text.length();
        if(len == 0) return null;
         
        char base = text.charAt(len-1); //the smallest char
        Tuple[] tA = new Tuple[len];
        Tuple[] tB = new Tuple[len]; //placeholder
        for(int i=0;i<len;i++){
            tA[i] = new Tuple(i,new int[]{0,text.charAt(i)-base});
        }
        Suffix suffix = rank(tA,tB,MAX_CHAR-base,1);
          
        int max = suffix.rank[suffix.sa[len-1]];
        int[] sa  = skew(suffix.rank,max);
         
        //caculate rank for result suffix array
        int[] r = new int[len];     
        for(int k=0;k<sa.length;k++){
            r[sa[k]] = k+1;
        }
        return new Suffix(sa,r);
         
    }
    //rank[p]'s index starts with 1 (not 0)
    public int[] computeLCPTable(String text,int[] sa,int[] rank){
        if(text == null)return null;
        int len = text.length();
        if(len == 0) return null;
          
        int[] lcpz = new int[len];
         
        //base case: p=0
        //caculate LCP of suffix[0]
        int lcp = 0;
        int r = rank[0]-1;
        if(r>0){
           int q=sa[r-1];
           //caculate LCP by definition
           for(int i=0,j=q;i<len && j<len;i++,j++){
               if(text.charAt(i) != text.charAt(j)){
                   lcp=i;
                   break;
               }
           }
        }
        lcpz[0] = lcp;
         
        //other cases: p>=1
        //ignore p == sa[0] because LCP=0 for suffix[p] where rank[p]=0             
        for(int p=1;p<len && p != sa[0];p++){
            int h = lcpz[p-1];
            int q=sa[rank[p]-2];
            lcp = 0;
            if(h>1){ //for h<=1, caculate LCP by definition (i.e. start with lcp=0)           
                //jump h-1 chars for suffix[p] and suffix[q]                        
                lcp = h-1;              
            }
            for(int i=p+lcp,j=q+lcp,k=0;i<len && j<len;i++,j++,k++){
               if(text.charAt(i) != text.charAt(j)){
                   lcp+=k;
                   break;
               }
            }
            lcpz[p] = lcp;
        }
         
        //caculate LCP
        int[] LCP = new int[len];
        for(int i=0;i<len;i++){
            LCP[i] = lcpz[sa[i]];
        }
        return LCP;
    }
  
     
    public void buildChildtab(){
        //step 1: caculate up and down value
        Stack<Integer> stack = new Stack<Integer>();
        int lastIndex = -1;
        stack.push(0);
        for(int i=1;i<len;i++){ 
            while(lcptable[i]<lcptable[stack.peek()]){
                lastIndex = stack.pop();
                int next = stack.peek();
                if(lcptable[i]<=lcptable[next] 
                        && lcptable[next] != lcptable[lastIndex]){
                    childtab[next] = lastIndex; 
                }
            }
            if(lastIndex != -1){
                childtab[i-1] = lastIndex;
                lastIndex = -1;
            }
            stack.push(i);
        }
        //process remaining elements
        while(0<lcptable[stack.peek()]){
            lastIndex = stack.pop();
            int next = stack.peek();
            if(0<=lcptable[next] 
                    && lcptable[next] != lcptable[lastIndex]){
                childtab[next] = lastIndex; 
            }
        }
         
         
        //step 2: caculate nextLIndex
        stack.clear();
        stack.push(0);
        for(int i=1;i<len;i++){ 
            while(lcptable[i]<lcptable[stack.peek()]){
                stack.pop();                
            }
            if(lcptable[i] == lcptable[stack.peek()]){              
                lastIndex = stack.pop();
                childtab[lastIndex] = i;
            }
            stack.push(i);
        }       
    }
     
    class LCPInterval{
        int lcp; //the lcp value of the lcp-interval
        int lb; //the left boundary suffix index
        int rb; //the right boundary suffix index
        List<LCPInterval> children;
        public LCPInterval(int lcp,int lb,int rb){
            this.lcp = lcp;
            this.lb = lb;
            this.rb = rb;
        }
        public String toString(){
            return String.format("%d-[%d..%d]", 
                    this.lcp,this.lb,this.rb);
        }
    }   
    private void reportLCPInterval(LCPInterval interval){
        if(interval.children.size()>0){
            StringBuilder sb = new StringBuilder();
            for(LCPInterval child:interval.children){
                sb.append(child.toString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length()-1);
            System.out.format("%s, children={%s}%n", 
                    interval,sb.toString());                
        }else{
            System.out.format("%s%n", interval);    
        }   
    }
     
    private int getUpValue(int i){
        int up = -1;
        if(i>=1 && lcptable[i-1]>lcptable[i]){
              up = childtab[i-1];//up value
        }
        return up;
    }
     
    private int getDownValue(int i){
        int down = -1;
        if(childtab[i]>-1 && lcptable[childtab[i]] > lcptable[i]){            
            down = childtab[i]; //down value
        }
        return down;
    }
     
     
    private int getLIndexValue(int i){
        int nextLIndex = -1;        
        if(i<len-1 && getUpValue(i+1)==-1
                && getDownValue(i)==-1){
            //neither down value nor up value
            nextLIndex = childtab[i];       
        }
        return nextLIndex;
    }    
      
    //Note: (i,j) != (0,n-1)
    private List<LCPInterval> getChildIntervals(int i,int j){     
        List<LCPInterval> children = new ArrayList<LCPInterval>();
                 
        int up = -1;
        if(j+1<len){
            up = getUpValue(j+1);
        }
        int i1 = -1;
        if(up>i && up<=j){
            i1 = up;
        }else{
            i1 = getDownValue(i);
        }       
        if(i<i1-1){
            int lcp = getlcp(i,i1-1);
            children.add(new LCPInterval(lcp,i,i1-1));
        }
         
        while(getLIndexValue(i1) != -1){
            int i2 = getLIndexValue(i1);            
            if(i1<i2-1){
                int lcp = getlcp(i1,i2-1);
                children.add(new LCPInterval(lcp,i1,i2-1)); 
            }
            i1 = i2;
        }
        if(i1<j){
            int lcp = getlcp(i1,j);
            children.add(new LCPInterval(lcp,i1,j));
        }
        return children;
    }
    //Note: (i,j) != (0,n-1)
    private int getlcp(int i,int j){ 
        if(i==j) return lcptable[i];
         
        int up = -1;
        if(j+1<len){
            up = getUpValue(j+1);
        }
 
        if(up>i && up<=j){
            return lcptable[up];
        }else{
            if(getDownValue(i)>-1)
                return lcptable[getDownValue(i)];
            else
                return -1;
        }
    }
    private void topDownTraverse(LCPInterval interval){     
        List<LCPInterval> childIntervals = getChildIntervals(interval.lb,interval.rb);
        interval.children = childIntervals;
        reportLCPInterval(interval);
        for(LCPInterval child:childIntervals){
            topDownTraverse(child);
        }
    }
    public void topDownTraverse(int[] lcptable){
        this.lcptable = lcptable;
         
        this.buildChildtab();
         
        System.out.format("lcptab: %n");
        for(int i=0;i<len;i++){
            System.out.format(" %d",this.lcptable[i]);
        }       
         
        System.out.format("%nchildtab: %n");
        for(int i=0;i<len;i++){
            System.out.format(" %d",this.childtab[i]);
        }
        System.out.format("%n");
         
        System.out.format("%nInternal Nodes:%n");
         
        LCPInterval root = new LCPInterval(0,0,len-1);
        List<LCPInterval> childIntervals = new ArrayList<LCPInterval>();
         
        int i1 = getLIndexValue(0);
        if(i1-1>0){
            int lcp = getlcp(0,i1-1);
            childIntervals.add(new LCPInterval(lcp,0,i1-1));
        }
        while(getLIndexValue(i1) != -1){
            int i2 = getLIndexValue(i1);    
            if(i1<i2-1){
                int lcp = getlcp(i1,i2-1);
                childIntervals.add(new LCPInterval(lcp,i1,i2-1));   
            }
            i1 = i2;
        }
        if(i1<len-1){
            int lcp = getlcp(i1,len-1);
            childIntervals.add(new LCPInterval(lcp,i1,len-1));
        }
         
        root.children = childIntervals;     
        reportLCPInterval(root);
         
        for(LCPInterval child:childIntervals){
            topDownTraverse(child);
        }
    }
     
    public void solve(){
        Suffix suffix = this.DC3(this.text);
        int[] sa = suffix.sa;
        int[] rank = suffix.rank;       
         
        int[] lcptable = this.computeLCPTable(this.text,sa,rank);
      
         
        this.topDownTraverse(lcptable);     
         
    }
     
    public static void main(String[] args) {        
        int[] lcptable = {0,2,1,3,1,2,0,2,0,1,0}; //AKO's example       
        ESAChildtab esa = new ESAChildtab(lcptable.length);     
        esa.topDownTraverse(lcptable);      
        System.out.format("%n********************************%n");
         
         
        String text = "mississippi#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
                         
        System.out.format("%n********************************%n");
        text = "GACCCACCACC#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
         
        System.out.format("%n********************************%n");      
        text = "abcdefghijklmmnopqrstuvwxyz#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
         
        System.out.format("%n********************************%n");      
        text = "yabbadabbado#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
         
        System.out.format("%n********************************%n");      
        text = "AAAAAAAAAAAAAAAAAAAAAAAAAA#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
         
        System.out.format("%n********************************%n");      
        text = "GGGGGGGGGGGGCGCAAAAGCGAGCAGAGAGAAAAAAAAAAAAAAAAAAAAAA#";
        System.out.format("Text: %s %n%n",text);
        esa = new ESAChildtab(text);        
        esa.solve();
          
    }
 
}
/*测试：
lcptab: 
 0 2 1 3 1 2 0 2 0 1 0
childtab: 
 6 1 4 3 5 2 8 7 10 9 -1

Internal Nodes:
0-[0..10], children={1-[0..5],2-[6..7],1-[8..9]}
1-[0..5], children={2-[0..1],3-[2..3],2-[4..5]}
2-[0..1]
3-[2..3]
2-[4..5]
2-[6..7]
1-[8..9]

********************************
Text: mississippi# 

lcptab: 
 0 0 1 1 4 0 0 1 0 2 1 3
childtab: 
 1 5 3 4 2 6 8 7 10 9 11 -1

Internal Nodes:
0-[0..11], children={1-[1..4],1-[6..7],1-[8..11]}
1-[1..4], children={4-[3..4]}
4-[3..4]
1-[6..7]
1-[8..11], children={2-[8..9],3-[10..11]}
2-[8..9]
3-[10..11]

********************************
Text: GACCCACCACC# 

lcptab: 
 0 0 3 3 0 1 4 1 2 5 2 0
childtab: 
 1 4 3 2 11 7 6 8 10 9 5 -1

Internal Nodes:
0-[0..11], children={3-[1..3],1-[4..10]}
3-[1..3]
1-[4..10], children={4-[5..6],2-[7..10]}
4-[5..6]
2-[7..10], children={5-[8..9]}
5-[8..9]

********************************
Text: abcdefghijklmmnopqrstuvwxyz# 

lcptab: 
 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
childtab: 
 1 2 3 4 5 6 7 8 9 10 11 12 13 15 14 16 17 18 19 20 21 22 23 24 25 26 27 -1

Internal Nodes:
0-[0..27], children={1-[13..14]}
1-[13..14]

********************************
Text: yabbadabbado# 

lcptab: 
 0 0 5 1 2 0 3 1 4 0 1 0 0
childtab: 
 1 5 2 4 3 9 6 8 7 11 10 12 -1

Internal Nodes:
0-[0..12], children={1-[1..4],1-[5..8],1-[9..10]}
1-[1..4], children={5-[1..2],2-[3..4]}
5-[1..2]
2-[3..4]
1-[5..8], children={3-[5..6],4-[7..8]}
3-[5..6]
4-[7..8]
1-[9..10]

********************************
Text: AAAAAAAAAAAAAAAAAAAAAAAAAA# 

lcptab: 
 0 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25
childtab: 
 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 -1

Internal Nodes:
0-[0..26], children={1-[1..26]}
1-[1..26], children={2-[2..26]}
2-[2..26], children={3-[3..26]}
3-[3..26], children={4-[4..26]}
4-[4..26], children={5-[5..26]}
5-[5..26], children={6-[6..26]}
6-[6..26], children={7-[7..26]}
7-[7..26], children={8-[8..26]}
8-[8..26], children={9-[9..26]}
9-[9..26], children={10-[10..26]}
10-[10..26], children={11-[11..26]}
11-[11..26], children={12-[12..26]}
12-[12..26], children={13-[13..26]}
13-[13..26], children={14-[14..26]}
14-[14..26], children={15-[15..26]}
15-[15..26], children={16-[16..26]}
16-[16..26], children={17-[17..26]}
17-[17..26], children={18-[18..26]}
18-[18..26], children={19-[19..26]}
19-[19..26], children={20-[20..26]}
20-[20..26], children={21-[21..26]}
21-[21..26], children={22-[22..26]}
22-[22..26], children={23-[23..26]}
23-[23..26], children={24-[24..26]}
24-[24..26], children={25-[25..26]}
25-[25..26]

********************************
Text: GGGGGGGGGGGGCGCAAAAGCGAGCAGAGAGAAAAAAAAAAAAAAAAAAAAAA# 

lcptab: 
 0 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 4 3 2 1 3 5 2 3 0 2 1 2 0 2 4 3 1 3 2 3 1 2 3 4 5 6 7 8 9 10 11
childtab: 
 1 31 26 25 24 23 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 6 5 4 3 29 28 27 30 2 35 32 34 33 39 38 37 36 43 40 42 41 44 45 46 47 48 49 50 51 52 53 -1

Internal Nodes:
0-[0..53], children={1-[1..30],1-[31..34],1-[35..53]}
1-[1..30], children={2-[2..25],2-[26..30]}
2-[2..25], children={3-[3..24]}
3-[3..24], children={4-[4..23]}
4-[4..23], children={5-[5..22]}
5-[5..22], children={6-[6..22]}
6-[6..22], children={7-[7..22]}
7-[7..22], children={8-[8..22]}
8-[8..22], children={9-[9..22]}
9-[9..22], children={10-[10..22]}
10-[10..22], children={11-[11..22]}
11-[11..22], children={12-[12..22]}
12-[12..22], children={13-[13..22]}
13-[13..22], children={14-[14..22]}
14-[14..22], children={15-[15..22]}
15-[15..22], children={16-[16..22]}
16-[16..22], children={17-[17..22]}
17-[17..22], children={18-[18..22]}
18-[18..22], children={19-[19..22]}
19-[19..22], children={20-[20..22]}
20-[20..22], children={21-[21..22]}
21-[21..22]
2-[26..30], children={3-[26..28],3-[29..30]}
3-[26..28], children={5-[27..28]}
5-[27..28]
3-[29..30]
1-[31..34], children={2-[31..32],2-[33..34]}
2-[31..32]
2-[33..34]
1-[35..53], children={2-[35..38],2-[39..42],2-[43..53]}
2-[35..38], children={3-[36..38]}
3-[36..38], children={4-[36..37]}
4-[36..37]
2-[39..42], children={3-[39..40],3-[41..42]}
3-[39..40]
3-[41..42]
2-[43..53], children={3-[44..53]}
3-[44..53], children={4-[45..53]}
4-[45..53], children={5-[46..53]}
5-[46..53], children={6-[47..53]}
6-[47..53], children={7-[48..53]}
7-[48..53], children={8-[49..53]}
8-[49..53], children={9-[50..53]}
9-[50..53], children={10-[51..53]}
10-[51..53], children={11-[52..53]}
11-[52..53]
*/