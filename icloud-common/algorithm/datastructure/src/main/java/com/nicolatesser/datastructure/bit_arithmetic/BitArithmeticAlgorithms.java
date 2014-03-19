package com.nicolatesser.datastructure.bit_arithmetic;

public class BitArithmeticAlgorithms {

	
	public static int sum(int a, int b)
	{
		int c = 0x00;
		
		int result = 0x00;
		for (int i=0;i<8;i++)
		{
			int bitmask = 0x01;
			for (int j=1;j<=i;j++)
			{
				bitmask = bitmask << 0x01;
			}
			
			int aIthBit = a & bitmask;
			int bIthBit = b & bitmask;
			
			for (int j=1;j<=i;j++)
			{
				aIthBit = aIthBit >> 0x01;
				bIthBit = bIthBit >> 0x01;
			}
			
			
			
			int resultBit = aIthBit ^ bIthBit ^ c;
			
			c = (aIthBit & bIthBit) | (aIthBit & c) | (c & bIthBit);
			
			for (int j=1;j<=i;j++)
			{
				resultBit = resultBit << 0x01;
			}
			
			result = result | resultBit;
		}
		
		
		return result;
	}
	
}
