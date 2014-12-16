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
import IC.Types.SymbolType;
import IC.Types.TypeTable;

public class TypeTableBuilder implements Visitor{
	
	private boolean state = true;
    
	
    public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public TypeTableBuilder(String fileName) {
        TypeTable.typeTableInit(fileName);
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
    
    private SymbolType type(IC.AST.Type nodeType) {
    	SymbolType type = SemanticUtils.convertNodeTypeToSymType(nodeType);
        if (type == null) {
           state = false;
        }      
        return type;
    }
    
    private SymbolType type(Method method) {
        if (state) {
            return SemanticUtils.convertNodeMethodToSymType(method);
        } 
        else 
        {
            return null;
        }
    }
    
	@Override
	public Object visit(Program program) throws SemanticError {
		for (ICClass icClass : program.getClasses()) {
            
            String className = icClass.getName();
            String superClassName = icClass.getSuperClassName();
            
            if (!TypeTable.classTypeExists(className)) { // make sure there are no classes redefinition
                TypeTable.classType(className, superClassName, icClass);
            } 
            else 
            {
            	state = false;
                return null;
            }
        }      
        stepIn(program.getClasses()); 	// start of "recursive" run
        return isState(); 				//returns true at the end of the recursive run if successes
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
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		stepIn(method.getType());
		stepIn(method.getFormals());
		stepIn(method.getStatements());
        type(method);
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		stepIn(method.getType());
		stepIn(method.getFormals());
		stepIn(method.getStatements());
        type(method);
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		stepIn(method.getType());
		stepIn(method.getFormals());
        type(method);
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) {
		type(type);
		return null;
	}

	@Override
	public Object visit(UserType type) {
		type(type);
		return null;
	}
	
	@Override
	public Object visit(Formal formal) throws SemanticError {
		stepIn(formal.getType());
		return null;
	}

	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		stepIn(assignment.getAssignment());
		stepIn(assignment.getVariable());
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
		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		stepIn(ifStatement.getCondition());
		stepIn(ifStatement.getOperation());
		stepIn(ifStatement.getElseOperation());
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		stepIn(whileStatement.getCondition());
		stepIn(whileStatement.getOperation());
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
		stepIn(localVariable.getType());
		stepIn(localVariable.getInitValue());
		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		stepIn(location.getLocation());
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		stepIn(location.getArray());
		stepIn(location.getIndex());
		return null;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		if (!TypeTable.classTypeExists(call.getClassName())) {
            state = false;
        } else {
            stepIn(call.getArguments());
        }
		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		stepIn(call.getLocation());
		stepIn(call.getArguments());
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		if (!TypeTable.classTypeExists(newClass.getName())) {
            state = false;
        }
		return null;
	}

	@Override
	public Object visit(NewArray arr) throws SemanticError {
		stepIn(arr.getType());
		stepIn(arr.getSize());
		return null;
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		stepIn(length.getArray());
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		stepIn(binaryOp.getFirstOperand());
		stepIn(binaryOp.getSecondOperand());
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		stepIn(binaryOp.getFirstOperand());
		stepIn(binaryOp.getSecondOperand());
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		stepIn(unaryOp.getOperand());
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		stepIn(unaryOp.getOperand());
		return null;
	}

	@Override
	public Object visit(Literal literal) {
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		stepIn(expressionBlock.getExpression());		
		return null;
	}

}
