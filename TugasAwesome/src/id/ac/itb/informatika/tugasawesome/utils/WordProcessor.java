package id.ac.itb.informatika.tugasawesome.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class WordProcessor {
    
    /**
     * Return all unique word in file
     * @param file is all line in string
     * @return 
     */
    public static List<String> getAllWord(List<String> file) {
        Set<String> index = new HashSet<>();
        String pattern = "[^\\w]"; //TODO : verify again to get best word for searching
        
        for (String line : file) {
            String[] splitted = line.split(pattern);
            for(String a : splitted) {
                if (a.trim().length() >0) {
                    index.add(a.toLowerCase());
                }
            }
        }
        return new ArrayList<>(index);
    }
}
