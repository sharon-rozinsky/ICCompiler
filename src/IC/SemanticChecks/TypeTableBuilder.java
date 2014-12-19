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
import IC.Types.ArrayType;
import IC.Types.MethodContent;
import IC.Types.SymbolType;
import IC.Types.TypeTable;
import IC.Types.VoidType;
 
public class TypeTableBuilder implements Visitor{
 

	public TypeTableBuilder(String fileName) {
        TypeTable.typeTableInit(fileName);
    }
    
    public void stepIn(ASTNode node) throws SemanticError {
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
    
    private SymbolType type(IC.AST.Type nodeType) throws SemanticError {
    	SymbolType type = SemanticUtils.convertNodeTypeToSymType(nodeType);
        if (type == null) {
        	//throw new SemanticError(0, "A reference to an undefined type - FIX"); //TODO: FIX - line number?!?!
        }      
        return type;
    }
    
    private SymbolType type(Method method) {
    	return SemanticUtils.convertNodeMethodToSymType(method);
    }
    
	@Override
	public Object visit(Program program) throws SemanticError {
		TypeTable.arrayType(TypeTable.strType);
		SymbolType[] sTarr = new SymbolType[1];
		sTarr[0] = (SymbolType) new ArrayType(TypeTable.strType, 6);
		MethodContent mC = new MethodContent("main", new VoidType(5), sTarr, true);
		TypeTable.methodType(mC);
		
		for (ICClass icClass : program.getClasses()) {
						
            String className = icClass.getName();
            String superClassName = icClass.getSuperClassName();
            
            if (!TypeTable.classTypeExists(className)) { // make sure there are no classes redefinition          	
                TypeTable.classType(className, superClassName, icClass);
                stepIn(icClass); 	// start of "recursive" run
            } 
            else 
            {
            	throw new SemanticError(icClass.getLine(), "Redifinition of a Class");
            }
        }    
        return true; 				//returns true at the end of the recursive run if successes
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		if(icClass.hasSuperClass()){
			boolean superClassExist = TypeTable.classTypeExists(icClass.getSuperClassName());
			if(!superClassExist){
				throw new SemanticError(icClass.getLine(), "Cannot extend a class that has not already been defined: " + icClass.getSuperClassName());
			}
		}
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
		type(method);
		stepIn(method.getType());
		stepIn(method.getFormals());
		stepIn(method.getStatements());
        
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		type(method);
		stepIn(method.getType());
		stepIn(method.getFormals());
		stepIn(method.getStatements());
        
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		type(method);
		stepIn(method.getType());
		stepIn(method.getFormals()); 
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) throws SemanticError {
		type(type);
		return null;
	}

	@Override
	public Object visit(UserType type) throws SemanticError {
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
		stepIn(localVariable.getInitValue());
		stepIn(localVariable.getType());
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
			//throw new SemanticError(call.getLine(), "A reference to an undefined Class type");
        } else {
            stepIn(call.getArguments());
        }
		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		stepIn(call.getArguments());
		stepIn(call.getLocation());
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		return null;
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		if (!TypeTable.classTypeExists(newClass.getName())) {
			//throw new SemanticError(newClass.getLine(), "An Instantiation of undefined Class type"); //TODO : is it right?!
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
