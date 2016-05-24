package id.ac.itb.informatika.tugasawesome.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class FileProcessor {
    
    /**
     * TODO: do not return as plain string (and yes i forgot what this mean)
     * Read file specified by {path} per line.
     * @param path
     * @return 
     */
    public static List<String> readFile(String path) {
        List<String> retval = new ArrayList<>();
        Path file = FileSystems.getDefault().getPath(path);
        Charset charset = Charset.forName("UTF-8"); //Need to be reviewed
        try(BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while((line = reader.readLine()) != null) {
                retval.add(line);
            }
            reader.close();
        } catch (IOException e) {
            System.err.format("IO Exception : " + e.getMessage());
        } 
        return retval;
    }
    
    /**
     * Read file specified by {path} as byte array
     * @param path
     * @return 
     */
    public static byte[] readFileAsBytes(String path) {
        byte[] fileContent = null;
        File file = new File(path);
        FileInputStream fin = null;
        
        try {
            fin = new FileInputStream(file);
            fileContent = new byte[(int)file.length()];
            fin.read(fileContent);
        } catch (Exception e) {
            System.err.format("Exception : " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                } 
            } catch (Exception e) {
                System.err.format("Exception : " + e.getMessage());
            }
        }
        return fileContent;
    }
    
}
