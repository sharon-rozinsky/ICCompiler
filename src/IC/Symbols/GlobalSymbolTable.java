package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

import IC.SemanticChecks.SemanticUtils;

public class GlobalSymbolTable extends SymbolTable{
	private Map<String, Symbol> classes;

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
		//TODO: consider throwing semantic error in here.
	}

	@Override
	public boolean symbolContainedInCurrentScope(String id) {
		if(classes.containsKey(id))
			return true;
		return false;
	}
	
	public Map<String, Symbol> getClasses() {
		return classes;
	}

	public void setClasses(Map<String, Symbol> classes) {
		this.classes = classes;
	}

	@Override
	public String toString() {
		return "Global Symbol Table: "
				+ id
				+ "\n"
				+ SemanticUtils.getSymbolTableDescription(this,
						classes.values());
	}	
}
