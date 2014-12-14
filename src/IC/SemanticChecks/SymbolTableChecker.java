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
import IC.AST.Method;
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
import IC.Symbols.CodeBlockSymbolTable;
import IC.Symbols.MethodSymbolTable;
import IC.Symbols.Symbol;
import IC.Symbols.SymbolTable;
import IC.Types.ClassType;
import IC.Types.Kind;
import IC.Types.TypeTable;

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
		propagate(program.getClasses(), program);
		return null;
	}

	@Override
	public Boolean visit(ICClass icClass, ASTNode scope) {
		propagate(icClass, icClass);
		return null;
	}

	@Override
	public Boolean visit(Field field, ASTNode scope) {
		return null;
	}

	public Boolean methodVisit(Method method, ASTNode scope){
		propagate(method.getStatements(), method);
		return null;
	}
	
	@Override
	public Boolean visit(VirtualMethod method, ASTNode scope) {
		return methodVisit(method, scope);
	}

	@Override
	public Boolean visit(StaticMethod method, ASTNode scope) {
		return methodVisit(method, scope);
	}

	@Override
	public Boolean visit(LibraryMethod method, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(Formal formal, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(PrimitiveType type, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(UserType type, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(Assignment assignment, ASTNode scope) {
		propagate(assignment.getVariable(), scope);
		propagate(assignment.getAssignment(), scope);
		return null;
	}

	@Override
	public Boolean visit(CallStatement callStatement, ASTNode scope) {
		propagate(callStatement.getCall(), scope);
		return null;
	}

	@Override
	public Boolean visit(Return returnStatement, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(If ifStatement, ASTNode scope) {
		propagate(ifStatement.getCondition(), scope);
		propagate(ifStatement.getOperation(), scope);
		propagate(ifStatement.getElseOperation(), scope);
		return null;
	}

	@Override
	public Boolean visit(While whileStatement, ASTNode scope) {
		propagate(whileStatement.getCondition(), scope);
		propagate(whileStatement.getOperation(), scope);
		return null;
	}

	@Override
	public Boolean visit(Break breakStatement, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(Continue continueStatement, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, ASTNode scope) {
		propagate(statementsBlock.getStatements(), scope);
		return null;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, ASTNode scope) {
		propagate(localVariable.getInitValue(), scope);
		return null;
	}

	@Override
	public Boolean visit(VariableLocation location, ASTNode scope) {
		String name = location.getName();
		SymbolTable locationScope = location.getEnclosingScopeSymTable();
		
		if(location.getLocation() != null){
			Symbol symbol = locationScope.getSymbol(name);
			Kind symbolKind = symbol.getKind();
			
			if(symbolKind != Kind.Parameter && symbolKind != Kind.MemberVariable && symbolKind != Kind.MethodVariable){
				//TODO: error - referencing a non variable
				return false;
			}
			if(scope instanceof StaticMethod && symbolKind == Kind.MethodVariable){
				//TODO: error - referencing a member variable from static method
				return false;
			}
			if(!locationScope.symbolContained(name)){
				//TODO: error - referencing an undefined variable
				return false;
			}
			
			//TODO: check for unresolved references. This requires saving unresolved variables from
			//the symbolTableBuilder stage
		} else if(location.getLocation() instanceof This){
			SymbolTable classScope = getClassScope(locationScope);
			
			if(classScope == null)
				return false;
			
			if(!classScope.symbolContained(name)){
				//TODO: error - reference to undefined variable
				return false;
			}
			if(scope instanceof StaticMethod){
				//TODO: error - referencing a member variable from static method
				return false;
			}
			Symbol symbol = classScope.getSymbol(name);
			if(symbol.getKind() != Kind.MemberVariable){
				//TODO: error - MemberVariable referencing a non member variable
				return false;
			}
		}
		
		propagate(location.getLocation(), scope);
		return null;
	}

	@Override
	public Boolean visit(ArrayLocation location, ASTNode scope) {
		propagate(location.getArray(), scope);
		propagate(location.getIndex(), scope);
		return null;
	}

	@Override
	public Boolean visit(StaticCall call, ASTNode scope) {
		ClassType classType = TypeTable.classType(call.getClassName(), null, null);
		ICClass icClass = classType.getClassNode();
		SymbolTable classScope = icClass.getEnclosingScopeSymTable();
		Symbol symbol = classScope.getSymbol(call.getName());
		
		if(symbol.getKind() != Kind.StaticMethod){
			//TODO: error - calling an undefined static method
			return false;
		}
		
		if(classScope.symbolContained(call.getName())){
			//TODO: error - calling a static method that is not in scope.
			return false;
		}
		
		propagate(call.getArguments(), scope);
		
		return null;
	}

	@Override
	public Boolean visit(VirtualCall call, ASTNode scope) {
		String callName = call.getName();
		SymbolTable callScope = call.getEnclosingScopeSymTable();
		SymbolTable classScope = getClassScope(callScope);
		
		if(call.getLocation() == null){
			if(!classScope.symbolContained(callName)){
				//TODO: error - calling an undefined method
				return false;
			}
			
			Symbol callSymbol = classScope.getSymbol(callName);
			if(scope instanceof StaticMethod && callSymbol.getKind() == Kind.Method){
				//TODO: error - calling a non static method from a static scope
				return false;
			}
			
			if(callSymbol.getKind() != Kind.Method){
				//TODO: error - calling undefined method
				return false;
			}
		} else if(call.getLocation() instanceof This){
			if(!classScope.symbolContained(callName)){
				//TODO: error - calling an undefined method
				return false;
			}
			
			Symbol callSymbol = classScope.getSymbol(callName);
			if(scope instanceof StaticMethod){
				//TODO: error - calling a non static method from a this expression
				return false;
			}
			
			if(callSymbol.getKind() != Kind.Method){
				//TODO: error - calling undefined method
				return false;
			}
		}
		return null;
	}

	@Override
	public Boolean visit(This thisExpression, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(NewClass newClass, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(NewArray newArray, ASTNode scope) {
		propagate(newArray.getSize(), scope);
		return null;
	}

	@Override
	public Boolean visit(Length length, ASTNode scope) {
		propagate(length.getArray(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, ASTNode scope) {
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, ASTNode scope) {
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, ASTNode scope) {
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, ASTNode scope) {
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(Literal literal, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, ASTNode scope) {
		propagate(expressionBlock.getExpression(), scope);
		return null;
	}

	private SymbolTable getClassScope(SymbolTable locationScope) {
		SymbolTable classScope = null;
		if(locationScope instanceof MethodSymbolTable)
			classScope = locationScope.getParentSymbolTable();
		else if(locationScope instanceof CodeBlockSymbolTable)
			classScope = locationScope.getParentSymbolTable().getParentSymbolTable();
		return classScope;
	}
}
