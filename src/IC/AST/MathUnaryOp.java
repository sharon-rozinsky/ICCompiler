package IC.AST;

import IC.UnaryOps;
import IC.SemanticChecks.SemanticError;


/**
 * Mathematical unary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class MathUnaryOp extends UnaryOp {

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new mathematical unary operation node.
	 * 
	 * @param operator
	 *            The operator.
	 * @param operand
	 *            The operand.
	 */
	public MathUnaryOp(UnaryOps operator, Expression operand) {
		super(operator, operand);
	}

	@Override
	public <D, U> U accept(PropagatingVisitor<D, U> v, D context) {
		return v.visit(this, context);
	}
}
