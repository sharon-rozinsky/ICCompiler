package IC.AST;

import java.util.List;

import IC.SemanticChecks.SemanticError;

/**
 * Virtual method call AST node.
 * 
 * @author Tovi Almozlino
 */
public class VirtualCall extends Call {

	private Expression location = null;

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}
	
	public Object accept(LIRVisitor lirVisitor){
		return lirVisitor.visit(this);
	}
	/**
	 * Constructs a new virtual method call node.
	 * 
	 * @param line
	 *            Line number of call.
	 * @param name
	 *            Name of method.
	 * @param arguments
	 *            List of all method arguments.
	 */
	public VirtualCall(int line, String name, List<Expression> arguments) {
		super(line, name, arguments);
	}

	/**
	 * Constructs a new virtual method call node, for an external location.
	 * 
	 * @param line
	 *            Line number of call.
	 * @param location
	 *            Location of method.
	 * @param name
	 *            Name of method.
	 * @param arguments
	 *            List of all method arguments.
	 */
	public VirtualCall(int line, Expression location, String name,
			List<Expression> arguments) {
		this(line, name, arguments);
		this.location = location;
	}

	public boolean isExternal() {
		return (location != null);
	}

	public Expression getLocation() {
		return location;
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
