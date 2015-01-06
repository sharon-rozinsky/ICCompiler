package IC;

import java.io.FileNotFoundException;
import java.io.IOException;

import IC.AST.ICClass;
import IC.AST.Program;
import IC.Parser.LexicalError;
import IC.Parser.SyntaxError;
import IC.SemanticChecks.BreakContinueChecker;
import IC.SemanticChecks.MaxIntegerChecker;
import IC.SemanticChecks.SemanticError;
import IC.SemanticChecks.SpecialSemanticChecks;
import IC.SemanticChecks.SymbolTableBuilder;
import IC.SemanticChecks.SymbolTableChecker;
import IC.SemanticChecks.TypeTableBuilder;
import IC.SemanticChecks.TypesCheck;
import IC.lir.LIRUtils;
import IC.lir.lirObject.LIRProgram;

public class Compiler {
	
	private static String file;
	private static boolean printtokens = true;
	protected static final String PRINT_AST_OPTION = "-print-ast";
	protected static final String PRINT_SYMTAB_OPTION = "-dump-symtab";
	protected static final String PRINT_LIR_OPTION = "-print-lir";
	private static boolean library = false;
	private static boolean printAST = false;
	private static boolean printSym = false;
	private static boolean printLir = false;
	
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
	
		updateFlags(args);
		// read the program file and parse it
		Utils.initSymbolToSignMap();
		Program root = Utils.parseProgram(file);
		if(library){
			ICClass libraryClass =Utils.parseLibrary(args[1], root);
			//Check correctness of library class name.
			if(libraryClass != null)
			{
				SpecialSemanticChecks.checkLibraryNameCorrectness(libraryClass);
			}
		}

		TypeTableBuilder ttb = new TypeTableBuilder(args[0]);				
		ttb.visit((Program) root);

		SymbolTableBuilder symbolBuilder = new SymbolTableBuilder("Global");
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

		//testing maxInteger
		MaxIntegerChecker maxInt = new MaxIntegerChecker();
		maxInt.visit((Program) root, null);

		SpecialSemanticChecks.allNoneVoidMethodReturnsNoneVoidType((Program) root);
		SpecialSemanticChecks.validateMainFunction((Program) root);
		SpecialSemanticChecks.allLocalVarsInit((Program) root);


		if(printAST){
			Utils.printAST(args[0], root);
		}
		if(printSym){
			Utils.printSymbolTable(root);
			Utils.printTypeTable();
		}

		if(printLir)
		{
			LIRProgram lirProgram = LIRUtils.getLIRProgram(root);
			LIRUtils.printLIR(lirProgram, "file");
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
	
	public static void updateFlags(String[] args){
		if (args.length == 0) {
			System.out.println("Error: Missing input file argument!");
			System.exit(-1);
		}
		else {
			file = args[0];
		}
		
		for (int i = 1; i < args.length; ++i) {
			if(args[i].equals(PRINT_AST_OPTION))
			{
				printAST = true;
			}
			else if(args[i].equals(PRINT_SYMTAB_OPTION))
			{
				printSym = true;
			}
			else if(args[i].startsWith("-L"))
			{
				library = true;
			}
			else if(args[i].equals(PRINT_LIR_OPTION))
			{
				printLir = true;
			}
			else {
				System.out.println("Encountered unknown option: " + args[i]);
				System.exit(-1);
			}
		}
	}

}