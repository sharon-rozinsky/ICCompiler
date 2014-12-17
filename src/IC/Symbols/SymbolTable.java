package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

public abstract class SymbolTable {
	protected String id;
	protected SymbolTable parentSymbolTable;
	protected Map<String, SymbolTable> childSymbolTables;

	public SymbolTable(String id, SymbolTable parentSymbolTable) {
		childSymbolTables = new HashMap<String, SymbolTable>();
		this.id = id;
		if(parentSymbolTable != null){
			this.parentSymbolTable = parentSymbolTable;
			addChildTable(id, parentSymbolTable);
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
		symTable.getChildSymbolTables().put(id, this);
	}
	
	public boolean symbolContained(String id){
		Symbol symbol = getSymbol(id);
		if(symbol != null){
			return true;
		}
		return false;
	}
	
	public abstract void addSymbol(Symbol symbol);
	public abstract Symbol getSymbol(String id);
	public abstract boolean symbolContainedInCurrentScope(String id);
	
	public String scopeToString(String filename){
		StringBuilder scope = new StringBuilder();
		scope.append(id+": "+ filename + "\n");
		//TODO : Shronnnnnn
		
		return "tom";
	}
}
