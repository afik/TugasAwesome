package id.ac.itb.informatika.tugasawesome.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Operations {
    
    private static SecureRandom RANDOM = new SecureRandom();
    private static final int SIZE = 16;
    private static final int BITLENGTH = 128;
    
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
     * Combination {threshold} element from 1 - {nShares} integer
     * @param nShares
     * @param threshold
     * @return 
     */
    public static List<List<Integer>> getCombinationIdx(int nShares, int threshold) {
        List<List<Integer>> result = new ArrayList<>();
        
        if (threshold == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        
        for(int val = threshold; val <= nShares; val++) {
            List<List<Integer>> sub = getCombinationIdx(val-1, threshold-1);
            for(int i=0; i< sub.size(); i++) {
                sub.get(i).add(val);
            }
            result.addAll(result.size(), sub);
        }
        
        return result;
    }
    
    
}
