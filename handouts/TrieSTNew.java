/* CS 1501 Summer 2020
   This is a modification of the Sedgewick TrieST class.
   It has been changed from the original Sedgewick class in
   the following ways:
   1) An extra field has been added to each Node object which is
      equal to the degree of the node (or the number of children
	  of the node or the number of non-null references).
   2) A searchPrefix() method has been added, which performs as
      explained below.  This method utilizes the degree field
      mentioned in 1) above.
   3) Some methods that are not relevant for our purposes have
      been removed.
      
   For the purposes of Assignment 1 you can use this class as is without
   necessarily understanding the implementation details (they can be 
   abstracted out of your view).  However, you definitely should understand
   what searchPrefix accomplishes so that you can utilize it in your
   solution.
   
   For future reference, understanding the code in the class is probably
   a good idea.  Note that the put and get methods in this class are 
   implemented recursively.  They are not required to be done recursively 
   but the author chose to do them that way.  Read these over and trace 
   them so that you understand how the put and get methods work.
*/

public class TrieSTNew<Value>
{
    private static final int R = 256;        // extended ASCII

    private Node root;

    private static class Node {
        private Object val;
        private Node [] next = new Node[R];
        private int degree;  // how many children?
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

   /****************************************************
    * Insert key-value pair into the symbol table.
    ****************************************************/
    // Note that each "key" is not actually put into the trie directly
    // but rather it is stored indirectly via a path from the root to a
    // leaf.  Note also that the value is placed in the node that comes 
    // AFTER the last character match.  For example, if the key "cat"
    // were inserted, 4 nodes would be created.  The first 3 would be used
    // to branch down the tree and the final node would be used to store
    // the value.
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        if (x.next[c] == null)
        	x.degree++;
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

	// This method will return one of four possible values:
	// 0 if the key is neither found in the TrieST nor a prefix to
	//      a key in the TrieST
	// 1 if the key is a prefix to some string in the TrieST but it
	//      is not itself in the TrieST
	// 2 if the key is found in the TrieST but it is not a prefix to
	//      a longer string within the TrieST
	// 3 if the key is found in the TrieST and is also a prefix to added
	//      longer string within the TrieST
	// This method will be key in pruning your execution tree for your
	// anagram problem.
	public int searchPrefix(String key)
	{
		int ans = 0;
		Node curr = root;  // Start at the root Node
		boolean done = false;
		int loc = 0;       // index within the key -- start at 0
		
		// Loop until we get to the "end" loc or until we get to a
		// null pointer
		while (curr != null && !done)
		{
			// A key would be found in the node AFTER the last
			// pointer corresponding to a character in the key.  This
			// would be at the loc value == length of the key
			if (loc == key.length())
			{
				if (curr.val != null) // if Node has a value then 
				{					  // the key is a word in the 
									  // TrieST
					ans += 2;
				}
				if (curr.degree > 0) // if Node has at least one child
				{					 // then the key is a prefix to
				                     // some longer key in the TrieST
					ans += 1;
				}
				done = true;
			}
			else  // Move down to the next node using the current
			{	  // character in the key
				curr = curr.next[key.charAt(loc)];
				loc++;
			}
		}
		return ans;
	}
}
