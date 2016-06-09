package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Extraction {
    
    public static String extract(byte[] cipher, GfPolynomial poly, List<String> guess, int threshold) {
        
        if (guess.size() < threshold) {
            System.err.format("minimal number of guess is " + threshold);
            return null;
        }
        
        //E.1 Compute each share
        List<PointByte> allShare = new ArrayList<>();
        for (String word : guess) {
            PointByte p = Mapping.getShare(poly, word.toLowerCase(), poly.getPrime());
            allShare.add(p);
        }
        
        //find all combination of shares
        List<List<Integer>> shareIdx = Operations.getCombinationIdx(allShare.size(), threshold);
        
        
        BigInteger key = null; 
        boolean success = false;
        
        //try share combination
        for (int i = 0; i<shareIdx.size(); i++) {
            
            List<PointByte> subShare = new ArrayList<>();
            for (Integer sidx : shareIdx.get(i)) {
                subShare.add(allShare.get(sidx-1));
            }
            
            //E.2 Compute key
            key =  Shamir.recoverKey(subShare, poly.getPrime());
            
            //valid key byte length is 16
            if (key.toByteArray().length != 16) {
                continue;
            }
            
            //E.2 Try to decrypt
            if (Encryption.checkFirstBlock(Operations.getFirstBlock(cipher, 16), key.toByteArray())) {
                success = true;
                break;
            } 
            
        }

        if (success) {
            byte[] decrypted = Encryption.decrypt(cipher, key.toByteArray());
            byte[] plain2 = Arrays.copyOfRange(decrypted, 16, decrypted.length);
            return new String(plain2);
        } else {
            return null;   
        }

    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException {
        if (args.length < 2) {
            System.out.println("Invalid argument.");
            System.out.println("Usage : java -jar extract.jar <root_file_path> <keywords>...");
            return;
        }
        
        String rootPath = args[0];
        List<String> wordQuery = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
        
        //Load meta file
        FileInputStream fis = new FileInputStream(rootPath + "/meta.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        List<GfPolynomial> allpolynomials = (List) in.readObject();
        in.close();
        fis.close();
        
        int threshold = allpolynomials.get(0).getDegree();
        
        List<Path> listFile = new ArrayList<>();
        Files.walk(Paths.get(rootPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                listFile.add(filePath);
            }
        });
        
        if (listFile.size() != allpolynomials.size()) {
            System.err.format("Invalid meta file\n");
            return;
        }
        
        System.out.println("Searching started with query : " + wordQuery);
        boolean found = false;
        for (int idx = 1; idx <listFile.size(); idx++) {
            byte[] fileCipher = FileProcessor.readFileAsBytes(listFile.get(idx));
            String plain = Extraction.extract(fileCipher, allpolynomials.get(idx), wordQuery, threshold);
            if (plain != null) {
                System.out.println("\n Found in file " + listFile.get(idx) + "\n");
                System.out.println(plain);
                System.out.println("=============================================");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("Not found");
        }
    }
}
