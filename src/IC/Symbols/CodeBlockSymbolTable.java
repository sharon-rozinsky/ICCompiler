package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

public class CodeBlockSymbolTable extends SymbolTable {
	public static int codeBlockNumber = 1;
	private Map<String, Symbol> variables;
	
	public CodeBlockSymbolTable(SymbolTable parentSymbolTable) {
		super("Code Block number " + codeBlockNumber++, parentSymbolTable);
		variables = new HashMap<String, Symbol>();
	}

	@Override
	public void addSymbol(Symbol symbol) {
		variables.put(symbol.getId(), symbol);
	}

	@Override
	public Symbol getSymbol(String id) {
		if(variables.containsKey(id)){
			return variables.get(id);
		} else{
			return getParentSymbolTable().getSymbol(id);
		}
	}

	@Override
	public boolean symbolContainedInCurrentScope(String id) {
		if(variables.containsKey(id))
			return true;
		return false;
	}
	
	public Map<String, Symbol> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Symbol> variables) {
		this.variables = variables;
	}
}
