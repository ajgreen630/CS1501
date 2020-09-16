// ==================================================
// Student Submission Information:
// ==================================================
// Name: Alexi Green
// MyPitt Username: AJG143
// MyPitt E-mail: ajg143@pitt.edu
// PeoplesoftID: 4193629
// Course Information: CS 1501 Section 19302
// ==================================================
// CS 1501 Summer 2020
// Assignment 2 Test Program

// Simple test program to demonstrate your Hybrid Trie implementation.
// This program requires your implementations to be within a valid package
// called TriePackage.  This package must contain the following:
// 		interface TrieNodeInt [ Assignment 2 version -- supplied to you ]
//		class MTAlphaNode [ written by you ]
//		class DLBNode [ written by you ]
//		class HybridTrieST [ written by you ]
// For more details on the classes that you must write, see the Assignment 2
// specifications.

// Read the code below carefully, taking note of the required methods and their
// parameters and effects.  Your output should match that shown on the CS 1501
// web site.

import java.io.*;
import java.util.*;
import TriePackage.*;

public class HybridTrieTest
{
	public static void main(String [] args) throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream(args[0]));
		// args[0] should be the name of the dictionary file
		
		Scanner inScan = new Scanner(System.in);
		System.out.println("Please choose your trie type:");
		System.out.println("0) Array based multiway trie nodes");
		System.out.println("1) DLB trie nodes");
		System.out.println("2) Hybrid trie (with both nodes)");
		int treeType = inScan.nextInt();
		// HybridTrieST class constructor must take an int argument to determine
		// which type of tree will be created.  Note (very important) that the
		// code / algorithms in your HybridTrieST class should be independent 
		// of the node implementation.  This is why the TrieNodeInt interface is
		// being used to represent the nodes.  However, the actual nodes must be
		// of some class (since an interface is abstract) and the treeType 
		// argument indicates how they will be generated.  For more details on
		// the constructor and rules for the hybrid tree structure, see the 
		// Assignment 2 specifications.
		HybridTrieST<String> D = new HybridTrieST<String>(treeType);
		
		// Add all of the words from the dictionary file to the trie.  Note that
		// your trie must maintain the words such that a traversal of the trie
		// will access them in alphabetical order.  The order of the words will
		// not affect the trie access times (asymptotically) but it will allow
		// the words to easily be saved in alphabetical order back to the file.
		// Thus, if any additions were made to the dictionary, when the file is
		// saved those additions will be in order alphabetically.
		String st;
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			D.put(st, st);
		}

		String [] tests = {"abc", "abe", "abet", "abx", "ace", "acid", "hives",
						   "iodin", "inval", "zoo", "zool", "zurich"};
		System.out.println("Testing get() method:");
		for (int i = 0; i < tests.length; i++)
		{
			String val = D.get(tests[i]);
			if (val != null)
				System.out.println(val + " is in the dictionary");
			else
				System.out.println(tests[i] + " is not in the dictionary");
		}
		System.out.println();
		System.out.println("Testing searchPrefix() method:");
		for (int i = 0; i < tests.length; i++)
		{
			int ans = D.searchPrefix(tests[i]);
			System.out.print(tests[i] + " is ");
			switch (ans)
			{
				case 0: System.out.println("not found");
					break;
				case 1: System.out.println("a prefix");
					break;
				case 2: System.out.println("a word");
					break;
				case 3: System.out.println("a word and prefix");
			}
		}	
		System.out.println();
		
		System.out.println("Adding some new words...");
		D.put("aaa", "aaa");
		D.put("aaaa", "aaaa");
		D.put("yzzz", "yzzz");
		D.put("zzzz", "zzzz");
		D.put("zaaa", "zaaa");
		D.put("zzz", "zzz");
		System.out.println();
		
		// Output the approximate size of the trie structure (not counting
		// the sizes of the actual string values that are stored in the trie).
		// This method will traverse through all of the nodes in the trie, 
		// utilizing the getSize() method for each node in order to get the overall
		// size of the trie.
		System.out.println("Size of the Trie is " + D.getSize() + " bytes");
		System.out.println();
		
		// The degreeDistribution() method should traverse the trie and return an 
		// int [] of size K+1 (where K is the maximum possible degree of a node in
		// the trie), indexed from 0 to K.  The value of each location dist[i] will
		// be equal to the number of nodes with degree i in the trie.  Note that in
		// our trie, the value K should be 26 since we are limiting it to lower
		// case letters, but for an arbitrary trie K could be as large as 256.
		int [] dist = D.degreeDistribution();
		int total = 0;
		System.out.println("Distribution of the trie nodes by degree: ");
		for (int i = 0; i < dist.length; i++)
		{
			System.out.println("Degree: " + i + "  Number: " + dist[i]);
			total += dist[i];
		}
		System.out.println();
		System.out.println("There were a total of " + total + " nodes in the trie");
		System.out.println();
		
		// Count the number of nodes of a given type. For this method we are using the
		// value 1 to indicate MTAlphaNode<?> nodes and the value 2 to indicated DLBNode<?>
		// nodes.  The actual method will traverse all of the nodes of the trie and use
		// the instanceof operator to test the types of the nodes.  A way to test the
		// correctness of this method and of the degreeDistribution method above is as
		// follows:  Your HybridTrieST should convert any nodes with a degree of 11 or
		// above to MTAlphaNode<?> nodes, while those with degree 10 or below should remain
		// as DLBNode<?> nodes.  Thus, if the hybrid version of the trie is being used,
		// the number of MTAlphaNode<?> nodes below should be equal to the sum of the 
		// distribution value from 11 to 26, while the number of DLBNode<?> nodes below
		// should be equal to the sum of the distribution value from 0 to 10.
		int nodes = D.countNodes(1);  // number of MTAlpha<?> Nodes
		System.out.println("The trie contains " + nodes + " MTAlphaNodes");
		System.out.println();
		
		nodes = D.countNodes(2);  // number of DLBNode<?> Nodes
		System.out.println("The trie contains " + nodes + " DLBNodes");
		System.out.println();
		
		System.out.println("Saving trie back to a file (should be alpha)");
		// Save the trie in order back to args[1].  This method will traverse
		// through all of the values in the trie in alpha order, saving all of them
		// to the file name provided in args[1].
		D.save(args[1]);
	}
}


