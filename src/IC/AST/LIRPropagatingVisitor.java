package IC.AST;

public interface LIRPropagatingVisitor <D, U>{

	public U visit(Program program, D scope) ;

	public U visit(ICClass icClass, D scope) ;

	public U visit(Field field, D scope) ;

	public U visit(VirtualMethod method, D scope) ;

	public U visit(StaticMethod method, D scope) ;

	public U visit(LibraryMethod method, D scope) ;

	public U visit(Formal formal, D scope) ;

	public U visit(PrimitiveType type, D scope);

	public U visit(UserType type, D scope);

	public U visit(Assignment assignment, D scope) ;

	public U visit(CallStatement callStatement, D scope) ;

	public U visit(Return returnStatement, D scope) ;

	public U visit(If ifStatement, D scope) ;

	public U visit(While whileStatement, D scope) ;

	public U visit(Break breakStatement, D scope) ;

	public U visit(Continue continueStatement, D scope) ;

	public U visit(StatementsBlock statementsBlock, D scope) ;

	public U visit(LocalVariable localVariable, D scope) ;

	public U visit(VariableLocation location, D scope) ;

	public U visit(ArrayLocation location, D scope) ;

	public U visit(StaticCall call, D scope) ;

	public U visit(VirtualCall call, D scope) ;

	public U visit(This thisExpression, D scope);

	public U visit(NewClass newClass, D scope) ;

	public U visit(NewArray newArray, D scope) ;

	public U visit(Length length, D scope) ;

	public U visit(MathBinaryOp binaryOp, D scope) ;

	public U visit(LogicalBinaryOp binaryOp, D scope) ;

	public U visit(MathUnaryOp unaryOp, D scope) ;

	public U visit(LogicalUnaryOp unaryOp, D scope) ;

	public U visit(Literal literal, D scope) ;

	public U visit(ExpressionBlock expressionBlock, D scope) ;
}