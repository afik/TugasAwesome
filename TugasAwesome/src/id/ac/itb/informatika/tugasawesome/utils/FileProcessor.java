package id.ac.itb.informatika.tugasawesome.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class FileProcessor {
    
    /**
     * Read file specified by {path} and return all unique words.
     * @param path
     * @return 
     */
    public static List<String> readFile(Path path) {
        Scanner s = null;
        Set<String> setVal = new HashSet<>();
        
        try {
            s = new Scanner(new BufferedReader(new FileReader(path.toFile())));
            s.useDelimiter("[\\W]");
            s.useLocale(Locale.US);
            while (s.hasNext()) {
                setVal.add(s.next().toLowerCase());
            }
        } catch (Exception ex) {
            System.err.println("IO Exception : " + ex.getMessage());
        } finally {
            if (s!= null) {
                s.close();
            }
        }
        
        List<String> result = new ArrayList<>(setVal);
        result.remove(0);
        return result;
                
    }
    
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
    
    public static void saveToFile(byte[] content, Path savePath, String filename) {
        File newfile = new File(savePath.toString() + "/" +filename);
        try {
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(content);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            System.err.format("Exception: " + e.getMessage());
        } 
    }
    
}
