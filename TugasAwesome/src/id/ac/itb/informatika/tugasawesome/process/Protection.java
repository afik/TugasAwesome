package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
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
     * @param key
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static GfPolynomial protect(String file, byte[] key, int threshold) {
        System.out.println("protect...");
        //P.2 Split the secret
        List<String> words = FileProcessor.readFile(file);
        
        BigInteger prime = Operations.randomPrime(new BigInteger(1,key));
        System.out.println("Prime " + prime + " " + prime.bitLength());
        
        List<BigInteger> xBytes = new ArrayList<>();
        List<PointByte> hashResult = Mapping.wordsToPoint(words, prime);
        for (PointByte point : hashResult) {
            xBytes.add(point.getX());
//            if (point.getX().compareTo(prime) >= 0) {
//                System.out.println("x hash lebih besar");
//            } 
//            
//            if (point.getY().compareTo(prime) >= 0) {
//                System.out.println("y hash lebih besar");
//            }
        }
        
        List<PointByte> shares = Shamir.splitKey(key, threshold, xBytes, prime);
        
        for (PointByte point : shares) {
            if (point.getY().bitLength() == prime.bitLength()) {
                point.setY(point.getY().mod(new BigInteger("2").pow(128)));
            }
            System.out.println(point.getX().bitLength() + " " + point.getY().bitLength());
        }
        
        //P.3 connect keyword to share
        GfPolynomial mappingFunction = Mapping.createMappingFunction(hashResult, shares, prime);
        
        System.out.println("end of protect...");
        return mappingFunction;
    }
}
