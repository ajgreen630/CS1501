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
// HybridTrieST<V> class

package TriePackage;

import java.util.*;
import java.io.*;

public class HybridTrieST<V> {

	// Reference to the root of the tree:
    private TrieNodeInt<V> root;
    // Indication of type of node to be created:
    int treeType = 0;
    	// treeType = 0 --> multiway trie for all nodes in the trie
    	// treeType = 1 --> DLB for all nodes in the trie
    	// treeType = 2 --> hybrid

    public HybridTrieST(int t) {	// DONE
    	treeType = t;
    	if(t == 0) {
    		root = new MTAlphaNode<V>();
    	}
    	else {
    		root = new DLBNode<V>();
    	}
    }

	// You must supply the methods for this class.  See test program
	// HybridTrieTest.java for details on the methods and their
	// functionality.  Also see handout TrieSTMT.java for a partial
	// implementation.

   /*===================================================
    	     Is the key in the symbol table?
    ===================================================*/

	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	public V get(String key) {
		TrieNodeInt<V> curr = get(root, key, 0);
		if(curr == null) {
			return null;
		}
		return curr.getData();
	}

	private TrieNodeInt<V> get(TrieNodeInt<V> curr, String key, int d) {
		if(curr == null) {
			return null;
		}
		if(d == key.length()) {
			return curr;
		}
		char c = key.charAt(d);
		return get(curr.getNextNode(c), key, d+1);
	}

	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	public int searchPrefix(String s) {

		int ans = 0;
		TrieNodeInt<V> curr = root;
		boolean done = false;
		int loc = 0;
		while (curr != null && !done) {
			if (loc == s.length()) {
				if (curr.getData() != null) {
					ans += 2;
				}
				if (curr.getDegree() > 0) {
					ans += 1;
				}
				done = true;
			}
			else {
				curr = curr.getNextNode(s.charAt(loc));
				loc++;
			}
		}
		return ans;
	}

   /*===================================================
    		  ***Additional Hybrid Methods***
    ===================================================*/

	// Algorithm discussed with Dr. Ramirez:
	// Retrieve whichever kind of data attribute you need from the root node.
	// Create a queue of type TrieNodeInt<V>.
	// Add the root to the queue (this will be the starting head of the list).
	// While the queue is not empty:
	//		For each child node (for loop calling node.childre()) of the node at 
	//		the head of the queue (removing the head at the same time):
	//			Add the child to the queue.
	//			Use the data of the child to update the return value.
	// Return data.

	// This algorithm is used for all of the methods below preceeding save().

	// getSize(): Return the accumulated sum of the size of all nodes in the trie.
	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	public int getSize() {
		// Initialize the size to the size of the root node:
		int size = root.getSize();
		// Create queue:
		LinkedList<TrieNodeInt<V>> queue = new LinkedList<TrieNodeInt<V>>();
		// Add the root to the queue:
		queue.add(root);
		while(!queue.isEmpty()) {
			// For child of the node at the head of the queue:
			// (This removes the head of the queue so that the first child of the current node will be next in the queue.)
			for(TrieNodeInt<V> child : queue.remove().children()) {
				// Add the child to the queue:
				queue.add(child);
				// Add the size of the child to the total size:
				// (Ran into logic errors with size += ... for some reason.)
				size = size + child.getSize();
			}
		}
		return size;
	}

	// degreeDistribution(): Return the degree distribution of all of the nodes in the trie 
	// within a single array.
	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	public int[] degreeDistribution() {
		// Array for degree distribution (size K + 1, where K is 26):
		int[] degDist = new int[27];
		// Add the degree of the root to the degDist array:
		degDist[root.getDegree()]++;
		LinkedList<TrieNodeInt<V>> queue = new LinkedList<TrieNodeInt<V>>();
		queue.add(root);
		while(!queue.isEmpty()) {
			for(TrieNodeInt<V> child : queue.remove().children()) {
				queue.add(child);
				// Add the degree of the child node to the degDist array:
				degDist[child.getDegree()]++;
			}
		}
		// Return degDist:
		return degDist;
	}

	// countNodes(): Return a count of all of the nodes in the trie:
	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	public int countNodes(int nodeType) {
		// nodeType = 1 -> MTAlphaNode<?>
		// nodeType = 2 -> DLBNode<?>
		int count = 0;
		// Adjust the count according to the root type:
		if(nodeType == 1 && root instanceof MTAlphaNode<?>) {
			count++;
		}
		else if(nodeType == 2 && root instanceof DLBNode<?>) {
			count++;
		}
		LinkedList<TrieNodeInt<V>> queue = new LinkedList<TrieNodeInt<V>>();
		queue.add(root);
		while(!queue.isEmpty()) {
			// For each child of the node at the head of the queue:
			for(TrieNodeInt<V> child : queue.remove().children()) {
				queue.add(child);
				// Adjust the count according to the node type:
				if(nodeType == 1 && child instanceof MTAlphaNode<?>) {
					count++;
				}
				else if(nodeType == 2 && child instanceof DLBNode<?>) {
					count++;
				}
			}
		}
		// Return count:
		return count;
	}

	// save(): Write the data of all nodes in the trie to the given file name:
	/* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes? YES!
	   3) Works for hybrid trie (with both nodes)? YES!
	*/
	// NOTE: save() method write all non-null values of the nodes in the trie to a file.
	// 		 I have submitted assuming that this is fine and we will not get points
	//		 taken off for not writing every single null value to the file alongside
	//		 the other values.
	public void save(String fileName) throws IOException {
		// Write all of the values in the trie to a file:
		try {
			FileWriter myWriter = new FileWriter(fileName);
			myWriter.write("");
			if(root.getData() != null) {
				myWriter.write((String)root.getData());
				myWriter.append("\n");
			}
			// Create queue:
			LinkedList<TrieNodeInt<V>> queue = new LinkedList<TrieNodeInt<V>>();
			queue.add(root);
			while(!queue.isEmpty()) {
				for(TrieNodeInt<V> child: queue.remove().children()) {
					queue.add(child);
					if(child.getData() != null) {
						myWriter.append((String)child.getData());
						myWriter.append("\n");
					}
				}
			}
			myWriter.close();
		} catch (IOException e) {
			System.out.println("An error occured.");
		}
		// Sort the file alphabetically:
		BufferedReader myReader = new BufferedReader(new FileReader(fileName));
		String s;
		List<String> list = new ArrayList<String>();
		while((s = myReader.readLine()) != null) {
			list.add(s);
		}
		myReader.close();
		Collections.sort(list);
		// Write the sorted list of values back to the file:
		FileWriter myWriter = new FileWriter(fileName);
		PrintWriter out = new PrintWriter(myWriter);
		for(String word : list) {
			out.println(word);
		}
		out.flush();
		out.close();
		myWriter.close();

	}

   /*===================================================
    	Insert key-value pair into the symbol table.
    ===================================================*/

    /* Checklist:
	   1) Works for array-based multiway trie nodes? YES!
	   2) Works for DLB trie nodes?
	   3) Works for hybrid trie (with both nodes)?
	*/
	public void put(String key, V val) {
		root = put(root, key, val, 0);
	}

	private TrieNodeInt<V> put(TrieNodeInt<V> curr, String key, V val, int d) {
		if(curr == null) {
			if(treeType == 0) {
				curr = new MTAlphaNode<V>();
			}
			else {
				curr = new DLBNode<V>();
			}
		}
		if(d == key.length()) {
			curr.setData(val);
			return curr;
		}
		char c = key.charAt(d);
		curr.setNextNode(c, put(curr.getNextNode(c), key, val, d+1));
		if(treeType == 2 && curr.getDegree() == 11 && curr instanceof DLBNode<?>) {
			curr = new MTAlphaNode<V>((DLBNode<V>)curr);
		}
		return curr;
	}
}
