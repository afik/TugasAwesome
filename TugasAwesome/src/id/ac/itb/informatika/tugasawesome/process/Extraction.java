package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
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
        System.out.println("Perform extraction........");
        //E.1 Compute each share
        List<PointByte> allShare = new ArrayList<>();
        System.out.println("hash guess");
        for (String word : guess) {
            PointByte p = Mapping.getShare(poly, word);
            allShare.add(p);
        }
        
        //E.2 Compute key
        
        BigInteger key = Shamir.recoverKey(allShare, poly.getPrime());
        System.out.println("Key rec " + key);
        
        //E.2 Try to decrypt
        if (Encryption.checkFirstBlock(ByteArrayOp.getFirstBlock(cipher, 16), key.toByteArray())) {
            byte[] decrypted = Encryption.decrypt(cipher, key.toByteArray());
            byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
            return new String(plain2);
        } else {
            return null;   
        }
    }
}
