package com.travelzen.datastructure.algorithm;

/*问题： 输入N个不同字符，如'a','b','c','d','e', 计算从这个字符集合中任意抽取n个(n<=N)不同字符的所有组合结果，比如从中抽取4个字符的组合结果为abcd， abce，abde，acde，bcde共5个。

分析：从字符全排列的算法(参见上一篇"字符集的排列算法")中可以知道，组合操作只是选取一部分字符作排列，同时需要把重复的情况剔除，如abc和 cba是两个不同排列，但是只算一个组合，去掉重复的情况可以按次序选取元素，
即下一个位置(pos)的字符需要大于上一个位置的字符。

算法实现：
?*/
public class CharCombination {
    public static void combine(int n,char[] chars,boolean[] used,int pos,char[] out){
        if(pos==chars.length){              
            return;
        }
                 
        for(int i=0;i<chars.length;i++){     
              
                if(pos==0 || (!used[i] &&   out[pos-1] < chars[i])){
                    out[pos] = chars[i];    
                    if( pos+1==n)
                        printSub(out,0,pos);
                    else if(pos+1<n){
                            used[i] = true;
                        combine(n,chars,used,pos+1,out);        
                        used[i] = false;   //用完需要标记该字符可用
                    }
                }
                 
        }
    }
    public static void printSub(char[] out,int start,int end){
         
        for(int i=start;i<=end;i++){
            System.out.print(out[i]);           
        }
        System.out.println();
    }
    //测试部分
    public static void main(String[] args) {
        char[] chars = new char[]{'a','b','c','d','e'};
        boolean[] used = new boolean[]{false,false,false,false,false};
        char[] out = new char[5];
        combine(4,chars,used,0,out);        
    }
}
/*out[pos-1] < chars[i] 语句用于保持次序，以防出现重复组合情况。*/