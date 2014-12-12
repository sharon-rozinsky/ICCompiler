package IC.Semantics.Types;

import java.util.HashMap;
import java.util.Map;

import IC.Semantics.Symbols.Symbol;
import IC.Semantics.Symbols.SymbolTable;

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
