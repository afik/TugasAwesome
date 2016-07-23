package id.ac.itb.informatika.tugasawesome.protection.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;

/**
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Operations {
    
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int BITLENGTH = 128;
    
    public static byte[] addBytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
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
    
}
