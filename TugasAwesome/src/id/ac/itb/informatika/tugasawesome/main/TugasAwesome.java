
package id.ac.itb.informatika.tugasawesome.main;

import id.ac.itb.informatika.tugasawesome.process.Encryption;
import id.ac.itb.informatika.tugasawesome.process.Mapping;
import id.ac.itb.informatika.tugasawesome.process.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.ByteArrayOp;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.utils.WordProcessor;
import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> tes = FileProcessor.readFile(mainDir + "testFile_2.txt");
//        System.out.println(tes.size());
//        for (String a : tes) {
//            System.out.println(a);
//        }
//        
//        System.out.println("==============================");
        List<String> dictionaryWord = WordProcessor.getAllWord(tes);
//        System.out.println(dictionaryWord.size());
//        int i = 0;
//        for (String a : dictionaryWord) {
//            System.out.println(i + ":" +a);
//            i++;
//        }
            
        /** Tests encrypt and decrypt **/
//        byte[] plainFile = FileProcessor.readFileAsBytes(mainDir + "testFile.txt");
//        System.out.println(plainFile.length);
//        System.out.println(Arrays.toString(plainFile));
//        System.out.println("=========key===============");
//        byte[] key = Encryption.generateKey();
//        System.out.println(key.length);
//        System.out.println(Arrays.toString(key));
//        System.out.println("=========encrypt===============");
//        byte[] cipher = Encryption.encrypt(plainFile, key, "boomboomboomboom");
//        System.out.println(cipher.length);
//        System.out.println(Arrays.toString(cipher));
//        System.out.println("===========decrypt=============");
//        if (Encryption.checkFirstBlock(ByteArrayOp.getFirstBlock(cipher, 16), key, "boomboomboomboom")) {
//            byte[] decrypted = Encryption.decrypt(cipher, key, "boomboomboomboom");
//            byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
//            System.out.println(plain2.length);
//            System.out.println(Arrays.toString(plain2));
//        }
          
            
        /** Tests Mapping **/
//        byte[] salt = ByteArrayOp.randomByte(16);
//        System.out.println(ByteArrayOp.toHex(salt));
//        String word = "bijective";
//        byte[] hashWord = Mapping.Hash(word.getBytes(), salt);
//        System.out.println(ByteArrayOp.toHex(hashWord));
//        PointByte point = new PointByte(hashWord);
//        point.print();
                
        List<PointByte> listPoint = Mapping.wordsToPoint(dictionaryWord);
        
        //send x-koordinates to secret share
        
        //secret share : choose x0 random, create polinomial so p(x0)=K
        
    }
    
}
