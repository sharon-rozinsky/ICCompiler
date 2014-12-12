package IC.Semantics.Symbols;

import java.util.HashMap;
import java.util.Map;

public abstract class SymbolTable {
	private String id;
	private SymbolTable parentSymbolTable;
	private Map<String, SymbolTable> childSymbolTables;

	public SymbolTable(String id, SymbolTable parentSymbolTable) {
		childSymbolTables = new HashMap<String, SymbolTable>();
		this.id = id;
		if(parentSymbolTable != null){
			this.parentSymbolTable = parentSymbolTable;
			addChildTable(id, this);
		}
	}
	
	public String getId() {
		return id;
	}

	public SymbolTable getParentSymbolTable() {
		return parentSymbolTable;
	}

	public Map<String, SymbolTable> getChildSymbolTables() {
		return childSymbolTables;
	}

	public void addChildTable(String id, SymbolTable symTable){
		childSymbolTables.put(id, symTable);
	}
	
	public abstract void addSymbol(Symbol symbol);
	public abstract Symbol getSymbol(String id);
}
