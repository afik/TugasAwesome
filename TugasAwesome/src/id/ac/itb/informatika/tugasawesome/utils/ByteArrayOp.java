package id.ac.itb.informatika.tugasawesome.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class ByteArrayOp {
    
    public static SecureRandom RANDOM = new SecureRandom();
    public static final int SIZE = 16;
     private static int BITLENGTH = 128;
    
    public static byte[] addBytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
    
    /**
     * Get first {size} byte of a
     */
    public static byte[] getFirstBlock(byte[] a, int size) {
        byte[] c = new byte[size];
        System.arraycopy(a, 0, c, 0, size);
        return c;
    }
    
    /**
     * Convert byte array to hexstring
     */
    public static String toHex(byte[] a) {
        return DatatypeConverter.printHexBinary(a);
    }
    
    /**
     * Generate random byte array with h between 0 to {size}
     */
    public static BigInteger randomByte() {
        BigInteger randomValue;
        do {
            randomValue = new BigInteger(BITLENGTH, RANDOM);
        }  while (randomValue.compareTo(BigInteger.ZERO) <0 || 
                  randomValue.bitLength() > BITLENGTH);
        
        return randomValue;
    }
    
    /**
     * Generate random prime number in byte with size <= {size}
     */
    public static BigInteger randomPrime(BigInteger secret) {
        BigInteger prime;
        if (secret == null ) {
            prime = BigInteger.probablePrime(BITLENGTH, RANDOM);
        } else {
            do {
                prime = BigInteger.probablePrime(BITLENGTH, RANDOM);
            } while (prime.compareTo(secret) <= 0);
        }
        return prime;
    }
    
}
