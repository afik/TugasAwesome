package id.ac.itb.informatika.tugasawesome.utils;

import static java.nio.file.StandardOpenOption.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.*;
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
     * Read file specified by {path} and return unique words.
     * @param path
     * @return 
     */
    public static List<String> readFile(String path) {
        Scanner s = null;
        Set<String> retval = new HashSet<>();
        
        try {
            s = new Scanner(new BufferedReader(new FileReader(path)));
            s.useDelimiter("[\\W]");
            s.useLocale(Locale.US);
            while (s.hasNext()) {
//                if (s.next().matches("\\w")) {
                    retval.add(s.next().toLowerCase());
//                }
            }
        } catch (Exception ex) {
            System.err.println("IO Exception : " + ex.getMessage());
        } finally {
            if (s!= null) {
                s.close();
            }
        }
        return new ArrayList<>(retval);
                
//        List<String> retval = new ArrayList<>();
//        Path file = FileSystems.getDefault().getPath(path);
//        Charset charset = Charset.forName("UTF-8"); //Need to be reviewed
//        try(BufferedReader reader = Files.newBufferedReader(file, charset)) {
//            String line = null;
//            while((line = reader.readLine()) != null) {
//                retval.add(line);
//            }
//            reader.close();
//        } catch (IOException e) {
//            System.err.format("IO Exception : " + e.getMessage());
//        } 
//        return retval;
    }
    
    /**
     * Read file specified by {path} as byte array
     * @param path
     * @return 
     */
    public static byte[] readFileAsBytes(String path) {
        File file = new File(path);
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
    
    public static void saveToFile(byte[] content, String filename) {
        File newfile = new File(filename);
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
