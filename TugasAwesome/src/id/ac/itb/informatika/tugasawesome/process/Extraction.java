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
    
    public static String extract(byte[] cipher, GfPolynomial poly, List<String> guess) {
        System.out.println("extract...");
        //E.1 Compute each share
        List<PointByte> allShare = new ArrayList<>();
        for (String word : guess) {
            PointByte p = Mapping.getShare(poly, word.toLowerCase(), poly.getPrime());
            allShare.add(p);
        }
        
        //E.2 Compute key
        BigInteger key =  Shamir.recoverKey(allShare, poly.getPrime());
        
        //valid key length is <= 127 
        if (key.bitLength() > 127) {
            System.err.format("Key recovered > 128");
            return null;
        }
        
        System.out.println("Key recovered: " + key);
        //E.2 Try to decrypt
        if (Encryption.checkFirstBlock(Operations.getFirstBlock(cipher, 16), key.toByteArray())) {
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
