package id.ac.itb.informatika.tugasawesome.utils;

import java.security.SecureRandom;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class ByteArrayOp {
    public static SecureRandom RANDOM = new SecureRandom();
    
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
     * Generate {size} random byte
     */
    public static byte[] randomByte(int size) {
        byte[] salt = new byte[size];
        RANDOM.nextBytes(salt);
        return salt;
    }
    
    public static String toHex(byte[] a) {
        return DatatypeConverter.printHexBinary(a);
    }
}
