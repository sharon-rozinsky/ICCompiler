package IC.lir;

import java.util.List;

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
import IC.AST.LIRPropagatingVisitor;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.Location;
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
import IC.SemanticChecks.SemanticError;
import IC.Symbols.Symbol;
import IC.Types.ClassType;
import IC.Types.Kind;
import IC.Symbols.MethodSymbolTable;
import IC.Symbols.Symbol;
import IC.Types.ClassType;
import IC.Types.TypeTable;
import IC.lir.instructions.ArrayLengthInstruction;
import IC.lir.instructions.BinaryInstruction;
import IC.lir.instructions.BranchInstruction;
import IC.lir.instructions.Instruction;
import IC.lir.instructions.LibraryInstruction;
import IC.lir.instructions.MoveArrayInstruction;
import IC.lir.instructions.MoveFieldInstruction;
import IC.lir.instructions.MoveInstruction;
import IC.lir.instructions.NewArrayInstruction;
import IC.lir.instructions.NewObjectInstruction;
import IC.lir.instructions.PseudoInstruction;
import IC.lir.instructions.ReturnInstruction;
import IC.lir.instructions.StaticCallInstruction;
import IC.lir.instructions.UnaryInstruction;
import IC.lir.lirObject.LIRClass;
import IC.lir.lirObject.LIRMethod;
import IC.lir.lirObject.LIRProgram;
import IC.lir.operands.AddressLabel;
import IC.lir.operands.ArrayOperand;
import IC.lir.operands.DummyRegister;
import IC.lir.operands.FieldOperand;
import IC.lir.operands.Immediate;
import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Memory;
import IC.lir.operands.Operand;
import IC.lir.operands.ParameterOperand;
import IC.lir.operands.Register;
import IC.lir.operands.ThisReference;

public class LIRTranslator implements LIRPropagatingVisitor<Object, Object>{

	private LIRProgram program;
	
	public LIRTranslator(LIRProgram program){
		this.program = program;
		Register.setRegCount(0);
		AddressLabel.setLabelId(0);
	}
	
	public LIRProgram getProgram(){
		return program;
	}
	
	private Object propagate(ASTNode node, Object scope) {
		if(node != null){
			return node.accept(this, scope);
		}
		return null;
	}
	
	private void propagate(Iterable iterable, Object scope){
		if(iterable != null){
			for(Object node : iterable){
				propagate((ASTNode)node, scope);
			}
		}
	}
	
	@Override
	public Object visit(Program program, Object scope) {
		propagate(program.getClasses(), null);
		return null;
	}

	@Override
	public Object visit(ICClass icClass, Object scope)  {
		LIRClass lirClass = new LIRClass(new AddressLabel(icClass.getName()));
		propagate(icClass.getMethods(), lirClass);
		program.addClass(lirClass);
		return null;
	}

	@Override
	public Object visit(Field field, Object scope)  {
		return null;
	}

	private void visitMethod(Method method, Object scope) {
		String methodName = method.getName();
		LIRClass lirClass = (LIRClass) scope;
		LIRMethod lirMethod = new LIRMethod(lirClass.getClassName(), new AddressLabel(methodName));
		
		propagate(method.getStatements(), lirMethod);
		Instruction inst = null;
		if(method instanceof StaticMethod){
			if(methodName.equals(LIRConstants.MAIN_METHOD_NAME)){
				Operand[] params = new Operand[] { new Immediate(0) };
				LibraryLabel exitLabel = new LibraryLabel(LIRConstants.LIBRARY_EXIT);
				inst = new LibraryInstruction(exitLabel, new DummyRegister(), params);
			} else if(method.getSymbolType() == TypeTable.voidType){
				inst = new ReturnInstruction(new DummyRegister());
			}
			lirMethod.addInstruction(inst);
		} else if(method.getSymbolType() == TypeTable.voidType){
			inst = new ReturnInstruction(new DummyRegister());
			lirMethod.addInstruction(inst);
		}
		
		lirClass.addMethod(lirMethod);
	}
	
	@Override
	public Object visit(VirtualMethod method, Object scope) {
		visitMethod(method, scope);
		return null;
	}

	@Override
	public Object visit(StaticMethod method, Object scope)  {
		visitMethod(method, scope);
		return null;
	}

	@Override
	public Object visit(LibraryMethod method, Object scope) {
		return null;
	}

	@Override
	public Object visit(Formal formal, Object scope)  {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type, Object scope) {
		return null;
	}

	@Override
	public Object visit(UserType type, Object scope) {
		return null;
	}

	@Override
	public Object visit(Assignment assignment, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
		Location location = assignment.getVariable();
		
		propagate(assignment.getAssignment(), scope);
		Register.incRegisterCounter(1);
		
		Object obj = null;
		if(location instanceof VariableLocation) {
			obj = visitVariableLocation((VariableLocation)location, scope, LIRConstants.STORE);
		} else if(location instanceof ArrayLocation) {
			obj = visitArrayLocation((ArrayLocation)location, scope, LIRConstants.STORE);
		}
		Register.decRegisterCounter(1);
		
		Instruction instruction = getInstruction(obj);
		lirMethod.addInstruction(instruction);
		return null;
	}

	public Instruction getInstruction(Object operand) {
		Instruction instruction = null;
		if(operand instanceof Memory){
			Memory memory = (Memory) operand;
			instruction = new MoveInstruction(new Register(), memory);
		} else if(operand instanceof ArrayOperand) {
			ArrayOperand arrayOperand = (ArrayOperand) operand;
			instruction = new MoveArrayInstruction(arrayOperand, new Register(), LIRConstants.Store);
		} else if(operand instanceof FieldOperand){
			FieldOperand fieldOperand = (FieldOperand) operand;
			instruction = new MoveFieldInstruction(fieldOperand, new Register(), LIRConstants.Store);
		}
		return instruction;
	}

	private Object visitArrayLocation(ArrayLocation location, Object scope,
			int storeOrLoad) {
		LIRMethod lirMethod = (LIRMethod) scope;

		propagate(location.getArray(), scope);
		Register.incRegisterCounter(1);
		propagate(location.getIndex(), scope);
		Register.decRegisterCounter(1);

		ArrayOperand arrayOperand = new ArrayOperand(new Register(),
				new Register(1));
		if (storeOrLoad == LIRConstants.LOAD) {
			MoveArrayInstruction moveArrayInstruction = new MoveArrayInstruction(
					arrayOperand, new Register(), LIRConstants.Load);
			lirMethod.addInstruction(moveArrayInstruction);
		}
		return arrayOperand;
	}

	private Object visitVariableLocation(VariableLocation location,
			Object scope, int storeOrLoad) {
		LIRMethod lirMethod = (LIRMethod) scope;
		String locationName = location.getName();

		if (location.isExternal()) {
			propagate(location.getLocation(), scope);

			ClassType type = (ClassType) location.getLocation().getSymbolType();
			String className = type.getClassName();
			int offset = getOffset(className, locationName);

			Immediate immediate = new Immediate(offset);
			FieldOperand fieldOperand = new FieldOperand(new Register(),
					immediate);
			if (storeOrLoad == LIRConstants.LOAD) {
				MoveFieldInstruction moveFieldInstruction = new MoveFieldInstruction(
						fieldOperand, new Register(), LIRConstants.Load);
				lirMethod.addInstruction(moveFieldInstruction);
			}

			return fieldOperand;
		} else {
			Symbol symbol = location.getLocationScope().getSymbol(locationName);

			if (symbol.getKind() != Kind.MemberVariable) {
				Memory memory = new Memory(symbol);
				if (storeOrLoad == LIRConstants.LOAD) {
					MoveInstruction moveInstruction = new MoveInstruction(
							memory, new Register());
					lirMethod.addInstruction(moveInstruction);
				}
				return memory;
			} else {
				ThisReference thisReference = new ThisReference();
				MoveInstruction instruction = new MoveInstruction(
						thisReference, new Register());
				lirMethod.addInstruction(instruction);

				String className = location.getEnclosingScopeSymTable()
						.getParentSymbolTable().getId();
				int offset = getOffset(className, locationName);
				Immediate immediate = new Immediate(offset);
				FieldOperand fieldOperand = new FieldOperand(new Register(),
						immediate);

				if (storeOrLoad == LIRConstants.LOAD) {
					MoveFieldInstruction moveFieldInstruction = new MoveFieldInstruction(
							fieldOperand, new Register(), LIRConstants.Load);
					lirMethod.addInstruction(moveFieldInstruction);
				}
				return fieldOperand;
			}
		}
	}

	public int getOffset(String className, String locationName) {
		return program.getClassLayoutByName(className).getFieldsOffset(locationName);
	}

	@Override
	public Object visit(CallStatement callStatement, Object scope) {
		propagate(callStatement.getCall(), scope);
		return null;
	}

	@Override
	public Object visit(Return returnStatement, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        Register returnReg;
        
        if (returnStatement.hasValue()) 
        {
        	propagate(returnStatement.getValue(), scope);
            returnReg = new Register();
        } 
        else 
        {
            returnReg = new DummyRegister(); //TODO: is this correct?
        }

        ReturnInstruction returnInstruction = new ReturnInstruction(returnReg);
        lirMethod.getInstructions().add(returnInstruction);
        
        return null;
	}

	@Override
	public Object visit(If ifStatement, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
		AddressLabel falseLabel = null;
        
		int endScopeId = ifStatement.getOperation().getEnclosingScopeSymTable().getScopeUniqueId();		
        AddressLabel ifEndLabel = new AddressLabel(LIRConstants.END_LABEL_PREFIX + endScopeId);
        PseudoInstruction endIfLabelInstruction = new PseudoInstruction(ifEndLabel, LIRConstants.Label);
		
        if (ifStatement.hasElse()) 
        {
            int falseScopeId = ifStatement.getElseOperation().getEnclosingScopeSymTable().getScopeUniqueId();
            falseLabel = new AddressLabel(LIRConstants.FALSE_LABEL_PREFIX + falseScopeId);
        }

        propagate(ifStatement.getCondition(), scope);
        
        BinaryInstruction compare = new BinaryInstruction(LIRConstants.Compare, new Immediate(0), new Register());
        lirMethod.getInstructions().add(compare);
		
        BranchInstruction branch;
        if (ifStatement.hasElse()) 
        {
        	branch = new BranchInstruction(LIRConstants.True, falseLabel);
        } 
        else 
        {
        	branch = new BranchInstruction(LIRConstants.True, ifEndLabel);
        }
        lirMethod.getInstructions().add(branch);
		
        propagate(ifStatement.getOperation(), scope);
        
        if (ifStatement.hasElse()) 
        {
        	BranchInstruction branchElse = new BranchInstruction(LIRConstants.Do, ifEndLabel);
            lirMethod.getInstructions().add(branchElse);
            
            PseudoInstruction falseLabelInstruction = new PseudoInstruction(falseLabel, LIRConstants.Label);
            lirMethod.getInstructions().add(falseLabelInstruction);
            
            propagate(ifStatement.getElseOperation(), scope);
        }
        
        lirMethod.getInstructions().add(endIfLabelInstruction);
		
		return null;
	}

	@Override
	public Object visit(While whileStatement, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        int scopeId = whileStatement.getOperation().getEnclosingScopeSymTable().getScopeUniqueId();
        lirMethod.getWhileLoopsStack().push(scopeId);
        
        AddressLabel whileStartLabel = new AddressLabel(LIRConstants.TEST_COND_LABEL_PREFIX + scopeId);
        AddressLabel whileEndLabel = new AddressLabel(LIRConstants.END_LABEL_PREFIX + scopeId);      
        PseudoInstruction whileStartPInstruction = new PseudoInstruction(whileStartLabel, LIRConstants.Label);
        PseudoInstruction whileEndPInstruction = new PseudoInstruction(whileEndLabel, LIRConstants.Label);
        
        lirMethod.getInstructions().add(whileStartPInstruction);
        
        propagate(whileStatement.getCondition(), scope);
        
        BinaryInstruction compare = new BinaryInstruction(LIRConstants.Compare, new Immediate(0), new Register());
        lirMethod.getInstructions().add(compare);
        
        BranchInstruction whileEndBranchInstruction =  new BranchInstruction(LIRConstants.True, whileEndLabel);
        lirMethod.getInstructions().add(whileEndBranchInstruction);
        
        propagate(whileStatement.getOperation(), scope);
        // always jumps back to while start, then check..
        BranchInstruction whileStartBranchInstruction = new BranchInstruction(LIRConstants.Do, whileStartLabel);
        lirMethod.getInstructions().add(whileStartBranchInstruction);
              
        lirMethod.getInstructions().add(whileEndPInstruction);
        
        lirMethod.getWhileLoopsStack().pop();
        
        return null;
	}

	@Override
	public Object visit(Break breakStatement, Object scope){ //TODO: break can be in If statement also, no?
		LIRMethod lirMethod = (LIRMethod) scope;
        
        int scope_uniqueId = lirMethod.getWhileLoopsStack().peek();
        
        AddressLabel whileEndlabel = new AddressLabel(LIRConstants.END_LABEL_PREFIX + scope_uniqueId);       
        BranchInstruction breakBranchIntruction = new BranchInstruction(LIRConstants.Do, whileEndlabel);
        lirMethod.getInstructions().add(breakBranchIntruction);
        
        return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        int scope_uniqueId = lirMethod.getWhileLoopsStack().peek();
        
        AddressLabel whileEndlabel = new AddressLabel(LIRConstants.TEST_COND_LABEL_PREFIX + scope_uniqueId);
        
        BranchInstruction breakBranchIntruction = new BranchInstruction(LIRConstants.Do, whileEndlabel);
        lirMethod.getInstructions().add(breakBranchIntruction);
        return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock, Object scope) {
		propagate(statementsBlock.getStatements(), scope);
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        if (localVariable.hasInitValue()) {
            propagate(localVariable.getInitValue(), scope);
            Symbol symb = localVariable.getEnclosingScopeSymTable().getSymbol(localVariable.getName());
            Memory mem = new Memory(symb);
            MoveInstruction moveIntruction = new MoveInstruction(new Register(), mem);
            lirMethod.getInstructions().add(moveIntruction);
        }
        
        return null;
	}

	@Override
	public Object visit(VariableLocation location, Object scope) {
		// TODO: Sharon check the Call - I think it should load?
		return visitVariableLocation(location, scope, 2);
	}

	@Override
	public Object visit(ArrayLocation location, Object scope){
		// TODO: Sharon check the Call - I think it should load here?
		return visitArrayLocation(location, scope, 2);
	}

	@Override
	public Object visit(StaticCall call, Object scope)  { // TODO : TOM !!
		LIRMethod lirMethod = (LIRMethod) scope;
        
        if (call.getClassName().equals(LIRConstants.LIBRARY)) {
            
            String methodName = call.getName();
            LibraryLabel libLabel = new LibraryLabel(methodName);
            
            Operand[] lirArgs = new Operand[call.getArguments().size()];
            
            for (int i=0; i<call.getArguments().size(); i++) {
                Register.incRegisterCounter(1);
                propagate(call.getArguments().get(i), scope);
                lirArgs[i] = new Register();
            }
                
            Register.decRegisterCounter(call.getArguments().size());
            
            LibraryInstruction libraryInstruction = new LibraryInstruction(libLabel, new Register(), lirArgs);
            
            lirMethod.getInstructions().add(libraryInstruction);
        } 
        else 
        {        
            String methodName = call.getName();
            String className = call.getClassName();
            
            AddressLabel label;
            if (methodName.equals(LIRConstants.MAIN_METHOD_NAME)) {
                label = new AddressLabel(LIRConstants.MAIN_LABEL_PREFIX, methodName);
            } else {
                label = new AddressLabel(className, methodName);
            }
            
            ClassType classType = TypeTable.classType(className, null, null);
            ICClass icClass = classType.getClassNode();
            MethodSymbolTable table = (MethodSymbolTable)icClass.getEnclosingScopeSymTable().getChildSymbolTables().get(methodName);
            
            List<Symbol> parameters = table.getParameters()
            ParameterOperand[] pairs = new ParameterOperand[parameters.size()];
            
            for (int i=0; i<parameters.size(); i++) {
                Register.incRegisterCounter(1);
                Memory mem = new Memory(parameters.get(i));
                propagate(call.getArguments().get(i), scope);
                pairs[i] = new ParameterOperand(mem, new Register());
            }
            Register.decRegisterCounter(call.getArguments().size());

            StaticCallInstruction inst = new StaticCallInstruction(label, new Register(), pairs);
            lirMethod.getInstructions().add(inst);
            
        }
        return null;
	}

	@Override
	public Object visit(VirtualCall call, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression, Object scope) {		
		Memory thisMemory = new ThisReference();
        MoveInstruction moveInstruction = new MoveInstruction(thisMemory, new Register());
        ((LIRMethod)scope).getInstructions().add(moveInstruction);

		return null;
	}

	@Override
	public Object visit(NewClass newClass, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
        String className = newClass.getName();
        int size = program.getClassesLayout().get(className).getClassLayoutSize();
        Immediate imm = new Immediate(size);
        
        NewObjectInstruction newObjInstruction = new NewObjectInstruction(new Register(), imm);
        lirMethod.getInstructions().add(newObjInstruction);
        AddressLabel dispatchVec = program.getClassesLayout().get(className).getDispatchTableLabel();
        
        FieldOperand feildOp = new FieldOperand(new Register(), new Immediate(0));
        
        MoveFieldInstruction moveDVInstruction = new MoveFieldInstruction(feildOp, dispatchVec, LIRConstants.Store);
        lirMethod.getInstructions().add(moveDVInstruction);
        
        return null;
	}

	@Override
	public Object visit(NewArray newArray, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
        propagate(newArray.getSize(), scope);
        Immediate byteSize = new Immediate(4);
        
        BinaryInstruction updateSizeInstruction = new BinaryInstruction(LIRConstants.Mul, byteSize, new Register());
        lirMethod.getInstructions().add(updateSizeInstruction);
        
        NewArrayInstruction newArrInstruction = new NewArrayInstruction(new Register(), new Register());
        lirMethod.getInstructions().add(newArrInstruction);
        
        return null;
	}

	@Override
	public Object visit(Length length, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        
		propagate(length.getArray(), scope);
        
        ArrayLengthInstruction arrLenInstruction = new ArrayLengthInstruction(new Register(), new Register());
        lirMethod.getInstructions().add(arrLenInstruction);
        
        return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp, Object scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp, Object scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        if (unaryOp.getOperator() == IC.UnaryOps.UMINUS) 
        {            
            propagate(unaryOp.getOperand(), scope);
            UnaryInstruction unaryInstruction = new UnaryInstruction(LIRConstants.Neg, new Register());
            lirMethod.getInstructions().add(unaryInstruction);            
        }
        return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;

		if (unaryOp.getOperator() == IC.UnaryOps.LNEG) 
		{            
			propagate(unaryOp.getOperand(), scope);
			BinaryInstruction binaryLnegInstruction = new BinaryInstruction(LIRConstants.Xor, new Immediate(1), new Register());
			lirMethod.getInstructions().add(binaryLnegInstruction);            
		}
		return null;
	}

	@Override
	public Object visit(Literal literal, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock, Object scope) {
		propagate (expressionBlock.getExpression(),scope);
		return null;
	}

}
