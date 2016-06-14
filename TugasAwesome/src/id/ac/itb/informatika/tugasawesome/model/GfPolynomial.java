package id.ac.itb.informatika.tugasawesome.model;

import id.ac.itb.informatika.tugasawesome.utils.Operations;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class GfPolynomial implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<BigInteger> coeff;
    private int degree;
    private BigInteger prime;
    
    /**
     * This is not valid constructor of GfPolynomial
     * Use to serialize threshold value
     */
    public GfPolynomial(int threshold) {
        degree = threshold;
    }
    
    /**
     * Create polynomial with coefficient from {init}
     * @param init 
     */
    public GfPolynomial(List<BigInteger> init, BigInteger prime) {
        //TODO  : check does init element valid
        degree = init.size() - 1;
        this.prime = prime;
        coeff = new ArrayList<>();
        for (BigInteger in : init) {
            coeff.add(in);
        }
    }
    
    /**
     * Create random polynomial of degree {degree}
     */
    public GfPolynomial(int degree, BigInteger prime) {
        this.degree = degree;
        coeff = new ArrayList<>();
        this.prime = prime;
        while (coeff.size() != degree +1) {
            coeff.add(randomCoeff(prime));
        }
    }
    
    /**
     * Random coefficient for polynomial, coefficient cannot greater than prime
     */
    private BigInteger randomCoeff(BigInteger prime) {
        BigInteger coef;
        do {
            coef = Operations.randomByte();
        } while(coef.compareTo(prime) >= 0);
        return coef;
    }
    
    /**
     * return value of {val} from polynomial {coeff}
     */
    public BigInteger evaluatePolynomial(BigInteger x) {
        BigInteger value = BigInteger.ZERO;
        for (int i = degree; i >= 0; --i) {
            value = value.multiply(x).add(coeff.get(i)).mod(getPrime());
        }
        return value;
    }
    
    /**
     * Modified from PolynomialFunctionLagrangeForm of Apache Common Math 3
     * Note that this will produce correct polynomial only if all points value smaller than prime
     ** Calculate the coefficients of Lagrange polynomial from the
     ** interpolation data. It takes O(n^2) time.
     ** Note that this computation can be ill-conditioned: Use with caution
     ** and only when it is necessary.
     */
    public static GfPolynomial interpolatePolynomial(List<PointByte> points, BigInteger prime) {
        int n = points.size();
        BigInteger[] coefficients = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            coefficients[i] = BigInteger.ZERO;
        }

        // c[] are the coefficients of P(x) = (x-x[0])(x-x[1])...(x-x[n-1])
        final BigInteger[] c = new BigInteger[n+1];
        c[0] = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            for (int j = i; j > 0; j--) {
                c[j] = prime.add((c[j-1].subtract(c[j].multiply(points.get(i).getX()).mod(prime))).mod(prime)).mod(prime);
            }
            c[0] = prime.add(c[0].multiply(points.get(i).getX().negate()).mod(prime)).mod(prime);
            c[i+1] = BigInteger.ONE;
        }

        final BigInteger[] tc = new BigInteger[n];
        for (int i = 0; i < n; i++) {
            // d = (x[i]-x[0])...(x[i]-x[i-1])(x[i]-x[i+1])...(x[i]-x[n-1])
            BigInteger d = BigInteger.ONE;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    d = d.multiply(prime.add(points.get(i).getX().subtract(points.get(j).getX()).mod(prime)).mod(prime)).mod(prime);
                }
            }
            
            final BigInteger t = points.get(i).getY().multiply(modInverse(d, prime));
            // Lagrange polynomial is the sum of n terms, each of which is a
            // polynomial of degree n-1. tc[] are the coefficients of the i-th
            // numerator Pi(x) = (x-x[0])...(x-x[i-1])(x-x[i+1])...(x-x[n-1]).
            tc[n-1] = c[n];     // actually c[n] = 1
            coefficients[n-1] = coefficients[n-1].add(t.multiply(tc[n-1]).mod(prime)).mod(prime);
            for (int j = n-2; j >= 0; j--) {
                tc[j] = c[j+1].add(tc[j+1].multiply(points.get(i).getX()).mod(prime)).mod(prime);
                coefficients[j] = coefficients[j].add(t.multiply(tc[j]).mod(prime)).mod(prime);
            }
        }
        
        List<BigInteger> coef = Arrays.asList(coefficients);
        GfPolynomial result = new GfPolynomial(coef, prime);
        return result;
    }
    
    public static BigInteger interpolateAtZero(List<PointByte> shares, BigInteger prime) {
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
    
    //translated from wikipedia by stackoverflow
    private static BigInteger[] gcdD(BigInteger a, BigInteger b)
    { 
        if (b.compareTo(BigInteger.ZERO) == 0)
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO}; 
        else
        { 
            BigInteger n = a.divide(b);
            BigInteger c = a.mod(b);
            BigInteger[] r = gcdD(b, c); 
            return new BigInteger[] {r[0], r[2], r[1].subtract(r[2].multiply(n))};
        }
    }
    
    //translated from wikipedia by stackoverflow
    public static BigInteger modInverse(BigInteger k, BigInteger prime)
    { 
        k = k.mod(prime);
        BigInteger r = (k.compareTo(BigInteger.ZERO) == -1) ? (gcdD(prime, k.negate())[2]).negate() : gcdD(prime,k)[2];
        return prime.add(r).mod(prime);
    }
    
    /**
     * @return the coeff
     */
    public List<BigInteger> getCoeff() {
        List<BigInteger> c = new ArrayList<>(coeff);
        return c;
    }

    /**
     * @param coeff the coeff to set
     */
    public void setCoeff(List<BigInteger> coeff) {
        this.coeff = coeff;
    }

    /**
     * @return the degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * @param degree the degree to set
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "GfPolynomial{" + 
                "coeff=" + coeff + 
                ", degree=" + degree + 
                ", prime=" + getPrime() + '}';
    }

    /**
     * @return the prime
     */
    public BigInteger getPrime() {
        return prime;
    }

    /**
     * @param prime the prime to set
     */
    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }
    
}
