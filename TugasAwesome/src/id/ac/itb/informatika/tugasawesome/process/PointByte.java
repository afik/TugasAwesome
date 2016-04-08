package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Class represent point of GF(2^(SIZE*8))
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class PointByte {
    /**
     * size of each point element
     **/
    private static int SIZE = 16; 
    
    private byte[] x; //first element of point
    private byte[] y; //second element of point
    
    public PointByte() {
        x = new byte[16];
        y = new byte[16];
    }
    
    public PointByte(byte[] a, byte[] b) {
        if (isValidElement(a) && isValidElement(b)) {
            x = Arrays.copyOf(a, 16);
            y = Arrays.copyOf(b, 16);
        } else {
            System.err.println("Error creating point : length of x and y should be 16");
        }
    }
    
    public PointByte(byte[] val) {
        if (val.length == SIZE*2) {
            x = Arrays.copyOfRange(val, 0, SIZE);
            y = Arrays.copyOfRange(val, SIZE, val.length);
        } else {
            System.err.println("Error creating point : length of val should be 32");
        }
    }

    /**
     * @return the x
     */
    public byte[] getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(byte[] x) {
        if (isValidElement(x)) {
            this.x = Arrays.copyOf(x,16);
        } else {
            System.err.println("Error set : length of x should be 16");
        }
    }

    /**
     * @return the y
     */
    public byte[] getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(byte[] y) {
        if (isValidElement(y)) {
            this.y = Arrays.copyOf(y,16);
        } else {
            System.err.println("Error set : length of y should be 16");
        }
    }
    
    /**
     * Check is element in GF(2^(SIZE*8))
     * @param element
     * @return true if element in GF(2^(SIZE*8))
     */
    public static boolean isValidElement(byte[] element) {
        return element.length == SIZE;
    }
    
    public void print() {
        System.out.println("("+ ByteArrayOp.toHex(x) + ","
                              + ByteArrayOp.toHex(y) + ")");
    }
    
}
