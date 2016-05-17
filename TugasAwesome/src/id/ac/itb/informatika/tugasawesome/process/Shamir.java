package id.ac.itb.informatika.tugasawesome.process;


import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import id.ac.itb.informatika.tugasawesome.utils.GfPolynomial;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public final class Shamir {
    public static List<PointByte> splitKey(byte[] key, int threshold, List<BigInteger> domain, BigInteger prime) {
       List<PointByte> allShare = new ArrayList<>();
                
       //construct polynomial from key and random coefficint
       List<BigInteger> coeff = new ArrayList<>();
       coeff.add(new BigInteger(1,key));
       for (int i = 0; i < threshold-1; i++) {
           coeff.add(randomCoef(new BigInteger(1,key), prime)); //TODO : creaate random coef, coef < secret, coeef < prime
       }
       
       GfPolynomial poly = new GfPolynomial(coeff, prime);
       
       //compute shares
       for (BigInteger x : domain) {
           BigInteger y = poly.evaluatePolynomial(x);
           PointByte point = new PointByte(x,y);
           allShare.add(point);
       }
       return allShare;
    }
    
    /**
     * Get value of f(0) from polynomials using Lagrange interpolation
     * @param shares : list of share as result of words query mapped to PointByte
     * @return byte[] of possible key
     */
    public static BigInteger recoverKey(List<PointByte> shares, BigInteger prime) {
        BigInteger key = BigInteger.ZERO;
        
        for (int i = 0; i < shares.size(); ++i) {
            PointByte share = shares.get(i);
            BigInteger thisX = share.getX();
            BigInteger thisY = share.getY();
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (int j = 0; j < shares.size(); j++) {
                PointByte otherShare = shares.get(j);
                if (!share.isEqual(otherShare)) {
                    BigInteger otherX = otherShare.getX();
                    numerator = numerator.multiply(otherX.negate()).mod(prime);
                    denominator = denominator.multiply(thisX.subtract(otherX)).mod(prime);
                } 
            }
            BigInteger delta = thisY.multiply(numerator).multiply(GfPolynomial.modInverse(denominator,prime));
            key = prime.add(key).add(delta).mod(prime);
       }
        return key;
    }
    
    /**
     * Random BigInteger that not bigger/equal to secret and prime number
     */
    private static BigInteger randomCoef(BigInteger secret, BigInteger prime) {
        BigInteger coef;
        do {
            coef = ByteArrayOp.randomByte();
        } while(coef.compareTo(prime) >= 0 ||
                coef.compareTo(secret) >= 0);
        return coef;
    }
    
    
}
