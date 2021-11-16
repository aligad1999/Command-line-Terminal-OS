package os;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class Parser {
    private List<String> args = new ArrayList<>();
    private String cmd;

    public boolean parseInput(String userInput){
        if(!args.isEmpty()) args.clear();
        cmd = "";


        userInput = userInput.trim();
        String parsed = "";
        // Parsing command
        for(int i=0; i<userInput.length() && userInput.charAt(i) != ' '; ++i){
            cmd+=userInput.charAt(i);
        }

        boolean quotes = false;
        // Parsing arguments
        for(int i=cmd.length(); i<userInput.length(); ++i){
            if(userInput.charAt(i) == '"'){
                if(quotes){
                    quotes = false;
                    if(!parsed.isEmpty())
                        args.add(parsed);
                    parsed = "";
                }
                else quotes = true;
            }
            else if(userInput.charAt(i) == ' ' && !quotes){
                if(!parsed.isEmpty())
                    args.add(parsed);
                parsed = "";
            }
            else parsed+=userInput.charAt(i);
        }
        if(!parsed.isEmpty()) args.add(parsed);

        if(cmd.equals("cp")){
            if(args.size() != 2){
                System.out.println("cp requires 2 arguments");
                return false;
            }
            return true;
        }

        else if(cmd.equals("cd")){
            if(args.size() > 1){
                System.out.println("cd requires at most 1 argument");
                return false;
            }
            if(args.isEmpty()) args.add("");
            return true;
        }

        else if(cmd.equals("ls")){
            if(args.size() > 1){
                System.out.println("ls requires at most 1 argument");
                return false;
            }
            if(args.isEmpty()) args.add("");
            return true;
        }

        else if (cmd.equals("cat")) {
            if (args.isEmpty()){
                System.out.println("cat requries atleast 1 argument");
                return false;
            }
            return true;
        }

        else if (cmd.equals("mkdir"))
        {
            if (args.size() != 1){
                System.out.println("mkdir requires exactly 1 argument");
                return false;
            }
            return true;
        }

        else if (cmd.equals("rmdir"))
        {
            if (args.size() != 1){
                System.out.println("rmdir requires exactly 1 argument");
                return false;
            }
            return true;
        }

        else if (cmd.equals("rm"))
        {
            if (args.size() != 1){
                System.out.println("rm requires exactly 1 argument");
                return false;
            }
            return true;
        }

        else if (cmd.equals("args"))
        {
            if (args.size() != 1){
                System.out.println("args requires exactly 1 argument");
                return false;
            }
            return true;
        }

        else if (cmd.equals("help"))
        {
            if (!args.isEmpty())
            {
                System.out.println("help does not take arguments");
                return false;
            }
            return true;
        }

        else if (cmd.equals("date"))
        {
            if (!args.isEmpty())
            {
                System.out.println("date does not take arguments");
                return false;
            }
            return true;
        }
        else if (cmd.equals("pwd"))
        {
            if (!args.isEmpty())
            {
                System.out.println("pwd does not take arguments");
                return false;
            }
            return true;
        }

        else if (cmd.equals("clear"))
        {
            if (!args.isEmpty())
            {
                System.out.println("clear does not take arguments");
                return false;
            }
            return true;
        }

        else if(cmd.equals("more")){
            if(args.size() > 2 || args.isEmpty()){
                System.out.println("'more' requires at most 2 arguments and at least 1 argument");
                return false;
            }
            return true;
        }
        else if (cmd.equals("mv")){
            if (args.size() != 2){
                System.out.println("'mv' requires 2 arguments");
                return false;
            }

            return true;
        }
        else{
            System.out.println("Invalid command, enter 'help' for instructions.");
            return false;
        }
    }
    public String getCmd(){ return cmd; }
    public List<String> getArgs(){ return args; }

}
