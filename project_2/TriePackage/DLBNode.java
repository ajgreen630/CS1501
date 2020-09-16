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
// DLB Trie Node implemented as an external class which
// implements the TrieNodeInt<V> Interface

package TriePackage;

import java.util.*;
public class DLBNode<V> implements TrieNodeInt<V>
{
    protected Nodelet front;
    protected int degree;
    protected V val;
    
    protected class Nodelet {
        protected char cval;
        protected Nodelet rightSib;
        protected TrieNodeInt<V> child;
    }
            
    // You must supply the methods for this class.  See TrieNodeInt.java for the
    // interface specifications.  You will also need a constructor or two.
    

    // Return the next node in the trie corresponding to character
    // c in the current node, or null if there is not next node for
    // that character.
    public TrieNodeInt<V> getNextNode(char c) {     // DONE
        
        // If the value in the front nodelet is equivalent to the character:
        if(front == null) {
            return null;
        }
        if(front.cval == c) {
            // If the nodelet does not have a child:
            if(front.child == null) {
                return null;
            }
            else{
                return front.child;
            }
        }
        // Otherwise, traverse through the linked list to find the character and
        // return the child of the appropriate nodelet:
        Nodelet curr = front;
        while(curr != null) {
            curr = curr.rightSib;
            if(curr != null && curr.cval == c) {
                return curr.child;
            }
        }
        // If curr == null, we've traversed our linked list without finding
        // the appropriate nodelet:
        return null;
    }
    
    // Set the next node in the trie corresponding to character char
    // to the argument node.  If the node at that position was previously
    // null, increase the degree of this node by one (since it is now
    // branching by one more link).
    public void setNextNode(char c, TrieNodeInt<V> node) {  // DONE
        // If front does not exist:
        if(front == null) {
            front = new Nodelet();
            front.cval = c;
            front.rightSib = null;
            front.child = node;
            degree++;
        }
        // Else if front holds the same value as c:
        else if(front.cval == c) {
            // If the child of front does not exist:
            if(front.child == null) {
                front.child = node;
                degree++;
            }
            // Otherwise, replace existing child with node:
            else {
                front.child = node;
            }
        }
        else if(c < front.cval) {
            Nodelet temp = new Nodelet();
            temp.cval = c;
            temp.rightSib = front;
            temp.child = node;
            front = temp;
            degree++;
        }
        else {
            // Flag for finding nodelet:
            boolean found = false;
            Nodelet prev = null;
            Nodelet curr = front;
            // Traverse linked list:
            while(curr != null)
            {
                prev = curr;
                curr = curr.rightSib;
                // If curr exists and holds the same value as c:
                if(curr != null && curr.cval == c) {
                    // Set found flag to true:
                    found = true;
                    // If child of curr does not exist yet:
                    if(curr.child == null) {
                        curr.child = node;
                        degree++;
                    }
                    // Otherwise, replace existing child with node:
                    else {
                        curr.child = node;
                    }
                    return;
                }
                else if(curr != null && curr.cval > c) {
                    break;
                }
            }
            // If nodelet was not found:
            if(!found) {
                // Put the character in the list:
                prev.rightSib = new Nodelet();
                prev.rightSib.rightSib = curr;
                prev.rightSib.child = node;
                prev.rightSib.cval = c;
                degree++;
            }
        }
    }
    
    // Return the data at the current node (or null if there is no data):
    public V getData() {    // DONE
        return val;
    }
    
    // Set the data at the current node to the data argument:
    public void setData(V data) {   // DONE
        val = data;
    }
    
    // Return the degree of the current node.  This corresponds to the
    // number of children that this node has.
    public int getDegree() {    // DONE
        return degree;
    }
    
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
    public int getSize() {  // DONE
        int size = 12 + (10 * degree);
        return size;
    }

    // Return an Iterable collection of the references to all of the children
    // of this node.  Do not put any null references into this result.  The
    // order of the children as stored in the TrieNodeInt<V> node must be
    // maintained in the returned Iterable.  The easiest way to do this is to 
    // put all of the references into a Queue and to return the Queue (since a
    // Queue implements Iterable and maintains the order of the children).
    // This method will allow us to access all of the children of a node without
    // having to know how the node is actually implemented.
    public Iterable<TrieNodeInt<V>> children() {    // DONE
        LinkedList<TrieNodeInt<V>> queue = new LinkedList<TrieNodeInt<V>>();
        for(Nodelet curr = front; curr != null; curr = curr.rightSib) {
            queue.add(curr.child);
        }
        return queue;
    }
}