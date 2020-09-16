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
// TrieNodeInt interface.  The idea behind this interface 
// is that it abstracts out the underlying implementation of the
// individual trie "nodes", thereby allowing a class that
// uses the trie to have either multi-way trie nodes or DLB
// nodes.  See the individual method specifications below.
package TriePackage;

public interface TrieNodeInt<V>
{
	// Return the next node in the trie corresponding to character
	// c in the current node, or null if there is not next node for
	// that character.
	public TrieNodeInt<V> getNextNode(char c);
	
	// Set the next node in the trie corresponding to character char
	// to the argument node.  If the node at that position was previously
	// null, increase the degree of this node by one (since it is now
	// branching by one more link).
	public void setNextNode(char c, TrieNodeInt<V> node);
	
	// Return the data at the current node (or null if there is no data)
	public V getData();
	
	// Set the data at the current node to the data argument
	public void setData(V data);
	
	// Return the degree of the current node.  This corresponds to the
	// number of children that this node has.
	public int getDegree();
	
	// Return the approximate size in bytes of the current node.  This is
	// a very rough approximation based on the following:
	// 1) Assume each reference in a node will use 4 bytes (whether it is
	//    used or it is null)
	// 2) Assume any primitive type is its specified size (see Java reference
	//    for primitive type sizes in bytes)
	// Note that the actual size of the node is implementation dependent and
	// is not specified in the Java language.  There are tools to give a better
	// approximation of this value but for our purposes, this approximation is
	// fine.
	public int getSize();
	
	// Return an Iterable collection of the references to all of the children
	// of this node.  Do not put any null references into this result.  The
	// order of the children as stored in the TrieNodeInt<V> node must be
	// maintained in the returned Iterable.  The easiest way to do this is to 
	// put all of the references into a Queue and to return the Queue (since a
	// Queue implements Iterable and maintains the order of the children).
	// This method will allow us to access all of the children of a node without
	// having to know how the node is actually implemented.
	public Iterable<TrieNodeInt<V>> children();
}