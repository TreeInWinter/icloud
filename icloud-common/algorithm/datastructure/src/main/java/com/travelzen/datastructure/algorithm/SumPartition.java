package com.travelzen.datastructure.algorithm;

/*整数的任意拆分问题（不允许重复）
 问题：输入两个整数n 和m，从数列1，2，3.......n 中随意取几个数(不许重复), 使其和等于m （m<=1+2+...n）,要求将其中所有的可能组合列出来.
 分析：记整数p可以用1,2...q的所有不重复数之和表示的组合为C(p,q)，则C(p,q)可以表示为以下的组合：
 {q} + C(p-q,q-1)  (如果q<=m)
 或者
 C(p,q-1)
 即要么包含q，要么不包含q。
 另外有C(0,K)={},  C(K,0)无解(K>0)
 因此可以使用动态规划的方法解决。
 实现:
 ?*/
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ljs 2011-05-11
 * 
 *         输入两个整数n 和m，从数列1，2，3.......n 中随意取几个数(不许重复), 使其和等于m
 *         （m<=1+2+...n）,要求将其中所有的可能组合列出来.
 * 
 */
public class SumPartition {
	private Object[][] C;
	private int m;
	private int n;

	public void sumPartitions(int m, int n) {
		this.m = m;
		this.n = n;

		C = new Object[m + 1][n + 1];
		// C[1][1]=new int[][]{{1}};

		// do the loop column by column
		for (int col = 1; col <= n; col++) {
			for (int row = 1; row <= m; row++) {
				List<int[]> combines = new ArrayList<int[]>();

				// calculate {n} U C(row-col,col-1)
				if (col < row) {
					if (col > 1) {// when col==1, there is no solution
						Object[] C0 = (Object[]) C[row - col][col - 1];

						for (int i = 0; i < C0.length; i++) {
							int[] C0item = (int[]) C0[i];

							// add n
							int[] cElementNew = new int[C0item.length + 1];
							System.arraycopy(C0item, 0, cElementNew, 0,
									C0item.length);
							cElementNew[C0item.length] = col;
							combines.add(cElementNew);
						}
					}
				} else if (col == row) {
					combines.add(new int[] { col });
				}

				// calculate C(row,col-1)
				// note: C(0,K) = {}; C(K,0) has no solution when K>0
				if (col > 1) {
					Object[] C1 = (Object[]) C[row][col - 1];
					for (int i = 0; i < C1.length; i++) {
						int[] C11 = (int[]) C1[i];
						combines.add(C11);
					}
				}
				C[row][col] = combines.toArray();
			}
		}
	}

	public void printSize() {

		Object[] ci = (Object[]) C[m][n];

		System.out.println("m:" + m + ",n=" + n + " size:" + ci.length);
	}

	public void prettyPrint() {
		// for(int i=0;i<=m;i++){
		Object[] ci = (Object[]) C[m][n];
		System.out.print("{");
		for (int j = 0; j < ci.length; j++) {
			int[] cij = (int[]) ci[j];
			System.out.print("{");
			for (int k = 0; k < cij.length - 1; k++)
				System.out.print(cij[k] + ",");
			if (cij.length - 1 >= 0)
				System.out.print(cij[cij.length - 1]);
			System.out.print("}");
		}
		System.out.print("}");

		// System.out.print(" m:"+ m + ",n=" + n + " size:" + ci.length);

		System.out.println();
		// }
	}

	public static void main(String[] args) {

		SumPartition sp = new SumPartition();

		int m = 6;
		int n = 5;
		sp.sumPartitions(m, n);
		sp.printSize();
		sp.prettyPrint();

		m = 20;
		n = 10;
		sp.sumPartitions(m, n);
		sp.printSize();
		sp.prettyPrint();
	}
}
/*
 * 测试输出： m:6,n=5 size:3 {{1,5}{2,4}{1,2,3}}
 * 
 * m:20,n=10 size:31 本实现中的row相当于上面讨论中的p， col相当于q。该算法的时间复杂度为O(mn)。
 */