package com.travelzen.datastructure.algorithm;

/*设模式串的长度为m，文本的长度为n，使用后缀数组做文本匹配，如果只用后缀表suftab和折半查找算法的复杂度为O(m*logn)；
 *  如果使用最长公共前缀表lcptab和折半查找算法，复杂度可以降至O(m+logn)；使用增强型后缀数组(ESA)表childtab，复杂度为 O(m)。
 *  本文使用复杂度为O(m)的算法，在匹配之前要求先构造SA（下面采用DC3算法构造后缀数组），然后计算出后缀数组的 suftab,lcptab和childtab。
 *  由于通过childtab可以在O(1)时间复杂度内找到每一个lcp-interval的所有 child-interval，因此这跟后缀树的自顶向下匹配模式串的算法复杂度相当；
 *  进一步通过lcp-interval可以很容易地得到z个匹配子串 的起始位置，复杂度为O(m+z)。
实现：
?*/
		
		
import java.util.Stack;
 
  
 
/**
 * 
 * 
 * finding all z occurrences of a pattern with suftab,lcptab and childtab
 * (m: the length of pattern, n: the length of text)
 * time complexity: O(m+z)
 * 
 * (The suffix array is constructed with DC3 algorithm)
 * 
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-07-28
 *
 */
public class ESAPatternMatch {
    private String text;
    private int[] suftab;
    private int[] lcptable;
    private int[] childtab;
    private int len;
     
    public ESAPatternMatch(String text){
        this.text = text;
        this.len = text.length();
         
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
    public Suffix DC3(String text) throws Exception{
        if(text == null)return null;
        int len = text.length();
        if(len == 0) return null;
         
        char base = text.charAt(len-1); //the smallest char
        Tuple[] tA = new Tuple[len];
        Tuple[] tB = new Tuple[len]; //placeholder
        for(int i=0;i<len;i++){
            int delta=text.charAt(i)-base;
            if(delta<0) throw new Exception("invalid input: last char must be the smallest one.");
            tA[i] = new Tuple(i,new int[]{0,delta});
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
    //get i's up value; otherwise return -1
    private int getUpValue(int i){
        int up = -1;
        if(i>=1 && lcptable[i-1]>lcptable[i]){
              up = childtab[i-1];//up value
        }
        return up;
    }
    //get i's down value; otherwise return -1
    private int getDownValue(int i){
        int down = -1;
        if(childtab[i]>-1 && lcptable[childtab[i]] > lcptable[i]){            
            down = childtab[i]; //down value
        }
        return down;
    }
     
    //get the i's next L-index value; otherwise return -1
    private int getNextLIndexValue(int i){
        int nextLIndex = -1;        
        if(i<len-1 && getUpValue(i+1)==-1
                && getDownValue(i)==-1){
            //neither down value nor up value
            nextLIndex = childtab[i];       
        }
        return nextLIndex;
    }    
     
    //i and j is an lcp-interval 
    private int getlcp(int i,int j){ 
        if(i==j) return lcptable[i];
         
        int up = -1;
        if(j+1<len){
            up = getUpValue(j+1);
        }
 
        if(up>i && up<=j){
            return lcptable[up];
        }else{
            int down = getDownValue(i);
            if(down>-1)
                return lcptable[down];
            else
                return 0;
        }
    }
    private int getFirstLIndex(int i,int j){
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
        if(i1==-1){ //accommodate: i's doesn't belong to an lcp-interval
            i1 = getNextLIndexValue(i);
        }
        return i1;
    }
    //find the child-interval or singleton interval starting with char c
    //return null if not found
    //i and j is the parent lcp-interval 
    private int[] getMatchedInterval(int i,int j,char c){
        int lcp = getlcp(i,j);
         
        int[] interval = null;
         
        int i1 = getFirstLIndex(i,j);
        //i..i1-1
        interval = getMatchedChildInterval(i,i1-1,c,lcp);
         
        if(interval != null)
            return interval;
         
        int nextLIndex = -1;
        while((nextLIndex = getNextLIndexValue(i1)) != -1){
            int i2 = nextLIndex;        
            interval = getMatchedChildInterval(i1,i2-1,c,lcp);          
            if(interval != null)
                return interval;
            i1 = i2;
        }
        interval = getMatchedChildInterval(i1,j,c,lcp); 
        return interval;
    }
    private int[] getMatchedChildInterval(int i,int j,char c,int lcp){
        int[] interval = null;
        if(i==j){
            //singleton interval
            if(text.charAt(this.suftab[i]+lcp)==c){
                interval = new int[]{getlcp(i,i),i,j};
            }
        }else{          
            if(text.charAt(this.suftab[i+1]+lcp)==c){
                interval = new int[]{getlcp(i,j),i,j};
            }
        }
        return interval;
    }
     
    public void enhanceSA() throws Exception{
        //prepare suftab, lcptab and childtab
        Suffix suffix = this.DC3(this.text);        
        this.suftab = suffix.sa;
        int[] sufinv = suffix.rank;             
        this.lcptable = this.computeLCPTable(this.text,this.suftab,sufinv);
        this.buildChildtab();    
    }
 
    //precondition: call this.enhanceSA() first
    public void match(String pattern){  
        int m = pattern.length();
        if(m==0) return;
                         
        int pos=0;      
        boolean found = true;
        int i=0;
        int j=len-1;
        int[] interval = null;
         
        while(found && pos<m && (interval = getMatchedInterval(i,j,pattern.charAt(pos))) != null){           
            i=interval[1];
            j=interval[2];
            if(i!=j){
                int lcp=interval[0];
                int min=(lcp<m)?lcp:m;
                found=(pattern.substring(pos,min).equals(
                        text.substring(this.suftab[j]+pos,this.suftab[j]+min)));
                pos=min;    
                //if lcp<m and found, continue 
                //if lcp>=m, exit whether found or not (pos==m)
                //if lcp<m but not found, exit (found=false)
            }else{              
                int tmp=this.suftab[i]+m;
                if(tmp>len) tmp=len;
                found=(pattern.substring(pos).equals(
                        text.substring(this.suftab[i]+pos,tmp)));
                pos=m; //exit whether found or not (pos==m)
            }
        }
        if(found && interval != null){
            report(pattern,i,j);
        }else{
            System.out.format("pattern \"%s\" not found!%n",pattern);
        }
    }
    //i,j is an interval (child-interval or a singleton interval)
    private void report(String pattern,int i,int j){
        System.out.format("matched suffix(es) with pattern \"%s\": %d%n",pattern,j-i+1);
        for(int z=i;z<=j;z++){
            System.out.format("[%d:%s]%n",this.suftab[z],this.text.substring(this.suftab[z],this.text.length()-1));
        }
    }
    public static void main(String[] args) throws Exception {   
        String text = "mississippi#";
        System.out.format("Text: %s %n",text.substring(0,text.length()-1));
        ESAPatternMatch esa = new ESAPatternMatch(text);        
        esa.enhanceSA();
        esa.match("z"); 
        esa.match("i"); 
        esa.match("iss");   
        esa.match("issi");  
        esa.match("j"); 
        esa.match("ir");    
        esa.match("ia");
         
        System.out.format("%n********************************%n");  
        text = "acaaacatat#";       
        System.out.format("Text: %s %n",text.substring(0,text.length()-1));
        esa = new ESAPatternMatch(text);    
        esa.enhanceSA();
        esa.match("ac");    
         
        System.out.format("%n********************************%n");  
        text = "After a long text, here's a needle ZZZZZ\u0000";        
        System.out.format("Text: %s %n",text.substring(0,text.length()-1));
        esa = new ESAPatternMatch(text);    
        esa.enhanceSA();
        esa.match("ZZZZZ"); 
         
        System.out.format("%n********************************%n");  
        text = "The quick brown fox jumps over the lazy dog.\u0000";        
        System.out.format("Text: %s %n",text.substring(0,text.length()-1));
        esa = new ESAPatternMatch(text);    
        esa.enhanceSA();
        esa.match("lazy");  
         
        System.out.format("%n********************************%n");  
        text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna...\u0000";     
        System.out.format("Text: %s %n",text.substring(0,text.length()-1));
        esa = new ESAPatternMatch(text);    
        esa.enhanceSA();
        esa.match("tempor");    
    }
 
}
/*测试：
Text: mississippi 
pattern "z" not found!
matched suffix(es) with pattern "i": 4
[10:i]
[7:ippi]
[4:issippi]
[1:ississippi]
matched suffix(es) with pattern "iss": 2
[4:issippi]
[1:ississippi]
matched suffix(es) with pattern "issi": 2
[4:issippi]
[1:ississippi]
pattern "j" not found!
pattern "ir" not found!
pattern "ia" not found!

********************************
Text: acaaacatat 
matched suffix(es) with pattern "ac": 2
[0:acaaacatat]
[4:acatat]

********************************
Text: After a long text, here's a needle ZZZZZ 
matched suffix(es) with pattern "ZZZZZ": 1
[35:ZZZZZ]

********************************
Text: The quick brown fox jumps over the lazy dog. 
matched suffix(es) with pattern "lazy": 1
[35:lazy dog.]

********************************
Text: Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna... 
matched suffix(es) with pattern "tempor": 1
[73:tempor incididunt ut labore et dolore magna...]
*/