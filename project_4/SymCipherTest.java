// CS 1501 Summer 2020
// This program tests the SymCipher interface and your two implementations,
// Substitute and Add128.

// This program should run correctly with your Substitute and Add128 classes.  Use
// it to test them before incorporating them into your overall secure client.

import java.math.*;
import java.util.*;

public class SymCipherTest
{
	public static void main (String [] args)
	{
		System.out.println("Testing Substitution Cipher\n");
		String test1 = new String("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuzwxyzABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ012345678901234567890123");
		String test2 = new String("+-)(*&^%$#@!");
		
		SymCipher sim = new Substitute();  // this should generate a random key
		
		System.out.println("Original Text (before encryption): \n" + test1 + "\n");
		
		// Get encoded bytes and show then (for testing purposes)
		byte [] bytes1 = sim.encode(test1);
		System.out.println("Encoded bytes: ");
		for (int i = 0; i < bytes1.length; i++)
		{
			System.out.print(bytes1[i] + " ");
		}
		System.out.println("\n");
		
		// Now we will get the key from the cipher and make a new Substitute object out
		// of it to test the 2nd constructor.
		byte [] subKey = sim.getKey();  // get the key from the cipher
		System.out.println("Substitution Cipher Key (as bytes): ");
		for (int i = 0; i < subKey.length; i++)
			System.out.print(subKey[i] + " ");
		System.out.println("\n");
		
		// Now we will make a new object using the same key
		SymCipher sim2 = new Substitute(subKey);
		
		// Now we will decode the bytes with the sim2 object
		String restore1 = sim2.decode(bytes1);
		System.out.println("Restored String: \n" + restore1 + "\n");
		
		// Note the length of the String and the byte array.  This should not affect your
		// program as long as you process the data 1 byte at a time
		System.out.println("Original Text (before encryption): \n" + test2);
		System.out.println("Original Text contains " + test2.length() + " characters\n");
		
		// Note: Java Strings are arrays of 2-byte characters.  However, the getBytes()
		// method is platform specific and may actually produce a single byte per char
		// if the "left" byte was not needed.  Because this behavior is different on Macs
		// and on Windows, we will limite our Strings to "regular" ASCII (up to 127).
		// However, your Substitute class should still encode / decode arbitrary bytes
		// (i.e. it should have 256 values in the key)
		byte [] origBytes = test2.getBytes();
		System.out.println("Original bytes: ");
		for (int i = 0; i < origBytes.length; i++)
		{
			System.out.print(origBytes[i] + " ");
		}
		System.out.println();
		System.out.println("There are " + origBytes.length + " bytes\n");
		
		// Encode the 2nd string as another test
		byte [] bytes2 = sim.encode(test2);
		System.out.println("Encoded bytes: ");
		for (int i = 0; i < bytes2.length; i++)
		{
			System.out.print(bytes2[i] + " ");
		}
		System.out.println("\n");
		String restore2 = sim2.decode(bytes2);
		System.out.println("Restored String: " + restore2 + "\n");
		
		System.out.println("Testing Add128 Cipher\n");
		sim = new Add128();      // this should generate a random key
		bytes1 = sim.encode(test1);
		
		byte [] addKey = sim.getKey();  // get the key from the cipher
		System.out.println("Add128 Cipher Key (as bytes): ");
		for (int i = 0; i < addKey.length; i++)
			System.out.print(addKey[i] + " ");
		System.out.println("\n");
		
		// Make a new Add128 object with the same key
		sim2 = new Add128(addKey);
		
		restore1 = sim2.decode(bytes1);
		System.out.println("Restored String (add128): " + restore1);
		System.out.println();
		
		// Now test second String
		bytes2 = sim.encode(test2);
		restore2 = sim2.decode(bytes2);
		System.out.println("Restored String (add128): " + restore2);
		
		
		StringBuilder longString = new StringBuilder("");
		for (int i = 0; i < 500; i++)
		{
			char val = (char) ('A' + i % 26);
			longString.append(val);
		}
		String test3 = longString.toString();
		
		System.out.println("\nTesting block handling of Add128");
		System.out.println("Original String:");
		System.out.println(test3);
		
		bytes1 = sim.encode(test3);
		restore1 = sim.decode(bytes1);
		System.out.println("Restored String:");
		System.out.println(restore1);
	}
}
