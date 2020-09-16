// CS 1501 Summer 2020
// Comparison of PowerMod function done iteratively, recursively in a simple
// way, and using Divide and Conquer.  The BigInteger class is used here so
// that we can get some very large numbers.  Note that these are designed
// for use with positive integers only.

import java.math.BigInteger;
import java.io.*;

public class Power
{
    private BigInteger pow1mults, pow2mults, pow3mults, pow4mults;
    public static final BigInteger zero = BigInteger.ZERO;
    public static final BigInteger one = BigInteger.ONE;
    public static final BigInteger two = new BigInteger("2");

    public Power() throws IOException
    {
          pow1mults = zero;
          pow2mults = zero;
          pow3mults = zero;
		  pow4mults = zero;

          BufferedReader indata = new BufferedReader(
                         new InputStreamReader(System.in));
          System.out.println("Enter the integer base: ");
          BigInteger base = new BigInteger(indata.readLine());
          System.out.println("Enter the integer exponent: ");
          BigInteger exp = new BigInteger(indata.readLine());
          System.out.println("Enter the mod value: ");
          BigInteger mod = new BigInteger(indata.readLine());
          System.out.println();
          BigInteger ans;

          ans = Pow1Mod(base, exp, mod);
          System.out.println(base + "^" + exp + " mod " + mod + " iter = \n" + ans);
          System.out.println("Requires " + pow1mults + " mults \n");  

  		  ans = Pow2Mod(base, exp, mod);
          System.out.println(base + "^" + exp + " mod " + mod + " rec = \n" + ans);
          System.out.println("Requires " + pow2mults + " mults \n");

	  	  ans = Pow3Mod(base, exp, mod);
          System.out.println(base + "^" + exp + " mod " + mod + " div/conq = \n" + ans);
          System.out.println("Requires " + pow3mults + " mults \n");        
       
  		  ans = Pow4Mod(base, exp, mod);
		  System.out.println(base + "^" + exp + " mod " + mod + " bottom up = \n" + ans);
		  System.out.println("Requires " + pow4mults + " mults \n");
          
          ans = base.modPow(exp, mod);
          System.out.println(base + "^" + exp + " mod " + mod + " predef. = \n" + ans);
          
    }

    public static void main (String [] args) throws IOException
    {
          Power P = new Power();
    }

	// Use a simple for loop to calculate X^N mod M
    public BigInteger Pow1Mod(BigInteger X, BigInteger N, BigInteger M)
    {
          BigInteger i, temp;
          temp = one;

       // If using regular ints, the loop below would look like:
       // for (i = 1; i <= N; i++)

          for (i = one; i.compareTo(N) <= 0; i = i.add(one))
          {
               temp = (temp.multiply(X)).mod(M);
               pow1mults = pow1mults.add(one);
          }
          return temp;
    }

	// Use a simple recursive function to calculate X^N mod M -- although
	// recursive the effect of this method is very similar to that
	// of Pow1 above.  For large N this will likely cause an exception due
	// to running out of stack space.
    public BigInteger Pow2Mod(BigInteger X, BigInteger N, BigInteger M)
    {
          if (N.compareTo(zero) == 0)
                return new BigInteger("1");
          else
          {
                pow2mults = pow2mults.add(one);
                return (X.multiply(Pow2Mod(X, N.subtract(one),M)).mod(M));
                      // X * Pow2(X, N-1)
          }
    }
    
	// Divide and conquer function to calculate X^N mod M
    public BigInteger Pow3Mod(BigInteger X, BigInteger N, BigInteger M)
    {
          if (N.compareTo(zero) == 0)
                return new BigInteger("1");
          {
                BigInteger NN = N.divide(two);
                BigInteger temp = Pow3Mod(X, NN, M);
                pow3mults = pow3mults.add(one);
                if ((N.mod(two)).compareTo(zero) == 0) // N is even
                {
                	BigInteger mVal = temp.multiply(temp);
                	BigInteger retVal = mVal.mod(M);
                    return (retVal);      // temp * temp
                }
                else                                   // N is odd
                {
                    pow3mults = pow3mults.add(one);
                    BigInteger MM = (temp.multiply(temp)).mod(M);
                    MM = (MM.multiply(X)).mod(M);
                    return (MM);           // X * temp * temp
                }
          }
    }

	// "Bottom up" approach to multiplication -- same principle as the divide
	// and conquer, but without the overhead of the recursive calls.
	public BigInteger Pow4Mod(BigInteger X, BigInteger N, BigInteger M)
	{
		BigInteger ans = one;
		for (int j = N.bitLength() - 1; j >= 0; j--)
		{
			ans = (ans.multiply(ans)).mod(M);
			pow4mults = pow4mults.add(one);
			if (N.testBit(j))  // if bit is a 1
			{
				ans = (ans.multiply(X)).mod(M);
				pow4mults = pow4mults.add(one);
			}
		}
		return ans;
	}
}
