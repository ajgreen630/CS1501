// CS 1501 Summer 2020
// Simple demo of generating and using RSA public key encryption

import java.math.*;
import java.util.*;
public class RSATest
{
	public static void main(String [] args)
	{	
		// Example from lecture notes
		BigInteger one = new BigInteger("1");
		BigInteger X = new BigInteger("7");
		BigInteger Y = new BigInteger("11");
		BigInteger N = X.multiply(Y);
		BigInteger PHI = X.subtract(one).multiply(Y.subtract(one));
		BigInteger E = new BigInteger("37");
	
		// The modInverse method is calculating D.  The algorithm used is
		// abstracted out of our view but it is likely that it is using some
		// variant of the Extended GCD algorithm as discussed in lecture
		BigInteger D = E.modInverse(PHI);
		
		// The decryption keys are not unique.  Any multiple of PHI added
		// to the original D will also work.  Note however, that if the
		// cryptanalyst does not know PHI this will be very difficult to
		// find for large numbers.
		BigInteger D2 = D.add(PHI);	 		// add PHI
		BigInteger D3 = D2.add(PHI);		// add PHI again
		
		// Try a simple test number.  Note that the number being encrypted 
		// must be smaller than N since we take our result mod N.
		BigInteger plain = new BigInteger("68");
		testRSA(plain, E, N, D);
		testRSA(plain, E, N, D2);
		testRSA(plain, E, N, D3);
		
		// Now test with a key of a more realistic size
		testRandomKey();
	}
	
	public static void testRSA(BigInteger plain, BigInteger E, BigInteger N, BigInteger D)
	{
		System.out.println("E: " + E);
		System.out.println("N: " + N);
		System.out.println("D: " + D);
		BigInteger cipher = plain.modPow(E, N);
		BigInteger orig = cipher.modPow(D, N);
		System.out.println("Plaintext: " + plain);
		System.out.println("Ciphertext: " + cipher);
		System.out.println("Decrypted: " + orig);
		System.out.println();
	}
	
	public static void testRandomKey()
	{
		BigInteger one = new BigInteger("1");
		BigInteger X, Y, N, PHI, E, D;
		Random R = new Random();
		
		// Generate random prime X and Y of size 1024 bits
		X = new BigInteger(1024, 100, R);
		Y = new BigInteger(1024, 100, R);
		N = X.multiply(Y);	// Calculate N = XY
	
		// Calculate PHI = (X-1)(Y-1)
		PHI = (X.subtract(one)).multiply(Y.subtract(one));
		
		// Make sure random prime E is less than PHI and they are 
		// relatively prime
		E = new BigInteger(1024, 100, R);
		while ((E.compareTo(PHI) >= 0) || !(PHI.gcd(E)).equals(one))
		{
			E = new BigInteger(1024, 100, R);
		}
		D = E.modInverse(PHI);			// get D
		BigInteger D2 = D.add(PHI);		// add PHI to D
		
		BigInteger plain = new BigInteger("123456789");
		testRSA(plain, E, N, D);
		testRSA(plain, E, N, D2);
	}
}
