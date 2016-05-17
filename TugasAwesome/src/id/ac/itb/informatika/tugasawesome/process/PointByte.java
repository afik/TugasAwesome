package id.ac.itb.informatika.tugasawesome.process;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

/**
 * Class represent point of GF(2^128)
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class PointByte {
    /**
     * size of each point element
     **/
    private static int BITLENGTH = 128; 
    private static int SIZE = 16;  //128 bit in byte, but sometimes 128 bit can use 17 byte array 
    
    private BigInteger x; //first element of point
    private BigInteger y; //second element of point
    
    public PointByte() {
        x = BigInteger.ZERO;
        y = BigInteger.ZERO;
    }
    
    public PointByte(BigInteger a, BigInteger b) {
        if (isValidElement(a) && isValidElement(b)) {
            this.x = a;
            this.y = b;
        } else {
            System.err.println("Error creating point : " + a.bitLength() + " , " + b.bitLength());
        }
        
    }
    
    public PointByte(byte[] a, byte[] b) {
        if (isValidElement(new BigInteger(1,a)) && 
            isValidElement(new BigInteger(1,b))) {
            byte[] xB = new byte[16];
            byte[] yB = new byte[16];
            System.arraycopy(a, 0, xB, SIZE-a.length, a.length);
            System.arraycopy(b, 0, yB, SIZE-b.length, b.length);
            x = new BigInteger(1,xB);
            y = new BigInteger(1,yB);
        } else {
            System.err.println("Error creating point : " + new BigInteger(1,a).bitLength() + " , " + new BigInteger(1,b).bitLength());
        }
    }
    
    public PointByte(byte[] val) {
//        System.out.println("val : "+ ByteArrayOp.toHex(val));
        
        byte[] xB = Arrays.copyOfRange(val, 0, SIZE);
        byte[] yB = Arrays.copyOfRange(val, SIZE, val.length);
//        System.out.println("xB : " + ByteArrayOp.toHex(xB));
//        System.out.println("yB : " + ByteArrayOp.toHex(yB));
        BigInteger xxB = new BigInteger(1,xB);
        BigInteger yyB = new BigInteger(1,yB);
        if (isValidElement(xxB) && isValidElement(yyB)) {
            x = xxB;
            y = yyB;
//            this.printHex();
        } else {
            System.err.println("Error creating point : " + val.length);
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
        if (isValidElement(x)) {
            this.x = x;
        } else {
            System.err.println("Error set : " + x.bitLength());
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
        if (isValidElement(y)) {
            this.y = y;
        } else {
            System.err.println("Error set : " + y.bitLength());
        }
    }
    
    /**
     * Check is element in GF(2^128)
     * @param element
     * @return true if element in GF(2^128)
     */
    public static boolean isValidElement(BigInteger element) {
        return element.bitLength() <= BITLENGTH && element.compareTo(BigInteger.ZERO) >= 0;
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
