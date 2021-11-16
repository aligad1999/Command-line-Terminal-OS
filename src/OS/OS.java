package os;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path.*;
import java.nio.file.*;
import java.nio.file.Paths;
import java.util.Vector;
import static os.Terminal.File;

/**
 *
 * @author ALI
 */


public class OS {


    public static void main(String[] args) throws FileNotFoundException {
        Parser par = new Parser();
        Terminal ter = new Terminal();
        Scanner sc = new Scanner(System.in);

        /*
         * NOTE: System.out value changes if redirected
         * Its first occurrence (the user's screen) is saved here.
         */
        PrintStream userScreen = System.out;

        while(true)
        {
            System.setOut(userScreen); // Any output is printed on the user's screen by default
            System.out.print(ter.getDirectory()+"> ");
            String userInput = sc.nextLine();
            if(userInput.equals("exit")) break;

            String[] calls = userInput.split("\\|");
            for (String c : calls) {
                int i = c.lastIndexOf('>');
                if (i != -1) {
                    if (c.charAt(i - 1) == '>') { // operator >>
                        ter.redirectOutput(c.substring(i + 1), true);
                        c = c.substring(0, i - 1);
                    }
                    else { // operator >
                        ter.redirectOutput(c.substring(i + 1), false);
                        c = c.substring(0, i);
                    }
                }
                else System.setOut(userScreen);

                if (par.parse(c))
                {
                    switch (par.getCmd()){
                        case "cd":
                            ter.changeDirectory(par.getArgs().get(0));
                            break;
                        case "ls":
                            ter.list(par.getArgs().get(0));
                            break;
                        case "cp":
                            ter.copy(par.getArgs().get(0), par.getArgs().get(1));
                            break;
                        case "cat":
                            ter.cat(par.getArgs());
                            break;
                        case "mkdir":
                            ter.makedir(par.getArgs().get(0));
                            break;
                        case "rmdir":
                            ter.rmdir(par.getArgs().get(0));
                            break;
                        case "mv":
                            ter.mv(par.getArgs().get(0), par.getArgs().get(1));
                            break;
                        case "rm":
                            ter.rm(par.getArgs().get(0));
                            break;
                        case "args":
                            ter.args(par.getArgs().get(0));
                            break;
                        case "date":
                            ter.date();
                            break;
                        case "help":
                            ter.help();
                            break;
                        case "more":
                            // handling the case where the user will not input any numeric value.
                            if(par.getArgs().size() == 1)
                                ter.more("0", par.getArgs().get(0));
                            else ter.more(par.getArgs().get(0), par.getArgs().get(1));
                            break;
                        case "pwd":
                            ter.pwd();
                            break;
                        case "clear":
                            ter.clear();
                            break;
                        default:
                            System.out.println("Invalid Command...");
                            break;
                    }
                }
            }
        }
    }
}
