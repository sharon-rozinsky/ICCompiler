package IC.Semantics.Symbols;

import IC.Semantics.Types.Kind;

public class Symbol {
	private String id;
	private Type type;
	private Kind kind;
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
