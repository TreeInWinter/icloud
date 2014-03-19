package com.travelzen.datastructure.algorithm;

/*所谓LCP(Longest Common Prefix)是指后缀数组中相邻两个后缀的最长公共前缀的长度。在后缀数组的应用中，LCP是很重要的信息。
设 后缀数组为SA, 用LCP(i)定义为第SA[i]个后缀和第SA[i-1]个后缀之间的最长公共前缀长度
。由于输入文本T的第p个后缀和第p-1个后缀之间存在如下关 系：LCP(p) >= LCP(p-1) - 1，因此如果已知第p-1个后缀的LCP(p-1)，那么在计算第p个后缀的LCP(p)时，
可以直接跳过第p个后缀的前LCP(p-1)-1个字符，然 后在下一个字符位置开始与后缀数组中与p相邻的前一个后缀（设它为文本T的第q个后缀，即q=SA[Rank[p]-2]）依次按照LCP的定义计算出 LCP(p)的值。
按照该算法计算出的LCP数组的复杂度为O(n)。
实现：
?*/
/**
 * 
 * Derive LCP(Longest Common Prefix) from Suffix Array in O(n)
 *  
 *  
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/)
 * Licensed under GPL (http://www.opensource.org/licenses/gpl-license.php) 
 * 
 * @author ljs
 * 2011-07-20
 *
 */
public class LCP {
    //rank[p]'s index starts with 1 (not 0)
    public int[] solve(String text,int[] sa,int[] rank){
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
    public static void main(String[] args) {
        String text = "mississippi#";
        LCP solver = new LCP();
        int[] LCP = solver.solve(text,
                new int[]{11,10,7,4,1,0,9,8,6,3,5,2},
                new int[]{6,5,12,10,4,11,9,3,8,7,2,1});
        System.out.format("LCP array for text: %s%n",text);
        for(int i=0;i<LCP.length;i++){
            System.out.format(" %d",LCP[i]);
        }       
    }
 
}
/*测试：
LCP array for text: mississippi#
 0 0 1 1 4 0 0 1 0 2 1 3
*/