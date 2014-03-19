package com.travelzen.datastructure.algorithm;

/*后缀数组自底向上遍历等价于后缀树的自底向上遍历。由于后缀数组不是树型结构，在遍历时除了SA本身之外还需要额外的信息，这时Suffix Array就是一个增强的后缀数组(Enhanced Suffix Array)了。该算法使用后缀数组的一个增强信息---LCP表，并通过堆栈模拟自底向上的遍历。遍历的结果就是一颗虚拟的lcp-interval 树，其中每一个结点对应后缀树的一个内部结点。有些应用中，遍历时需要知道每个结点的孩子信息，因此在下面的实现中提供了两个版本 bottomUpTraverseWithoutChildren和bottomUpTraverseWithChildren。
 需要说明 的是，树中每一个lcp-interval结点表示为：lcp-[i..j]，其中lcp相当于后缀树的pathlen，i和j分别是以该lcp- interval结点为根结点表示的子树中的最小和最大后缀数组下标，例如mississippi$的一个lcp-interval结点是 1-[1..4]，表示后缀数组中第1个后缀到第4个后缀这总共四个后缀作为该lcp-interval结点为根的子树的四个叶子结点。
 实现：
 ?*/
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 
 * Bottom-Up Traversal of a suffix array (with LCP table) (The suffix array is
 * constructed with prefix doubling algorithm)
 * 
 * 
 * Copyright (c) 2011 ljs (http://blog.csdn.net/ljsspace/) Licensed under GPL
 * (http://www.opensource.org/licenses/gpl-license.php)
 * 
 * @author ljs 2011-07-23
 * 
 */
public class ESABottomUpTraversal {

	public static final char MAX_CHAR = '\u00FF';

	private String text;
	private int[] suffixarray;
	private int[] ranktable;
	private int[] lcptable;

	public ESABottomUpTraversal(String text) {
		this.text = text;
	}

	class Suffix {
		int[] sa;
		// Note: the p-th suffix in sa: SA[rank[p]-1]];
		// p is the index of the array "rank", start with 0;
		// a text S's p-th suffix is S[p..n], n=S.length-1.
		int[] rank;
		boolean done;

		public Suffix(int[] sa, int[] rank) {
			this.sa = sa;
			this.rank = rank;
		}
	}

	// a prefix of suffix[isuffix] represented with digits
	class Tuple {
		int isuffix; // the p-th suffix
		int[] digits;

		public Tuple(int suffix, int[] digits) {
			this.isuffix = suffix;
			this.digits = digits;
		}

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append(isuffix);
			sb.append("(");
			for (int i = 0; i < digits.length; i++) {
				sb.append(digits[i]);
				if (i < digits.length - 1)
					sb.append("-");
			}
			sb.append(")");
			return sb.toString();
		}
	}

	// d: the digit to do countingsort
	// max: A value's range is 0...max
	private void countingSort(int d, Tuple[] tA, Tuple[] tB, int max) {
		// init the counter array
		int[] C = new int[max + 1];
		for (int i = 0; i <= max; i++) {
			C[i] = 0;
		}
		// stat the count
		for (int j = 0; j < tA.length; j++) {
			C[tA[j].digits[d]]++;
		}
		// process the counter array C
		for (int i = 1; i <= max; i++) {
			C[i] += C[i - 1];
		}
		// distribute the values
		for (int j = tA.length - 1; j >= 0; j--) {
			// C[A[j]] <= A.length
			tB[--C[tA[j].digits[d]]] = tA[j];
		}
	}

	// tA: input
	// tB: output for rank caculation
	private void radixSort(Tuple[] tA, Tuple[] tB, int max, int digitsLen) {
		int len = tA.length;
		int digitsTotalLen = tA[0].digits.length;

		for (int d = digitsTotalLen - 1, j = 0; j < digitsLen; d--, j++) {
			this.countingSort(d, tA, tB, max);
			// assign tB to tA
			if (j < digitsLen - 1) {
				for (int i = 0; i < len; i++) {
					tA[i] = tB[i];
				}
			}
		}
	}

	// max is the maximum value in any digit of TA.digits[], used for counting
	// sort
	// tA: input
	// tB: the place holder, reused between iterations
	private Suffix rank(Tuple[] tA, Tuple[] tB, int max, int digitsLen) {
		int len = tA.length;
		radixSort(tA, tB, max, digitsLen);

		int digitsTotalLen = tA[0].digits.length;

		// caculate rank and sa
		int[] sa = new int[len];
		sa[0] = tB[0].isuffix;

		int[] rank = new int[len + 2]; // add 2 for sentinel
		rank[len] = 1;
		rank[len + 1] = 1;
		int r = 1; // rank starts with 1
		rank[tB[0].isuffix] = r;
		for (int i = 1; i < len; i++) {
			sa[i] = tB[i].isuffix;

			boolean equalLast = true;
			for (int j = digitsTotalLen - digitsLen; j < digitsTotalLen; j++) {
				if (tB[i].digits[j] != tB[i - 1].digits[j]) {
					equalLast = false;
					break;
				}
			}
			if (!equalLast) {
				r++;
			}
			rank[tB[i].isuffix] = r;
		}

		Suffix suffix = new Suffix(sa, rank);
		// judge if we are done
		if (r == len) {
			suffix.done = true;
		} else {
			suffix.done = false;
		}
		return suffix;

	}

	private int[] orderSuffixes(Tuple[] tA, Tuple[] tB, int max, int digitsLen) {
		int len = tA.length;
		radixSort(tA, tB, max, digitsLen);
		// caculate rank and sa
		int[] sa = new int[len];
		for (int i = 0; i < len; i++) {
			sa[i] = tB[i].isuffix;
		}
		return sa;
	}

	// rank needs sentinel: len+2
	private Suffix reduce(int[] rank, int max) {
		int len = rank.length - 2;

		int n1 = (len + 1) / 3;
		int n2 = len / 3;
		Tuple[] tA = new Tuple[n1 + n2];
		Tuple[] tB = new Tuple[n1 + n2];

		for (int i = 0, j = 1; i < n1; i++, j += 3) {
			int r1 = rank[j];
			int r2 = rank[j + 1];
			int r3 = rank[j + 2];
			tA[i] = new Tuple(i, new int[] { r1, r2, r3 });
		}
		for (int i = n1, j = 2; i < n1 + n2; i++, j += 3) {
			int r1 = rank[j];
			int r2 = rank[j + 1];
			int r3 = rank[j + 2];
			tA[i] = new Tuple(i, new int[] { r1, r2, r3 });
		}

		return rank(tA, tB, max, 3);
	}

	private int[] skew(int[] rank, int max) {
		int len = rank.length - 2;

		// step 1: caculate sa12
		Suffix suffixT12 = reduce(rank, max);

		int[] sa12 = null;
		if (!suffixT12.done) {
			int[] rankT12 = suffixT12.rank;
			int maxT12 = rankT12[suffixT12.sa[suffixT12.sa.length - 1]];
			sa12 = skew(rankT12, maxT12);
			// debug for string: GACCCACCACC#
			// s12 = new Suffix();
			// s12.rank = new int[]{3,6,5,4,7,2,1,1,1};
			// s12.sa = new int[]{7,6,5,0,3,2,1,4};
			// s12.done =true;
		} else {
			sa12 = suffixT12.sa;
		}

		// index conversion for sa12
		int n1 = (len + 1) / 3;
		for (int j = 0; j < sa12.length; j++) {
			if (sa12[j] < n1) {
				sa12[j] = 1 + 3 * sa12[j];
			} else {
				sa12[j] = 2 + 3 * (sa12[j] - n1);
			}
		}
		// recaculate rank for sa12
		int[] rank12 = new int[len + 2];
		rank12[len] = 1;
		rank12[len + 1] = 1;
		for (int k = 0; k < sa12.length; k++) {
			rank12[sa12[k]] = k + 1;
		}

		// step 2: caculate sa0
		int n0 = (len + 2) / 3;
		Tuple[] tA = new Tuple[n0];
		Tuple[] tB = new Tuple[n0];
		for (int i = 0, j = 0; i < n0; i++, j += 3) {
			int r1 = rank[j];
			int r2 = rank12[j + 1];
			tA[i] = new Tuple(i, new int[] { r1, r2 });
		}
		int max12 = rank12[sa12[sa12.length - 1]];
		int[] sa0 = orderSuffixes(tA, tB, max < max12 ? max12 : max, 2);
		// index conversion for sa0
		for (int j = 0; j < n0; j++) {
			sa0[j] = 3 * sa0[j];
		}

		// step 3: merge sa12 and sa0
		int[] sa = new int[len];
		int i = 0, j = 0;
		int k = 0;
		while (i < sa12.length && j < sa0.length) {
			int p = sa12[i];
			int q = sa0[j];
			if (p % 3 == 1) {
				// case 1
				if (rank[p] < rank[q]) {
					sa[k++] = p;
					i++;
				} else if (rank[p] > rank[q]) {
					sa[k++] = q;
					j++;
				} else {
					if (rank12[p + 1] < rank12[q + 1]) {
						sa[k++] = p;
						i++;
					} else {
						sa[k++] = q;
						j++;
					}
				}
			} else {
				// case 2
				if (rank[p] < rank[q]) {
					sa[k++] = p;
					i++;
				} else if (rank[p] > rank[q]) {
					sa[k++] = q;
					j++;
				} else {
					if (rank[p + 1] < rank[q + 1]) {
						sa[k++] = p;
						i++;
					} else if (rank[p + 1] > rank[q + 1]) {
						sa[k++] = q;
						j++;
					} else {
						if (rank12[p + 2] < rank12[q + 2]) {
							sa[k++] = p;
							i++;
						} else {
							sa[k++] = q;
							j++;
						}
					}
				}
			}
		}
		for (int m = i; m < sa12.length; m++) {
			sa[k++] = sa12[m];
		}
		for (int m = j; m < sa0.length; m++) {
			sa[k++] = sa0[m];
		}

		return sa;
	}

	// Precondition: the last char in text must be less than other chars.
	public void computeSuffixArray() {
		if (text == null)
			return;
		int len = text.length();
		if (len == 0)
			return;

		char base = text.charAt(len - 1); // the smallest char
		Tuple[] tA = new Tuple[len];
		Tuple[] tB = new Tuple[len]; // placeholder
		for (int i = 0; i < len; i++) {
			tA[i] = new Tuple(i, new int[] { 0, text.charAt(i) - base });
		}
		Suffix suffix = rank(tA, tB, MAX_CHAR - base, 1);

		int max = suffix.rank[suffix.sa[len - 1]];
		int[] sa = skew(suffix.rank, max);

		// caculate rank for result suffix array
		int[] r = new int[len];
		for (int k = 0; k < sa.length; k++) {
			r[sa[k]] = k + 1;
		}

		this.suffixarray = sa;
		this.ranktable = r;
	}

	// rank[p]'s index starts with 1 (not 0)
	public void computeLCPtable() {
		if (text == null)
			return;
		int len = text.length();
		if (len == 0)
			return;

		int[] sa = this.suffixarray;
		int[] rank = this.ranktable;

		int[] lcpz = new int[len];

		// base case: p=0
		// caculate LCP of suffix[0]
		int lcp = 0;
		int r = rank[0] - 1;
		if (r > 0) {
			int q = sa[r - 1];
			// caculate LCP by definition
			for (int i = 0, j = q; i < len && j < len; i++, j++) {
				if (text.charAt(i) != text.charAt(j)) {
					lcp = i;
					break;
				}
			}
		}
		lcpz[0] = lcp;

		// other cases: p>=1
		// ignore p == sa[0] because LCP=0 for suffix[p] where rank[p]=0
		for (int p = 1; p < len && p != sa[0]; p++) {
			int h = lcpz[p - 1];
			int q = sa[rank[p] - 2];
			lcp = 0;
			if (h > 1) { // for h<=1, caculate LCP by definition (i.e. start
							// with lcp=0)
				// jump h-1 chars for suffix[p] and suffix[q]
				lcp = h - 1;
			}
			for (int i = p + lcp, j = q + lcp, k = 0; i < len && j < len; i++, j++, k++) {
				if (text.charAt(i) != text.charAt(j)) {
					lcp += k;
					break;
				}
			}
			lcpz[p] = lcp;
		}

		// caculate LCP
		int[] LCP = new int[len];
		for (int i = 0; i < len; i++) {
			LCP[i] = lcpz[sa[i]];
		}
		this.lcptable = LCP;
	}

	private void reportLCP() {
		System.out.format("Text: %s%n", text);

		int len = this.text.length();

		System.out.println("suffix array:");
		for (int i = 0; i < len; i++) {
			System.out.format(" %s", this.suffixarray[i]);
		}
		System.out.println();
		System.out.println("rank table:");
		for (int i = 0; i < len; i++) {
			System.out.format(" %s", this.ranktable[i]);
		}
		System.out.println();
		System.out.println("lcp table:");
		for (int i = 0; i < len; i++) {
			System.out.format(" %s", this.lcptable[i]);
		}
	}

	class LCPInterval {
		int lcp; // the lcp value of the lcp-interval
		int lb; // the left boundary suffix index
		int rb; // the right boundary suffix index
		List<LCPInterval> children = new ArrayList<LCPInterval>();

		public LCPInterval(int lcp, int lb, int rb) {
			this.lcp = lcp;
			this.lb = lb;
			this.rb = rb;
		}

		public String toString() {
			return String.format("%d-[%d..%d]", this.lcp, this.lb, this.rb);
		}
	}

	private void reportLCPInterval(LCPInterval interval) {
		if (interval.children.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (LCPInterval child : interval.children) {
				sb.append(child.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			System.out.format("%s, children={%s}%n", interval, sb.toString());
		} else {
			System.out.format("%s%n", interval);
		}
	}

	// traverse the corresponding suffix tree with a bottom-up approach
	// each internal node is equivalent to an lcp-interval.
	public void bottomUpTraverseWithoutChildren() {
		int len = text.length();

		Stack<LCPInterval> stack = new Stack<LCPInterval>();
		int lb = -1;
		// push root's first child
		stack.push(new LCPInterval(0, 0, -1));
		for (int i = 1; i < len; i++) {
			// save lb
			lb = i - 1;
			// pop all the deeper suffixes
			while (lcptable[i] < stack.peek().lcp) {
				LCPInterval interval = stack.pop();
				// update the popped interval's rb
				interval.rb = i - 1;
				reportLCPInterval(interval);

				// update lb
				lb = interval.lb;
			}
			if (lcptable[i] > stack.peek().lcp) {
				stack.push(new LCPInterval(lcptable[i], lb, -1));
			} // if lcptable[i]==interval.lcp, no push because rb is updated
				// when popped
		}

		while (!stack.isEmpty()) {
			LCPInterval interval = stack.pop();
			// update the popped interval's rb
			interval.rb = len - 1;

			reportLCPInterval(interval);
		}
	}

	public void bottomUpTraverseWithChildren() {
		int len = text.length();

		Stack<LCPInterval> stack = new Stack<LCPInterval>();
		int lb = -1;
		LCPInterval lastInterval = null;
		// push root's first child
		stack.push(new LCPInterval(0, 0, -1));
		for (int i = 1; i < len; i++) {
			// save lb
			lb = i - 1;
			// pop all the deeper suffixes
			while (lcptable[i] < stack.peek().lcp) {
				lastInterval = stack.pop();
				// update the popped interval's rb
				lastInterval.rb = i - 1;
				// Note: when lastInterval is popped out, if it has children
				// then its
				// children are already known!
				reportLCPInterval(lastInterval);

				// update lb
				lb = lastInterval.lb;

				if (lcptable[i] <= stack.peek().lcp) {
					// case 1: top>next>i, then top is next's child
					LCPInterval next = stack.peek();
					next.children.add(lastInterval);
					lastInterval = null;
				}
			}
			if (lcptable[i] > stack.peek().lcp) {
				LCPInterval curr = new LCPInterval(lcptable[i], lb, -1);
				if (lastInterval != null) {
					// case 2: top>i>next, then top is i's child
					curr.children.add(lastInterval);
					lastInterval = null;
				}
				stack.push(curr);
			} // if lcptable[i]==interval.lcp, no push because rb is updated
				// when popped
		}

		while (!stack.isEmpty()) {
			lastInterval = stack.pop();
			// update the popped interval's rb
			lastInterval.rb = len - 1;

			reportLCPInterval(lastInterval);

			if (!stack.isEmpty()) {// case 1: top > next, i.e. top is next's
									// child
				LCPInterval next = stack.peek();
				next.children.add(lastInterval);
			}
		}
	}

	public void solve() {
		this.computeSuffixArray();
		this.computeLCPtable();
		this.reportLCP();

		System.out.format("%nbottom-up traversal with no children list: %n");
		this.bottomUpTraverseWithoutChildren();
		System.out.format("%nbottom-up traversal with children list: %n");
		this.bottomUpTraverseWithChildren();
	}

	public static void main(String[] args) {
		String text = "mississippi#";
		ESABottomUpTraversal esa = new ESABottomUpTraversal(text);
		esa.solve();
		System.out.format("%n********************************%n");

		text = "GACCCACCACC#";
		esa = new ESABottomUpTraversal(text);
		esa.solve();
		System.out.format("%n********************************%n");
	}
}
/*
 * 测试： Text: mississippi# suffix array: 11 10 7 4 1 0 9 8 6 3 5 2 rank table: 6
 * 5 12 10 4 11 9 3 8 7 2 1 lcp table: 0 0 1 1 4 0 0 1 0 2 1 3 bottom-up
 * traversal with no children list: 4-[3..4] 1-[1..4] 1-[6..7] 2-[8..9]
 * 3-[10..11] 1-[8..11] 0-[0..11]
 * 
 * bottom-up traversal with children list: 4-[3..4] 1-[1..4],
 * children={4-[3..4]} 1-[6..7] 2-[8..9] 3-[10..11] 1-[8..11],
 * children={2-[8..9],3-[10..11]} 0-[0..11],
 * children={1-[1..4],1-[6..7],1-[8..11]}
 * 
 * ******************************* Text: GACCCACCACC# suffix array: 11 8 5 1 10
 * 7 4 9 6 3 2 0 rank table: 12 4 11 10 7 3 9 6 2 8 5 1 lcp table: 0 0 3 3 0 1 4
 * 1 2 5 2 0 bottom-up traversal with no children list: 3-[1..3] 4-[5..6]
 * 5-[8..9] 2-[7..10] 1-[4..10] 0-[0..11]
 * 
 * bottom-up traversal with children list: 3-[1..3] 4-[5..6] 5-[8..9] 2-[7..10],
 * children={5-[8..9]} 1-[4..10], children={4-[5..6],2-[7..10]} 0-[0..11],
 * children={3-[1..3],1-[4..10]}
 * 
 * *******************************
 * 
 * 参考： Mohamed Ibrahim Abouelhoda, Stefan Kurtz, Enno Ohlebusch: Replacing
 * suffix tree with enhanced suffix arrays (2004)
 */