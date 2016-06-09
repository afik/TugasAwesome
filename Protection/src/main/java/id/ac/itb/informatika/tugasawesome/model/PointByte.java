package id.ac.itb.informatika.tugasawesome.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class represent point of GF(2^128) -> integer mod p, where p.bitLength() <= 128
 * value of x and y cannot bigger than p if {mod} == true
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class PointByte {
    /**
     * size of each point element
     **/
    private final int SIZE = 16;  //128 bit in byte, but sometimes 128 bit can use 17 byte array 
    
    private BigInteger x; //first element of point
    private BigInteger y; //second element of point
    private BigInteger prime; //prime 
    private boolean mod; //flag indicate if x & y should be mod prime; 
                         //set to false only if you sure  x & y < prime
    
    public PointByte(BigInteger p, boolean mod) {
        x = BigInteger.ZERO;
        y = BigInteger.ZERO;
        this.mod = mod;
        prime = p;
    }
    
    public PointByte(BigInteger a, BigInteger b, BigInteger p, boolean mod) {
        
        this.prime = p;
        this.mod = mod;
        
        if (mod) {
            if (a.compareTo(a.mod(p)) > 0) System.out.println("x berubah");
            if (b.compareTo(b.mod(p)) > 0) System.out.println("y berubah");
        
            this.x = a.mod(p);
            this.y = b.mod(p);
        } else {
            this.x = a;
            this.y = b;
        }
        
    }
    
    public PointByte(byte[] a, byte[] b, BigInteger p, boolean mod) {
        byte[] xB = new byte[16];
        byte[] yB = new byte[16];
        System.arraycopy(a, 0, xB, SIZE-a.length, a.length);
        System.arraycopy(b, 0, yB, SIZE-b.length, b.length);
        
        prime = p;
        this.mod = mod;
        
        if (mod) {
            x = new BigInteger(1,xB).mod(p);
            y = new BigInteger(1,yB).mod(p);
        } else {
            x = new BigInteger(1,xB);
            y = new BigInteger(1,yB);
        }
        
    }
    
    public PointByte(byte[] val, BigInteger p, boolean mod) {        
        byte[] xB = Arrays.copyOfRange(val, 0, SIZE);
        byte[] yB = Arrays.copyOfRange(val, SIZE, val.length);
        
        BigInteger a = new BigInteger(1,xB);
        BigInteger b = new BigInteger(1,yB);
        
        prime = p;
        this.mod = mod;
        
        if (mod) {
            if (a.compareTo(a.mod(p)) > 0) System.out.println("x berubah");
            if (b.compareTo(b.mod(p)) > 0) System.out.println("y berubah");
            x = a.mod(p);
            y = b.mod(p);
        } else {
            x = a;
            y = b;
        }
        
    }

    /**
     * @return the x
     */
    public BigInteger getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(BigInteger x) {
        if (mod) {
            this.x = x.mod(prime);
        } else {
            this.x = x;
        }
    }

    /**
     * @return the y
     */
    public BigInteger getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(BigInteger y) {
        if (mod) {
            this.y = y.mod(prime);
        } else {
            this.y = y;
        }
    }
    
    
    public BigInteger getPrime() {
        return prime;
    }

    public void setPrime(BigInteger prime) {
        this.prime = prime;
    }
    
    public boolean isMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }
    
    public void printHex() {
        System.out.println("("+ x.toString(16) + ","
                              + y.toString(16) + ")");
    }
    
    public void printBigInt() {
        System.out.println("("+ x + ","
                              + y + ") " + x.bitLength() + "," +y.bitLength());
    }
    
    public boolean isEqual(PointByte point) {
        return (this.x.compareTo(point.getX()) == 0) &&
               (this.y.compareTo(point.getY()) == 0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.x);
        hash = 47 * hash + Objects.hashCode(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PointByte other = (PointByte) obj;
        if (!Objects.equals(this.x, other.x)) {
            return false;
        }
        if (!Objects.equals(this.y, other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PointByte{" + "x=" + x + ", y=" + y + '}';
    }
   
}
