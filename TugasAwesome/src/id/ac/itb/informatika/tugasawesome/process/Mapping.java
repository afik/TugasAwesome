package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import java.security.MessageDigest;
import java.security.SecureRandom;
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
        boolean repeat = false;
        Set<PointByte> setPoint = new HashSet<>();
        byte[] salt = ByteArrayOp.randomByte(16);
        
        //get all pairwise distict point
        while (setPoint.size() != listWords.size()) { 
            for (String word : listWords) {
                byte[] hash = Hash(word.getBytes(), salt);
                PointByte point = new PointByte(hash);
                
                if (!setPoint.add(point)) {
                    repeat = true;
                    break;
                } else {
                    System.out.println("Add : "); point.print();
                    System.out.println("Set size : " + setPoint.size()); 
                }
            }
            
            if (repeat) {
                salt = ByteArrayOp.randomByte(16);
                setPoint.clear();
                System.out.println("Hash repeated with new salt : " 
                        + ByteArrayOp.toHex(salt));
            }
        }
        
        return new ArrayList<>(setPoint);
    }
  
}
