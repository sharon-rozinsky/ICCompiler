package IC.AST;

import java.util.List;

import IC.SemanticChecks.SemanticError;

/**
 * Statements block AST node.
 * 
 * @author Tovi Almozlino
 */
public class StatementsBlock extends Statement {

	private List<Statement> statements;

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}
	
	public Object accept(LIRVisitor lirVisitor){
		return lirVisitor.visit(this);
	}
	/**
	 * Constructs a new statements block node.
	 * 
	 * @param line
	 *            Line number where block begins.
	 * @param statements
	 *            List of all statements in block.
	 */
	public StatementsBlock(int line, List<Statement> statements) {
		super(line);
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return statements;
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
