package IC.AST;

import IC.SemanticChecks.SemanticError;

/**
 * Break statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Break extends Statement {

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}
	
	public Object accept(LIRVisitor lirVisitor){
		return lirVisitor.visit(this);
	}
	/**
	 * Constructs a break statement node.
	 * 
	 * @param line
	 *            Line number of break statement.
	 */
	public Break(int line) {
		super(line);
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
