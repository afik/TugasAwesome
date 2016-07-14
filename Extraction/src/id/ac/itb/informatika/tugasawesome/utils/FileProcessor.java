package id.ac.itb.informatika.tugasawesome.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class FileProcessor {
 
    
    /**
     * Read file specified by {path} as byte array
     * @param path
     * @return 
     */
    public static byte[] readFileAsBytes(Path path) {
        File file = path.toFile();
        FileInputStream fin = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fin = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fin.read(buf))!=-1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (Exception e) {
            System.err.format("Exception : " + e.getMessage());
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                } 
            } catch (Exception e) {
                System.err.format("Exception : " + e.getMessage());
            }
        }
        return bos.toByteArray();
    }
    
    public static byte[] readFirstBytes(Path path) {
        File file = path.toFile();
        FileInputStream fin = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fin = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int readNum = 0;
            while ((readNum = fin.read(buf))!=-1 && bos.size() < 9216) {
                bos.write(buf, 0, readNum);
            }
        } catch (Exception e) {
            System.err.format("IO Exception : " + e.getMessage());
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                } 
            } catch (Exception e) {
                System.err.format("IO Exception : " + e.getMessage());
            }
        }
        return bos.toByteArray();
    }
    
    public static void saveToFile(byte[] content, Path savePath, Path original) {
        Path finalPath = Paths.get(savePath.toString() + "/" + original.getFileName().toString());
        File newfile = new File(finalPath.toString());
        try {
            BasicFileAttributes attr = Files.readAttributes(original, BasicFileAttributes.class);
            
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(content);
            fos.flush();
            fos.close();
            Files.setAttribute(finalPath, "creationTime", attr.creationTime());
            Files.setAttribute(finalPath, "lastModifiedTime", attr.lastModifiedTime());
            Files.setAttribute(finalPath, "creationTime", attr.lastAccessTime());
            
        } catch (Exception e) {
            System.err.format("IO Exception : " + e.getMessage());
        } 
    }
    
}
