package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Mapping {
    
    // actually this salt should be change in mapWordsToPoint funtion if 
    // hash result collide, but let's hope we lucky enough
    private static final String dummySalt = "TugasAwesome"; 
    
    /**
     * Return 256 bit hash value of word + salt as byte array
     * @param word
     * @param salt
     * @return 
     */
    public static byte[] Hash(byte[] word, byte[] salt) {
        byte[] hashWord = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] wordBytes = new byte[word.length + salt.length];
            wordBytes = Operations.addBytes(word, salt);
            md.update(wordBytes);
            
            hashWord = md.digest();
        } catch (Exception e) {
            System.err.format("Hash Exception : " + e.getMessage());
        }
        return hashWord;
    }
    
    /**
     * Convert all word in list words into list of pairwise distinct pointbyte
     * @param listWords
     * @return 
     */
    public static List<PointByte> wordsToPoint(List<String> listWords, BigInteger prime) {
        System.out.println("map words to point...");
        Set<PointByte> setPoint = new HashSet<>();
        byte[] salt = dummySalt.getBytes();
        
        //get all pairwise distict point
        while (setPoint.size() != listWords.size()) { 
            boolean repeat = false;
            for (String word : listWords) {
                byte[] hash = Hash(word.getBytes(), salt);
                PointByte point = new PointByte(hash, prime, true);
                
                if (!setPoint.add(point)) {
                    repeat = true;
                    break;
                } 
            }
            
            
            if (repeat) {
                //let's hope we'll never reach here
                //salt = ByteArrayOp.randomByte().toByteArray();
                
                salt = dummySalt.getBytes();
                setPoint.clear();
                System.out.println("Hash repeated with new salt : " 
                        + Operations.toHex(salt));
            }
        }
        System.out.println("end of map words to point...");
        return new ArrayList<>(setPoint);
    }
    
    /**
     * Create a polynomial by interpolating value of (x, y XOR z)
     * @param hashResult list of point (x, z)
     * @param shares list of point (x, y)
     * @param prime
     * @return 
     */
    public static GfPolynomial createMappingFunction(List<PointByte> hashResult,
                                                    List<PointByte> shares, BigInteger prime) {
        System.out.println("create mapping function...");
        
        if (hashResult.size() != shares.size()) {
            throw new IllegalArgumentException("Mapping error : hashResult and shares should be same size");
        }
        
        List<PointByte> points = new ArrayList<>();
        
        for (int i = 0; i< hashResult.size(); i++) {
            
            if (!hashResult.get(i).getX().equals(shares.get(i).getX())) {
                throw new IllegalArgumentException("Mapping Error : All X value in hashResult and shares should equal");
            } 
            
            
            BigInteger valY = hashResult.get(i).getY().xor(shares.get(i).getY());
            PointByte a = new PointByte(shares.get(i).getX(), valY, prime, false);
            points.add(a);
        }
        
        System.out.println("end of create mapping function...");
        return GfPolynomial.interpolatePolynomial(points, prime);
    }

    /**
     * Map one {word} to corresponding share by evaluating x in {poly}
     * @param poly
     * @param word
     * @param prime
     * @return 
     */
    public static PointByte getShare(GfPolynomial poly, String word, BigInteger prime) {
        byte[] hash = Hash(word.getBytes(), dummySalt.getBytes());
        PointByte hashPoint = new PointByte(hash, prime, true);
        BigInteger valY = poly.evaluatePolynomial(hashPoint.getX()).xor(hashPoint.getY());
        return new PointByte(hashPoint.getX(), valY, prime, false);
    }

}
