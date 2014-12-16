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
import IC.Symbols.MethodSymbolTable;
import IC.Types.SymbolType;

public class TypesCheck implements Visitor{

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
		stepIn(field);
		ClassSymbolTable classSymbolTable = (ClassSymbolTable)  field.getEnclosingScopeSymTable();
		field.setSymbolType(classSymbolTable.getMemberVariables().get(field.getName()).getType());
		return null;
	}
	
	public void visitMethodWraper(Method method) throws SemanticError {
		stepIn(method.getType());
		stepIn(method.getStatements());
		stepIn(method.getFormals());
		
		method.setSymbolType(((ClassSymbolTable) method.getEnclosingScopeSymTable()).getMethods().get(method.getName()).getType());
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
			throw new SemanticError(assignment.getLine(),String.format("Can't assign variable from type %S to type %S", t2.toString(),t1.toString()));
		}
		
		return null;
	}


	@Override
	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VariableLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewArray newArray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return null;
	}

}
