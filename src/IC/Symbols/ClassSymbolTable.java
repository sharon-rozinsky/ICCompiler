package IC.Symbols;

import java.util.HashMap;
import java.util.Map;

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
		
		if(symbolKind == Kind.Method){
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

}
