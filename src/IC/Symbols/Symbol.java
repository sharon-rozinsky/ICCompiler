package IC.Symbols;

import IC.Types.Kind;
import IC.Types.Type;

public class Symbol {
	private String id;
	private Type type;
	private Kind kind;
	
	public Symbol(String id, Type type, Kind kind) {
		super();
		this.id = id;
		this.type = type;
		this.kind = kind;
	}
	
	public String getId() {
		return id;
	}
	
	public Type getType() {
		return type;
	}
	
	public Kind getKind() {
		return kind;
	}
}
