package id.ac.itb.informatika.tugasawesome.protection.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public class PdfFileProcessor extends FileProcessor{
    
    @Override
    public List<String> readFile(Path path) {
         String content = getText(path);
         
         Set<String> setVal = new HashSet<>();
         String pattern = "[^\\w]";
         String[] splitted = content.split(pattern);
         for (String a : splitted) {
             if (a.trim().length() > 0) {
                 setVal.add(a.toLowerCase());
             }
         }
         
        return new ArrayList<>(setVal);
    }
    
    String getText (Path path) {
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = path.toFile();
        String parsedText = "";
        try {
            PDFParser parser = new PDFParser(new FileInputStream(file));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(pdDoc.getNumberOfPages());
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            System.err.format("Exception : " + e.getMessage());
        } finally {
            try {
                if (pdDoc != null) {
                pdDoc.close();
                }
            } catch (IOException e) {
                System.err.format("Exception : " + e.getMessage());
            }
        }
        return parsedText;
    }
 
}
