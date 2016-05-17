package id.ac.itb.informatika.tugasawesome.process;

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
            e.printStackTrace();
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
        byte[] salt = ByteArrayOp.randomByte().toByteArray();
        
        //get all pairwise distict point
        while (setPoint.size() != listWords.size()) { 
            boolean repeat = false;
            for (String word : listWords) {
                byte[] hash = Hash(word.getBytes(), salt);
                PointByte point = new PointByte(hash);
                
                if (!setPoint.add(point)) {
                    repeat = true;
                    break;
                } 
            }
            
            if (repeat) {
                salt = ByteArrayOp.randomByte().toByteArray();
                setPoint.clear();
                System.out.println("Hash repeated with new salt : " 
                        + ByteArrayOp.toHex(salt));
            }
        }
        
        return new ArrayList<>(setPoint);
    }
  
}
