package id.ac.itb.informatika.tugasawesome.protection.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.protection.utils.Operations;
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
     * Convert all word in list words into list of pairwise distinct pointbyte
     * @param listWords
     * @param prime
     * @return 
     */
    public static List<PointByte> wordsToPoint(List<String> listWords, BigInteger prime) {
        Set<PointByte> setPoint = new HashSet<>();
        byte[] salt = DUMMYSALT.getBytes();
        
        //get all pairwise distict point
        while (setPoint.size() != listWords.size()) { 
            boolean repeat = false;
            for (String word : listWords) {
                byte[] hash = Hash(word.getBytes(), salt);
                PointByte point = new PointByte(hash, prime, false);
                
                
                if (!setPoint.add(point)) {
                    repeat = true;
                    break;
                } 
            }
            
            if (repeat) {
                //let's hope we'll never reach here
                //salt = ByteArrayOp.randomByte().toByteArray();
                
                salt = DUMMYSALT.getBytes();
                setPoint.clear();
                System.out.println("Hash repeated with new salt : " 
                        + Operations.toHex(salt));
            }
        }
        
        return new ArrayList<>(setPoint);
    }
    
    /**
     * Create list of subpolynomial by interpolating value of (x, y XOR z)
     * @param hashResult list of point (x, z)
     * @param shares list of point (x, y)
     * @param prime
     * @return 
     */
    public static List<GfPolynomial> createMappingFunction(List<PointByte> hashResult,
                                                    List<PointByte> shares, BigInteger prime) {
        
        if (hashResult.size() != shares.size()) {
            throw new IllegalArgumentException("Mapping error : hashResult and shares should be same size");
        }
        
        List<GfPolynomial> result = new ArrayList<>();
        
        List<List<PointByte>> points = new ArrayList<>();
        for (int i=0; i<16; ++i) {
            List<PointByte> el = new ArrayList<>();
            points.add(el);
        }
        
        //find point for each corresponding subfuntion
        for (int i = 0; i< hashResult.size(); i++) {
            if (!hashResult.get(i).getX().equals(shares.get(i).getX())) {
                throw new IllegalArgumentException("Mapping Error : All X value in hashResult and shares should equal");
            } 
            BigInteger valY = hashResult.get(i).getY().xor(shares.get(i).getY());
            PointByte a = new PointByte(shares.get(i).getX(), valY, prime, false);
            BigInteger index = hashResult.get(i).getX().mod(new BigInteger("16"));
            points.get(index.intValueExact()).add(a);
        }
        
        //interpolate each subfunction
        points.stream().forEach((p) -> {
            result.add(GfPolynomial.interpolatePolynomial(p, prime));
        });
        
        return result;
    }

}
