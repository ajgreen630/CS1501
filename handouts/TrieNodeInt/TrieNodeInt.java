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
	// c in the current node, or null if there is no next node for
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
	
}
