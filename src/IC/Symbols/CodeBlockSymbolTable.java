package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

public class CodeBlockSymbolTable extends SymbolTable {
	private Map<String, Symbol> variables;
	
	public CodeBlockSymbolTable(String id, SymbolTable parentSymbolTable) {
		super(id, parentSymbolTable);
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
}
