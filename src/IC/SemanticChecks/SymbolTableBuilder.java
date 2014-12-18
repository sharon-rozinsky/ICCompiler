package IC.SemanticChecks;

import java.util.HashSet;
import java.util.Set;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.Expression;
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
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.Symbols.ClassSymbolTable;
import IC.Symbols.CodeBlockSymbolTable;
import IC.Symbols.GlobalSymbolTable;
import IC.Symbols.MethodSymbolTable;
import IC.Symbols.Symbol;
import IC.Symbols.SymbolTable;
import IC.Types.ClassType;
import IC.Types.Kind;
import IC.Types.MethodContent;
import IC.Types.MethodType;
import IC.Types.SymbolType;
import IC.Types.TypeTable;

public class SymbolTableBuilder implements PropagatingVisitor<SymbolTable, Boolean> {

	private String fileName; 
	private Set<VariableLocation> unresolvedReferences = new HashSet<VariableLocation>();
	
	public SymbolTableBuilder(String fileName) {
		this.fileName = fileName;
	}
	
	public Set<VariableLocation> getUnresolvedReferences(){
		return unresolvedReferences;
	}
	
	/**
	 * Given an AST node, call it's accept method.
	 * This method will be called by a parent node on it's child nodes.
	 * @param node
	 * @param symbolTable
	 * @throws SemanticError 
	 */
	private void propagate(ASTNode node, SymbolTable symbolTable) throws SemanticError{
		if(node != null){
			node.accept(this, symbolTable);
		}
	}
	
	/**
	 * Propagate over a list of nodes.
	 * @param nodeList
	 * @param symbolTable
	 * @throws SemanticError 
	 */
	private void propagate(Iterable nodeList, SymbolTable symbolTable) throws SemanticError{
		if(nodeList != null){
			for(Object node : nodeList){
				propagate((ASTNode)node, symbolTable);
			}
		}
	}
	
	@Override
	public Boolean visit(Program program, SymbolTable scope) throws SemanticError {
		GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable(fileName);
		program.setEnclosingScopeSymTable(globalSymbolTable);
		propagate(program.getClasses(), globalSymbolTable);
		return null;
	}

	@Override
	public Boolean visit(ICClass icClass, SymbolTable scope) throws SemanticError {
		String className = icClass.getName();
		String superClassName = "";
		SymbolTable parentSymbolTable = scope;
		
		// Handle super class
		if(icClass.hasSuperClass()){
			superClassName = icClass.getSuperClassName();
			if(className.equals(superClassName)){
				throw new SemanticError(icClass.getLine(), "Class name cannot be identical to super class name");
			} else if(scope.symbolContained(className)){
				throw new SemanticError(icClass.getLine(), "Another class with the same name already exist");
			} else {
				//if a super class exists, the scope of the current class is
				//nested in the scope of the super class
				ICClass superClass = TypeTable.classType(superClassName, null, null).getClassNode();
				parentSymbolTable = superClass.getEnclosingScopeSymTable();
			}
		}
		
		ClassType type = TypeTable.classType(className, null, null);
		Symbol symbol = new Symbol(className, type, Kind.Class); 
		scope.addSymbol(symbol);
		
		ClassSymbolTable classSymbolTable = new ClassSymbolTable(className, parentSymbolTable);
		icClass.setEnclosingScopeSymTable(classSymbolTable);
		
		propagate(icClass.getFields(), classSymbolTable);
		propagate(icClass.getMethods(), classSymbolTable);
		return null;
	}

	@Override
	public Boolean visit(Field field, SymbolTable scope) throws SemanticError {
		if(scope.symbolContained(field.getName())){
			throw new SemanticError(field.getLine(), "Variable with the same name already defined");
		} else {
			SymbolType type = SemanticUtils.convertNodeTypeToSymType(field.getType()); 
			Symbol symbol = new Symbol(field.getName(), type, Kind.MemberVariable);
			scope.addSymbol(symbol);
		}
		field.setEnclosingScopeSymTable(scope);
		propagate(field.getType(), scope);
		return null;
	}

	public Boolean methodVisit(Method method, SymbolTable scope, boolean isStaticMethod) throws SemanticError{
		
		String methodName = method.getName();
		SymbolType methodType = TypeTable.methodType(method);
		Kind methodKind;
		if(isStaticMethod){
			methodKind = Kind.StaticMethod;
		} else {
			methodKind = Kind.Method;
		}
		
		if(scope.symbolContainedInCurrentScope(methodName)){
			throw new SemanticError(method.getLine(), "Method overloading is not supported");
		} else if(scope.symbolContained(methodName)){
			Symbol methodInstanceSymbol = scope.getSymbol(methodName);
			Kind methodInstanceKind = methodInstanceSymbol.getKind();
			SymbolType methodInstanceType = methodInstanceSymbol.getType();
			
			if(methodType != methodInstanceType && methodKind != methodInstanceKind){
				throw new SemanticError(method.getLine(), "Method or variable with the same name already defined");
			}
		}
		
		Symbol symbol = new Symbol(methodName, methodType, methodKind);
		scope.addSymbol(symbol);
		
		MethodSymbolTable methodSymbolTable = new MethodSymbolTable(methodName, scope);
		method.setEnclosingScopeSymTable(methodSymbolTable);
		
		propagate(method.getFormals(), methodSymbolTable);
		propagate(method.getStatements(), methodSymbolTable);
		propagate(method.getType(), methodSymbolTable);
		
		return null;
	}
	
	@Override
	public Boolean visit(VirtualMethod method, SymbolTable scope) throws SemanticError {
		return methodVisit(method, scope, false);
	}

	@Override
	public Boolean visit(StaticMethod method, SymbolTable scope) throws SemanticError {
		return methodVisit(method, scope, true);
	}

	@Override
	public Boolean visit(LibraryMethod method, SymbolTable scope) throws SemanticError {
		return methodVisit(method, scope, true);
	}

	@Override
	public Boolean visit(Formal formal, SymbolTable scope) throws SemanticError {
		String formalName = formal.getName();
		if(scope.symbolContainedInCurrentScope(formal.getName())){
			throw new SemanticError(formal.getLine(), "Parameter with the same name already defined");
		} else {
			formal.setEnclosingScopeSymTable(scope);
			SymbolType type = SemanticUtils.convertNodeTypeToSymType(formal.getType());
			Symbol symbol = new Symbol(formalName, type, Kind.Parameter);
			scope.addSymbol(symbol);
			propagate(formal.getType(), scope);
		}
		return null;
	}

	@Override
	public Boolean visit(PrimitiveType type, SymbolTable scope) {
		type.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(UserType type, SymbolTable scope) {
		type.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(Assignment assignment, SymbolTable scope) throws SemanticError {
		assignment.setEnclosingScopeSymTable(scope);
		propagate(assignment.getVariable(), scope);
		propagate(assignment.getAssignment(), scope);
		return null;
	}

	@Override
	public Boolean visit(CallStatement callStatement, SymbolTable scope) throws SemanticError {
		callStatement.setEnclosingScopeSymTable(scope);
		propagate(callStatement.getCall(), scope);
		return null;
	}

	@Override
	public Boolean visit(Return returnStatement, SymbolTable scope) throws SemanticError {
		returnStatement.setEnclosingScopeSymTable(scope);
		propagate(returnStatement.getValue(), scope);
		return null;
	}

	@Override
	public Boolean visit(If ifStatement, SymbolTable scope) throws SemanticError {
		
		ifStatement.setEnclosingScopeSymTable(scope);
		propagate(ifStatement.getCondition(),scope);
		
		if(ifStatement.getOperation() instanceof StatementsBlock){
			propagate(ifStatement.getOperation(), scope);
		} else{
			propagate(ifStatement.getOperation(), new CodeBlockSymbolTable(scope));
		}
		
		if(ifStatement.hasElse()){
			if(ifStatement.getElseOperation() instanceof StatementsBlock){
				propagate(ifStatement.getElseOperation(), scope);
			} else{
				propagate(ifStatement.getElseOperation(), new CodeBlockSymbolTable(scope));
			}
		}
		return null;
	}

	@Override
	public Boolean visit(While whileStatement, SymbolTable scope) throws SemanticError {
		
		whileStatement.setEnclosingScopeSymTable(scope);
		propagate(whileStatement.getCondition(), scope);
		
		//propagate(whileStatement.getOperation(), scope);
		if(whileStatement.getOperation() instanceof StatementsBlock){
			propagate(whileStatement.getOperation(), scope);	
		} else {
			propagate(whileStatement.getOperation(), new CodeBlockSymbolTable(scope));
		}
		return null;
	}

	@Override
	public Boolean visit(Break breakStatement, SymbolTable scope) {
		breakStatement.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(Continue continueStatement, SymbolTable scope) {
		continueStatement.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(StatementsBlock statementsBlock, SymbolTable scope) throws SemanticError {
		CodeBlockSymbolTable symbolTable = new CodeBlockSymbolTable(scope);
		statementsBlock.setEnclosingScopeSymTable(symbolTable);
		propagate(statementsBlock.getStatements(), symbolTable);
		return null;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, SymbolTable scope) throws SemanticError {
		
		localVariable.setEnclosingScopeSymTable(scope);
		propagate(localVariable.getInitValue(), scope);
		if(scope.symbolContainedInCurrentScope(localVariable.getName())){
			throw new SemanticError(localVariable.getLine(), "Variable with the same name already defined");
		} else {
			SymbolType type = SemanticUtils.convertNodeTypeToSymType(localVariable.getType());
			Symbol symbol = new Symbol(localVariable.getName(), type, Kind.MethodVariable);
			scope.addSymbol(symbol);
		}
		return null;
	}

	@Override
	public Boolean visit(VariableLocation location, SymbolTable scope) throws SemanticError {
		location.setEnclosingScopeSymTable(scope);
		if(location.getLocation() == null){
			if(!scope.symbolContained(location.getName())){
				unresolvedReferences.add(location);
			} else {
				SymbolTable locationScope = scope.findSymbolTable(location.getName());
				location.setLocationScope(locationScope);
			}
		} 
		propagate(location.getLocation(), scope);
		return null;
	}

	@Override
	public Boolean visit(ArrayLocation location, SymbolTable scope) throws SemanticError {
		location.setEnclosingScopeSymTable(scope);
		propagate(location.getArray(), scope);
		propagate(location.getIndex(), scope);
		return null;
	}

	@Override
	public Boolean visit(StaticCall call, SymbolTable scope) throws SemanticError {
		call.setEnclosingScopeSymTable(scope);
		propagate(call.getArguments(), scope);
		return null;
	}

	@Override
	public Boolean visit(VirtualCall call, SymbolTable scope) throws SemanticError {
		call.setEnclosingScopeSymTable(scope);
		propagate(call.getArguments(), scope);
		propagate(call.getLocation(), scope);
		return null;
	}

	@Override
	public Boolean visit(This thisExpression, SymbolTable scope) {
		thisExpression.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(NewClass newClass, SymbolTable scope) {
		newClass.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(NewArray newArray, SymbolTable scope) throws SemanticError {
		newArray.setEnclosingScopeSymTable(scope);
		propagate(newArray.getType(), scope);
		propagate(newArray.getSize(), scope);
		return null;
	}

	@Override
	public Boolean visit(Length length, SymbolTable scope) throws SemanticError {
		length.setEnclosingScopeSymTable(scope);
		propagate(length.getArray(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, SymbolTable scope) throws SemanticError {
		binaryOp.setEnclosingScopeSymTable(scope);
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, SymbolTable scope) throws SemanticError {
		binaryOp.setEnclosingScopeSymTable(scope);
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, SymbolTable scope) throws SemanticError {
		unaryOp.setEnclosingScopeSymTable(scope);
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, SymbolTable scope) throws SemanticError {
		unaryOp.setEnclosingScopeSymTable(scope);
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(Literal literal, SymbolTable scope) {
		literal.setEnclosingScopeSymTable(scope);
		return null;
	}

	@Override
	public Boolean visit(ExpressionBlock expressionBlock, SymbolTable scope) throws SemanticError {
		expressionBlock.setEnclosingScopeSymTable(scope);
		propagate(expressionBlock.getExpression(), scope);
		return null;
	}
	
}