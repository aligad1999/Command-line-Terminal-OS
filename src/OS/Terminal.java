package os;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Terminal {
    private String currentDirectory;
    private String defaultDirectory;

    /**
     * String 'defaultDirectory' is the default directory that the shell accesses upon executing.
     * ASSERT that this directory exists on the PC before running the shell
     */
    Terminal(){
//        defaultDirectory = System.getProperty("user.home");
        defaultDirectory = "D:\\FCI\\newFolder";
        currentDirectory = defaultDirectory;
    }

    /**
     * Checks if the given directory is an absolute directory e.g. G:\Folder1\File
     * if it is not an absolute directory e.g. File.txt, the current directory is appended to the directory String
     * File.txt -> G:\File.txt
     * @param directory the directory given as an argument
     * @return directory
     */
   private String appendCurrentDirectory(String directory){
        directory = directory.trim();
        if(directory.length() < 2 || directory.charAt(1) != ':'){
            if (directory.charAt(0) != '\\' && currentDirectory.charAt(currentDirectory.length()-1) != '\\')
                directory = "\\" + directory;
            return currentDirectory+directory;
        }
        else return directory;
    }

    /**
     * Copies the file at source to the file at destination.
     * file to file
     * file to directory
     * absolute paths or relative paths are used.
     */
    public void copy(String source, String destination){
        source = appendCurrentDirectory(source);
        destination = appendCurrentDirectory(destination);
        Path sourcePath = Paths.get(source);
        Path destinationPath = Paths.get(destination);

        if(new File(source).isFile()){
            if( Files.isDirectory(destinationPath) ){
                String[] sourceArray = source.split("\\\\");
                String destinationFileName = sourceArray[sourceArray.length-1];
                destination+="\\"+destinationFileName;

                File destinationFile = new File(destination);
                try { destinationFile.createNewFile(); }
                catch (Exception e){ e.printStackTrace(); }

                try{Files.copy(sourcePath, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);}
                catch (Exception e) {e.printStackTrace();}
            }
            else if(new File(destination).isFile()){
                try { Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING ); }
                catch (Exception e) { e.printStackTrace(); }
            }
            else System.out.println("The specified destination " + "'" + destination + "'" +" is not a valid directory");

        }
        else System.out.println("Cannot find the specified file " + "'" + source + "'");
    }

    /*
    goes back to parent folder
    If no parent folder (Currently in drive), does nothing
     */
    void goBack(){
        int i = currentDirectory.length() - 1;
        while(i >= 0 && currentDirectory.charAt(i) != '\\' && currentDirectory.charAt(i) != ':')
            --i;
        if (currentDirectory.charAt(i) != ':') // no parent folder. I'm in drive
            currentDirectory = currentDirectory.substring(0, i);
    }

    public void changeDirectory(String newDirectory){
        if (newDirectory.equals(".."))
        {
            goBack();
        }
        else {
            if (!newDirectory.isEmpty()) newDirectory = appendCurrentDirectory(newDirectory);
            if (newDirectory.isEmpty())
                currentDirectory = defaultDirectory;
            else if (new File(newDirectory).isDirectory())
                currentDirectory = newDirectory;
            else
                System.out.println("'" + newDirectory + "'" + " is not a valid directory");
        }
    }

    public void list(String directory){
        if(directory.isEmpty()) directory = currentDirectory;
        directory = appendCurrentDirectory(directory);

        File folder = new File(directory);

        if(!folder.isDirectory()){
            System.out.println("'" + directory + "'" + " is not a valid directory");
            return;
        }

        File[] filesList = folder.listFiles();
        for(int i=0; i<filesList.length; i++){
            System.out.println(filesList[i].getName());
        }
    }

    /*
       Takes a List of string of FileNames
       Outputs the contents of files concatenated sequentially to System.out
       Handles relative and full paths
     */
    public void cat(List<String> fileNames)
    {
        String out = new String("");
        for (String fileName : fileNames)
        {
            fileName = appendCurrentDirectory(fileName);
            if (!(new File(fileName).isFile())){
                System.out.println(fileName + " is not a valid file path");
                return;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String line = reader.readLine();
                while (line != null) {
                    out = out + line;
                    out = out + '\n';
                    line = reader.readLine();
                }
                reader.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
        System.out.print(out);
    }

    public void makedir(String directory)
    {
        directory = appendCurrentDirectory(directory);
        if (new File(directory).mkdir())
            System.out.println("Directory created sucessfully");
        else
            System.out.println("Directory couldn't be created");
    }

    public void rmdir(String directory)
    {
        directory = appendCurrentDirectory(directory);
        if (directory.equals(defaultDirectory))
        {
            System.out.println("Cannot delete default directory");
            return;
        }
        File f = new File(directory);
        if (!f.isDirectory())
            System.out.println("Given path does not correspond to a directory");
        else if (f.delete()) {
            System.out.println("Directory deleted sucessfully");
            if (currentDirectory.equals(directory))
                currentDirectory = defaultDirectory;
        }
        else
            System.out.println("Directory couldn't be deleted\n" +
                    "Directory has to be empty before getting deleted");
    }

    public void rm(String path)
    {
        path = appendCurrentDirectory(path);
        File f = new File(path);
        if (!f.isFile())
            System.out.println("Given path does not correspond to a file");
        else if (!f.delete())
            System.out.println("File couldn't be deleted");
    }

    public void mv(String source, String destination)
    {
        source = appendCurrentDirectory(source);
        destination = appendCurrentDirectory(destination);
        if (!(new File(source).isFile()))
        {
            System.out.println("Given source path does not correspond to a file");
            return;
        }
        if (!(new File(destination).isDirectory()))
        {
            System.out.println("Given destination path does not correspond to a directory");
            return;
        }

        copy(source, destination);
        rm(source);
    }

    private void printCommandArguments(String command, ArrayList<String> args)
    {
        System.out.println("Command: " + command);
        if (args.isEmpty())
            System.out.println("No Arguments");
        for (int i = 0; i < args.size(); ++i)
        {
            System.out.print("arg" + String.valueOf(i + 1) + ": " + args.get(i));
            if (i == args.size() - 1)
                System.out.println();
            else
                System.out.print(" ");
        }
    }

    public void args(String cmd)
    {
        switch (cmd) {
            case "cp":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("SourcePath", "DestinationPath")));
                break;
            case "cd":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("DestinationPath")));
                break;
            case "ls":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("DirectoryPath")));
                break;
            case "more":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("+,- numric value (optional)", "file")));
                break;
            case "cat":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("file1", "file2", "...")));
                break;
            case "mkdir":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("DirectoryPath")));
                break;
            case "rmdir":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("DirectoryPath")));
                break;
            case "mv":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("SourcePath", "DestinationPath")));
                break;
            case "rm":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("FilePath")));
                break;
            case "args":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList("Command")));
                break;
            case "date":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList()));
                break;
            case "help":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList()));
                break;
            case "pwd":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList()));
                break;
            case "clear":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList()));
                break;
            case "exit":
                printCommandArguments(cmd, new ArrayList<String>(Arrays.asList()));
                break;
            default:
                System.out.println(cmd + " is not a valid command");
                break;
        }
    }

    public void help(){
        args("cd");
        args("ls");
        args("cp");
        args("cat");
        args("mkdir");
        args("rmdir");
        args("mv");
        args("rm");
        args("args");
        args("date");
        args("pwd");
        args("clear");
        args("more");
        args("pwd");
        args("exit");
    }

    public void date(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);
        System.out.println(formattedDate);
    }

    public void pwd(){
        System.out.println(currentDirectory);
    }

    public void clear(){
        for (int i = 0; i < 15; ++i)
            System.out.println();
    }

    /**
     * TODO the following function has a problem
     * hasNext() and hasNextLine() return false even if the file has contents and linebreaks
     * It probably has to do with the format of the file being read e.g. UTF-8
     */
    public void more(String lineControl, String filePath){
        filePath = appendCurrentDirectory(filePath);

        if(!(new File(filePath).isFile())){
            System.out.println("cannot find file " + "'" + filePath + "'");
            return;
        }
        try{
            Integer lc = Integer.parseInt(lineControl);
            File inputFile = new File (filePath);
            Scanner fileReader = new Scanner(inputFile);

            /*
             * if lc is a negative value
             * read the first |lc| lines from the file
             * if the file has remaining lines
             * output these lines when the user presses 'Enter' key
             */
            if(lc < 0){
                Integer toRead = lc*-1;
                while(fileReader.hasNext()){
                    if(toRead != 0){
                        System.out.println(fileReader.nextLine());
                        toRead--;
                        if(toRead==0) System.out.println("Press 'Enter' to view the rest of the file...");
                    }
                    else{
                        Scanner keyReader = new Scanner(System.in);
                        String readString = keyReader.nextLine();
                        Boolean canReadFile = true;
                        while(readString != null && fileReader.hasNext()){
                            if(readString.isEmpty())
                                System.out.println(fileReader.nextLine());
                            else{
                                canReadFile = false;
                                break;
                            }
                            readString = keyReader.nextLine();
                        }
                        if(!canReadFile) break;
                    }
                }
            }
            else{
                Integer readTillNow = 1; // helps identify the first line that can be printed.
                while(fileReader.hasNext()){
                    String fileData = fileReader.nextLine();
                    if(readTillNow>lc) System.out.println(fileData);
                    readTillNow++;
                }
            }
        }
        catch (Exception e){
            System.out.println("'" + lineControl + "'" + " is not a numeric value");
            return;
        }
    }

    public void redirectOutput(String Path, boolean append)
    {
        Path = appendCurrentDirectory(Path);
        File f = new File(Path);
        if(f.isDirectory()){
            System.out.println("'" + Path + "'" + " is not a valid file name");
            return;
        }
        if (!f.isFile()) {
            try {
                f.createNewFile();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            PrintStream out = new PrintStream(new FileOutputStream(Path, append));
            System.setOut(out);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getDirectory(){return currentDirectory;}
}
