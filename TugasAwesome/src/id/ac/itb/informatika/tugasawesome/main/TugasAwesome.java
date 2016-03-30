
package id.ac.itb.informatika.tugasawesome.main;

import id.ac.itb.informatika.tugasawesome.utils.FileProcessor;
import java.io.File;
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
//        String filePath = new File("").getAbsolutePath();
//        System.out.println(filePath);
        List<String> tes = FileProcessor.readFile(mainDir + "password.txt");
        
        for (String a : tes) {
            System.out.println(a);
        }
    }
    
}
