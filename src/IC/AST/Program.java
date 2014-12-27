package IC.AST;

import java.util.ArrayList;
import java.util.List;

import IC.SemanticChecks.SemanticError;

/**
 * Root AST node for an IC program.
 * 
 * @author Tovi Almozlino
 */
public class Program extends ASTNode {

	private List<ICClass> classes;

	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}
	
	public Object accept(LIRVisitor lirVisitor){
		return lirVisitor.visit(this);
	}
	/**
	 * Constructs a new program node.
	 * 
	 * @param classes
	 *            List of all classes declared in the program.
	 */
	public Program(List<ICClass> classes) {
		super(0);
		this.classes = classes;
	}
	
	/**
	 * Constructs a new program node.
	 * 
	 * @param class
	 *            first classes declared in the program.
	 */
	public Program(ICClass icclass) {
		super(0);
		List<ICClass> classes = new ArrayList<>();
		classes.add(icclass);
		this.classes = classes;
	}

	public List<ICClass> getClasses() {
		return classes;
	}

	@Override
	public <D, U> U accept(PropagatingVisitor<D, U> v, D context) throws SemanticError {
		return v.visit(this, context);
	}
}
