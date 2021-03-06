package id.ac.itb.informatika.tugasawesome.protection.process;

import id.ac.itb.informatika.tugasawesome.model.GfPolynomial;
import id.ac.itb.informatika.tugasawesome.model.PointByte;
import id.ac.itb.informatika.tugasawesome.protection.utils.FileProcessor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class Protection {
    private static int threshold;
    private static List<String> blacklist;
    
    /**
     * Perform protect algorithm P.2 and P.3
     * @param words
     * @param key
     * @param threshold to split key, shouldn't bigger than number of distinct words in {file}
     * @return 
     */
    public static List<GfPolynomial> protect(List<String> words, byte[] key) {
        //P.2 Split the secret
        BigInteger prime = Shamir.getPrime();
        
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
    
    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Invalid argument.");
            System.out.println("Usage : java -jar Protection.jar <root_file_path> <threshold> <output_path> <blacklist_path>");
            return;
        }
        
        String rootPath = args[0];
        threshold = Integer.valueOf(args[1]);
        String outputPath = args[2];
        if (args.length == 4) {
            Path bl = Paths.get(args[3]);
            blacklist = FileProcessor.readFile(bl);
        } else {
            blacklist = new ArrayList<>();
        }
        
        HashMap<String, List<GfPolynomial>> allpolynomials = new HashMap<>();
        List<String> md5 = new ArrayList<>();
        
        //threshold value stored in GfPoly for serialization
        GfPolynomial thPoly = new GfPolynomial (threshold);
        List<GfPolynomial> p = new ArrayList<>();
        p.add(thPoly);
        allpolynomials.put("threshold", p);
        
        new File(outputPath).mkdir();
        Path toSave = Paths.get(outputPath);
        
        System.out.println("Protection started with threshold : " + threshold);
        long startTime = System.nanoTime();
        Files.walk(Paths.get(rootPath)).forEach(filePath -> {
            try {
                if (Files.isRegularFile(filePath)) {
                    long subTime = System.nanoTime();

                    String hashCheck = filePath.toString() + " " +FileProcessor.getMd5(filePath);
                    md5.add(hashCheck);

                    byte[] key = Encryption.generateKey();
                    Encryption.encryptLarge(filePath, toSave, key);

                    //P.2 and P.3
                    List<String> wordsInFile = FileProcessor.readFile(filePath);
                    
                    if (wordsInFile.removeAll(blacklist)) {
                        System.out.println("blacklist found");
                    }
                    
                    if (wordsInFile != null && wordsInFile.size() > 0) {
                        String filetype = FileProcessor.getFileExtension(filePath.toFile());
                        List<GfPolynomial> poly = protect(wordsInFile, key);
                        allpolynomials.put(filePath.getFileName().toString(), poly);
                        
                        long subTime2 = System.nanoTime() - subTime;
                        System.out.println(filePath + " " + filetype + " " + wordsInFile.size() + " " + subTime2/1000000L+ "ms");
                    }else {
                        List<GfPolynomial> poly = new ArrayList<>();
                        allpolynomials.put(filePath.getFileName().toString(), poly);
                    }
                }
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
                System.out.println("File " + filePath + " not recognized.");
                List<GfPolynomial> poly = new ArrayList<>();
                allpolynomials.put(filePath.getFileName().toString(), poly);
            }
        });
        long totalTime = System.nanoTime() - startTime;
        System.out.println("Finished in : " + totalTime/ 1000000L + " ms");
        
        //Save GF poynomials to file
        FileOutputStream fos = new FileOutputStream(outputPath + "/meta.ser");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(allpolynomials);
        out.close();
        fos.close();
        System.out.println("Meta files saved in " + outputPath+"/meta.ser");
        
        //Save hash to file
        FileProcessor.saveToFile(md5, Paths.get(outputPath), "md5.txt");
        System.out.println("Md5 files saved in " + outputPath+"/md5.txt");
    }
    
}
