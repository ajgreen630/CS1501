// CS 1501 Summer 2020
// Demonstration of using a message digest in Java.  See also MessageDigest
// in the Java API for more information

import java.security.*;

public class MD
{
	public static void main(String [] args) throws Exception
	{
		String S1 = new String("Here is the original string!  Cool!");
		String S2 = new String("Here is the original string!  Cool!");
		String S3 = new String("Here is che original string!  Cool!");

		MessageDigest m1 = MessageDigest.getInstance("SHA-256");
		MessageDigest m2 = MessageDigest.getInstance("SHA-256");
		MessageDigest m3 = MessageDigest.getInstance("SHA-256");

		byte [] b1 = S1.getBytes();
		byte [] b2 = S2.getBytes();
		byte [] b3 = S3.getBytes();

		m1.update(b1);  // Adding strings to digests
		m2.update(b2);
		m3.update(b3);

		byte [] digest1 = m1.digest();  // Completing digests / signatures
		byte [] digest2 = m2.digest();
		byte [] digest3 = m3.digest();

		System.out.println(digest1.length);

		if (MessageDigest.isEqual(digest1, digest2))
			System.out.println("Equal");
		if (MessageDigest.isEqual(digest1, digest3))
			System.out.println("Equal");
		else
			System.out.println("Not equal");
	}
}

	
