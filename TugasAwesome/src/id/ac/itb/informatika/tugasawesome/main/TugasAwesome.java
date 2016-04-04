
package id.ac.itb.informatika.tugasawesome.main;

import id.ac.itb.informatika.tugasawesome.process.Encryption;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.utils.WordProcessor;
import java.io.File;
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
        
//        List<String> tes = FileProcessor.readFile(mainDir + "testFile.txt");
//        for (String a : tes) {
//            System.out.println(a);
//        }
//        
//        System.out.println("==============================");
//        List<String> dictionaryWord = WordProcessor.getAllWord(tes);
//        int i = 0;
//        for (String a : dictionaryWord) {
//            System.out.println(i + ":" +a);
//            i++;
//        }

          byte[] plainFile = FileProcessor.readFileAsBytes(mainDir + "testFile.txt");
          System.out.println(plainFile.length);
          System.out.println(Arrays.toString(plainFile));
          System.out.println("=========key===============");
          byte[] key = Encryption.generateKey();
          System.out.println(key.length);
          System.out.println(Arrays.toString(key));
          System.out.println("=========encrypt===============");
          byte[] cipher = Encryption.encrypt(plainFile, key, "boomboomboomboom");
          System.out.println(cipher.length);
          System.out.println(Arrays.toString(cipher));
          System.out.println("===========decrypt=============");
          if (Encryption.checkFirstBlock(Encryption.getFirstBlock(cipher), key, "boomboomboomboom")) {
              byte[] decrypted = Encryption.decrypt(cipher, key, "boomboomboomboom");
              byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
              System.out.println(plain2.length);
              System.out.println(Arrays.toString(plain2));
          }
          
          
    }
    
}
