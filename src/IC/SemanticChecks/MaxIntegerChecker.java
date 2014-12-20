package IC.SemanticChecks;

import IC.LiteralTypes;
import IC.UnaryOps;
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

public class MaxIntegerChecker implements PropagatingVisitor<ASTNode, Boolean> {

	protected boolean propagate(ASTNode node, ASTNode parentNode) throws SemanticError {
        if(node == null){
        	return true;
        }
        return node.accept(this,parentNode);
    }
    
    protected boolean propagate(Iterable iterable, ASTNode parentNode) throws SemanticError {
        if (iterable != null)
            for (Object node : iterable) {
                if (!propagate((ASTNode)node, parentNode)) {
                    return false;
                }
            }
        
        return true;
    }  
	
	public Boolean visit(Literal literal, ASTNode context) throws SemanticError {
        if ( (literal.getType() == LiteralTypes.INTEGER) && (literal.getValue().equals("2147483648")))
        	if (!((context instanceof MathUnaryOp) && ((MathUnaryOp)context).getOperator() == UnaryOps.UMINUS)) {
        		throw new SemanticError(literal.getLine(), "Integer out of bounds");
        }

        return true;
    }
    
    public Boolean visit(Program program, ASTNode context) throws SemanticError {
        propagate(program.getClasses(), program);
        return true;
    }

    public Boolean visit(ICClass icClass, ASTNode context) throws SemanticError {
        propagate(icClass.getMethods(), icClass);
        return true;
    }

    public Boolean visit(Field field, ASTNode context) {
        return true;
    }

    public Boolean visit(VirtualMethod method, ASTNode context) throws SemanticError {
        propagate(method.getStatements(), method);
        return true;
    }

    public Boolean visit(StaticMethod method, ASTNode context) throws SemanticError {
        propagate(method.getStatements(), method);
        return true;
    }

    public Boolean visit(LibraryMethod method, ASTNode context) {
        return true;
    }

    public Boolean visit(Formal formal, ASTNode context) {
        return true;
    }

    public Boolean visit(PrimitiveType type, ASTNode context) {
        return true;
    }

    public Boolean visit(UserType type, ASTNode context) {
        return true;
    }

    public Boolean visit(Assignment assignment, ASTNode context) throws SemanticError {
        propagate(assignment.getVariable(), assignment);
        propagate(assignment.getAssignment(), assignment);
        return true;
    }

    public Boolean visit(CallStatement callStatement, ASTNode context) throws SemanticError {
        propagate(callStatement.getCall(), callStatement);
        return true;
    }

    public Boolean visit(Return returnStatement, ASTNode context) throws SemanticError {
        propagate(returnStatement.getValue(), returnStatement);
        return true;
    }

    public Boolean visit(If ifStatement, ASTNode context) throws SemanticError {
        propagate(ifStatement.getCondition(), ifStatement);
        propagate(ifStatement.getOperation(), ifStatement);
        propagate(ifStatement.getElseOperation(), ifStatement);
        return true;
    }

    public Boolean visit(While whileStatement, ASTNode context) throws SemanticError {
        propagate(whileStatement.getCondition(), whileStatement);
        propagate(whileStatement.getOperation(), whileStatement);
        return true;
    }

    public Boolean visit(Break breakStatement, ASTNode context) {
        return true;
    }

    public Boolean visit(Continue continueStatement, ASTNode context) {
        return true;
    }

    public Boolean visit(StatementsBlock statementsBlock, ASTNode context) throws SemanticError {
        propagate(statementsBlock.getStatements(), statementsBlock);
        return true;
    }

    public Boolean visit(LocalVariable localVariable, ASTNode context) throws SemanticError {
        propagate(localVariable.getInitValue(), localVariable);
        return true;
    }

    public Boolean visit(VariableLocation location, ASTNode context) throws SemanticError {
        propagate(location.getLocation(), location);
        return true;
    }

    public Boolean visit(ArrayLocation location, ASTNode context) throws SemanticError {
        propagate(location.getArray(), location);
        propagate(location.getIndex(), location);
        return true;
    }

    public Boolean visit(StaticCall call, ASTNode context) throws SemanticError {
        propagate(call.getArguments(), call);
        return true;
    }

    public Boolean visit(VirtualCall call, ASTNode context) throws SemanticError {
        propagate(call.getArguments(), call);
        return true;
    }

    public Boolean visit(This thisExpression, ASTNode context) {
        return true;
    }

    public Boolean visit(NewClass newClass, ASTNode context) {
        return true;
    }

    public Boolean visit(NewArray newArray, ASTNode context) throws SemanticError {
        propagate(newArray.getSize(), newArray);
        return true;
    }

    public Boolean visit(Length length, ASTNode context) throws SemanticError {
        propagate(length.getArray(), length);
        return true;
    }

    public Boolean visit(MathBinaryOp binaryOp, ASTNode context) throws SemanticError {
        propagate(binaryOp.getFirstOperand(), binaryOp);
        propagate(binaryOp.getSecondOperand(), binaryOp);
        return true;
    }

    public Boolean visit(LogicalBinaryOp binaryOp, ASTNode context) throws SemanticError {
        propagate(binaryOp.getFirstOperand(), binaryOp);
        propagate(binaryOp.getSecondOperand(), binaryOp);
        return true;
    }

    public Boolean visit(MathUnaryOp unaryOp, ASTNode context) throws SemanticError {
        propagate(unaryOp.getOperand(), unaryOp);
        return true;
    }

    public Boolean visit(LogicalUnaryOp unaryOp, ASTNode context) throws SemanticError {
        propagate(unaryOp.getOperand(), unaryOp);
        return true;
    }

    public Boolean visit(ExpressionBlock expressionBlock, ASTNode context) throws SemanticError {
        propagate(expressionBlock.getExpression(), expressionBlock);
        return true;
    }
}
