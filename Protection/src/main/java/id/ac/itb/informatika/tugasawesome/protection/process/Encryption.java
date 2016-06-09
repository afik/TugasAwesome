package id.ac.itb.informatika.tugasawesome.protection.process;

import id.ac.itb.informatika.tugasawesome.protection.utils.Operations;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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
    private static final String IV = "boomboomboomboom"; 
    
    public static byte[] encrypt(byte[] plain, byte[] key) {
        byte[] encrypted = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            
            encrypted =  cipher.doFinal(Operations.addBytes(PAD, plain));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            System.err.format("Encrypt exception : " + e.getMessage());
        }
        return encrypted;
    }
    
    /**
     * Generate key with bit length < 128
     * @return 
     */
    public static byte[] generateKey() {
        byte[] keyByte = null;
        try {
            do {
                KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
                generator.init(SIZE);
                SecretKey key = generator.generateKey();
            
                keyByte = key.getEncoded();
            } while (new BigInteger(1, keyByte).bitLength() >= 128);
        } catch (Exception e) {
            System.err.format("Generate key exception : " + e.getMessage());
        }
        return keyByte;
    }
    
}
