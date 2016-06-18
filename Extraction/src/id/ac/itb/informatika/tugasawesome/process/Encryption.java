package id.ac.itb.informatika.tugasawesome.process;

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Encryption {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int SIZE = 128;
    private static final byte[] PAD = new byte[SIZE/8];
    private static final String IV = "boomboomboomboom"; 
   
    
    public static byte[] decrypt(byte[] encrypted, byte[] key) {
        byte[] plain = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            plain =  cipher.doFinal(encrypted);
        } catch (Exception e) {
            System.err.format("Decrypt exception : " + e.getMessage());
        }
        return plain;
    }
    
    /**
     * Decrypt first 16 bytes of cipher text and match it to padding value
     * @param firstBlock
     * @param key
     * @return true if decrypted block match to padding value 
     */
    public static boolean checkFirstBlock(byte[] firstBlock, byte[] key) {
        byte[] plain = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            plain =  cipher.doFinal(firstBlock);
        } catch (Exception e) {
            System.err.format("Encrypt exception : " + e.getMessage());
        }
        return Arrays.equals(plain, PAD);
    }
    
}
