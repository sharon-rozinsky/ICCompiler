package IC.AST;

import IC.SemanticChecks.SemanticError;

/**
 * Array creation AST node.
 * 
 * @author Tovi Almozlino
 */
public class NewArray extends New {

	private Type type;

	private Expression size;

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}
	
	public Object accept(LIRVisitor lirVisitor){
		return lirVisitor.visit(this);
	}
	/**
	 * Constructs a new array creation expression node.
	 * 
	 * @param type
	 *            Data type of new array.
	 * @param size
	 *            Size of new array.
	 */
	public NewArray(Type type, Expression size) {
		super(type.getLine());
		this.type = type;
		this.size = size;
	}

	public Type getType() {
		return type;
	}

	public Expression getSize() {
		return size;
	}

	@Override
	public <D, U> U accept(PropagatingVisitor<D, U> v, D context) throws SemanticError {
		return v.visit(this, context);
	}
	
	@Override
	public <D, U> U accept(LIRPropagatingVisitor<D, U> v, D context) {
		return v.visit(this, context);
	}
}
