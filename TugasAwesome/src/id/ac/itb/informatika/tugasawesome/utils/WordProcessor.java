package id.ac.itb.informatika.tugasawesome.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class WordProcessor {
    
    public static List<String> getAllWord(List<String> file) {
        List<String> index = new ArrayList<>();
        String pattern = "[^\\w]"; //TODO : verify again to get best word for searching
        
        for (String line : file) {
            String[] splitted = line.split(pattern);
            for(String a : splitted) {
                if (a.trim().length() >0) {
                    index.add(a);
                }
            }
        }
        return index;
    }
}
