
package id.ac.itb.informatika.tugasawesome.main;

import id.ac.itb.informatika.tugasawesome.process.Encryption;
import id.ac.itb.informatika.tugasawesome.process.Mapping;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.process.Shamir;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.process.Extraction;
import id.ac.itb.informatika.tugasawesome.process.Protection;
import id.ac.itb.informatika.tugasawesome.utils.WordProcessor;
import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
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
//        String mainDir = "../test/";
        
        /**Test word processor**/
//        Path p = Paths.get(mainDir + "txt\\testFile_3.txt");
//        List<String> tes = FileProcessor.readFile(p);
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
//        byte[] plainFile = FileProcessor.readFileAsBytes(mainDir + "testFile_2.txt");
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
//        if (Encryption.checkFirstBlock(Operations.getFirstBlock(cipher, 16), key)) {
//            byte[] decrypted = Encryption.decrypt(cipher, key);
//            FileProcessor.saveToFile(decrypted, mainDir+"dec.txt");
//            byte[] dec = FileProcessor.readFileAsBytes(mainDir+"dec.txt");
//            byte[] plain2 = Arrays.copyOfRange(dec, 16, dec.length);
//            System.out.println(plain2.length);
//            System.out.println(Arrays.toString(plain2));
//        }
            
        /** Tests Mapping - Pass**/
//        BigInteger prime = Operations.randomPrime(null);
//        BigInteger salt = Operations.randomByte();
//        System.out.println(salt);
//        String word = "bijective";
//        byte[] hashWord = Mapping.Hash(word.getBytes(), salt.toByteArray());
//        System.out.println(Operations.toHex(hashWord));
//        PointByte point = new PointByte(hashWord, prime);
//        point.printHex();


        /*Testing secret share - Pass*/
//        //generate key
//        byte[] key = Encryption.generateKey();
//        BigInteger keyB = new BigInteger(1,key);
//        System.out.println("Key awal : " + keyB + ", " + keyB.bitLength());
//        
//        //generate random prime
//        BigInteger prime = Operations.getPrime();
//        System.out.println("Prime : " + prime + ", l : " + prime.bitLength());
//        
//        
//        //compute domain
//        List<BigInteger> xBytes = new ArrayList<>();
//        List<PointByte> listPoint = Mapping.wordsToPoint(tes, prime);
//        for (PointByte point : listPoint) {
//            xBytes.add(point.getX());
//        }
////        List<BigInteger> xBytes2 = new ArrayList<>();
////        for (int i = 0; i<10; i++) {
////            BigInteger randVal = ByteArrayOp.randomByte();
////            xBytes.add(randVal);
////        }
//        
//        
//        //split key
//        List<PointByte> shares = Shamir.splitKey(key, 3, xBytes, prime);
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
//        shareToRecover.add(new PointByte(Operations.randomByte(), Operations.randomByte(), prime, false));
//        shareToRecover.add(new PointByte(Operations.randomByte(), Operations.randomByte(), prime, false));
//        shareToRecover.add(new PointByte(Operations.randomByte(), Operations.randomByte(), prime, false));
//        BigInteger keyRecovered = Shamir.recoverKey(shareToRecover, prime);
//        System.out.println("Key akhir : " + keyRecovered + " " + keyRecovered.bitLength());

        
        /*Testing Lagrange interpolation - Pass*/
//        List<PointByte> points = new ArrayList<>();
//        BigInteger prime = Operations.getPrime();
//        BigInteger x1 = Operations.randomByte();
//        BigInteger x2 = Operations.randomByte();
//        BigInteger x3 = Operations.randomByte();
//        BigInteger y1 = Operations.randomByte();
//        BigInteger y2 = Operations.randomByte();
//        BigInteger y3 = Operations.randomByte();
//        PointByte p1 = new PointByte(x1,y1, prime, false);
//        PointByte p2 = new PointByte(x2,y2, prime, false);
//        PointByte p3 = new PointByte(x3,y3, prime, false);
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p3);
//        points.add(p1);
//        points.add(p2);
//        points.add(p3);
        
//        GfPolynomial poly = GfPolynomial.interpolatePolynomial(points, prime);
//        System.out.println(poly.evaluatePolynomial(x1));
//        System.out.println(poly.evaluatePolynomial(x2));
//        System.out.println(poly.evaluatePolynomial(x3));
        
        /*Protect Mechanism*/
        Path file = Paths.get(mainDir + "txt/testFile.txt");
        
        //P.1 Encrypt message
        byte[] filePlain = FileProcessor.readFileAsBytes(file);
        byte[] key = Encryption.generateKey();
        System.out.println("Key " + new BigInteger(1,key) + " : " + new BigInteger(1,key).bitLength());
        byte[] cipher = Encryption.encrypt(filePlain, key);
        Path toSave = Paths.get(mainDir + "cipher/");
        FileProcessor.saveToFile(cipher, toSave, "cipher.txt");
        
        //P.2 and P.3
        int threshold = 3;
        GfPolynomial poly = Protection.protect(file, key, threshold);
        
        /*Extraction Mechanism*/
        List<String> guess = Arrays.asList("level", "Mobile", "package", "class", "JAVA");
//        List<String> guess = Arrays.asList("afik", "integer", "tolep", "ipsum", "Lorem");
//        List<String> guess = Arrays.asList("token", "defaults", "notice", "xanadu", "scanner");
        Path toRead = Paths.get(toSave.toString() + "/cipher.txt");
        byte[] fileCipher = FileProcessor.readFileAsBytes(toRead);
        String plain = Extraction.extract(fileCipher, poly, guess, threshold);
        System.out.println(plain);
        
        
    }
    
}
