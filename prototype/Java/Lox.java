package prototype.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * the task of this program is intended for scanning and lexing
 * @author: Bob Nystrom, annotated by Dave Amiana
 */

public class Lox{
    static boolean hadError = false;
    public static void main(String[] args) throws IOException{
        if(args.length>1){
            System.out.println("Usage: jlox [script]");
            System.exit(64); // terminates the program
        } 
        else if(args.length == 1){
            runFile(args[0]); 
        }
        else{
            runPrompt();
        }
    }

    /**
     * reads file from the command prompt
     */
    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if(hadError) System.exit(65); // exit prompt; indicate an error in the exit code
    }

    /**
     * runs the interpreter interactively; executes code one at a time
     */
    private static void runPrompt() throws IOException{
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        
        for(;;){
        // infinite loop, can be modified to do while?
            System.out.print(">> ");
            String line = reader.readLine();
            if(line == null) break;
            run(line);
            hadError = false; // if the user made a mistake it shouldn't kill the entire session
        }
    }
 
    private static void run(String source){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // for now, just print tokens
        for(Token token: tokens){ 
            System.out.println(token);
        }
    }

    /**
     * error handling functions {error and report}
     * @adeeconometrics you can improve this
     */
    static void error(int line, String message){
        report(line, "", message);
    }

    private static void report(int line, String where, String message){
        System.err.println("[line " + line + "] Error" + where + ": " + message);
    }
}