package IC.Symbols;

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
import IC.AST.Return;
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
import IC.SemanticChecks.SemanticError;


/**
 *  
 *  This visitor prints the symbol tables of a given Program
 *
 */
public class SymbolTablePrinter implements Visitor {

	    StringBuilder sb = new StringBuilder();
	    
	    private void printTable(ASTNode node) {
	        if (node != null) {
	            sb.append(node.getEnclosingScopeSymTable().toString() + "\n\n");
	        }
	    }
	    
	    private void stepIn(ASTNode node) throws SemanticError {
	        if (node != null) {
	            node.accept(this);
	        }
	    }
	    
	    @SuppressWarnings("rawtypes")
	    private void stepIn(Iterable iterable) throws SemanticError {
	        if (iterable != null) {
	            for (Object node : iterable) {
	                stepIn((ASTNode)node);
	            }
	        }
	    }
	    
	    public Object visit(Program program) throws SemanticError {
	        printTable(program);
	        stepIn(program.getClasses());
	        return sb.toString();
	    }

	    public Object visit(ICClass icClass) throws SemanticError {
	        printTable(icClass);
	        stepIn(icClass.getMethods());
	        return null;
	    }

	    public Object visit(VirtualMethod method) throws SemanticError {
	        printTable(method);
	        stepIn(method.getStatements());
	        return null;
	    }

	    public Object visit(StaticMethod method) throws SemanticError {
	        printTable(method);
	        stepIn(method.getStatements());
	        return null;
	    }
	    
	    public Object visit(LibraryMethod method) {
	        printTable(method);
	        return null;
	    }

	    public Object visit(If ifStatement) throws SemanticError {
	        stepIn(ifStatement.getOperation());
	        stepIn(ifStatement.getElseOperation());
	        return null;
	    }

	    public Object visit(While whileStatement) throws SemanticError {
	        stepIn(whileStatement.getOperation());
	        return null;
	    }

	    public Object visit(StatementsBlock statementsBlock) throws SemanticError {
	        printTable(statementsBlock);
	        stepIn(statementsBlock.getStatements());
	        return null;
	    }
	    
	    public Object visit(Field field) { return null; }
	    public Object visit(Formal formal) { return null; }
	    public Object visit(PrimitiveType type) { return null; }
	    public Object visit(UserType type) { return null; }
	    public Object visit(Assignment assignment) { return null; }
	    public Object visit(CallStatement callStatement) { return null; }
	    public Object visit(Return returnStatement) { return null; }
	    public Object visit(Break breakStatement) { return null; }
	    public Object visit(Continue continueStatement) { return null; }
	    public Object visit(LocalVariable localVariable) { return null; }
	    public Object visit(VariableLocation location) { return null; }
	    public Object visit(ArrayLocation location) { return null; }
	    public Object visit(StaticCall call) { return null; }
	    public Object visit(VirtualCall call) { return null; }
	    public Object visit(This thisExpression) { return null; }
	    public Object visit(NewClass newClass) { return null; }
	    public Object visit(NewArray newArray) { return null; }
	    public Object visit(Length length) { return null; }
	    public Object visit(MathBinaryOp binaryOp) { return null; }
	    public Object visit(LogicalBinaryOp binaryOp) { return null; }
	    public Object visit(MathUnaryOp unaryOp) { return null; }
	    public Object visit(LogicalUnaryOp unaryOp) { return null; }
	    public Object visit(Literal literal) { return null; }
	    public Object visit(ExpressionBlock expressionBlock) { return null; }

}
