package id.ac.itb.informatika.tugasawesome.protection.utils;

import java.io.BufferedReader;
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
public class TxtFileProcessor extends FileProcessor {
    /**
     * Read simple text file specified by {path} and return all unique words.
     * @param path
     * @return 
     */
    @Override
    public List<String> readFile(Path path) {
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
}
