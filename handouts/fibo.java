// CS1501
// Comparing Fibonacci sequence iteratively vs. recursively
// For the timing, I am running the function a number of times and then dividing
// by the number of runs so that I can get an answer within the resolution of
// the system clock.  As the problem size increases, the number of runs required
// decreases.
import java.util.*;
public class fibo
{
	public static long adds;
	public static void main(String [] args)
	{
		Scanner inScan = new Scanner(System.in);
		long start, stop;
		double total;
		int n;
		long result = 0, aveadds;
		System.out.println("Enter a positive integer (or <= 0 to quit) > ");
		n = inScan.nextInt();
		while (n > 0)
		{
			start = System.nanoTime();
			long max = 102400000L/(n * n * n * n * n);   // Approximate number
						// of iterations based on size of n so that timer will
						// give a reasonable result.  We decrease the iterations
						// as the problem size gets bigger.  When n >= 35 the
						// number of iterations will be 1.
			//System.out.println("Recursive iterations: " + max);
			if (max < 1) max = 1;
			adds = 0;
			for (int i = 1; i <= max; i++)    
				result = fibrec(n);
			stop = System.nanoTime();
			total = ((double)(stop - start))/1000/max; // Calculate average time
						// in microseconds for a single result
			aveadds = adds / max;  // Calculate number of additions for a single
						// result

			System.out.println("The " + n + "th Fib. number is " + result);
			System.out.println("Recursively, one result took " + total + " microseconds ");
			System.out.println("Recursively, it required " + aveadds + " additions ");

			max = 60000000L/n;   // Same idea as above, but since the run-time is
								 // linear, we decrease the number of iterations
								 // much more slowly here.
			//System.out.println("Iterative iterations: " + max);
			if (max < 1) max = 1;
			start = System.nanoTime();
			adds = 0;
			for (int i = 1; i <= max; i++) 
				result = fibit(n);
			stop = System.nanoTime();
			total =  ((double)(stop - start))/1000/ max;
			aveadds = adds / max;

			System.out.println("\nThe " + n + "th Fib. number is " + result);
			System.out.println("Bottom up, one result took " + total + " microseconds ");
			System.out.println("Bottom up, it required " + aveadds + " additions ");

			System.out.println("\nEnter a positive integer (or <= 0 to quit) > ");
			n = inScan.nextInt();
		}
		System.out.println("Goodbye");
	}

	static long fibrec(int n)
	{
		if (n <= 2)
		{
			// no adds are done
			return 1;   // base case
		}
		else
		{
			adds++;   // do one add to complete result of rec. calls
			return (fibrec(n-1) + fibrec(n-2));
		}                         // recursive case
	}

	static long fibit(int n)
	{   // in this version, we build the fibonacci numbers from the bottom
		// up, starting with the first, then second and so on.  we stop when
		// we get to the appropriate one.  note that this is different from
		// the recursive version, in which we start at the top, and recursively
		// head toward the bottom, then back up to the top.
		long current = 0, onebef, twobef, i;

		if (n <= 2) return 1;           // special case for first 2
		else
		{
			twobef = 1;                     // regular case is below
			onebef = 1;
			for (i = 3; i <= n; i++)
			{
				adds++;
				current = onebef + twobef;
				twobef = onebef;            
				onebef = current;
			}
			return current;
		}
	}
}
