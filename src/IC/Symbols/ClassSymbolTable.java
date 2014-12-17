package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

import IC.SemanticChecks.SemanticUtils;
import IC.Types.Kind;

public class ClassSymbolTable extends SymbolTable {
	
	private Map<String, Symbol> methods;
	private Map<String, Symbol> memberVariables;
	
	public ClassSymbolTable(String id, SymbolTable parentSymbolTable) {
		super(id, parentSymbolTable);
		methods = new HashMap<String, Symbol>();
		memberVariables = new HashMap<String, Symbol>();
	}

	@Override
	public void addSymbol(Symbol symbol) {
		Kind symbolKind = symbol.getKind();
		
		if((symbolKind == Kind.Method) || (symbolKind == Kind.StaticMethod)){
			methods.put(symbol.getId(), symbol);
		} else if(symbolKind == Kind.MemberVariable){
			memberVariables.put(symbol.getId(), symbol);
		}
	}

	@Override
	public Symbol getSymbol(String id) {
		if(methods.containsKey(id)){
			return methods.get(id);
		} else if (memberVariables.containsKey(id)){
			return memberVariables.get(id);
		} else {
			return getParentSymbolTable().getSymbol(id);
		}
	}

	@Override
	public boolean symbolContainedInCurrentScope(String id) {
		if(methods.containsKey(id) || memberVariables.containsKey(id))
			return true;
		return false;
	}

	public Map<String, Symbol> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, Symbol> methods) {
		this.methods = methods;
	}

	public Map<String, Symbol> getMemberVariables() {
		return memberVariables;
	}

	public void setMemberVariables(Map<String, Symbol> memberVariables) {
		this.memberVariables = memberVariables;
	}

	@Override
	public String toString() {
		return "Class Symbol Table: "
				+ id
				+ "\n"
				+ SemanticUtils.getSymbolTableDescription(this,
						memberVariables.values(), methods.values());
	}
}
