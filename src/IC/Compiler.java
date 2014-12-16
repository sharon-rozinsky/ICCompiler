package IC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;
import IC.AST.ASTNode;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.*;
import IC.SemanticChecks.BreakContinueChecker;
import IC.SemanticChecks.SpecialSemanticChecks;
import IC.SemanticChecks.SymbolTableBuilder;
import IC.SemanticChecks.SymbolTableChecker;
import IC.SemanticChecks.TypesCheck;
import IC.Symbols.SymbolTable;
import IC.Symbols.SymbolTablePrinter;
import IC.SemanticChecks.TypeTableBuilder;
import IC.Types.TypeTable;

public class Compiler {
	
	private static boolean printtokens = true;

	public static void main(String[] args) throws LexicalError {
		Reader reader;
		try {
			if(args.length != 0) {
				//parser.isDebugMode = printtokens;
				// read the program file and parse it
				Utils.initSymbolToSignMap();
				reader = new FileReader(new File(args[0]));
				Lexer lexer = new Lexer(reader);
				Parser parser = new Parser(lexer);
				Symbol parseSymbol = parser.parse();
				System.out.println("The file: " + args[0] + " was successfully parsed");
				
				// print the AST of the program
				PrettyPrinter printer = new PrettyPrinter(args[0]);
				ASTNode root = (ASTNode) parseSymbol.value;
				TypeTableBuilder ttb = new TypeTableBuilder(args[0]);				
				ttb.visit((Program) root);
				String typeT = TypeTable.printTable();
				System.out.println(typeT);
				String output = (String) root.accept(printer);
				System.out.println(output);
				reader.close();
				
//				//testing symbol table
				SymbolTableBuilder symbolBuilder = new SymbolTableBuilder(args[0]);
		        root.accept(symbolBuilder, null);  
//	        
//				//testing symbol scope
//		        SymbolTableChecker scopeCheck = new SymbolTableChecker();
//		        scopeCheck.visit((Program) root, null);
		        
		        //print Symbol Table
		        //SymbolTablePrinter symTablePrinter = new SymbolTablePrinter();
		        //String printedSymbolTables = (String)symTablePrinter.visit((Program) root);
		        //System.out.println(printedSymbolTables);
		        
		        //testing type check
		        //TypesCheck typeCheck = new TypesCheck();
		        //typeCheck.visit((Program) root);
//		        
//		        //testing breakCont
//		        BreakContinueChecker breakCont = new BreakContinueChecker();
//		        breakCont.visit((Program) root, null);
//		        
//		        SpecialSemanticChecks.allNoneVoidMethodReturnsNoneVoidType((Program) root);
//		        SpecialSemanticChecks.validateMainFunction((Program) root);
//		        
				System.out.println("good till here !!");
				
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
		System.out.println ("Error!\t"+errMsg);
	}

}