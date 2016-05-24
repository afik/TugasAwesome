package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.utils.WordProcessor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Protection {
    
    /**
     * Perform protect algorithm
     * @param file
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static GfPolynomial protect(String file, int threshold) {
        
        //P.1 Encrypt message
        byte[] filePlain = FileProcessor.readFileAsBytes(file);
        byte[] key = Encryption.generateKey();
        byte[] cipher = Encryption.encrypt(filePlain, key);
        
        //P.2 Split the secret
        List<String> lines = FileProcessor.readFile(file);
        List<String> words = WordProcessor.getAllWord(lines);
        BigInteger prime = ByteArrayOp.randomPrime(new BigInteger(1,key));
        List<BigInteger> xBytes = new ArrayList<>();
        List<PointByte> hashResult = Mapping.wordsToPoint(words);
        for (PointByte point : hashResult) {
            xBytes.add(point.getX());
        }
        List<PointByte> shares = Shamir.splitKey(key, threshold, xBytes, prime);
        
        //P.3 connect keyword to share
        GfPolynomial mappingFunction = Mapping.createMappingFunction(hashResult, shares);
        
        return mappingFunction;
    }
}
