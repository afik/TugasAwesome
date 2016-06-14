package id.ac.itb.informatika.tugasawesome.protection.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public abstract class FileProcessor {
    
    public FileProcessor() {}
    
    public abstract List<String> readFile(Path path);
    
    /**
     * Read file specified by {path} as byte array
     * @param path
     * @return 
     */
    public byte[] readFileAsBytes(Path path) {
        File file = path.toFile();
        FileInputStream fin = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fin = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fin.read(buf))!=-1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (Exception e) {
            System.err.format("IO Exception : " + e.getMessage());
        } finally {
            try {
                if (fin != null) {
                    fin.close();
                } 
            } catch (Exception e) {
                System.err.format("IO Exception : " + e.getMessage());
            }
        }
        return bos.toByteArray();
    }
    
    public void saveToFile(byte[] content, Path savePath, Path original) {
        Path finalPath = Paths.get(savePath.toString() + "/" + original.getFileName().toString());
        File newfile = new File(finalPath.toString());
        try {
            BasicFileAttributes attr = Files.readAttributes(original, BasicFileAttributes.class);
            
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(content);
            fos.flush();
            fos.close();
            Files.setAttribute(finalPath, "creationTime", attr.creationTime());
            Files.setAttribute(finalPath, "lastModifiedTime", attr.lastModifiedTime());
            Files.setAttribute(finalPath, "creationTime", attr.lastAccessTime());
            
        } catch (Exception e) {
            System.err.format("IO Exception : " + e.getMessage());
        } 
    }
    
    public static String getFileExtension(final File file) {
        try (TikaInputStream tikaIS = TikaInputStream.get(file)) {

            final Metadata metadata = new Metadata();
           
            return DETECTOR.detect(tikaIS, metadata).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static final Detector DETECTOR = new DefaultDetector(
            MimeTypes.getDefaultMimeTypes());
    
    public static boolean isWordDocument(final File file) {
        return ("application/x-tika-ooxml").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isTxtDocument(final File file) {
        return ("text/plain").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isPdfDocument(final File file) {
        return ("application/pdf").equalsIgnoreCase(getFileExtension(file));
    }
    
}
