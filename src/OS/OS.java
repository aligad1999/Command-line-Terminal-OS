package os;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Path.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Vector;
import static os.Terminal.File;
/**
 *
 * @author ALI
 */
public class OS {
    public static void main(String[] args) throws IOException {
        PrintWriter br = new PrintWriter(new FileWriter("LOL.txt"),true);
        br.print("Run");     
        Terminal a = new Terminal();
//        Scanner in = new Scanner(System.in);
//        String s1,s2;
//        s1 = in.nextLine();  
//        s2 = in.nextLine();
    a.cp("Q:/Java/OS/a", "Q:/Java/OS/b");
        br.close();
    }
    
}
