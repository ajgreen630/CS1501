// CS 1501 Summer 2020
// MultiWay Trie Node implemented as an external class which
// implements the TrieNodeInt Interface.

package TriePackage;

import java.util.*;
public class MTNode<V> implements TrieNodeInt<V>
{
	private static final int R = 256;

    protected V val;
	// Note that the next array is an array of TrieNodeInt<V>
	// and not MTNode<V>.  Note that in fact the MTNode<V> type
	// does not appear in here other than in the name of the
	// class and the constructors.
    protected TrieNodeInt<V> [] next;
	protected int degree;

	// Note the syntax for creating the array below.  This is
	// because it is an array of a parameterized type.  If we
	// tried to make a    new TrieNodeInt<V>[R]
	// we would get a compilation error due to the differences
	// in type checking of array objects and other Java objects.
	// For more information on this, Google:
	// creating java generic arrays
	// By using the <?> we are in effect allowing any type for the
	// parameter rather than requiring it to match V.  This is legal
	// but results in a warning during compilation (unchecked cast).
	public MTNode(V data)
	{
		val = data;
		degree = 0;
		next = (TrieNodeInt<V> []) new TrieNodeInt<?>[R];
	}
	
	public MTNode()
	{
		val = null;
		degree = 0;
		next = (TrieNodeInt<V> []) new TrieNodeInt<?>[R];
	}
	
	// Return the reference corresponding to character c
	public TrieNodeInt<V> getNextNode(char c)
	{
		return next[c];
	}
	
	// Assign the argument node to the location corresponding
	// to character c.  If the reference had been null we
	// increase the degree of the current node by one.
	public void setNextNode(char c, TrieNodeInt<V> node)
	{
		if (next[c] == null)
			degree++;
		next[c] = node;
	}
	
	// Return the value of this node
	public V getData()
	{
		return val;
	}
	
	// Set the value of this node
	public void setData(V data)
	{
		val = data;
	}
	
	// Return the degree of this node
	public int getDegree()
	{
		return degree;
	}
}
