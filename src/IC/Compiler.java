package IC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;
import IC.AST.ASTNode;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.*;
import IC.SemanticChecks.BreakContinueChecker;
import IC.SemanticChecks.SemanticError;
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
	private static final String PRINT_AST_OPTION = "-print-ast";
	protected static final String PRINT_SYMTAB_OPTION = "-dump-symtab";
	
	public static void main(String[] args) throws LexicalError {
		try {
			Compile(args);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			
		} catch (LexicalError e){
			PrintTokenError(e.getMessage());
		} catch(SyntaxError e){
			System.out.println(e.getMessage());
		} catch(SemanticError e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void Compile(String [] args) throws Exception { 
		if(args.length != 0) {
			// read the program file and parse it
			Utils.initSymbolToSignMap();
			Program root = Utils.parseProgram(args[0]);
			if(args.length >= 2){
				ICClass libraryClass =Utils.parseLibrary(args[1], root);
				//Check correctness of library class name.
				if(libraryClass != null)
				{
					SpecialSemanticChecks.checkLibraryNameCorrectness(libraryClass);
				}
			}
			
			TypeTableBuilder ttb = new TypeTableBuilder(args[0]);				
			ttb.visit((Program) root);
			
			SymbolTableBuilder symbolBuilder = new SymbolTableBuilder(args[0]);
	        root.accept(symbolBuilder, null);  
			 
			//testing symbol scope
	        SymbolTableChecker scopeCheck = new SymbolTableChecker(symbolBuilder.getUnresolvedReferences());
	        scopeCheck.visit((Program) root, null);
	        
	        //testing type check
	        TypesCheck typeCheck = new TypesCheck();
	        typeCheck.visit((Program) root);
	        
	        //testing breakCont
	        BreakContinueChecker breakCont = new BreakContinueChecker();
	        breakCont.visit((Program) root, null);
	        
	        SpecialSemanticChecks.allNoneVoidMethodReturnsNoneVoidType((Program) root);
	        SpecialSemanticChecks.validateMainFunction((Program) root);
	        SpecialSemanticChecks.allLocalVarsInit((Program) root);
	        
	        if(args.length > 2)
	        {
	        	if(args[2].equals(PRINT_AST_OPTION)){
					Utils.printAST(args[0], root);
				}
				if(args[2].equals(PRINT_SYMTAB_OPTION)){
					Utils.printSymbolTable(root);
					Utils.printTypeTable();
				}
	        }
	        
			System.out.println("good till here !!");
		} else{
			System.out.println("No file was given, please run again and insert which file do you want to parse!");
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