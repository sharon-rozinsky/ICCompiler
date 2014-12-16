package IC.SemanticChecks;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.PropagatingVisitor;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;

public class BreakContinueChecker implements PropagatingVisitor<ASTNode, Boolean> {

	@Override
	public Boolean visit(Program program, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(ICClass icClass, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Field field, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(VirtualMethod method, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(StaticMethod method, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(LibraryMethod method, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Formal formal, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(PrimitiveType type, ASTNode scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(UserType type, ASTNode scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Assignment assignment, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(CallStatement callStatement, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Return returnStatement, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(If ifStatement, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(While whileStatement, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Break breakStatement, ASTNode scope) throws SemanticError {
		if(isInLoop(scope)){
			throw new SemanticError(breakStatement.getLine(), "break key word used outside of loop");
		}
		return true;
	}

	@Override
	public Boolean visit(Continue continueStatement, ASTNode scope) throws SemanticError {
		if(isInLoop(scope)){
			throw new SemanticError(continueStatement.getLine(), "continue key word used outside of loop");
		}
		return true;
	}
	
	public Boolean isInLoop(ASTNode scope){
		if(scope instanceof While){
			return true;
		}
		return false;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(VariableLocation location, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(ArrayLocation location, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(StaticCall call, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(VirtualCall call, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(This thisExpression, ASTNode scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(NewClass newClass, ASTNode scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(NewArray newArray, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Length length, ASTNode scope) throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(Literal literal, ASTNode scope)
		throws SemanticError {
			// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, ASTNode scope)
			throws SemanticError {
		// TODO Auto-generated method stub
		return true;
	}

}
