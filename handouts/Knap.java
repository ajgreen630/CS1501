// CS 1501
// Solutions to the Knapsack problem using recursive branch and bound
// and dynamic programming.  See additional comments below.

import java.io.*;
import java.util.*;

public class Knap
{
	int [] size;	// These are instance variables which will be accessed
	int [] val;		// in various methods
	int [] curstore, maxstore;
	int N, M, maxval;

	public void getinstance(Scanner keyin)  // input a knapsack instance
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
		// Create arrays for instance
		size = new int[N+1];
		val = new int[N+1];

		for (int i = 1; i <= N; i++)
		{
			size[i] = infile.nextInt(); System.out.print(size[i] + " ");
		}
		System.out.println();
		for (int i = 1; i <= N; i++)
		{
			val[i] = infile.nextInt(); System.out.print(val[i] + " ");
			
	
		}
		System.out.println();
		infile.close();
	}

	public void branchKnap()
	{
		curstore = new int[N+1];
		maxstore = new int[N+1];

		for (int i = 0; i < curstore.length; i++)
		{
			curstore[i] = 0;
			maxstore[i] = 0;
		}
		maxval = 0;
		for (int i = 1; i <= N; i++)
		{
			if (size[i] <= M)		// Initiate process with each legit. item
				knapsack(i, 0, 0);  
		}
		System.out.println();
		System.out.println("A B&B Knapsack solution = " + maxval);
		System.out.println("   Item  Size  Value  Num. Taken ");
		for (int i = 1; i <= N; i++)
		{
			System.out.println("    " + i + "     " + size[i] + "     "
							   + val[i] + "       " + maxstore[i]);
		}
	}

	// Branch and bound code for knapsack.  Note that this code is in fact quite
	// similar to the code for subset sum.  However, now each item can be selected
	// more than once and we are maximizing the value.  Thus we need some extra
	// variables -- maxval to maintain the current maximum obtained and maxstore
	// to maintain the items selected in the current maximum.  Also note that
	// instead of using the store in a binary way (1 and 0) we are maintaining
	// counts since each item can be selected multiple times.  Notice also that the
	// for loop within the function is almost the same as for subset sum, except that
	// the index starts at lvl instead of lvl + 1.  This allows the same item to be
	// selected recursively multiple times.   Finally, realize that since we are
	// trying to maximize the value, we don't know what our solution is until we are
	// finished (unlike subset sum).  Thus, we have to try all potential solutions
	// before stopping.
	public void knapsack(int lvl, int sum, int vl)
	{
		curstore[lvl]++;        // add another current item to store
		sum += size[lvl];       // increment sum
		vl += val[lvl];         // increment val

		if (sum <= M)
		{
			if (vl > maxval)     // if new maxval is found, update relevant variables
			{
				maxval = vl;
				for (int i = 1; i <= N; i++)
					maxstore[i] = curstore[i];
			}
			for (int i = lvl; i <= N; i++)     // start loop with current item
			{
				if (sum + size[i] <= M)        // only try an item if size will not
				{                              // go over M
					knapsack(i, sum, vl);
				}
			}
		}

		curstore[lvl]--;        // backtrack by decrementing count of cur. item
								// We do not have to "undo" the sum and vl
				// variables since they are value parameters (the values will not
				// be passed back to the calling method)
	}

	// Dynamic programming knapsack solution.  See more details within the code.
	public void dynaKnap()
	{
		curstore = new int[M+1];  // Now curstore represents the current "best"
								  // "solution" (max ptotal value) for each value
			// of M.  Note that this is a different use of curstore from the Branch
			// and Bound algorithm.  In that algorithm, curstore kept a count of each
			// item chosen, while in this algorithm it keeps a sum of the values.
								  
		maxstore = new int[M+1];  // maxstore keeps the index of the last added item
								  // to reach a given M
		for (int i = 0; i < curstore.length; i++)
		{
			curstore[i] = 0;
			maxstore[i] = 0;
		}
		maxval = 0;
		// Main algorithm to determine optimal filling of knapsack

		for (int j = 1; j <= N; j++) // Try each item, one at a time
		{
			// Given the current item, solve the knapsack problem for each
			// i from 1 up to the stated value for M.  Note that, unlike the
			// Subset Sum problem, the sizes of the items do not have to equal
			// M -- instead they must be <= M. 
			for (int i = 1; i <= M; i++)
			{
				if (i >= size[j]) // Will item j fit into knapsack of size i?
				{
					// If the current solution for knapsack of size i is less then
					// solution would be by adding item j, then add j.  Unlike
					// subset sum, j can be added multiple times
					if (curstore[i] < curstore[i - size[j]] + val[j])
					{
						curstore[i] = curstore[i - size[j]] + val[j];
						maxstore[i] = j;
					}
				}
			}
		}
		maxval = curstore[M];
		System.out.println("Dynamic Programming Solution = " + maxval);
		System.out.println("   Item  Size  Value");   
		for (int i = M; i > 0 && maxstore[i] > 0; i = i - size[maxstore[i]])
		{
			System.out.println("    " + maxstore[i] + "    " + size[maxstore[i]]
				             + "      " + val[maxstore[i]]);
		}
		System.out.println();
	}

	public Knap()
	{
		Scanner keyin = new Scanner(System.in);
		getinstance(keyin);
		System.out.println("Enter a value for M: ");
		M = keyin.nextInt();
		branchKnap();

		System.out.println();
		dynaKnap();

		System.out.println();
	}

	public static void main(String[] args)
	{
		new Knap();
	}
}
