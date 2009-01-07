package it.polito.memory;

public class test {
	
	public static void main () {
		int n = 0;
		for(int x = 0; x < Integer.MAX_VALUE; x++ ) {
			int[][] t = new int[8][8];
			System.out.print("Allocation " + ++n);
		}
	}
	 
}
