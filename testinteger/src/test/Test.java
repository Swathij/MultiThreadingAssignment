package test;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long now, done;
		
		long overflow = 0;
		
		now = System.nanoTime();
		
		System.out.println("Starting test");
		
		while (overflow >= 0)
			overflow++;

		done = System.nanoTime();
		
		System.out.printf("Time taken is %l", done - now);
	}
}
