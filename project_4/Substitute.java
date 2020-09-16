import java.util.*;

public class Substitute implements SymCipher {
    // Instance variables:
    private byte [] key;
    private byte [] revKey;

    // Constructor (without parameters):
    public Substitute() {
        // Create a 256 byte array:
        key = new byte[256];
        for(int i = 0; i < key.length; i++) {
            key[i] = (byte) i;
        }
        // Shuffle the array into a random permutation:
        shuffle(new Random((long) 1), key);

        // Reverse map values of key array to reverse key array:
        revKey = new byte[256];
        for (int i = 0; i < key.length; i++) {
            if(key[i] < 0) {
                revKey[(int) key[i] + 256] = (byte) i;
            }
            else {
                revKey[(int) key[i]] = (byte) i;
            }
        }
    }
    // Constructor (with byte array parameter):
    public Substitute(final byte[] keyArr) {
        key = keyArr;
        revKey = new byte[256];

        // Reverse map key array to reverse key array:
        for (int i = 0; i < key.length; i++) {
            if(key[i] < 0) {
                revKey[(int) key[i] + 256] = (byte) i;
            }
            else {
                revKey[(int) key[i]] = (byte) i;
            }
        }
    }

    // Return an array of bytes that represent the key for the cipher
    public byte[] getKey() {
        return key;
    }

    // Encode the string using the key and return the result as an array of
    // bytes. Note that you will need to convert the String to an array of bytes
    // prior to encrypting it:
    public byte[] encode(final String S) {
        // Convert String parameter to an array of bytes:
        byte[] bytes = S.getBytes();
        byte[] out = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) {
            // In case of negative byte values:
            byte curr = bytes[i];
            int index = curr;
            if(index < 0) {
                // Substitute the value of key at i plus offset of 256
                // in for bytes at i:
                index = index + 256;
            }
                out[i] = key[index];
        }
        // Return the encoded array:
        return out;
    }

    // Decrypt the array of bytes and generate and return the corresponding String.
    public String decode(final byte[] bytes) {
        // Reverse the subsitution using the revKey array:
        byte[] out = new byte[bytes.length];
        for(int i = 0; i < bytes.length; i++) {
            // In case of negative byte values:
            byte curr = bytes[i];
            int index = curr;
            if(index < 0) {
                // Substitute the value of key at i plus offset of 256
                // in for bytes at i:
                index = index + 256;
            }
            out[i] = revKey[index];
        }
        // Convert bytes back to a String:
		String S = new String(out);
		// Return String:
		return S;
    }

    public void shuffle(Random rand, byte[] arr) {
        int count = arr.length;
        for (int i = count; i > 1; i--) {
            swap(arr, i - 1, rand.nextInt(i));
        }
    }

    public void swap(byte[] arr, int i, int j) {
        byte temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}