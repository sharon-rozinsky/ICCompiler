package IC.SemanticChecks;

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
	
	public SymbolTableBuilder(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Given an AST node, call it's accept method.
	 * This method will be called by a parent node on it's child nodes.
	 * @param node
	 * @param symbolTable
	 */
	private void propagate(ASTNode node, SymbolTable symbolTable){
		if(node != null){
			node.accept(this, symbolTable);
		}
	}
	
	/**
	 * Propagate over a list of nodes.
	 * @param nodeList
	 * @param symbolTable
	 */
	private void propagate(Iterable nodeList, SymbolTable symbolTable){
		if(nodeList != null){
			for(Object node : nodeList){
				propagate((ASTNode)node, symbolTable);
			}
		}
	}
	
	@Override
	public Boolean visit(Program program, SymbolTable scope) {
		GlobalSymbolTable globalSymbolTable = new GlobalSymbolTable(fileName);
		program.setEnclosingScopeSymTable(globalSymbolTable);
		propagate(program.getClasses(), globalSymbolTable);
		return null;
	}

	@Override
	public Boolean visit(ICClass icClass, SymbolTable scope) {
		String className = icClass.getName();
		String superClassName = "";
		SymbolTable parentSymbolTable = scope;
		
		// Handle super class
		if(icClass.hasSuperClass()){
			superClassName = icClass.getSuperClassName();
			if(className.equals(superClassName)){
				//TODO: error handling
				return false;
			} else if(scope.symbolContained(className)){
				//TODO: error handling
				return false;
			} else {
				//if a super class exists, the scope of the current class is
				//nested in the scope of the super class
				ICClass superClass = TypeTable.classType(superClassName, null, null).getClassNode();
				parentSymbolTable = superClass.getEnclosingScopeSymTable();
			}
		}
		
		ClassType type = TypeTable.classType(className, null, null);
		Symbol symbol = new Symbol(className, type, Kind.Class); 
		parentSymbolTable.addSymbol(symbol);
		
		ClassSymbolTable classSymbolTable = new ClassSymbolTable(className, parentSymbolTable);
		icClass.setEnclosingScopeSymTable(classSymbolTable);
		
		propagate(icClass.getFields(), classSymbolTable);
		propagate(icClass.getMethods(), classSymbolTable);
		return null;
	}

	@Override
	public Boolean visit(Field field, SymbolTable scope) {
		if(scope.symbolContained(field.getName())){
			//TODO: error handling
			return false;
		} else {
			SymbolType type = SemanticUtils.convertNodeTypeToSymType(field.getType()); 
			Symbol symbol = new Symbol(field.getName(), type, Kind.MemberVariable);
			scope.addSymbol(symbol);
		}
		field.setEnclosingScopeSymTable(scope);
		propagate(field.getType(), scope);
		return null;
	}

	public Boolean methodVisit(Method method, SymbolTable scope, boolean isStaticMethod){
		
		String methodName = method.getName();
		SymbolType methodType = TypeTable.methodType(method);
		Kind methodKind;
		if(isStaticMethod){
			methodKind = Kind.StaticMethod;
		} else {
			methodKind = Kind.Method;
		}
		
		if(scope.symbolContainedInCurrentScope(methodName)){ //method overloading is not supported
			//TODO: error handling
			return false;
		} else if(scope.symbolContained(methodName)){
			Symbol methodInstanceSymbol = scope.getSymbol(methodName);
			Kind methodInstanceKind = methodInstanceSymbol.getKind();
			SymbolType methodInstanceType = methodInstanceSymbol.getType();
			
			if(methodType != methodInstanceType && methodKind != methodInstanceKind){
				//TODO: error handling. method name found in another scope with another kind or type
				return false;
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
	public Boolean visit(VirtualMethod method, SymbolTable scope) {
		return methodVisit(method, scope, false);
	}

	@Override
	public Boolean visit(StaticMethod method, SymbolTable scope) {
		return methodVisit(method, scope, true);
	}

	@Override
	public Boolean visit(LibraryMethod method, SymbolTable scope) {
		return methodVisit(method, scope, true);
	}

	@Override
	public Boolean visit(Formal formal, SymbolTable scope) {
		String formalName = formal.getName();
		if(scope.symbolContainedInCurrentScope(formal.getName())){
			//TODO: error handling
			return false;
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
	public Boolean visit(Assignment assignment, SymbolTable scope) {
		assignment.setEnclosingScopeSymTable(scope);
		propagate(assignment.getVariable(), scope);
		propagate(assignment.getAssignment(), scope);
		return null;
	}

	@Override
	public Boolean visit(CallStatement callStatement, SymbolTable scope) {
		callStatement.setEnclosingScopeSymTable(scope);
		propagate(callStatement.getCall(), scope);
		return null;
	}

	@Override
	public Boolean visit(Return returnStatement, SymbolTable scope) {
		returnStatement.setEnclosingScopeSymTable(scope);
		propagate(returnStatement.getValue(), scope);
		return null;
	}

	@Override
	public Boolean visit(If ifStatement, SymbolTable scope) {
		ifStatement.setEnclosingScopeSymTable(scope);
		propagate(ifStatement.getOperation(), new CodeBlockSymbolTable(scope));
		if(ifStatement.hasElse()){
			propagate(ifStatement.getElseOperation(), new CodeBlockSymbolTable(scope));
		}
		return null;
	}

	@Override
	public Boolean visit(While whileStatement, SymbolTable scope) {
		whileStatement.setEnclosingScopeSymTable(scope);
		propagate(whileStatement.getCondition(), scope);
		propagate(whileStatement.getOperation(), new CodeBlockSymbolTable(scope));
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
	public Boolean visit(StatementsBlock statementsBlock, SymbolTable scope) {
		CodeBlockSymbolTable symbolTable = new CodeBlockSymbolTable(scope);
		statementsBlock.setEnclosingScopeSymTable(symbolTable);
		propagate(statementsBlock.getStatements(), symbolTable);
		return null;
	}

	@Override
	public Boolean visit(LocalVariable localVariable, SymbolTable scope) {
		
		localVariable.setEnclosingScopeSymTable(scope);
		propagate(localVariable.getInitValue(), scope);
		if(scope.symbolContainedInCurrentScope(localVariable.getName())){
			//TODO: error handling
			return false;
		} else {
			SymbolType type = SemanticUtils.convertNodeTypeToSymType(localVariable.getType());
			Symbol symbol = new Symbol(localVariable.getName(), type, Kind.MethodVariable);
			scope.addSymbol(symbol);
		}
		return null;
	}

	@Override
	public Boolean visit(VariableLocation location, SymbolTable scope) {
		location.setEnclosingScopeSymTable(scope);
		if(location.getLocation() == null){
			if(!scope.symbolContainedInCurrentScope(location.getName())){
				//TODO: error handling - unresolved reference
			} else {
				//TODO: should i set the actual scope of location to scope of the
				//actuall variable? if so need to add method to get SymbolTable by id
			}
		} else {
			propagate(location.getLocation(), scope);
		}
		return null;
	}

	@Override
	public Boolean visit(ArrayLocation location, SymbolTable scope) {
		location.setEnclosingScopeSymTable(scope);
		propagate(location.getArray(), scope);
		propagate(location.getIndex(), scope);
		return null;
	}

	@Override
	public Boolean visit(StaticCall call, SymbolTable scope) {
		call.setEnclosingScopeSymTable(scope);
		propagate(call.getArguments(), scope);
		return null;
	}

	@Override
	public Boolean visit(VirtualCall call, SymbolTable scope) {
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
	public Boolean visit(NewArray newArray, SymbolTable scope) {
		newArray.setEnclosingScopeSymTable(scope);
		propagate(newArray.getType(), scope);
		propagate(newArray.getSize(), scope);
		return null;
	}

	@Override
	public Boolean visit(Length length, SymbolTable scope) {
		length.setEnclosingScopeSymTable(scope);
		propagate(length.getArray(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathBinaryOp binaryOp, SymbolTable scope) {
		binaryOp.setEnclosingScopeSymTable(scope);
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalBinaryOp binaryOp, SymbolTable scope) {
		binaryOp.setEnclosingScopeSymTable(scope);
		propagate(binaryOp.getFirstOperand(), scope);
		propagate(binaryOp.getSecondOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(MathUnaryOp unaryOp, SymbolTable scope) {
		unaryOp.setEnclosingScopeSymTable(scope);
		propagate(unaryOp.getOperand(), scope);
		return null;
	}

	@Override
	public Boolean visit(LogicalUnaryOp unaryOp, SymbolTable scope) {
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
	public Boolean visit(ExpressionBlock expressionBlock, SymbolTable scope) {
		expressionBlock.setEnclosingScopeSymTable(scope);
		propagate(expressionBlock.getExpression(), scope);
		return null;
	}
	
}