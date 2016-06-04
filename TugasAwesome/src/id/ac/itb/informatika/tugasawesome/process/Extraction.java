package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Extraction {
    
    public static String extract(byte[] cipher, GfPolynomial poly, List<String> guess, int threshold) {
        System.out.println("extract...");
        
        if (guess.size() < threshold) {
            System.err.format("minimal number of guess is " + threshold);
            return null;
        }
        
        //E.1 Compute each share
        List<PointByte> allShare = new ArrayList<>();
        for (String word : guess) {
            PointByte p = Mapping.getShare(poly, word.toLowerCase(), poly.getPrime());
            allShare.add(p);
        }
        
        //find all combination of shares
        List<List<Integer>> shareIdx = Operations.getComposition(allShare.size(), threshold);
        
        
        BigInteger key = null; 
        boolean success = false;
        
        //try share combination
        for (int i = 0; i<shareIdx.size(); i++) {
            
            List<PointByte> subShare = new ArrayList<>();
            for (Integer sidx : shareIdx.get(i)) {
                subShare.add(allShare.get(sidx-1));
            }
            
            //E.2 Compute key
            key =  Shamir.recoverKey(subShare, poly.getPrime());
            
            //valid key length is <= 127 
            if (key.bitLength() > 127) {
                continue;
            }
            
            System.out.println("Key recovered: " + key);
        
            //E.2 Try to decrypt
            if (Encryption.checkFirstBlock(Operations.getFirstBlock(cipher, 16), key.toByteArray())) {
                success = true;
                break;
            } 
            
        }

        if (success) {
            byte[] decrypted = Encryption.decrypt(cipher, key.toByteArray());
            byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
            System.out.println("end of extract...");
            return new String(plain2);
        } else {
            System.out.println("end of extract...");
            return null;   
        }


    }
}
