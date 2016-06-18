package id.ac.itb.informatika.tugasawesome.protection.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.protection.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.protection.utils.Operations;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Protection {
    
    /**
     * Perform protect algorithm P.2 and P.3
     * @param words
     * @param key
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static List<GfPolynomial> protect(List<String> words, byte[] key, int threshold) {
        //P.2 Split the secret
        BigInteger prime = Operations.getPrime();
        
        List<BigInteger> xBytes = new ArrayList<>();
        List<PointByte> hashResult = Mapping.wordsToPoint(words, prime);
        for (PointByte point : hashResult) {
            xBytes.add(point.getX());
        }
        
        List<PointByte> shares = new ArrayList<>();
        
        //To make sure we get valid shares
        int i = 1;
        do {
            shares = Shamir.splitKey(key, threshold, xBytes, prime);
            i++;
        } while (shares.isEmpty());
        
        //P.3 connect keyword to share
        List<GfPolynomial> mappingFunction = Mapping.createMappingFunction(hashResult, shares, prime);
        
        return mappingFunction;
    }
    
    public static void main(String args[]) throws IOException, Exception {
        if (args.length < 3) {
            System.out.println("Invalid argument.");
            System.out.println("Usage : java -jar Protection.jar <root_file_path> <threshold> <output>");
            return;
        }
        
        String rootPath = args[0];
        int threshold = Integer.valueOf(args[1]);
        String outputPath = args[2];
        
        List<List<GfPolynomial>> allpolynomials = new ArrayList<>();
        List<String> md5 = new ArrayList<>();
        
        //threshold value stored in GfPoly for serialization
        GfPolynomial thPoly = new GfPolynomial (threshold);
        List<GfPolynomial> p = new ArrayList<>();
        p.add(thPoly);
        allpolynomials.add(p);
        
        new File(outputPath).mkdir();
        Path toSave = Paths.get(outputPath);
        
        System.out.println("Protection started with threshold : " + threshold);
        long startTime = System.nanoTime();
        Files.walk(Paths.get(rootPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                long subTime = System.nanoTime();
                
                //TODO : count md5 each file
                
                byte[] filePlain = FileProcessor.readFileAsBytes(filePath);
                byte[] key = Encryption.generateKey();
                byte[] cipher = Encryption.encrypt(filePlain, key);
                
                FileProcessor.saveToFile(cipher, toSave, filePath);

                //P.2 and P.3
                List<String> wordsInFile = FileProcessor.readFile(filePath);
                if (wordsInFile.size() > 0) {
                    String filetype = FileProcessor.getFileExtension(filePath.toFile());
                    List<GfPolynomial> poly = protect(wordsInFile, key, threshold);
                    allpolynomials.add(poly);
                
                    long subTime2 = System.nanoTime() - subTime;
                    System.out.println(filePath + " " + filetype + " " + wordsInFile.size() + " " + subTime2/1000000L+ "ms");
                }
            }
        });
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Finished in : " + totalTime/ 1000000L + " ms");
        System.out.println("Total Files : " + (allpolynomials.size()-1));
        
        //Save GF poynomials to file
        FileOutputStream fos = new FileOutputStream(outputPath + "/meta.ser");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(allpolynomials);
        out.close();
        fos.close();
        System.out.println("Meta files saved in " + outputPath+"/meta.ser");
        
    }
    
}