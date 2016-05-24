
package id.ac.itb.informatika.tugasawesome.main;

import id.ac.itb.informatika.tugasawesome.process.Encryption;
import id.ac.itb.informatika.tugasawesome.process.Mapping;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.process.Shamir;
import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.process.Protection;
import id.ac.itb.informatika.tugasawesome.utils.WordProcessor;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author TOLEP
 */
public class TugasAwesome {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String mainDir = "D:\\AFIK\\Project\\T(ugas) A(wesome)\\test\\";
        
        /**Test word processor**/
//        List<String> tes = FileProcessor.readFile(mainDir + "testFile_2.txt");
//        System.out.println(tes.size());
//        for (String a : tes) {
//            System.out.println(a);
//        }
//        
//        System.out.println("==============================");
//        List<String> dictionaryWord = WordProcessor.getAllWord(tes);
//        System.out.println(dictionaryWord.size());
//        int i = 0;
//        for (String a : dictionaryWord) {
//            System.out.println(i + ":" +a);
//            i++;
//        }
            
        /** Tests encrypt and decrypt - Pass **/
//        byte[] plainFile = FileProcessor.readFileAsBytes(mainDir + "testFile.txt");
//        System.out.println(plainFile.length);
//        System.out.println(Arrays.toString(plainFile));
//        System.out.println("=========key===============");
//        byte[] key = Encryption.generateKey();
//        System.out.println(key.length);
//        System.out.println(Arrays.toString(key));
//        System.out.println("=========encrypt===============");
//        byte[] cipher = Encryption.encrypt(plainFile, key);
//        System.out.println(cipher.length);
//        System.out.println(Arrays.toString(cipher));
//        System.out.println("===========decrypt=============");
//        if (Encryption.checkFirstBlock(ByteArrayOp.getFirstBlock(cipher, 16), key)) {
//            byte[] decrypted = Encryption.decrypt(cipher, key);
//            byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
//            System.out.println(plain2.length);
//            System.out.println(Arrays.toString(plain2));
//        }
            
        /** Tests Mapping - Pass**/
//        BigInteger salt = ByteArrayOp.randomByte();
//        System.out.println(salt);
//        String word = "bijective";
//        byte[] hashWord = Mapping.Hash(word.getBytes(), salt.toByteArray());
//        System.out.println(ByteArrayOp.toHex(hashWord));
//        PointByte point = new PointByte(hashWord);
//        point.printHex();

          /*Testing evaluate poly - Pass*/
//          List<BigInteger> coef = new ArrayList<>();
//          coef.add(ByteArrayOp.randomByte());
//          coef.add(ByteArrayOp.randomByte());
//          coef.add(ByteArrayOp.randomByte());
//          System.out.println("Coef :" + coef.get(0) + " " + coef.get(1) + " "+ coef.get(2));
//          BigInteger val = ByteArrayOp.randomByte();
//          BigInteger prime = ByteArrayOp.randomPrime(val);
//          BigInteger hasil = Shamir.evaluatePolynomial(coef, val, prime);
//          System.out.println(hasil);
        

        /*Testing secret share - Pass*/
        //compute domain
//        List<BigInteger> xBytes = new ArrayList<>();
//        List<PointByte> listPoint = Mapping.wordsToPoint(dictionaryWord);
//        for (PointByte point : listPoint) {
//            xBytes.add(point.getX());
//        }
//        List<BigInteger> xBytes2 = new ArrayList<>();
//        for (int i = 0; i<10; i++) {
//            BigInteger randVal = ByteArrayOp.randomByte();
//            xBytes.add(randVal);
//        }
//        
//        
//        //generate key
//        byte[] key = Encryption.generateKey();
//        BigInteger keyB = new BigInteger(1,key);
//        System.out.println("Key awal : " + keyB + ", " + keyB.bitLength());
//        
//        //generate random prime
//        BigInteger prime = ByteArrayOp.randomPrime(new BigInteger(1,key));
//        System.out.println("Prime : " + prime + ", l : " + prime.bitLength());
//        
//        //split key
//        List<PointByte> shares = Shamir.splitKey(key, 5, xBytes, prime);
//        
//        
//        //recover key
//        List<PointByte> shareToRecover = new ArrayList<>();
//        Set<Integer> setIdx = new HashSet<>();
//        while (setIdx.size() < 5) {
//            Random rand = new Random();
//            int a;
//            do {
//                a = rand.nextInt(xBytes.size());
//            } while (!setIdx.add(a));
//            shareToRecover.add(shares.get(a));
//        }
//        BigInteger keyRecovered = Shamir.recoverKey(shareToRecover, prime);
//        System.out.println("Key akhir : " + keyRecovered + " " + keyRecovered.bitLength());

        
        /*Testing Lagrange interpolation - Pass*/
//        BigInteger prime = new BigInteger("6");
//        List<PointByte> points = new ArrayList<>();
//        points.add(new PointByte(BigInteger.ONE, new BigInteger("12")));
//        points.add(new PointByte(new BigInteger("2"), new BigInteger("13")));
//        GfPolynomial poly = GfPolynomial.interpolatePolynomial(points, prime);
//        System.out.println(poly + "\n " +poly.evaluatePolynomial(new BigInteger("0")));

        /*Protect Mechanism*/
        String file = mainDir + "testFile_2.txt";
        GfPolynomial poly = Protection.protect(file, 3);
        System.out.println(poly.getDegree());
    }
    
}
