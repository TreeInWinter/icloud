package com.travelzen.framework.core.util;

public class RandomInt {

	private static final int BUFFER_SIZE = 101;

	private static double[] buffer = new double[BUFFER_SIZE];

	private int low;

	private int high;

	static {
		for (int i = 0; i < BUFFER_SIZE; i++) {
			buffer[i] = java.lang.Math.random();
		}
	}

	public RandomInt(int low, int high) {
		this.low = low;
		this.high = high;
	}

	private static double nextRandom() {
		int pos = (int) (java.lang.Math.random() * BUFFER_SIZE);
		if (pos == BUFFER_SIZE)
			pos = BUFFER_SIZE - 1;
		double r = buffer[pos];
		buffer[pos] = java.lang.Math.random();
		return r;
	}

	/**
	 * 得到随机数
	 * 
	 * @return
	 * 
	 */
	public int draw() {
		int r = low + (int) ((high - low + 1) * nextRandom());
		return r;
	}

	public static void main(String[] args) {
		while (true) {
			RandomInt random = new RandomInt(300, 250);
			System.out.println(random.draw());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
