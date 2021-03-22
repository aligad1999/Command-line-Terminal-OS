package os;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.in;
import static java.lang.System.lineSeparator;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Vector;

public class Terminal {

    public static String current = "Q:\\";
    public static String File = "";
    public static boolean flag = false;
    public static int toFile = 0;
    
    
    public static void cp(String sourcePath, String destinationPath) { //Doesn't copy Directories
        if (sourcePath == "" || destinationPath == "") {
            out.println("command cp should have two parameters.");
            return;
        }
        try 
        {
            Files.copy(new File(sourcePath).toPath(), new File(destinationPath).toPath());
            System.out.println("Done");
        } 
        catch (IOException e) {
            out.println("No Such File or Directory");
        }
    }


    
    
    

}
