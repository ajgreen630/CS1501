import java.util.*;

public class Add128 implements SymCipher {
	// Instant variables:
	private byte [] key;

	// Constructor (without parameters):
	public Add128() {
		// Create a random 128-byte additive key,
		// store within array of bytes:
		key = new byte [128];

		Random rnd = new Random((long) 1);
		rnd.nextBytes(key);			// Generates random bytes
									// and places them into byte array.
	}
	// Constructor (with byte array parameter):
	public Add128(byte [] keyArr) {
		key = keyArr;
	}

	// Return an array of bytes that represent the key for the cipher
	public byte [] getKey() {
		return key;
	}
	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	// prior to encrypting it.  Also note that String S could have an arbitrary
	// length, so your cipher may have to "wrap" when encrypting (remember that
	// it is a block cipher)
	public byte [] encode(String S) {
		// Convert S to array of bytes:
		byte [] bytes = S.getBytes();
		for(int i = 0; i < bytes.length; i++) {
			// If the index in message is outside of the bounds
			// of the key:
			if(i > key.length - 1) {
				// Wrap around the key:
				int j = i % key.length;
				// Add the corresponding byte of the key
				// to the current index of bytes:
				bytes[i] = (byte) (bytes[i] + key[j]);
			}
			else {
				// Add the corresponding byte of the key
				// to the current index of bytes:
				bytes[i] = (byte) (bytes[i] + key[i]);
			}
		}
		// Return the encoded String:
		return bytes;
	}
	
	// Decrypt the array of bytes and generate and return the corresponding String.
	public String decode(byte [] bytes) {
		for(int i = 0; i < bytes.length; i++) {
			// If the index in message is outside of the bounds
			// of the key:
			if(i > key.length - 1) {
				// Wrap around the key:
				int j = i % key.length;
				// Substract the corresponding byte of the key
				// from the current index of bytes:
				bytes[i] = (byte) (bytes[i] - key[j]);
			}
			else {
				// Subtract the corresponding byte of the key
				// from the current index of bytes:
				bytes[i] = (byte) (bytes[i] - key[i]);
			}
		}
		// Convert bytes back to a String:
		String S = new String(bytes);
		// Return String:
		return S;
	}
}