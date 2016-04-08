package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
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
    
    public static byte[] encrypt(byte[] plain, byte[] key, String IV) {
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            
            encrypted =  cipher.doFinal(ByteArrayOp.addBytes(PAD, plain));
        } catch (Exception e) {
            System.err.format("Encrypt exception : " + e.getMessage());
            e.printStackTrace();
        }
        return encrypted;
    }
    
    public static byte[] decrypt(byte[] encrypted, byte[] key, String IV) {
        byte[] plain = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            plain =  cipher.doFinal(encrypted);
        } catch (Exception e) {
            System.err.format("Decrypt exception : " + e.getMessage());
            e.printStackTrace();
        }
        return plain;
    }
    
    public static boolean checkFirstBlock(byte[] firstBlock, byte[] key, String IV) {
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
    
    public static byte[] generateKey() {
        byte[] keyByte = null;
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            generator.init(SIZE);
            SecretKey key = generator.generateKey();
            
            keyByte = key.getEncoded();
        } catch (Exception e) {
            System.err.format("Generate key exception : " + e.getMessage());
        }
        return keyByte;
    }
    
}
