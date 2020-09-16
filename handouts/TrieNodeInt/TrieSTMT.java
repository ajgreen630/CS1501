// CS 1501 Summer 2020
// This code is based on Sedgewick's TrieST class, with the nodes in the
// trie expressed as implementations of the TrieNodeInt<V> interface.  Compare
// this to the TrieSTNew class that you are using for Assignment 1.

// Note how the code is based on the TrieNodeInt<V> interface exclusively,
// except where a node object must be generated -- since a class is required
// for this.  Note, in particular, that there is no inner class for the Node
// type, since the Node is now abstracted via the TrieNodeInt interface.

package TriePackage;

import java.util.*;

public class TrieSTMT<V> {

    private TrieNodeInt<V> root;

	public TrieSTMT()
	{
		root = null;
	}

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public V get(String key) {
        TrieNodeInt<V> x = get(root, key, 0);
        if (x == null) return null;
        return x.getData();
    }

    private TrieNodeInt<V> get(TrieNodeInt<V> x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.getNextNode(c), key, d+1);
    }
	
	// Compare to searchPrefix in TrieSTNew.  Note that in this class all
	// of the accesses to a node are via the interface methods.
	public int searchPrefix(String key)
	{
		int ans = 0;
		TrieNodeInt<V> curr = root;
		boolean done = false;
		int loc = 0;
		while (curr != null && !done)
		{
			if (loc == key.length())
			{
				if (curr.getData() != null)
				{
					ans += 2;
				}
				if (curr.getDegree() > 0)
				{
					ans += 1;
				}
				done = true;
			}
			else
			{
				curr = curr.getNextNode(key.charAt(loc));
				loc++;
			}
		}
		return ans;
	}
		
   /****************************************************
    * Insert key-value pair into the symbol table.
    ****************************************************/
    public void put(String key, V val) {
        root = put(root, key, val, 0);
    }

	// This method requires us to create a new node -- which in turn requires
	// a class.  This is the only place where a MTNode<V> object is explicitly
	// used.
    private TrieNodeInt<V> put(TrieNodeInt<V> x, String key, V val, int d) {
        if (x == null) x = new MTNode<V>();
        if (d == key.length()) {
            x.setData(val);
            return x;
        }
        char c = key.charAt(d);
        x.setNextNode(c, put(x.getNextNode(c), key, val, d+1));
        return x;
    }
}
