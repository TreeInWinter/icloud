package com.travelzen.datastructure.algorithm;

/*问题：输入一串字符，每个字符都是唯一的，比如'a','b','c','d', 需要输出这串字符的全部排列。
分析：根据排列原理，排列的可能性有n!种（n为字符的个数）。直觉上讲，首先从字符集合中选取一个字符，然后从剩下的字符中选取另外一个字符，如 此下去，直到所有字符用完为止。另外一种可能性是：先假设一个子串已经排列好，比如abc，然后将下一个字符串d插入到已经排好的字符串的两边和中间位置 （这里共有4个位置）。下面实现的是第一种算法：
实现：
?
//chars: 输入的字符串
//used: 用于标记已经用过的字符
//pos: 当前处理的字符位置（从0到n-1）
//out:　输出的一个排列
public class CharPermutation {
        //字符串排序算法的一种实现
    public static void permute(char[] chars,boolean[] used,int pos,char[] out){
        if(pos==chars.length){
            System.out.println(out);            
            return;
        }
                 
        for(int i=0;i<chars.length;i++){             
            if(!used[i]){
                out[pos] = chars[i];
                used[i] = true;
                permute(chars,used,pos+1,out);      
                used[i] = false;   //排列完毕，需要标记该字符可用
            }
        }
    }
    //测试部分
    public static void main(String[] args) {
        char[] chars = new char[]{'a','b','c','d'};
        boolean[] used = new boolean[]{false,false,false,false};
        char[] out = new char[4];
        permute(chars,used,0,out);
                 
    }
}

上面程序中语句used[i] = false; 非常重要，递归调用完毕需要重新标记该字符可用。这样递归返回时可以在下一个for循环中再次使用该字符。例如如果当前循环的out为adb, 递归调用返回后字符c被标记为可用，这样下一次for循环可以使用c，即out的下一个值为adc。 如果有重复元素，以下是实现代码：
?*/
//Permutatation with repeated elements
public class CharPermutation {
    public static int COUNT;
    public static void permute(char[] chars,boolean[] used,int pos,char[] out){
        if(pos==chars.length){
            System.out.println(out);    
            COUNT++;
             
            return;
        }
                 
        OUTER:
        for(int i=0;i<chars.length;i++){             
            if(!used[i]){
                for(int j=0;j<chars.length;j++){
                    if(used[j]){
                        //eliminate repeated elements by looking forward
                        if(chars[i] == chars[j] && i<j){
                            continue OUTER;
                        }
                    }
                }
                 
                out[pos] = chars[i];
                used[i] = true;
                permute(chars,used,pos+1,out);      
                used[i] = false;  
            }
        }
    }
     
    public static void main(String[] args) {
        char[] chars = new char[]{'1','2','2','3','4','5'};     
        boolean[] used = new boolean[chars.length]; 
            
        char[] out = new char[chars.length];
        permute(chars,used,0,out);
        System.out.format("total permutations: %d%n",CharPermutation.COUNT);
    }
}
