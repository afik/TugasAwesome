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
    
    /**
     * Get value of f(0) from polynomials using Lagrange interpolation
     * @param shares : list of share as result of words query mapped to PointByte
     * @param prime
     * @param threshold
     * @return byte[] of possible key
     */
    public static BigInteger recoverKey(List<PointByte> shares, BigInteger prime) {   
        return GfPolynomial.interpolateAtZero(shares, prime);

//        GfPolynomial gf = GfPolynomial.interpolatePolynomial(shares, prime);
//        return gf.evaluatePolynomial(BigInteger.ZERO);
    }
    
    
    
}
