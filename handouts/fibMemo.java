// CS1501
// Dynamic programming Fibonacci solution using memoization.  The idea is that we still
// make recursive calls when necessary but as we calculate subproblem results we store them
// in a table.  If a result is already present in the table we do not recalculate it.  Note
// the savings in additions with this technique.  However, we now need to store the
// calculated solutions in an array.  This costs us Theta(N) memory to store the solutions.
// However, since we are storing the results in a static array, we can reuse them from call
// to call as long as the array is large enough.  This cuts the number of additions in some
// cases to zero.
//
// Thus, overall the "bottom up" method from fibo.java will save us space and is probably
// a better choice for a single call.  However, if we are making multiple calls this
// memoization method could be preferred since it has the potential to reduce the additions
// to zero for a previously stored result.

import java.util.*;
public class fibMemo
{
	public static long [] fibSol = null;
	public static long adds;
	public static void main(String [] args)
	{
		Scanner inScan = new Scanner(System.in);
		int n;
		long result = 0;
		System.out.println("Enter a positive integer (or <= 0 to quit) > ");
		n = inScan.nextInt();
		while (n > 0)
		{
			if (fibSol == null || n >= fibSol.length)
				fibSol = new long[n+1];  // Make array to store partial answers
			adds = 0;
			result = fibM(n);
			System.out.println("The " + n + "th Fib. number is " + result);
			System.out.println("Memoized, it required " + adds + " additions");
			System.out.println();
			adds = 0;
			result = fibrec(n);
			System.out.println("The " + n + "th Fib. number is " + result);
			System.out.println("Recursively, it required " + adds + " additions");
			
			System.out.println("\nEnter a positive integer (or <= 0 to quit) > ");
			n = inScan.nextInt();
		}
		System.out.println("Goodbye");
	}

	static long fibM(int n)  // Memoization version of recursive Fibonacci algo.
	{
		if (n <= 2)
		{
			return 1;   // base case
		}
		else if (fibSol[n] > 0)		// If answer has been stored, use it
		{
			return fibSol[n];
		}
		else
		{
			adds++;   // do one add to complete result of rec. calls
			long ans = fibM(n-1) + fibM(n-2);	// Make recursive calls
			fibSol[n] = ans;					// Store answer in array
			return ans;
		}
	}
	
	static int fibrec(int n)
	{
		if (n <= 2)
		{
			return 1;   // base case
		}
		else
		{
			adds++;   // do one add to complete result of rec. calls
			return (fibrec(n-1) + fibrec(n-2));
		}                         // recursive case
	}

}
