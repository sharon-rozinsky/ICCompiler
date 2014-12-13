package IC.Symbols;

import IC.Types.Kind;
import IC.Types.SymbolType;

public class Symbol {
	private String id;
	private SymbolType type;
	private Kind kind;
	
	public Symbol(String id, SymbolType type, Kind kind) {
		super();
		this.id = id;
		this.type = type;
		this.kind = kind;
	}
	
	public String getId() {
		return id;
	}
	
	public SymbolType getType() {
		return type;
	}
	
	public Kind getKind() {
		return kind;
	}
}
