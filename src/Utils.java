import java.util.HashMap;
import java.util.TreeMap;


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
}
