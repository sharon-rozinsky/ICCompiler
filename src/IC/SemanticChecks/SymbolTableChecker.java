package IC.SemanticChecks;

import java.util.Set;

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

	private Set<VariableLocation> unresolvedRefrences;
    
    public SymbolTableChecker(Set<VariableLocation> unresolvedRefrences) {
        this.unresolvedRefrences = unresolvedRefrences;
    }
	
	/**
	 * Given an AST node, call it's accept method.
	 * This method will be called by a parent node on it's child nodes.
	 * @param node
	 * @param symbolTable
	 * @throws SemanticError 
	 */
	private void propagate(ASTNode node, ASTNode scopeNode) throws SemanticError{
		if(node != null){
			node.accept(this, scopeNode);
		}
	}
	
	/**
	 * Propagate over a list of nodes.
	 * @param nodeList
	 * @param symbolTable
	 * @throws SemanticError 
	 */
	private void propagate(Iterable nodeList, ASTNode scopeNode) throws SemanticError{
		if(nodeList != null){
			for(Object node : nodeList){
				propagate((ASTNode)node, scopeNode);
			}
		}
	}
	
	@Override
	public Boolean visit(Program program, ASTNode scope) throws SemanticError {
		propagate(program.getClasses(), program);
		return null;
	}

	@Override
	public Boolean visit(ICClass icClass, ASTNode scope) throws SemanticError {
		propagate(icClass.getMethods(), icClass);
		return null;
	}

	@Override
	public Boolean visit(Field field, ASTNode scope) {
		return null;
	}

	public Boolean methodVisit(Method method, ASTNode scope) throws SemanticError{
		propagate(method.getStatements(), method);
		return null;
	}
	
	@Override
	public Boolean visit(VirtualMethod method, ASTNode scope) throws SemanticError {
		return methodVisit(method, scope);
	}

	@Override
	public Boolean visit(StaticMethod method, ASTNode scope) throws SemanticError {
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
	public Boolean visit(Assignment assignment, ASTNode scope) throws SemanticError {
		propagate(assignment.getVariable(), scope);
		propagate(assignment.getAssignment(), scope);
		return null;
	}

	@Override
	public Boolean visit(CallStatement callStatement, ASTNode scope) throws SemanticError {
		propagate(callStatement.getCall(), scope);
		return null;
	}

	@Override
	public Boolean visit(Return returnStatement, ASTNode scope) throws SemanticError {
		if(returnStatement.getValue() != null){
			propagate(returnStatement.getValue(), scope);
		}
		return null;
	}

	@Override
	public Boolean visit(If ifStatement, ASTNode scope) throws SemanticError {
		propagate(ifStatement.getCondition(), scope);
		propagate(ifStatement.getOperation(), scope);
		propagate(ifStatement.getElseOperation(), scope);
		return null;
	}

	@Override
	public Boolean visit(While whileStatement, ASTNode scope) throws SemanticError {
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
	public Boolean visit(StatementsBlock statementsBlock, ASTNode scope) throws SemanticError {
		propagate(statementsBlock.getStatements(), scope);
		return null;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, ASTNode scope) throws SemanticError {
		propagate(localVariable.getInitValue(), scope);
		return null;
	}

	@Override
	public Boolean visit(VariableLocation location, ASTNode scope) throws SemanticError {
		String name = location.getName();
		SymbolTable locationScope = location.getEnclosingScopeSymTable();
		
		if(location.getLocation() != null){
			Symbol symbol = locationScope.getSymbol(name);
			if(symbol == null){
				propagate(location.getLocation(), scope);
				return null;
			}
			Kind symbolKind = symbol.getKind();
			
			if(symbolKind != Kind.Parameter && symbolKind != Kind.MemberVariable && symbolKind != Kind.MethodVariable){
				throw new SemanticError(location.getLine(), "referencing a non variable");
			}
			if(scope instanceof StaticMethod && symbolKind == Kind.MethodVariable){
				throw new SemanticError(location.getLine(), "referencing a member variable from static method");
			}
			if(!locationScope.symbolContained(name)){
				throw new SemanticError(location.getLine(), "referencing an undefined variable");
			}
		} else if(location.getLocation() instanceof This){
			SymbolTable classScope = getClassScope(locationScope);
			
			if(classScope == null)
				return false;
			
			if(!classScope.symbolContained(name)){
				throw new SemanticError(location.getLine(), "referencing an undefined variable");
			}
			if(scope instanceof StaticMethod){
				throw new SemanticError(location.getLine(), "referencing a non static member variable from static method");
			}
			Symbol symbol = classScope.getSymbol(name);
			if(symbol.getKind() != Kind.MemberVariable){
				throw new SemanticError(location.getLine(), "Member variable referencing a non member variable");
			}
		} else if(location.getLocation() == null){
			if((scope instanceof StaticMethod) && !scope.getEnclosingScopeSymTable().symbolContained(name)){
				throw new SemanticError(location.getLine(), "referencing a non static member variable from static method");
			}
			else if (unresolvedRefrences.contains(location)) {
            	throw new SemanticError(location.getLine(), "referencing an undefined variable");
            }
		}
		
		propagate(location.getLocation(), scope);
		return null;
	}

	@Override
	public Boolean visit(ArrayLocation location, ASTNode scope) throws SemanticError {
		propagate(location.getArray(), scope);
		propagate(location.getIndex(), scope);
		return null;
	}

	@Override
	public Boolean visit(StaticCall call, ASTNode scope) throws SemanticError {
		ClassType classType = TypeTable.classType(call.getClassName(), null, null);
		ICClass icClass = classType.getClassNode();
		if(icClass == null){
			throw new SemanticError(call.getLine(), "calling an undefined class");
		}
		SymbolTable classScope = icClass.getEnclosingScopeSymTable();
		Symbol symbol = classScope.getSymbol(call.getName());
		if(symbol == null)
		{
			throw new SemanticError(call.getLine(), "calling an undefined method");
		}
		if(symbol.getKind() != Kind.StaticMethod){
			throw new SemanticError(call.getLine(), "calling an undefined static method");
		}
		
		if(!classScope.symbolContained(call.getName())){
			throw new SemanticError(call.getLine(), "calling a static method that is not in scope");
		}
		
		propagate(call.getArguments(), scope);
		
		return null;
	}

	@Override
	public Boolean visit(VirtualCall call, ASTNode scope) throws SemanticError {
		String callName = call.getName();
		SymbolTable callScope = call.getEnclosingScopeSymTable();
		SymbolTable classScope = getClassScope(callScope);
		
		if(call.getLocation() == null){
			if(!classScope.symbolContained(callName)){
				throw new SemanticError(call.getLine(), "calling an undefined method");
			}
			
			Symbol callSymbol = classScope.getSymbol(callName);
			if(scope instanceof StaticMethod && callSymbol.getKind() == Kind.Method){
				throw new SemanticError(call.getLine(), "calling a non static method from a static scope");
			}
			
			if(callSymbol.getKind() != Kind.Method && callSymbol.getKind() != Kind.StaticMethod){
				throw new SemanticError(call.getLine(), "calling an undefined method");
			}
		} else if(call.getLocation() instanceof This){
			if(!classScope.symbolContained(callName)){
				throw new SemanticError(call.getLine(), "calling an undefined method");
			}
			
			Symbol callSymbol = classScope.getSymbol(callName);
			if(scope instanceof StaticMethod){
				throw new SemanticError(call.getLine(), "calling a non static method from a this expression");
			}
			
			if(callSymbol.getKind() != Kind.Method){
				throw new SemanticError(call.getLine(), "calling an undefined method");
			}
		}
		return null;
	}

	@Override
	public Boolean visit(This thisExpression, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(NewClass newClass, ASTNode scope) throws SemanticError {
		String className = newClass.getName();
		if(!TypeTable.classTypeExists(className)){
			throw new SemanticError(newClass.getLine(), "Creating an instance of undeclared class: " + className);
		}
		return null;
	}

	@Override
	public Boolean visit(NewArray newArray, ASTNode scope) throws SemanticError {
		propagate(newArray.getSize(), scope);
		return null;
	}

	@Override
	public Boolean visit(Length length, ASTNode scope) throws SemanticError {
		propagate(length.getArray(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, ASTNode scope) throws SemanticError {
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, ASTNode scope) throws SemanticError {
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, ASTNode scope) throws SemanticError {
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, ASTNode scope) throws SemanticError {
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(Literal literal, ASTNode scope) {
		return null;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, ASTNode scope) throws SemanticError {
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
