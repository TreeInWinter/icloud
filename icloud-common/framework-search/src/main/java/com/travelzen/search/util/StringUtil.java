package com.travelzen.search.util;

public class StringUtil {

	public static String ToDBC(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		return returnString;
	}

	public static boolean isEmpty(String inputStr) {
		if (inputStr == null || inputStr.length() == 0)
			return true;
		return false;
	}

	private static int Minimum(int a, int b, int c) {
		int mi;

		mi = a;
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;

	}

	public static int LD(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1
		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];

		// Step 2
		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3
		for (i = 1; i <= n; i++) {

			s_i = s.charAt(i - 1);

			// Step 4
			for (j = 1; j <= m; j++) {

				t_j = t.charAt(j - 1);

				// Step 5
				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6
				d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
						d[i - 1][j - 1] + cost);

			}

		}
		// Step 7
		return d[n][m];
	}

	public static int LCS_len(String strx, String stry) {
		char[] x = strx.toCharArray(), y = stry.toCharArray();
		int[] oldrow = new int[x.length];
		int[] row = new int[x.length];
		int maxlen = 0;

		for (int i = 0; i < x.length; i++) {
			row[i] = 0;
			oldrow[i] = 0;
		}

		for (int i = 0; i < y.length; i++) {
			for (int j = 0; j < x.length; j++) {
				if (y[i] == x[j]) {
					if (j == 0)
						row[j] = 1;
					else
						row[j] = oldrow[j - 1] + 1;
				} else {
					row[j] = 0;
				}

				if (row[j] > maxlen)
					maxlen = row[j];
			}
			oldrow = row.clone();
		}

		return maxlen;
	}

	public boolean isEnglishLetter(char c) {
		if (Character.isUpperCase(c) || Character.isLowerCase(c)) {
			return true;
		}
		return false;
	}
}
