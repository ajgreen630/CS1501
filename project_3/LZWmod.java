/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/
import java.io.*;
import java.util.*;

public class LZWmod {
    private static final int R = 256;       // number of input chars
    private static int currL = 512;      // number of codewords = 2^W
    private static int currW = 9;
    private static final int maxL = 65536;      // number of codewords = 2^W
    private static final int maxW = 16;        // codeword width

    public static void compress(String tag) {   // parameter tag to indicate reset status
        TST<Integer> st = new TST<Integer>();
        StringBuilder input = new StringBuilder();

        for(int i = 0; i < R; i++) {
            input.append((char) i);     // put i in symbol table
            st.put(input, i);
            input.delete(0, input.length());    // clear input
        }
        int code = R + 1;

        if(tag.equals("r")) BinaryStdOut.write('r', 9);     // write 'r' to file
        else if(tag.equals("n")) BinaryStdOut.write('n', 9);     // write 'n' to file
        // CHECK: Is this line necessary?
        input.append(BinaryStdIn.readChar());   // read in first char

        while(!BinaryStdIn.isEmpty()) {     // while input not at end of file
            // find max valid prefix
            char ch = BinaryStdIn.readChar();
            input.append(ch);   // append next char

            if(!st.contains(input)) {   // if st does not contain current input
                input.deleteCharAt(input.length() - 1); // delete appended char
                BinaryStdOut.write(st.get(input), currW); // write previous input to file
                input.append(ch); // re append char
                    
                if(!BinaryStdIn.isEmpty() && code < maxL) {   // if not at codeword limit
                        if(code == currL) {
                            currL = currL * 2;      // increase curr limit
                            currW = currW + 1;      // increase curr width
                        }
                    st.put(input, code++);  // put input into st, update code
                }
                else if(tag.equals("r") && code == maxL && currW == maxW) {  // check for reset
                    TST<Integer> newST = new TST<Integer>();        // create new TST
                    StringBuilder builder = new StringBuilder();    // temp string builder
                
                    for(int i = 0; i < R; i++) { 
                        builder.append((char) i);     // put i in symbol table
                        newST.put(builder, i);
                        builder.delete(0, builder.length());    // clear input
                    }

                    st = newST;     // set st to newST
                    code = R + 1;   // reset code
                    currL = 512;    // reset currL
                    currW = 9;      // reset currW
                }
                input.delete(0, input.length()-1);  // scan past input to last char
            }
        }
        BinaryStdOut.write(st.get(input), currW);
        BinaryStdOut.write(R, currW);
        BinaryStdOut.close();

        /* Old code:
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), currW);      // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < maxL) {    // Add s to symbol table.
                if (code == currL) {
                    currL = currL * 2;
                    currW = currW + 1;
                }
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, currW);
        BinaryStdOut.close();*/
    } 

    public static void expand() {
        String[] st = new String[maxL];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++) {
            st[i] = "" + (char) i;
        }
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(currW);
        String tag = st[codeword];
        
        codeword = BinaryStdIn.readInt(currW);
        String val = st[codeword];
        BinaryStdOut.write(val);
        
        while (true) {
            if(tag.equals("r") && i == maxL && currW == maxW) { // check for reset
                String[] newST = new String[maxL];
                int j;

                for(j = 0; j < R; j++) newST[j] = "" + (char) j;
                newST[j++] = "";

                st = newST;
                i = j;
                currL = 512;
                currW = 9;
                
                codeword = BinaryStdIn.readInt(currW);  // read in next codeword
                val = st[codeword];
                BinaryStdOut.write(val);    // write val to file
            }
            else if(i < maxL && i == currL) {   // check to update currL and currW
                currL = currL * 2;
                currW = currW + 1;
            }
            codeword = BinaryStdIn.readInt(currW);  // read in next codeword

            if(codeword == R) break;    // end of file

            String s = st[codeword];
            if(i == codeword) s = val + val.charAt(0);  // hack if expand is behind
            if(i < currL) st[i++] = val + s.charAt(0);

            val = s;    // set val to updated s
            BinaryStdOut.write(val);    // write val to file
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress(args[1]);
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
