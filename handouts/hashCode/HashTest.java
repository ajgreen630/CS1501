// CS 1501 Summer 2020
// This handout demonstrates how hash values can be generated
// using the predefined Java hashCode() method.  Note how the
// value is converted into a positive number before actually being mapped
// into the table index range.  Also note how the second hash value
// is calculated by seeding the random number generator and getting a
// single integer from the generator.  Remember that the second hash value
// is an INCREMENT and not an absolute address.

// Note that in the test strings, "bogusness" and
// "weasel" have the same h(x), so they would collide.  However, they
// have different h2(x) increment values -- this is the benefit of 
// double hashing over linear probing.

import java.util.*;
public class HashTest
{
	public static void main(String [] args)
	{
		String [] A = {"bogus", "bogusness", "wacky", "weasel", "calypso", "hibernate", "bog",
		               "negativity", "A", "B", "AB", "BA", "ABC", "BAC", "CBA"};
		int M = 101;
		Random R = new Random();
		System.out.println("Table Size: " + M);
		System.out.println("String     \thashCode() \tPos hash code \th(x) \th2(x)");
		System.out.println("------     \t---------- \t------------- \t---- \t-----");
		for (int i = 0; i < A.length; i++)
		{
			int hashcode = A[i].hashCode();
			int poshashcode = hashcode & 0x7FFFFFFF;  // Make hash code positive
			int index = poshashcode % M;	// Get the value mod M
			R.setSeed(poshashcode);
			int incr = R.nextInt(M-1) + 1;  // Get h2
			System.out.print(A[i]);
			if (A[i].length() < 8) System.out.print("\t");
			System.out.print("\t" + hashcode);
			if (Math.abs(hashcode) < 100000) System.out.print("\t");
			System.out.print("\t" + poshashcode);
			if (poshashcode < 100000) System.out.print("\t");
			System.out.println("\t" + index + "\t" + incr);
		}
	}
}
