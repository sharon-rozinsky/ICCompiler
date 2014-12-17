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

	@Override
	public String toString() {
		switch (kind) {
		case Class:
			return "Class: " + id;
		case MemberVariable:
			return "Field: " + type.toString() + " " + id;
		case Method:
			return "Virtual method: " + id + " " + type.toString();
		case MethodVariable:
			return "Local variable: " + type.toString()  + " " + id;
		case Parameter:
			return "Parameter: " + type.toString() + " " + id;
		case StaticMethod:
			return "Static method: " + id + " " + type.toString();

		default:
			return null;
		}
	}
	
	
}
