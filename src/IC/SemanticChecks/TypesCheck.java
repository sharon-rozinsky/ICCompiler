package IC.SemanticChecks;

import IC.BinaryOps;
import IC.LiteralTypes;
import IC.UnaryOps;
import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.BinaryOp;
import IC.AST.Break;
import IC.AST.Call;
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
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UnaryOp;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.Symbols.ClassSymbolTable;
import IC.Symbols.MethodSymbolTable;
import IC.Symbols.Symbol;
import IC.Symbols.SymbolTable;
import IC.Types.ArrayType;
import IC.Types.ClassType;
import IC.Types.Kind;
import IC.Types.MethodType;
import IC.Types.SymbolType;
import IC.Types.TypeTable;

public class TypesCheck implements Visitor{
	
	private ClassSymbolTable getClassSymbolTable(ASTNode node){
		SymbolTable currentSymbolTable = node.getEnclosingScopeSymTable();
		while (!(currentSymbolTable instanceof ClassSymbolTable)){
			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}
		return (ClassSymbolTable) currentSymbolTable;
	}
	
	private MethodSymbolTable getMethodSymbolTable(ASTNode node){
		SymbolTable currentSymbolTable = node.getEnclosingScopeSymTable();
		while (!(currentSymbolTable instanceof MethodSymbolTable)){
			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}
		return (MethodSymbolTable) currentSymbolTable;
	}
	
	protected void stepIn(ASTNode node) throws SemanticError {
		if (node != null) {
			node.accept(this);
		}
	}

	@SuppressWarnings("rawtypes")
	protected void stepIn(Iterable iterable) throws SemanticError {
		if (iterable != null) {
			for (Object node : iterable) {
				stepIn((ASTNode)node);
			}
		}
	}

	@Override
	public Object visit(Program program) throws SemanticError {
		stepIn(program.getClasses());
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		stepIn(icClass.getFields());
		stepIn(icClass.getMethods());
		return null;
	}

	@Override
	public Object visit(Field field) throws SemanticError {
		stepIn(field.getType());
		ClassSymbolTable classSymbolTable = (ClassSymbolTable)  field.getEnclosingScopeSymTable();
		field.setSymbolType(classSymbolTable.getMemberVariables().get(field.getName()).getType());
		return null;
	}

	public void visitMethodWraper(Method method) throws SemanticError {
		stepIn(method.getType());
		stepIn(method.getStatements());
		stepIn(method.getFormals());

		method.setSymbolType((getClassSymbolTable(method)).getMethods().get(method.getName()).getType());
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		if(method != null)
		{
			visitMethodWraper(method);
		}
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		if(method != null)
		{
			visitMethodWraper(method);
		}
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		if(method != null)
		{
			visitMethodWraper(method);
		}
		return null;
	}

	@Override
	public Object visit(Formal formal) throws SemanticError {
		stepIn(formal.getType());

		formal.setSymbolType(((MethodSymbolTable) formal.getEnclosingScopeSymTable()).getParameters().get(formal.getName()).getType());
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) {
		type.setSymbolType(SemanticUtils.convertNodeTypeToSymType(type));
		return null;
	}

	@Override
	public Object visit(UserType type) {
		type.setSymbolType(SemanticUtils.convertNodeTypeToSymType(type));
		return null;
	}

	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		stepIn(assignment.getAssignment());
		stepIn(assignment.getVariable());

		SymbolType t1 = assignment.getAssignment().getSymbolType();
		SymbolType t2 = assignment.getVariable().getSymbolType();

		if(!t1.isSubClass(t2))
		{
			throw new SemanticError(assignment.getLine(),String.format("Can't assign variable from type %s to type %s", t2.toString(),t1.toString()));
		}

		return null;
	}


	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		stepIn(callStatement.getCall());
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		stepIn(returnStatement.getValue());
		ClassSymbolTable classSymTbl = getClassSymbolTable(returnStatement);
		MethodSymbolTable methodSymTbl = getMethodSymbolTable(returnStatement);
		
		MethodType methodType = (MethodType) classSymTbl.getMethods().get(methodSymTbl.getId()).getType();
		SymbolType methodRetType = methodType.getMethod().getRetType();
		SymbolType retType = returnStatement.getValue().getSymbolType();

		if(returnStatement.hasValue() && !(retType.isSubClass(methodRetType)))
		{
			throw new SemanticError(returnStatement.getLine(), 
					String.format("none void method return type mismatch. excpected %s but returns %s",methodRetType.toString(),retType.toString()));
		}
		if(returnStatement.hasValue() && (methodRetType == TypeTable.voidType))
		{
			throw new SemanticError(returnStatement.getLine(), 
					String.format("Void method returns none void type. %s.",retType.toString()));
		}
		if(!returnStatement.hasValue() && (methodRetType != TypeTable.voidType))
		{
			throw new SemanticError(returnStatement.getLine(), 
					String.format("none void method returns void type. excpected %s.",methodRetType.toString()));
		}

		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		stepIn(ifStatement.getCondition());
		stepIn(ifStatement.getOperation());
		stepIn(ifStatement.getElseOperation());
		
		if(ifStatement.getCondition().getSymbolType() != TypeTable.boolType)
		{
			throw new SemanticError(ifStatement.getLine(), 
					String.format("If condition must be boolean"));
		}
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		stepIn(whileStatement.getCondition());
		stepIn(whileStatement.getOperation());
		
		if(whileStatement.getCondition().getSymbolType() != TypeTable.boolType)
		{
			throw new SemanticError(whileStatement.getLine(), 
					String.format("While condition must be boolean"));
		}
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
		stepIn(statementsBlock.getStatements());
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		stepIn(localVariable.getInitValue());
		stepIn(localVariable.getType());
		
		SymbolType varType = localVariable.getType().getSymbolType();
		SymbolType initType = null;
		
		if(localVariable.hasInitValue())
			initType = localVariable.getInitValue().getSymbolType();
			if(!initType.isSubClass(varType))
			{
				throw new SemanticError(localVariable.getLine(), 
						String.format("Variable initialization with wrong type. got %s, expected %s.",initType.toString(),varType.toString()));
			}
		
		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		stepIn(location.getLocation());
		
		String locationId = location.getName();
		
		if(location.getLocation() == null)  //assign symbol type to VariableLocation. 
		{
			SymbolType locationType = location.getEnclosingScopeSymTable().getSymbol(locationId).getType();
			
			location.setSymbolType(locationType);
		}
		else //need to check that the types match.
		{
			SymbolType eType = location.getLocation().getSymbolType();
			
			if(eType instanceof ClassType)
			{
				ClassType cType = (ClassType) eType;
				ClassSymbolTable classSymTbl = (ClassSymbolTable) cType.getClassNode().getEnclosingScopeSymTable();
				
				if(classSymTbl.getMemberVariables().containsKey(locationId))
				{
					location.setSymbolType(classSymTbl.getMemberVariables().get(locationId).getType());
				}
				else
				{
					if(classSymTbl.getMethods().containsKey(locationId))
					{
						throw new SemanticError(location.getLine(), 
								String.format("None field location Identifier. %s.",locationId));
					}
					else
					{
						throw new SemanticError(location.getLine(), 
								String.format("Identifier is not defined in location %s.",locationId));
					}
				}
			}
		}
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		stepIn(location.getArray());
		stepIn(location.getIndex());
		
		SymbolType arrType = location.getArray().getSymbolType();
		SymbolType indxType = location.getIndex().getSymbolType();
		
		if(!(arrType instanceof ArrayType))
		{
			throw new SemanticError(location.getLine(), 
					String.format("Array reference to none array type. got %s.",arrType.toString()));
		}
		
		ArrayType arrTypeValid = (ArrayType) arrType;
		
		
		if(indxType != TypeTable.intType)
		{
			throw new SemanticError(location.getLine(), 
					String.format("Ilegal array index argument, index must be from type int. got %s.",indxType.toString()));
		}
		
		location.setSymbolType(arrTypeValid.getElementsType());
		return null;
	}
	
	public Object visitCallWraper(Call call,MethodType methodType) throws SemanticError {
		SymbolType retType =  methodType.getMethod().getRetType();
		SymbolType[] paramsType =  methodType.getMethod().getParams();
		
		if(call.getArguments().size() != paramsType.length)
		{
			if(call.getArguments().size() < paramsType.length)
			{
				throw new SemanticError(call.getLine(), 
						String.format("Not enough input arguments to method %s. got %d, expected %d.",methodType.toString(),call.getArguments().size(),paramsType.length));
			}
			else
			{
				throw new SemanticError(call.getLine(), 
						String.format("To many input arguments to method %s. got %d, expected %d.",methodType.toString(),call.getArguments().size(),paramsType.length));
			}
		}
		
		int i = 0;
		for(SymbolType s:paramsType)
		{
			if(!call.getArguments().get(i).getSymbolType().isSubClass(s))
			{
				throw new SemanticError(call.getLine(), 
						String.format("Type mismatch in atgument #%d (zero based). got %s, expected %s.",i,call.getArguments().get(i).getSymbolType().toString(),s.toString()));
			}
			i++;
		}
		
		call.setSymbolType(retType);
		return null;
	}
	
	
	@Override
	public Object visit(StaticCall call) throws SemanticError {
		stepIn(call.getArguments());
		
		ClassType cType = TypeTable.uniqueClassTypes.get(call.getClassName());
		
		visitCallWraper(call,(MethodType) ((ClassSymbolTable) cType.getClassNode().getEnclosingScopeSymTable()).getMethods().get(call.getName()).getType());
		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		stepIn(call.getArguments());
		stepIn(call.getLocation());
		
		if(call.getLocation() == null)  //assign symbol type to VariableLocation. 
		{
			MethodType methodType = (MethodType) ((ClassSymbolTable) call.getEnclosingScopeSymTable()).getMethods().get(call.getName()).getType();
			
			visitCallWraper(call, methodType);
		}
		else //need to check that the types match.
		{
			SymbolType eType = call.getLocation().getSymbolType();
			
			if(eType instanceof ClassType)
			{
				ClassType cType = (ClassType) eType;
				ClassSymbolTable classSymTbl = (ClassSymbolTable) cType.getClassNode().getEnclosingScopeSymTable();
				
				if(classSymTbl.getMethods().containsKey(call.getName()))
				{
					visitCallWraper(call,(MethodType) classSymTbl.getMethods().get(call.getName()).getType());
				}
				else
				{
					throw new SemanticError(call.getLine(), 
							String.format("Reference to undefind method %s.",call.getName()));
				}
			}
		}
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		MethodSymbolTable thisScope =  (MethodSymbolTable) thisExpression.getEnclosingScopeSymTable();
		ClassSymbolTable thisClassScope = (ClassSymbolTable)  thisScope.getParentSymbolTable();
		
		thisExpression.setSymbolType(TypeTable.uniqueClassTypes.get(thisClassScope.getId()));
		
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		newClass.setSymbolType(newClass.getEnclosingScopeSymTable().getSymbol(newClass.getName()).getType());
		return null;
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		stepIn(newArray.getSize());
		stepIn(newArray.getType());
		
		if(newArray.getSize().getSymbolType() != TypeTable.intType)
		{
			throw new SemanticError(newArray.getLine(), 
					String.format("Ilegal array size argument, size must be from type int. got %s.",newArray.getSize().getSymbolType().toString()));
		}
		else
		{
			newArray.setSymbolType(TypeTable.uniqueArrayTypes.get(newArray.getType().getSymbolType()));
		}
			
		return null;
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		stepIn(length.getArray());
		
		if(length.getArray().getSymbolType() instanceof ArrayType)
		{
			length.setSymbolType(TypeTable.intType);
		}
		else
		{
			throw new SemanticError(length.getLine(), 
					String.format("Ilegal use of length, %s must be array type.",length.getArray().getSymbolType()));
		}
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		stepIn(binaryOp.getFirstOperand());
		stepIn(binaryOp.getSecondOperand());
		
		BinaryOps binOp = binaryOp.getOperator();
		Expression e1 = binaryOp.getFirstOperand();
		Expression e2 = binaryOp.getSecondOperand();
		
		SymbolType _int = TypeTable.intType;
		SymbolType _string = TypeTable.strType;
		if((e1.getSymbolType() == _int) && (e2.getSymbolType() == _int)
				&& ((binOp == BinaryOps.MINUS)
						|| (binOp == BinaryOps.PLUS)
						|| (binOp == BinaryOps.DIVIDE)
						|| (binOp == BinaryOps.MULTIPLY)
						|| (binOp == BinaryOps.MOD)))
		{
			binaryOp.setSymbolType(_int);
		}
		else if((e1.getSymbolType() == _string) && (e2.getSymbolType() == _string)
				&& (binOp == BinaryOps.PLUS))
		{
			binaryOp.setSymbolType(_string);
		}
		else
		{
			throw new SemanticError(binaryOp.getLine(), 
					String.format("Binary operation is not defined for the given arguments"));
		}
		
		
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		stepIn(binaryOp.getFirstOperand());
		stepIn(binaryOp.getSecondOperand());
		
		BinaryOps binOp = binaryOp.getOperator();
		Expression e1 = binaryOp.getFirstOperand();
		Expression e2 = binaryOp.getSecondOperand();
		
		SymbolType _int = TypeTable.intType;
		SymbolType _boolean = TypeTable.boolType;
		
		if((e1.getSymbolType() == _int) && (e2.getSymbolType() == _int)
				&& ((binOp == BinaryOps.LT)
						|| (binOp == BinaryOps.LTE)
						|| (binOp == BinaryOps.GT)
						|| (binOp == BinaryOps.GTE)))
		{
			binaryOp.setSymbolType(_boolean);
		}
		else if((e1.getSymbolType() == _boolean) && (e2.getSymbolType() == _boolean)
				&& ((binOp == BinaryOps.LAND)|| (binOp == BinaryOps.LOR)))
		{
			binaryOp.setSymbolType(_boolean);
		}
		else if((e1.getSymbolType().isSubClass(e2.getSymbolType())) || (e2.getSymbolType().isSubClass(e1.getSymbolType()))
				&& ((binOp == BinaryOps.EQUAL)|| (binOp == BinaryOps.NEQUAL)))
		{
			binaryOp.setSymbolType(_boolean);
		}
		else
		{
			throw new SemanticError(binaryOp.getLine(), 
					String.format("Binary logical operation is not defined for the given arguments"));
		}
		
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		stepIn(unaryOp.getOperand());
		
		UnaryOps uOp = unaryOp.getOperator();
		Expression e = unaryOp.getOperand();
		
		SymbolType _int = TypeTable.intType;
		if((e.getSymbolType() == _int) && (uOp == UnaryOps.UMINUS))
		{
			unaryOp.setSymbolType(_int);
		}
		else
		{
			throw new SemanticError(unaryOp.getLine(), 
					String.format("unary operation is not defined for the given arguments"));
		}
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		stepIn(unaryOp.getOperand());
		
		UnaryOps uOp = unaryOp.getOperator();
		Expression e = unaryOp.getOperand();
		
		SymbolType _boolean = TypeTable.boolType;
		if((e.getSymbolType() == _boolean) && (uOp == UnaryOps.LNEG))
		{
			unaryOp.setSymbolType(_boolean);
		}
		else
		{
			throw new SemanticError(unaryOp.getLine(), 
					String.format("unary operation is not defined for the given arguments"));
		}
		return null;
	}

	@Override
	public Object visit(Literal literal) {
			
		SymbolType _int = TypeTable.intType;
		SymbolType _string = TypeTable.strType;
		SymbolType _boolean = TypeTable.boolType;
		SymbolType _null = TypeTable.nullType;
		
		LiteralTypes literalType = literal.getType();
		
		if(literalType == LiteralTypes.INTEGER)
		{
			literal.setSymbolType(_int);
		}
		else if(literalType == LiteralTypes.STRING)
		{
			literal.setSymbolType(_string);
		}
		else if((literalType == LiteralTypes.FALSE) || (literalType == LiteralTypes.TRUE)) 
		{
			literal.setSymbolType(_boolean);
		}
		else
		{
			literal.setSymbolType(_null);
		}
		
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		stepIn(expressionBlock.getExpression());
		expressionBlock.setSymbolType(expressionBlock.getExpression().getSymbolType());
		return null;
	}

}
