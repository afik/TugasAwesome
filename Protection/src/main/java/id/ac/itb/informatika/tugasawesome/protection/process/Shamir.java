package id.ac.itb.informatika.tugasawesome.protection.process;


import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.protection.utils.Operations;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public final class Shamir {
    
    /**
     * Shamir's Secret Share implementation to split key into domain.size() share
     * @param key
     * @param threshold : number of minimal share to recover key
     * @param domain : represent participant
     * @param prime : should bigger than largest possible key, all coefficient, and number of participant
     *                in this implementation, prime bit length should be bigger than key & participant bit length
     * @return 
     */
    public static List<PointByte> splitKey(byte[] key, int threshold, List<BigInteger> domain, BigInteger prime) {
 
        List<PointByte> allShare = new ArrayList<>();
                
       //construct polynomial from key and random coefficint
       List<BigInteger> coeff = new ArrayList<>();
       coeff.add(new BigInteger(1,key));
       for (int i = 0; i < threshold-1; i++) {
           coeff.add(randomCoef(new BigInteger(1,key), prime)); 
       }
       
       GfPolynomial poly = new GfPolynomial(coeff, prime);
       
       //compute shares
       for (BigInteger x : domain) {
           
           BigInteger y = poly.evaluatePolynomial(x);
           
           //All shares bit length should < prime bit length 
           //so it wont create participant larger than prime after xor
           if (y.bitLength() == prime.bitLength()){
               allShare.clear();
               break;
           }
           
           PointByte point = new PointByte(x,y,prime, false);
           allShare.add(point);
       }
       return allShare;
    }
    
    /**
     * Random BigInteger that smaller to secret and prime number
     */
    private static BigInteger randomCoef(BigInteger secret, BigInteger prime) {
        BigInteger coef;
        do {
            coef = Operations.randomByte();
        } while(coef.compareTo(prime) >= 0 ||
                coef.compareTo(secret) >= 0);
        return coef;
    }
    
    /**
     * Get smallest 129 bit prime number
     */
    public static BigInteger getPrime() {
        //340282366920938463463374607431768211455 is largest number with 128 bit length
        return new BigInteger("340282366920938463463374607431768211455").nextProbablePrime();
        
    }
    
    
}
