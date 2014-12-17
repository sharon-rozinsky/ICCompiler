package IC;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.TreeMap;

import java_cup.runtime.Symbol;
import IC.AST.ASTNode;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.Lexer;
import IC.Parser.LibraryParser;
import IC.Parser.Parser;
import IC.Parser.sym;
import IC.SemanticChecks.SemanticError;
import IC.Symbols.SymbolTablePrinter;
import IC.Types.TypeTable;


public class Utils {
	
	public static HashMap<String, String> SymbolToSignMap = new HashMap<String,String>();
	
	public static void initSymbolToSignMap()
	{
		SymbolToSignMap.put("EOF", "EOF");
		SymbolToSignMap.put("RETURN","return");
		SymbolToSignMap.put("CLASS","class");
		SymbolToSignMap.put("EXTENDS","extends");
		SymbolToSignMap.put("STATIC","static");
		SymbolToSignMap.put("VOID","void");
		SymbolToSignMap.put("INT","int");
		SymbolToSignMap.put("BOOLEAN","boolean");
		SymbolToSignMap.put("STR","string");
		SymbolToSignMap.put("IF","if");
		SymbolToSignMap.put("ELSE","else");
		SymbolToSignMap.put("WHILE","while");
		SymbolToSignMap.put("BREAK","break");
		SymbolToSignMap.put("CONTINUE","continue");
		SymbolToSignMap.put("THIS","this");
		SymbolToSignMap.put("NEW","new");
		SymbolToSignMap.put("LENGTH","length");
		SymbolToSignMap.put("TRUE","true");
		SymbolToSignMap.put("FALSE","false");
		SymbolToSignMap.put("NULL","null");
		SymbolToSignMap.put("EQ","=");
		SymbolToSignMap.put("PLUS","+");
		SymbolToSignMap.put("MUL","*");
		SymbolToSignMap.put("SUB","-");
		SymbolToSignMap.put("DIV","/");
		SymbolToSignMap.put("EOL",";");
		SymbolToSignMap.put("MOD","%");
		SymbolToSignMap.put("ST","<");
		SymbolToSignMap.put("STEQ","<=");
		SymbolToSignMap.put("GT",">");
		SymbolToSignMap.put("GTEQ",">=");
		SymbolToSignMap.put("EQEQ","==");
		SymbolToSignMap.put("NOTEQ","!=");
		SymbolToSignMap.put("AND","&&");
		SymbolToSignMap.put("OR","||");
		SymbolToSignMap.put("COMMA",",");
		SymbolToSignMap.put("DOT",".");
		SymbolToSignMap.put("NOT","!");
		SymbolToSignMap.put("UMINUS","-");
		SymbolToSignMap.put("LP","{");
		SymbolToSignMap.put("RP","}");
		SymbolToSignMap.put("LRP","(");
		SymbolToSignMap.put("RRP",")");
		SymbolToSignMap.put("LSP","[");
		SymbolToSignMap.put("RSP","]");
		SymbolToSignMap.put("CLASS_ID","CLASS_ID");
		SymbolToSignMap.put("ID","ID");
		SymbolToSignMap.put("INTEGER","INTEGER");
		SymbolToSignMap.put("STRING","STRING");
	}
	
	public static String getSymbolSign(String symbol)
	{
		return SymbolToSignMap.get(symbol);
	}
	
	public static String getTokenName(int tokenId){
		return sym.terminalNames[tokenId];
	}
	
	public static Program parseProgram(String filePath) throws Exception{
		Reader reader = new FileReader(new File(filePath));
		Lexer lexer = new Lexer(reader);
		Parser parser = new Parser(lexer);
		Symbol parseSymbol = parser.parse();
		ASTNode root = (ASTNode) parseSymbol.value;
		System.out.println("The file: " + filePath + " was successfully parsed");
		return (Program)root;
	}
	
	public static void parseLibrary(String filePath, Program program) throws Exception{
		if(!filePath.startsWith("-L")){
			System.out.println("The second parameter should start with -L");
		} else{
			String fileName = filePath.substring(2);
			Reader reader = new FileReader(new File(fileName));
			Lexer libLexer = new Lexer(reader);
			LibraryParser libParser = new LibraryParser(libLexer);
			Symbol libParseSymbol = libParser.parse();
			System.out.println("The file: " + filePath + " was successfully parsed");
			reader.close();
			
			((Program)program).getClasses().add(0, (ICClass)libParseSymbol.value);
		}
	}
	
	public static void printAST(String filePath, ASTNode root) throws SemanticError{
		PrettyPrinter printer = new PrettyPrinter(filePath);
		String output = (String) root.accept(printer);
		System.out.println(output);
	}
	
	public static void printTypeTable(){
		String typeT = TypeTable.printTable();
		System.out.println(typeT);
	}
	
	public static void printSymbolTable(Program root) throws SemanticError {
		SymbolTablePrinter symTablePrinter = new SymbolTablePrinter();
		String printedSymbolTables = (String)symTablePrinter.visit((Program) root);
		System.out.println(printedSymbolTables);
	}
}
