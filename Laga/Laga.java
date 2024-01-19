package Laga;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Laga{
    public static void main (String[] args) throws IOException {
        if (args.length > 1) {  
            System.out.println("Usage: jlaga [script]");              // script will be implemented and indicate a usage pattern to the user
            System.exit(64);                                          // can't run multiple files at once
        } else if (args.length == 1) {                                // run the given file
            runFile(args[0]);
        }
        else{
            runPrompt();              // Handle running code from command line, line by line. Look up runPrompt function.
        }
        } 

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));                   // charset used to ensure default encoding
        if (hadError) System.exit(65);                                      
        
    }

    private static void runPrompt() throws IOException {                    // running from terminal or command line
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
    }
    
    static void error(int line, String message)                 // will be improved in the future
    {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {               // function for reporting errors
        System.err.println("[line " + line + "] Error" + where + ":" + message);
        hadError = true;
    }

    static boolean hadError = false;
    }