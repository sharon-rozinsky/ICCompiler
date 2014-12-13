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
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;
import IC.Symbols.SymbolTable;

public class SymbolTableChecker implements PropagatingVisitor<ASTNode, Boolean>{

	/**
	 * Given an AST node, call it's accept method.
	 * This method will be called by a parent node on it's child nodes.
	 * @param node
	 * @param symbolTable
	 */
	private void propagate(ASTNode node, ASTNode scopeNode){
		if(node != null){
			node.accept(this, scopeNode);
		}
	}
	
	/**
	 * Propagate over a list of nodes.
	 * @param nodeList
	 * @param symbolTable
	 */
	private void propagate(Iterable nodeList, ASTNode scopeNode){
		if(nodeList != null){
			for(Object node : nodeList){
				propagate((ASTNode)node, scopeNode);
			}
		}
	}
	
	@Override
	public Boolean visit(Program program, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ICClass icClass, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Field field, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(VirtualMethod method, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(StaticMethod method, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(LibraryMethod method, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Formal formal, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(PrimitiveType type, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(UserType type, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Assignment assignment, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(CallStatement callStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Return returnStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(If ifStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(While whileStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Break breakStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Continue continueStatement, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(VariableLocation location, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ArrayLocation location, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(StaticCall call, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(VirtualCall call, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(This thisExpression, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(NewClass newClass, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(NewArray newArray, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Length length, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Literal literal, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, ASTNode scope) {
		// TODO Auto-generated method stub
		return null;
	}

}
