package id.ac.itb.informatika.tugasawesome.process;


import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public final class Shamir {
    public static List<PointByte> splitKey(byte[] key, int threshold, List<BigInteger> domain, BigInteger prime) {
       System.out.println("split...");
        
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
           if (y.compareTo(prime) > 0) System.out.println("y split lebih besar");
           PointByte point = new PointByte(x,y,prime, false);
           allShare.add(point);
       }
       System.out.println("end of split...");
       return allShare;
    }
    
    /**
     * Get value of f(0) from polynomials using Lagrange interpolation
     * @param shares : list of share as result of words query mapped to PointByte
     * @return byte[] of possible key
     */
    public static BigInteger recoverKey(List<PointByte> shares, BigInteger prime) {        
        List<PointByte> modShares = new ArrayList<>();
        
        return GfPolynomial.interpolateAtZero(shares, prime);

//        GfPolynomial gf = GfPolynomial.interpolatePolynomial(modShares, prime);
//        return gf.evaluatePolynomial(BigInteger.ZERO);
    }
    
    /**
     * Random BigInteger that not bigger/equal to secret and prime number
     */
    private static BigInteger randomCoef(BigInteger secret, BigInteger prime) {
        BigInteger coef;
        do {
            coef = Operations.randomByte();
        } while(coef.compareTo(prime) >= 0 ||
                coef.compareTo(secret) >= 0);
        return coef;
    }
    
    
}
