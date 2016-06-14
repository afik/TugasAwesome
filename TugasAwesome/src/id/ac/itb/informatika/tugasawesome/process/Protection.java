package id.ac.itb.informatika.tugasawesome.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import id.ac.itb.informatika.tugasawesome.utils.Operations;
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
     * @param file
     * @param key
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static GfPolynomial protect(Path file, byte[] key, int threshold) {
//        System.out.println("protect...");
        //P.2 Split the secret
        List<String> words = FileProcessor.readFile(file);
                
        BigInteger prime = Operations.getPrime();
//        System.out.println("Prime " + prime + " " + prime.bitLength());
        
        List<BigInteger> xBytes = new ArrayList<>();
        List<PointByte> hashResult = Mapping.wordsToPoint(words, prime);
        for (PointByte point : hashResult) {
            xBytes.add(point.getX());
        }
        
        List<PointByte> shares = new ArrayList<>();
        
        //To make sure we get valid shares
        int i = 1;
        do {
//            System.out.println("splitting ke - " + i);
            shares = Shamir.splitKey(key, threshold, xBytes, prime);
            i++;
        } while (shares.isEmpty());
        
        //P.3 connect keyword to share
        GfPolynomial mappingFunction = Mapping.createMappingFunction(hashResult, shares, prime);
        
//        System.out.println("end of protect...");
        return mappingFunction;
    }
    
    public static void main(String args[]) throws IOException {
       
        if (args.length < 3) {
            System.out.println("Invalid argument.");
            System.out.println("Usage : java -jar protect.jar <root_file_path> <threshold> <output>");
            return;
        }
        
        String rootPath = args[0];
        int threshold = Integer.valueOf(args[1]);
        String outputPath = args[2];
        
        List<GfPolynomial> allpolynomials = new ArrayList<>();
        
        //threshold value stored in GfPoly for serialization
        GfPolynomial thPoly = new GfPolynomial (threshold);
        allpolynomials.add(thPoly);
        
        System.out.println("Protection started with threshold : " + threshold);
        Files.walk(Paths.get(rootPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                System.out.println(filePath);
                byte[] filePlain = FileProcessor.readFileAsBytes(filePath);
                byte[] key = Encryption.generateKey();
                byte[] cipher = Encryption.encrypt(filePlain, key);
                new File(outputPath).mkdir();
                Path toSave = Paths.get(outputPath);
                FileProcessor.saveToFile(cipher, toSave, filePath);

                //P.2 and P.3
                GfPolynomial poly = protect(filePath, key, threshold);
                allpolynomials.add(poly);
                
            }
        });
        
        System.out.println("Total Files : " + allpolynomials.size());
        
        //Save GF poynomials to file
        FileOutputStream fos = new FileOutputStream(outputPath + "/meta.ser");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(allpolynomials);
        out.close();
        fos.close();
        System.out.println("Meta files saved in " + outputPath+"/meta.ser");
    }
}
