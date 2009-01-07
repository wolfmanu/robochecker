package it.polito.memory;

public class memoryTest {
	
	public static void main (String[] args) {
		recursion(0);
	}
	 
	public static void recursion (int depth) {
		if (depth==Integer.MAX_VALUE)
			return;
		int[][] t = new int[8][8];
		System.out.println("Allocation " + Integer.toString(depth));
		recursion(depth+1);
	}
	
}
