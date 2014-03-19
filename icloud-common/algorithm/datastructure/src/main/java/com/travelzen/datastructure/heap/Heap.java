package com.travelzen.datastructure.heap;

/*输入一序列的整数(共n个)，找出前十个元素（Top 10）。可以用Heap实现：在堆满时，如果要插入一个新的元素，则需比较该元素是不是当前堆中最小的元素，如果不是，则需要将该新元素替换最小的元素， 
 * 从而维护一个TOP N的堆。通常最小的元素查找需要线性时间，因为只需要查找叶子节点（这是由堆的ordering property决定的），而叶子节点个数最多为2^O([logn] ) = O(n)，
 * 所以查找最小元的总开销为O(n)，然后需要重新构建堆，其总开销为O(nlogn), 二者加起来总的时间复杂度为O(nlogn)， 空间开销为O(1)。显然比通过排序查找Top N的空间复杂度要低得多！
实现：
?*/
public class Heap {
    public static class HeapTree{       
        private int size=0;
        private int[] heapArray;
        private int capacity;
        public HeapTree(int n){
            heapArray = new int[n];
            this.capacity = n;
        }       
        public int size(){
            return this.size;
        }               
         
        public void insert(int val){
            if(this.size==0){                
                 heapArray[0] = val;
                 this.size++;
            }else if(this.size==capacity){
                //capacity full
                tryReplaceLeast(val);
            }else{              
                heapArray[this.size]=val;
                siftUp(this.size);          
                this.size++;
            }
        }
        private void tryReplaceLeast(int val){          
            int least =  Integer.MAX_VALUE;
            int leastIndex=-1;
            //the first leaf node starts with index n/2 
            for(int i=this.size/2;i<this.size;i++){
                if(heapArray[i]<least){
                    least = heapArray[i];
                    leastIndex = i;
                }
            }
            /*
            //only check leaf nodes: 2*i+1>size-1
            for(int i=this.size-1;i>=0;i--){
                if(2*i+1 > size - 1){
                    if(heapArray[i]<least){
                        least = heapArray[i];
                        leastIndex = i;
                    }
                }else{
                    break;
                }
            }
            */
             
            //try to replace the least with the input value if the input value is larger
            if(val>heapArray[leastIndex]) {
                heapArray[leastIndex] = val;
                siftUp(leastIndex);
            }       
        }
         
        private void siftUp(int index){
            if(index<=0)return;
            int parent = (index - 1)/2;
             
            //a max-heap            
            if(heapArray[parent]<=heapArray[index]){
                //swap
                int tmp = heapArray[parent];
                heapArray[parent] = heapArray[index];
                heapArray[index] = tmp;
                 
                siftUp(parent);
            }           
        }
         
        public void showTopN(){     
            while(this.size>0) {             
                //print the root
                System.out.print(heapArray[0] + " ");               
                //replace the root with last item
                heapArray[0] = heapArray[this.size-1];
                this.size--;
                if(this.size>0){
                    siftDown(0);
                }
            }
        }
        private void siftDown(int index){
            int leftChildIndex = 2*index+1;
            int rightChildIndex =leftChildIndex+1;
             
            int val = heapArray[index];
            if(leftChildIndex>=this.size){
                //index is a leaf node
                return;
            }else if(rightChildIndex>=this.size){
                //only have a left child
                int leftChildVal = heapArray[leftChildIndex];
                if(val<=leftChildVal){
                    //swap
                    heapArray[leftChildIndex] = val;
                    heapArray[index] = leftChildVal;
                     
                    siftDown(leftChildIndex);
                }
            }else{
                //both children are available
                int maxChildVal = heapArray[leftChildIndex];
                int maxIndex = leftChildIndex;
                if(heapArray[rightChildIndex]>maxChildVal){
                    maxChildVal = heapArray[rightChildIndex];
                    maxIndex = rightChildIndex;
                }
                if(val <=  maxChildVal) {
                    //swap with maxIndex
                    heapArray[index] = maxChildVal;
                    heapArray[maxIndex] = val;
                    siftDown(maxIndex);
                }
            }
             
        }
    }
     
    public static void main(String[] args) {
        int[] items = {3,6,2,78,0,13,66,9,1,20,4};
        int N = 10;
        HeapTree heaptree = new HeapTree(N);
        for(int i=0;i<items.length;i++){
            heaptree.insert(items[i]);
        }
        heaptree.showTopN();
    }
}
/*测试输出：
78 66 20 13 9 6 4 3 2 1*/

