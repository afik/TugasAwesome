package id.ac.itb.informatika.tugasawesome.protection.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

/**
 *
 * @author Khoirunnisa Afifah <khoirunnisa.afik@gmail.com>
 */
public abstract class FileProcessor {
    
    public FileProcessor() {}
    
    public static List<String> readFile(Path path) {
        String content = getText(path);
         
        if (content != null) {
            Set<String> setVal = new HashSet<>();
            String pattern = "[^\\w]"; //split with anything but word
            String pattern2 = "^[0-9]*$"; //eliminate all number only
            String[] splitted = content.split(pattern);
            for (String a : splitted) {
                if (a.trim().length() > 0 && !a.matches(pattern2)) {
                    setVal.add(a.toLowerCase());
                }
            }

            return new ArrayList<>(setVal);
        } else {
            return null;
        }
    }
    
    private static String getText(Path path) {
        try{
            File file = path.toFile();
            Parser parser = new AutoDetectParser();
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            FileInputStream stream = new FileInputStream(file);
            ParseContext context = new ParseContext();
            parser.parse(stream, handler, metadata, context);
            return handler.toString();
        } catch (Exception e) {
            System.err.format("Exception : " + e.getMessage());
            return null;
        }
    }
    
    
    /**
     * Read file specified by {path} as byte array
     * @param path
     * @return 
     */
    public static byte[] readFileAsBytes(Path path) {
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
    
    public static void saveToFile(byte[] content, Path savePath, Path original) {
        Path finalPath = Paths.get(savePath.toString() + "/" + original.getFileName().toString());
        File newfile = new File(finalPath.toString());
        try {
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(content);
            fos.flush();
            fos.close();
            BasicFileAttributes attr = Files.readAttributes(original, BasicFileAttributes.class);
            Files.setAttribute(finalPath, "creationTime", attr.creationTime());
            Files.setAttribute(finalPath, "lastModifiedTime", attr.lastModifiedTime());
            Files.setAttribute(finalPath, "creationTime", attr.lastAccessTime());
            Files.setAttribute(finalPath, "dos:readonly", true);
        } catch (Exception e) {
            System.err.format("IO Exception : " + e.getMessage());
        } 
    }
    
    public static void saveToFile(List<String> content, Path savePath, String filename) {
        Path finalPath = Paths.get(savePath.toString()+"/"+filename);
        try (FileWriter fw = new FileWriter(finalPath.toString())) {
            for (String s : content) {
                fw.write(s+"\n");
            }
            fw.close();
            Files.setAttribute(finalPath, "dos:readonly", true);
        } catch (Exception ex) {
            System.err.format("IO Exception : " + ex.getMessage());
        }
    }
    
    public static String getMd5(Path filepath) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(filepath.toFile());
            byte[] data = new byte[1024];
            int nread = 0;
            while((nread = in.read(data)) != -1) {
                md.update(data, 0, nread);
            }
            byte[] digest = md.digest();
            return Operations.toHex(digest);
        } catch (Exception e) {
            System.err.format("Hash Exception : " + e.getMessage());
            return null;
        }
    }
    
    public static String getFileExtension(final File file) {
        try (TikaInputStream tikaIS = TikaInputStream.get(file)) {

            final Metadata metadata = new Metadata();
           
            return DETECTOR.detect(tikaIS, metadata).toString();
        } catch (IOException e) {
            System.err.format("Exception : " + e.getMessage());
            return null;
        }
    }
    
    private static final Detector DETECTOR = new DefaultDetector(
            MimeTypes.getDefaultMimeTypes());
    
    public static boolean isWordDocument(final File file) {
        return ("application/vnd.openxmlformats-officedocument.wordprocessingml.document").equalsIgnoreCase(getFileExtension(file))
                || ("application/msword").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isTxtDocument(final File file) {
        return ("text/plain").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isPdfDocument(final File file) {
        return ("application/pdf").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isXlsDocument(final File file) {
        return ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet").equalsIgnoreCase(getFileExtension(file))
                || ("application/vnd.ms-excel").equalsIgnoreCase(getFileExtension(file));
    }
    
    public static boolean isPptDocument(final File file) {
        return ("application/vnd.openxmlformats-officedocument.presentationml.presentation").equalsIgnoreCase(getFileExtension(file));
    }
}