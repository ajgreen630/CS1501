// CS 1501
// Recursive branch and bound and dynamic programming solutions to the subset
// sum problem.  See additional comments below and also see course notes.
/*
Run this on some of the example files:
subset.dat
subset2.dat

You will be prompted for the input file name.
For each input file, try different values for M to see the solutions and
how much work they take to calculate
*/

import java.util.*;
import java.io.*;
public class subset
{
	private int [] size;	// To hold set data (int values) 
	private int [] store;	// For branch and bound algorithms, the store array
							// is effectively a boolean array, but for the
							// dynamic programming algorithm it needs to be ints
	private int N, M, calls, states;
	private boolean found;

	public void getset(Scanner keyin)    // input a set containing N integer values
	{
		Scanner infile = null;
		String ifname;
		
		boolean ok = false;
		while (!ok)   // prompt for name until it is legal
		{
			System.out.println("File name? > ");
			ifname = keyin.nextLine();
			try
			{
				infile = new Scanner(new File(ifname));
				ok = true;
			}
			catch (FileNotFoundException e)
			{
				System.out.println("File not found -- please re-enter");
			}
		}
		N = infile.nextInt();  System.out.println(N);
		size = new int[N+1];
		for (int i = 1; i <= N; i++)
		{
			size[i] = infile.nextInt();  System.out.print(size[i] + " ");
		}
		System.out.println();
	}

	// Kirk's Recursive Branch and Bound Code for Subset Sum. Note the two
	// recursive calls that allow all combinations (within bound) to be tried
	public void find_Subset (int lvl, int sum)  // prints all subsets with sum = M
	{
		if ( lvl == N+1 )
		{
			if (sum == M)
			{
				for (int i = 1; i <= N; i++)
					if (store[i] > 0) System.out.print(size[i] + " ");
				System.out.println();
			}
		}
		else
		{
			find_Subset(lvl+1, sum);
			store[lvl]=1;
			if (sum <= M) find_Subset(lvl+1, sum + size[lvl]);
			store[lvl]=0;
		}
	}

	// Alternative implemenation of Branch and Bound Subset Sum code.  Note that in
	// this implementation, we need a for loop in the main program -- we could
	// alternatively have the loop in a separate driver function.  This code is not
	// as elegant as the code above, but is more run-time efficient due to the loops
	// instead of the additional recursive calls.
	public void find_Subset2 (int lvl, int sum)  // prints all subsets with sum = M
	{
		calls++;
		states += 4;  // statements not within conditionals

		store[lvl] = 1;         // add current item to set
		sum += size[lvl];       // increment sum
		if (sum == M)           // if solution is found, print it out
		{
			found = true;
			for (int i = 1; i <= N; i++)
				if (store[i]> 0) System.out.print(size[i] + " ");
			System.out.println(" after " +calls+ " calls and " +states+" statements "); 
		}
		else                    // otherwise try other items in set
		{
			for (int i = lvl + 1; i <= N; i++)
			{
				states++;
				if (sum + size[i] <= M)			// only try an item if it will not
				{								// go over M -- this is where the
					states++;					// pruning comes into play (i.e the
					find_Subset2 (i, sum);		// "bound")
				}
			}
		}
		store[lvl] = 0;        // backtrack by removing current item from set
		// since sum is a value parameter, we don't have
		// 		 to subtract from it -- the sum from the
		//       previous call will be in effect when this
		//       call ends
	}

	public subset()
	{
		Scanner keyin = new Scanner(System.in);
		getset(keyin);
		System.out.println("Enter value for M:");
		M = keyin.nextInt();
		store = new int[N+1];
		for (int i=1; i<=N; i++) store[i]=0; // For branch and bound algorithms,
		// the store and size arrays are the same size.

		System.out.println("Totally Recursive Version: ");
		find_Subset(1, 0);
		System.out.println();

		for (int i=1; i<=N; i++) store[i]=0;

		// I am counting the number of function calls and am also doing a VERY VERY
		// VERY rough count of the statements executed by this algorithm.  This can
		// be VERY VERY VERY roughly compared to the amount of work required by the
		// dynamic programming solution below.  Note that the number of subsets in
		// a set of size N is 2^N, so you can compare the number of calls required
		// to that number.

		System.out.println("Version with iteration + recursion: ");
		calls = 0;
		states = 0;
		found = false;
		for (int i = 1; i <= N; i++)
		{
			states++;
			if (size[i] <= M)
				find_Subset2(i, 0);
		}
		if (!found)
		{
			System.out.println("No solution found after " + calls + " calls and " +
				states + " statements ");
		}
		else System.out.println();

		// The code below uses dynamic programming to obtain a pseudo-polynomial
		// solution to the subset sum problem.   Note that the time for
		// this algo. can easily be predicted: Theta(NM).  This seems polynomial, but
		// recall that M is the sum of a subset, whose items can be arbitrarily large.
		// Thus, the algorithm can in fact be slower than the branch and bound algorithm
		// in some cases.  Consider file subset4.dat and you can see where the dynamic
		// programming algorithm falters.  Also remember that the dynamic programming
		// algorithm requires Theta(M) memory, and this certainly can be a problem for
		// large M as well.
		System.out.println("Dynamic Programming Algo: ");
		states = 0;
		store = new int[M+1];	// For dynamic programming algorithm, the store array
								// needs to be size M+1
		for (int i = 1; i<=M; i++)
			store[i]=0;
		store[0] = -1;   // set store 0 to dummy non-zero value
		for (int j = 1; j <= N; j++)	// each time try to solve the problem
										// considering an additional item
		{
			for (int i = 1; i <= M; i++)
			{
				states++;
				if ((i >= size[j]) && (store[i - size[j]] != 0) && (j != store[i - size[j]])
					&& (store[i] == 0))

					// add item to solution for a given M only if
					// Condition 1) M is at least as big as the item considered
					// Condition 2) A solution has been found for M - size of the
					//              current item
					// Condition 3) The same item is not already used in the previous
					//              part of the solution
					// Condition 4) No solution has yet been found for the current M
				{
					//System.out.println("store[" + i + "]= " + j);
					store[i] = j;
					states++;
				}
			}
			if (store[M] > 0) break;
		}
		if (store[M] > 0)	// Solution found
		{
			for (int i = M; i > 0; i = i - size[store[i]])
				System.out.print(size[store[i]] + " ");
			System.out.println(" in " + states + " statements ");
		}
		else
			System.out.println("No solution found after " +	states + " statements ");
	}

	public static void main(String [] args)
	{	new subset();	}
}
