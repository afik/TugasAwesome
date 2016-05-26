package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
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
    
    public static int aaa;
    public static int bbb;
    public static int ccc;
    
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
            wordBytes = ByteArrayOp.addBytes(word, salt);
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
    public static List<PointByte> wordsToPoint(List<String> listWords) {
        Set<PointByte> setPoint = new HashSet<>();
        byte[] salt = dummySalt.getBytes();
        PointByte p = new PointByte();
        PointByte pp = new PointByte();
        PointByte ppp = new PointByte();
        //byte[] salt = ByteArrayOp.randomByte().toByteArray();
        
        //get all pairwise distict point
        while (setPoint.size() != listWords.size()) { 
            boolean repeat = false;
            for (String word : listWords) {
                byte[] hash = Hash(word.getBytes(), salt);
                PointByte point = new PointByte(hash);
                if ("sollicitudin".equals(word)) {
                    p.setX(point.getX());
                    p.setY(point.getY());
                    System.out.println("Hash sol " + point);
                }
                if ("faucibus".equals(word)) {
                    pp.setX(point.getX());
                    pp.setY(point.getY());
                    System.out.println("Hash fa " + point);
                }
                if ("non".equals(word)) {
                    ppp.setX(point.getX());
                    ppp.setY(point.getY());
                    System.out.println("Hash non " + point);
                }
                
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
                        + ByteArrayOp.toHex(salt));
            }
        }
        List<PointByte> aa = new ArrayList<>(setPoint);
        aaa = aa.indexOf(p);
        bbb = aa.indexOf(pp);
        ccc = aa.indexOf(ppp);
        return new ArrayList<>(setPoint);
    }
    
    /**
     * Create a polynomial by interpolating value of (x, y XOR z)
     * @param hashResult list of point (x, z)
     * @param shares list of point (x, y)
     * @return 
     */
    public static GfPolynomial createMappingFunction(List<PointByte> hashResult,
                                                    List<PointByte> shares, BigInteger prime) {
        if (hashResult.size() != shares.size()) {
            throw new IllegalArgumentException("Mapping error : hashResult and shares should be same size");
        }
        
        List<PointByte> points = new ArrayList<>();
        
        for (int i = 0; i< hashResult.size(); i++) {
            
            if (!hashResult.get(i).getX().equals(shares.get(i).getX())) {
                throw new IllegalArgumentException("Mapping Error : All X value in hashResult and shares should equal");
            } 
            
//            if (i == Mapping.aaa || i == Mapping.bbb || i == Mapping.ccc) {
//                System.out.println("i : " + i);
//                System.out.println("H " + hashResult.get(i));
//                System.out.println("S " + shares.get(i));
//            }
            
            BigInteger valY = hashResult.get(i).getY().xor(shares.get(i).getY());
            PointByte a = new PointByte(shares.get(i).getX(), valY);
            points.add(a);
        }
        
        
        return GfPolynomial.interpolatePolynomial(points, prime);
    }

    /**
     * Map one {word} to corresponding share by evaluating x in {poly}
     * @param poly
     * @param word
     * @return 
     */
    public static PointByte getShare(GfPolynomial poly, String word) {
        byte[] hash = Hash(word.getBytes(), dummySalt.getBytes());
        PointByte hashPoint = new PointByte(hash);
//        System.out.println("hash " + word + " " +hashPoint);
        BigInteger valY = poly.evaluatePolynomial(hashPoint.getX()).xor(hashPoint.getY());
        return new PointByte(hashPoint.getX(), valY);
    }

}
