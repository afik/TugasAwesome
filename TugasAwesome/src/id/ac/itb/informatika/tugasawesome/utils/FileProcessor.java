package id.ac.itb.informatika.tugasawesome.utils;

import java.io.BufferedReader;
import java.io.File;
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
     * TODO: check file type, do not return as plain string
     * @param path
     * @return 
     */
    public static List<String> readFile(String path) {
        List<String> retval = new ArrayList<>();
        Path file = FileSystems.getDefault().getPath(path);
        System.out.println(file.toString());
        Charset charset = Charset.forName("UTF-8"); //Need to be reviewed
        try(BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            while((line = reader.readLine()) != null) {
                retval.add(line);
            }
        } catch (IOException e) {
            System.err.format("IO Exception : ");
            e.printStackTrace();
        }
        
        return retval;
    }
    
    
}
