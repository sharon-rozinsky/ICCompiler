package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

import IC.SemanticChecks.SemanticUtils;
import IC.Types.Kind;

public class MethodSymbolTable extends SymbolTable {
	private Map<String, Symbol> parameters;
	private Map<String, Symbol> variables;
	
	public MethodSymbolTable(String id, SymbolTable parentSymbolTable) {
		super(id, parentSymbolTable);
		parameters = new HashMap<String, Symbol>();
		variables = new HashMap<String, Symbol>();
	}

	@Override
	public void addSymbol(Symbol symbol) {
		Kind symbolKind = symbol.getKind();
		if(symbolKind == Kind.Parameter){
			parameters.put(symbol.getId(), symbol);
		} else if(symbolKind == Kind.MethodVariable){
			variables.put(symbol.getId(), symbol);
		}
	}

	@Override
	public Symbol getSymbol(String id) {
		if(parameters.containsKey(id)){
			return parameters.get(id);
		} else if (variables.containsKey(id)){
			return variables.get(id);
		} else {
			return getParentSymbolTable().getSymbol(id);
		}
	}

	@Override
	public boolean symbolContainedInCurrentScope(String id) {
		if(parameters.containsKey(id) || variables.containsKey(id))
			return true;
		return false;
	}
	
	public Map<String, Symbol> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Symbol> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Symbol> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Symbol> variables) {
		this.variables = variables;
	}
	
	@Override
	public String toString() {
		return "Method Symbol Table: "
				+ id
				+ "\n"
				+ SemanticUtils.getSymbolTableDescription(this,
						parameters.values(), variables.values());
	}
}
