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
     * Perform protect algorithm P.2 and P.3
     * @param file
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static GfPolynomial protect(String file, byte[] key, int threshold) {
        
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
        System.out.println("Share after split : ");
        System.out.println(shares.get(Mapping.aaa));
        System.out.println(shares.get(Mapping.bbb));
        System.out.println(shares.get(Mapping.ccc));
        
        //P.3 connect keyword to share
        GfPolynomial mappingFunction = Mapping.createMappingFunction(hashResult, shares, prime);
        
        
        System.out.println("y xor z " + shares.get(Mapping.aaa).getY().xor(hashResult.get(Mapping.aaa).getY()));
        System.out.println("poly result " + mappingFunction.evaluatePolynomial(shares.get(Mapping.aaa).getX()));
        
        return mappingFunction;
    }
}
