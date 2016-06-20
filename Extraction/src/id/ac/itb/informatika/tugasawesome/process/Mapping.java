package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import java.math.BigInteger;
import java.security.MessageDigest;
/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Mapping {
    
    // actually this salt should be change in mapWordsToPoint funtion if 
    // hash result collide, but let's hope we lucky enough
    private static final String DUMMYSALT = "TugasAwesome"; 
    
    /**
     * Return 256 bit hash value of word + salt as byte array
     * @param word
     * @param salt
     * @return 
     */
    private static byte[] Hash(byte[] word, byte[] salt) {
        byte[] hashWord = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] wordBytes = Operations.addBytes(word, salt);
            md.update(wordBytes);
            
            hashWord = md.digest();
        } catch (Exception e) {
            System.err.format("Hash Exception : " + e.getMessage());
        }
        return hashWord;
    }
    

    /**
     * Map one {word} to corresponding share by evaluating x in {poly}
     * @param poly
     * @param word
     * @param prime
     * @return 
     */
    public static PointByte getShare(GfPolynomial poly, String word) {
        byte[] hash = Hash(word.getBytes(), DUMMYSALT.getBytes());
        PointByte hashPoint = new PointByte(hash, poly.getPrime(), false);
        int idx = hashPoint.getX().mod(new BigInteger("15")).intValueExact();
        BigInteger valY = poly.evaluatePolynomial(hashPoint.getX()).xor(hashPoint.getY());
        return new PointByte(hashPoint.getX(), valY, poly.getPrime(), false);
    }
    
    public static int getSubFunctionIdx(String word) {
        byte[] hash = Hash(word.getBytes(), DUMMYSALT.getBytes());
        PointByte hashPoint = new PointByte(hash, BigInteger.ONE, false);
        int idx = hashPoint.getX().mod(new BigInteger("16")).intValueExact();
        return idx;
    }

}
