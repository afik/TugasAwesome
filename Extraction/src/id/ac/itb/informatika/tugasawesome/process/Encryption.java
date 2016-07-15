package id.ac.itb.informatika.tugasawesome.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
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
    
    
    public static boolean decryptLarge(Path encrypted, Path saveTo, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            
            int count;
            byte[] buffer = new byte[16];
            Path finalPath = Paths.get(saveTo.toString() + "/" + encrypted.getFileName().toString());
            
            //encrypt to temporary
            File temp = File.createTempFile("ppdef",".tmp", saveTo.toFile());
            FileInputStream in = new FileInputStream(encrypted.toFile());
            FileOutputStream fos = new FileOutputStream(temp);
            CipherOutputStream out = new CipherOutputStream(fos, cipher);

            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            in.close();
            out.close();
            fos.close();
            
            //remove padding
            File newfile = new File(finalPath.toString());
            FileInputStream in2 = new FileInputStream(temp);
            FileOutputStream out2 = new FileOutputStream(newfile);
            byte[] buf = new byte[16];
            in2.read(buf);
            while((count = in2.read(buffer)) > 0) {
                out2.write(buffer, 0, count);
            }
            in2.close();
            out2.close();
            
            temp.delete();
            
            BasicFileAttributes attr = Files.readAttributes(encrypted, BasicFileAttributes.class);
            Files.setAttribute(finalPath, "creationTime", attr.creationTime());
            Files.setAttribute(finalPath, "lastModifiedTime", attr.lastModifiedTime());
            Files.setAttribute(finalPath, "lastAccessTime", attr.lastAccessTime());
            Files.setAttribute(finalPath, "dos:readonly", true);
            
            return true;
        } catch (Exception  e) {
            System.err.format("Encrypt exception : " + e.getMessage());
            return false;
        } 
    }
    
}
