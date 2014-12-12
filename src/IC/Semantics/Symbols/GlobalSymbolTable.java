package IC.Semantics.Symbols;

import java.util.HashMap;
import java.util.Map;

public class GlobalSymbolTable extends SymbolTable{
	public Map<String, Symbol> classes;
	
	public GlobalSymbolTable(String id) {
		// global symbol table has no parent symbol tables.
		super(id, null);
		classes = new HashMap<String, Symbol>();
	}

	@Override
	public void addSymbol(Symbol symbol) {
		classes.put(symbol.getId(), symbol);
	}

	@Override
	public Symbol getSymbol(String id) {
		if(classes.containsKey(id)){
			return classes.get(id);
		}
		return null;
	}
}
