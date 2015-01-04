package IC.lir;

import java.util.LinkedHashMap;
import java.util.Map;

import IC.LiteralTypes;
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
import IC.AST.LIRVisitor;
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
import IC.AST.While;
import IC.lir.lirObject.LIRStringLiteral;
import IC.lir.operands.Label;

public class StringLiteralExtractor implements LIRVisitor{
	
    private int uniqueliteralId = 0;
    
    // (string literal) -> (string label)
    private Map<String, LIRStringLiteral> stringLiterals = new LinkedHashMap<String,LIRStringLiteral>();
    
    public Map<String, LIRStringLiteral> getStringLiterals() {
        return stringLiterals;
    }

	protected void stepIn(ASTNode node){
        if (node != null) {
            node.accept(this);
        }
    }
    
    protected void stepIn(Iterable iterable){
        if (iterable != null) {
            for (Object node : iterable) {
                stepIn((ASTNode)node);
            }
        }
    }

    @Override
	public Object visit(Literal literal)  {
 
		if (literal.getType() == LiteralTypes.STRING) {
			String literalValue = "\"" + (String)literal.getValue() + "\"";
            if (!stringLiterals.containsKey(literalValue)) {
                String labelName = LIRConstants.STRING_LITERAL_PREFIX + uniqueliteralId;
                Label label = new Label(labelName);
                LIRStringLiteral stringLiteral = new LIRStringLiteral(label, literalValue);
                uniqueliteralId++;
                stringLiterals.put(literalValue, stringLiteral);
            }
        }
		return null;
	}
    
	@Override
	public Object visit(Program program)  {
		stepIn(program.getClasses());
		return null;
	}

	@Override
	public Object visit(ICClass icClass)  {
		stepIn(icClass.getMethods());
		return null;
	}

	@Override
	public Object visit(Field field)  {
		return null;
	}

	@Override
	public Object visit(VirtualMethod method)  {
		stepIn(method.getStatements());
		return null;
	}

	@Override
	public Object visit(StaticMethod method)  {
		stepIn(method.getStatements());
		return null;
	}

	@Override
	public Object visit(LibraryMethod method)  {
		return null;
	}

	@Override
	public Object visit(Formal formal)  {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type)  {
		return null;
	}

	@Override
	public Object visit(UserType type)  {
		return null;
	}

	@Override
	public Object visit(Assignment assignment)  {
		stepIn(assignment.getAssignment());
		stepIn(assignment.getVariable());
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement)  {
		stepIn(callStatement.getCall());
		return null;
	}

	@Override
	public Object visit(Return returnStatement)  {
		stepIn(returnStatement.getValue());
		return null;
	}

	@Override
	public Object visit(If ifStatement)  {
		stepIn(ifStatement.getCondition());
		stepIn(ifStatement.getOperation());
		stepIn(ifStatement.getElseOperation());
		return null;
	}

	@Override
	public Object visit(While whileStatement)  {
		stepIn(whileStatement.getCondition());
		stepIn(whileStatement.getOperation());
		return null;
	}

	@Override
	public Object visit(Break breakStatement)  {
		return null;
	}

	@Override
	public Object visit(Continue continueStatement)  {
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock)  {
		stepIn(statementsBlock.getStatements());
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable)  {
		stepIn(localVariable.getInitValue());
		stepIn(localVariable.getType());
		return null;
	}

	@Override
	public Object visit(VariableLocation location)  {
		stepIn(location.getLocation());
		return null;
	}

	@Override
	public Object visit(ArrayLocation location)  {
		stepIn(location.getArray());
		stepIn(location.getIndex());
		return null;
	}

	@Override
	public Object visit(StaticCall call)  {
		stepIn(call.getArguments());
		return null;
	}

	@Override
	public Object visit(VirtualCall call)  {
		stepIn(call.getArguments());
		return null;
	}

	@Override
	public Object visit(This thisExpression)  {
		return null;
	}

	@Override
	public Object visit(NewClass newClass)  {
		return null;
	}

	@Override
	public Object visit(NewArray newArray)  {
		stepIn(newArray.getSize());
		stepIn(newArray.getType());
		return null;
	}

	@Override
	public Object visit(Length length)  {
		stepIn(length.getArray());
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp)  {
		stepIn(binaryOp.getFirstOperand());
		stepIn(binaryOp.getSecondOperand());
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp)  {
		stepIn(binaryOp.getFirstOperand());
        stepIn(binaryOp.getSecondOperand());
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp)  {
		stepIn(unaryOp.getOperand());
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp)  {
		stepIn(unaryOp.getOperand());
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock)  {
		stepIn(expressionBlock.getExpression());
		return null;
	}
}
