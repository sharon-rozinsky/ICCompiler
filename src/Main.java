import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import com.sun.java_cup.internal.runtime.Symbol;

import IC.AST.StatementsBlock;
import IC.Error.LexicalError;


public class Main {
	
	private static boolean printtokens = true;

	public static void main(String[] args) throws LexicalError {
		Reader reader;
		ArrayList<Token> tokArr = new ArrayList<Token>();
		try {
			 if(args.length != 0)
			{
				reader = new FileReader(new File(args[0]));
				Lexer lexer = new Lexer(reader);
//				Parser parser = new Parser(scanner);
//				parser.printTokens = printtokens;
//				
//				Symbol parseSymbol = parser.parse();
//				System.out.println("Parsed " + args[0] + " successfully!");
//				StatementsBlock root = (StatementsBlock) parseSymbol.value;
//				
//				// Pretty-print the program to System.out
//				PrettyPrinter printer = new PrettyPrinter(root);
//				printer.print();
				
				Token t = lexer.next_token();
				PrintHeader();
				while(!t.getTag().equals("EOF")){
					PrintToken(t.getValue(), t.getTag(), t.getLine(), t.getColumn());
					tokArr.add(t);
					t = lexer.next_token();
				}
			}
			else
			{
				System.out.println("No file was given, please run again and insert which file do you want to parse!");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			
		} catch (LexicalError e){
			PrintTokenError(e.getMessage());
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

// Command to run from terminal to create: Lexer.java