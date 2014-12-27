package IC.AST;

import IC.SemanticChecks.SemanticError;
import IC.Symbols.SymbolTable;
import IC.Types.SymbolType;

/**
 * Abstract AST node base class.
 * 
 * @author Tovi Almozlino
 */
public abstract class ASTNode {

	private int line;
	private SymbolType symbolType;
	/** reference to symbol table of enclosing scope **/
	private SymbolTable enclosingScopeSymTable;

	/**
	 * Double dispatch method, to allow a visitor to visit a specific subclass.
	 * 
	 * @param visitor
	 *            The visitor.
	 * @return A value propagated by the visitor.
	 * @throws SemanticError 
	 */
	public abstract Object accept(Visitor visitor) throws SemanticError;
	public abstract Object accept(LIRVisitor lirVisitor);

	public abstract <D,U> U accept(PropagatingVisitor<D,U> v,D context) throws SemanticError;

	/**
	 * Constructs an AST node corresponding to a line number in the original
	 * code. Used by subclasses.
	 * 
	 * @param line
	 *            The line number.
	 */
	protected ASTNode(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}

	public SymbolTable getEnclosingScopeSymTable() {
		return enclosingScopeSymTable;
	}

	public void setEnclosingScopeSymTable(SymbolTable enclosingScopeSymTable) {
		this.enclosingScopeSymTable = enclosingScopeSymTable;
	}

	/**
	 * @return the symbolType
	 */
	public SymbolType getSymbolType() {
		return symbolType;
	}

	/**
	 * @param symbolType the symbolType to set
	 */
	public void setSymbolType(SymbolType symbolType) {
		this.symbolType = symbolType;
	}

}
