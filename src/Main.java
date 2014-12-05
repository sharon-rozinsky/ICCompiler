import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;
import IC.AST.ASTNode;
import IC.AST.PrettyPrinter;
import IC.Error.LexicalError;
import IC.Error.SyntaxError;


public class Main {
	
	private static boolean printtokens = true;

	public static void main(String[] args) throws LexicalError {
		Reader reader;
		try {
			if(args.length != 0) {
				//parser.isDebugMode = printtokens;
				// read the program file and parse it
				reader = new FileReader(new File(args[0]));
				Lexer lexer = new Lexer(reader);
				parser parser = new parser(lexer);
				Symbol parseSymbol = parser.parse();
				System.out.println("The file: " + args[0] + " was successfully parsed");
				
				// print the AST of the program
				PrettyPrinter printer = new PrettyPrinter(args[0]);
				ASTNode root = (ASTNode) parseSymbol.value;
				String output = (String) root.accept(printer);
				System.out.println(output);
				reader.close();
				
				// Check if there is a library file and parse it
				if(args.length == 2){
					if(!args[1].startsWith("-L")){
						System.out.println("The second parameter should start with -L");
					} else{
						String fileName = args[1].substring(2);
						reader = new FileReader(new File(fileName));
						Lexer libLexer = new Lexer(reader);
						LibraryParser libParser = new LibraryParser(libLexer);
						Symbol libParseSymbol = libParser.parse();
						System.out.println("The file: " + args[1] + " was successfully parsed");
						
						printer = new PrettyPrinter(args[1]);
						ASTNode libRoot = (ASTNode) libParseSymbol.value;
						output = (String) libRoot.accept(printer);
						System.out.println(output);
						reader.close();
					}
				}
			} else{
				System.out.println("No file was given, please run again and insert which file do you want to parse!");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			
		} catch (LexicalError e){
			PrintTokenError(e.getMessage());
		} catch(SyntaxError e){
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}
	
	public static void PrintHeader(){
		System.out.println("token\ttag\tline :column");
	}
	
	public static void PrintTokenError(String errMsg) { 
		System.err. println ("Error!\t"+errMsg);
	}

}