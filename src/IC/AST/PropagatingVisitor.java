package IC.AST;

import IC.SemanticChecks.SemanticError;

/**
 * AST PropagatingVisitor interface.
 * Declares methods for visiting each type of AST node.
 * Every visit get the scope to which it is found in and returns the 
 * upper scope.
 * @author Tovi Almozlino
 */
public interface PropagatingVisitor <D, U>{

	public U visit(Program program, D scope) throws SemanticError;

	public U visit(ICClass icClass, D scope) throws SemanticError;

	public U visit(Field field, D scope) throws SemanticError;

	public U visit(VirtualMethod method, D scope) throws SemanticError;

	public U visit(StaticMethod method, D scope) throws SemanticError;

	public U visit(LibraryMethod method, D scope) throws SemanticError;

	public U visit(Formal formal, D scope) throws SemanticError;

	public U visit(PrimitiveType type, D scope);

	public U visit(UserType type, D scope);

	public U visit(Assignment assignment, D scope) throws SemanticError;

	public U visit(CallStatement callStatement, D scope) throws SemanticError;

	public U visit(Return returnStatement, D scope) throws SemanticError;

	public U visit(If ifStatement, D scope) throws SemanticError;

	public U visit(While whileStatement, D scope) throws SemanticError;

	public U visit(Break breakStatement, D scope) throws SemanticError;

	public U visit(Continue continueStatement, D scope) throws SemanticError;

	public U visit(StatementsBlock statementsBlock, D scope) throws SemanticError;

	public U visit(LocalVariable localVariable, D scope) throws SemanticError;

	public U visit(VariableLocation location, D scope) throws SemanticError;

	public U visit(ArrayLocation location, D scope) throws SemanticError;

	public U visit(StaticCall call, D scope) throws SemanticError;

	public U visit(VirtualCall call, D scope) throws SemanticError;

	public U visit(This thisExpression, D scope);

	public U visit(NewClass newClass, D scope) throws SemanticError;

	public U visit(NewArray newArray, D scope) throws SemanticError;

	public U visit(Length length, D scope) throws SemanticError;

	public U visit(MathBinaryOp binaryOp, D scope) throws SemanticError;

	public U visit(LogicalBinaryOp binaryOp, D scope) throws SemanticError;

	public U visit(MathUnaryOp unaryOp, D scope) throws SemanticError;

	public U visit(LogicalUnaryOp unaryOp, D scope) throws SemanticError;

	public U visit(Literal literal, D scope) throws SemanticError;

	public U visit(ExpressionBlock expressionBlock, D scope) throws SemanticError;
}
