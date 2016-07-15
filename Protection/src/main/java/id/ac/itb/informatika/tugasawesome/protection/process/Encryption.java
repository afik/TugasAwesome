package id.ac.itb.informatika.tugasawesome.protection.process;

import id.ac.itb.informatika.tugasawesome.protection.utils.Operations;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
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
    
    public static void encryptLarge(Path plain, Path saveTo, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            
            int count;
            byte[] buffer = new byte[16];
            Path finalPath = Paths.get(saveTo.toString() + "/" + plain.getFileName().toString());
            File newfile = new File(finalPath.toString());
            
            FileInputStream in = new FileInputStream(plain.toFile());
            FileOutputStream fos = new FileOutputStream(newfile);
            CipherOutputStream out = new CipherOutputStream(fos, cipher);
            out.write(PAD, 0, 16);
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            in.close();
            out.close();
            fos.close();
            
            BasicFileAttributes attr = Files.readAttributes(plain, BasicFileAttributes.class);
            Files.setAttribute(finalPath, "creationTime", attr.creationTime());
            Files.setAttribute(finalPath, "lastModifiedTime", attr.lastModifiedTime());
            Files.setAttribute(finalPath, "lastAccessTime", attr.lastAccessTime());
            Files.setAttribute(finalPath, "dos:readonly", true);
        } catch (Exception  e) {
            System.err.format("Encrypt exception : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void decryptLarge(Path encrypted, Path saveTo, byte[] key) {
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
        } catch (Exception  e) {
            System.err.format("Encrypt exception : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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
