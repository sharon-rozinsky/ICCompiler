package IC.lir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import IC.BinaryOps;
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
import IC.Types.MethodType;
import IC.Types.Kind;
import IC.Types.SymbolType;
import IC.Symbols.ClassSymbolTable;
import IC.Symbols.CodeBlockSymbolTable;
import IC.Symbols.MethodSymbolTable;
import IC.Symbols.Symbol;
import IC.Symbols.SymbolTable;
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
import IC.lir.instructions.StringCatInstruction;
import IC.lir.instructions.UnaryInstruction;
import IC.lir.instructions.VirtualCallInstruction;
import IC.lir.instructions.ZeroDivCheckInstruction;
import IC.lir.lirObject.LIRClass;
import IC.lir.lirObject.LIRMethod;
import IC.lir.lirObject.LIRProgram;
import IC.lir.operands.AddressLabel;
import IC.lir.operands.ArrayOperand;
import IC.lir.operands.DummyRegister;
import IC.lir.operands.FieldOperand;
import IC.lir.operands.Immediate;
import IC.lir.operands.Label;
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
		LIRMethod lirMethod = new LIRMethod(new AddressLabel(methodName), lirClass.getClassName());
		
		propagate(method.getStatements(), lirMethod);
		Instruction inst = null;
		SymbolType methodReturnType = getMethodReturnType(method);
		
		if(method instanceof StaticMethod){
			if(methodName.equals(LIRConstants.MAIN_METHOD_NAME)){
				Operand[] params = new Operand[] { new Immediate(0) };
				LibraryLabel exitLabel = new LibraryLabel(LIRConstants.LIBRARY_EXIT);
				inst = new LibraryInstruction(exitLabel, new DummyRegister(), params);
			} else if(methodReturnType != null && methodReturnType.getClass().equals(TypeTable.voidType.getClass())){
				inst = new ReturnInstruction(new DummyRegister());
			}
			if(inst != null){
				lirMethod.addInstruction(inst);
			}
		} else if(methodReturnType != null && methodReturnType.getClass().equals(TypeTable.voidType.getClass())){
			inst = new ReturnInstruction(new DummyRegister());
			lirMethod.addInstruction(inst);
		}
		
		lirClass.addMethod(lirMethod);
	}
	
	private SymbolType getMethodReturnType(Method method) {
		SymbolType methodType = method.getSymbolType();
		SymbolType returnType = ((MethodType)methodType).getReturnType();
		return returnType;
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
				Memory thisReference = new ThisReference();
				MoveInstruction instruction = new MoveInstruction(
						thisReference, new Register());
				lirMethod.addInstruction(instruction);

				String className = getClassSymbolTableByNode(location).getId();
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
        lirMethod.addInstruction(returnInstruction);
        
        return null;
	}

	@Override
	public Object visit(If ifStatement, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
		AddressLabel falseLabel = null;
		int endScopeId;
		
		ASTNode br = ifStatement.getOperation();
		if (br instanceof Break)
		{
			Break brk = (Break) br;
			endScopeId = brk.getUniqueId();
		}
		else
		{
			endScopeId = ifStatement.getOperation().getEnclosingScopeSymTable().getScopeUniqueId();	
		}

        	
        AddressLabel ifEndLabel = new AddressLabel(LIRConstants.END_LABEL_PREFIX + endScopeId);
        PseudoInstruction endIfLabelInstruction = new PseudoInstruction(ifEndLabel, LIRConstants.Label);
		
        if (ifStatement.hasElse()) {
            int falseScopeId = ifStatement.getElseOperation().getEnclosingScopeSymTable().getScopeUniqueId();
            falseLabel = new AddressLabel(LIRConstants.FALSE_LABEL_PREFIX + falseScopeId);
        }

        propagate(ifStatement.getCondition(), scope);
        
        BinaryInstruction compare = new BinaryInstruction(LIRConstants.Compare, new Immediate(0), new Register());
        lirMethod.addInstruction(compare);
		
        BranchInstruction branch;
        if (ifStatement.hasElse()) {
        	branch = new BranchInstruction(LIRConstants.True, falseLabel);
        } 
        else {
        	branch = new BranchInstruction(LIRConstants.True, ifEndLabel);
        }       
        lirMethod.addInstruction(branch);
		
        propagate(ifStatement.getOperation(), scope);
        
        if (ifStatement.hasElse()) {
        	BranchInstruction branchElse = new BranchInstruction(LIRConstants.Do, ifEndLabel);
            lirMethod.addInstruction(branchElse);
            
            PseudoInstruction falseLabelInstruction = new PseudoInstruction(falseLabel, LIRConstants.Label);
            lirMethod.addInstruction(falseLabelInstruction);
            
            propagate(ifStatement.getElseOperation(), scope);
        }
        
        lirMethod.addInstruction(endIfLabelInstruction);
		
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
        
        lirMethod.addInstruction(whileStartPInstruction);
        
        propagate(whileStatement.getCondition(), scope);
        
        BinaryInstruction compare = new BinaryInstruction(LIRConstants.Compare, new Immediate(0), new Register());
        lirMethod.addInstruction(compare);
        
        BranchInstruction whileEndBranchInstruction =  new BranchInstruction(LIRConstants.True, whileEndLabel);
        lirMethod.addInstruction(whileEndBranchInstruction);
        
        propagate(whileStatement.getOperation(), scope);
        // always jumps back to while start, then check..
        BranchInstruction whileStartBranchInstruction = new BranchInstruction(LIRConstants.Do, whileStartLabel);
        lirMethod.addInstruction(whileStartBranchInstruction);
              
        lirMethod.addInstruction(whileEndPInstruction);
        
        lirMethod.getWhileLoopsStack().pop();
        
        return null;
	}

	@Override
	public Object visit(Break breakStatement, Object scope){ //TODO: break can be in If statement also, no?
		LIRMethod lirMethod = (LIRMethod) scope;
        
        int scope_uniqueId = lirMethod.getWhileLoopsStack().peek();
        
        AddressLabel whileEndlabel = new AddressLabel(LIRConstants.END_LABEL_PREFIX + scope_uniqueId);       
        BranchInstruction breakBranchIntruction = new BranchInstruction(LIRConstants.Do, whileEndlabel);
        lirMethod.addInstruction(breakBranchIntruction);
        
        return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        int scope_uniqueId = lirMethod.getWhileLoopsStack().peek();
        
        AddressLabel whileEndlabel = new AddressLabel(LIRConstants.TEST_COND_LABEL_PREFIX + scope_uniqueId);
        
        BranchInstruction breakBranchIntruction = new BranchInstruction(LIRConstants.Do, whileEndlabel);
        lirMethod.addInstruction(breakBranchIntruction);
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
            lirMethod.addInstruction(moveIntruction);
        }
        
        return null;
	}

	@Override
	public Object visit(VariableLocation location, Object scope) {
		return visitVariableLocation(location, scope, LIRConstants.LOAD);
	}

	@Override
	public Object visit(ArrayLocation location, Object scope){
		return visitArrayLocation(location, scope, LIRConstants.LOAD);
	}

	@Override
	public Object visit(StaticCall call, Object scope)  { 
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
            
            lirMethod.addInstruction(libraryInstruction);
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
            
            Collection<Symbol> parameters = (Collection<Symbol>) table.getParameters().values();
            ParameterOperand[] paramOp = new ParameterOperand[parameters.size()];
            
            int i = 0;
            
            for(Symbol symb: parameters){
            	Register.incRegisterCounter(1);
            	Memory mem = new Memory(symb);
            	propagate(call.getArguments().get(i), scope);
                paramOp[i] = new ParameterOperand(mem, new Register());
                i++;
            }

            Register.decRegisterCounter(call.getArguments().size());

            StaticCallInstruction statCallinstruction = new StaticCallInstruction(label, new Register(), paramOp);
            lirMethod.addInstruction(statCallinstruction);
        }
        return null;
	}

	@Override
	public Object visit(VirtualCall call, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        String methodName = call.getName();
        String className;
        ClassType classType;
        
        if (call.isExternal()) {
            propagate(call.getLocation(), scope);
            classType = (ClassType) call.getLocation().getSymbolType();
            className = classType.getClassName();
        } else {
        	Memory thisMemory = new ThisReference();
            MoveInstruction moveInstruction = new MoveInstruction(thisMemory, new Register());
            lirMethod.addInstruction(moveInstruction);
            // TODO: get class scope - guy implemented somewhere- so I prefer not to add the function...
            className = getClassSymbolTableByNode(call).getId();
            classType = TypeTable.classType(className, null, null);
        }
        
        ICClass icClass = classType.getClassNode();
        MethodSymbolTable table = (MethodSymbolTable) getMethodSymbolTable(icClass, methodName);
        if(table == null)
        	return null;
        
        List<Symbol> parameters = new ArrayList<Symbol>(table.getParameters().values());
        ParameterOperand[] paramOp = new ParameterOperand[parameters.size()];
        
        for (int i=0; i<parameters.size(); i++) {
        	Register.incRegisterCounter(1);
            Memory mem = new Memory(parameters.get(i));
            propagate(call.getArguments().get(i), scope);
            paramOp[i] = new ParameterOperand(mem, new Register());
        }
        Register.decRegisterCounter(call.getArguments().size());

        int intOffset = program.getClassesLayout().get(className).getMethodsOffset(methodName);
        Immediate offset = new Immediate(intOffset);
        VirtualCallInstruction virtCallinstruction = new VirtualCallInstruction(new Register(), offset, new Register(), paramOp);
        lirMethod.addInstruction(virtCallinstruction);
        
        return null;
	}

	@Override
	public Object visit(This thisExpression, Object scope) {		
		Memory thisMemory = new ThisReference();
        MoveInstruction moveInstruction = new MoveInstruction(thisMemory, new Register());
        ((LIRMethod)scope).addInstruction(moveInstruction);

		return null;
	}

	@Override
	public Object visit(NewClass newClass, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
        String className = newClass.getName();
        int size = program.getClassesLayout().get(className).getClassLayoutSize();
        Immediate imm = new Immediate(size);
        
        NewObjectInstruction newObjInstruction = new NewObjectInstruction(new Register(), imm);
        lirMethod.addInstruction(newObjInstruction);
        AddressLabel dispatchVec = program.getClassesLayout().get(className).getDispatchTableLabel();
        
        FieldOperand feildOp = new FieldOperand(new Register(), new Immediate(0));
        
        MoveFieldInstruction moveDVInstruction = new MoveFieldInstruction(feildOp, dispatchVec, LIRConstants.Store);
        lirMethod.addInstruction(moveDVInstruction);
        
        return null;
	}

	@Override
	public Object visit(NewArray newArray, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
		
        propagate(newArray.getSize(), scope);
        Immediate byteSize = new Immediate(4);
        
        BinaryInstruction updateSizeInstruction = new BinaryInstruction(LIRConstants.Mul, byteSize, new Register());
        lirMethod.addInstruction(updateSizeInstruction);
        
        NewArrayInstruction newArrInstruction = new NewArrayInstruction(new Register(), new Register());
        lirMethod.addInstruction(newArrInstruction);
        
        return null;
	}

	@Override
	public Object visit(Length length, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        
		propagate(length.getArray(), scope);
        
        ArrayLengthInstruction arrLenInstruction = new ArrayLengthInstruction(new Register(), new Register());
        lirMethod.addInstruction(arrLenInstruction);
        
        return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;

		propagate(binaryOp.getFirstOperand(), scope);
		Register.incRegisterCounter(1);
		propagate(binaryOp.getSecondOperand(), scope);
		Register.decRegisterCounter(1);

		// If binOp is of type str1 + str2 
		if (binaryOp.getFirstOperand().getSymbolType().equals(TypeTable.strType)) {
			StringCatInstruction strCatInstruction = new StringCatInstruction(new Register(), new Register(), new Register(1));
			lirMethod.addInstruction(strCatInstruction);

			return null;
		}

		Register R0 = new Register();
		Register R1 = new Register(1);
		String binOp = getBinaryOperationString(binaryOp);
		if(binOp == LIRConstants.Div){
			ZeroDivCheckInstruction zeroDivCheckInstruction = new ZeroDivCheckInstruction(R1.toString());
			lirMethod.addInstruction(zeroDivCheckInstruction);
		}
		BinaryInstruction binInstruction = new BinaryInstruction(binOp, R1, R0);
		lirMethod.addInstruction(binInstruction);

		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp, Object scope) {
		LIRMethod lirMethod = (LIRMethod) scope;
        AddressLabel endLabel = new AddressLabel(LIRConstants.END_BOOL_LABEL_PREFIX + AddressLabel.getLabelId());
        AddressLabel.incLabelId(1);
        
        propagate(binaryOp.getFirstOperand(), scope);

        if (binaryOp.getOperator() == BinaryOps.LAND || 
            binaryOp.getOperator() == BinaryOps.LOR) {
            
            Immediate imm;
            String op;
            
            if (binaryOp.getOperator() == BinaryOps.LAND) {
                imm = new Immediate(0);
                op = LIRConstants.And;
            } else {
                imm = new Immediate(1);
                op = LIRConstants.Or;
            }
            
            BinaryInstruction compareInst = new BinaryInstruction(LIRConstants.Compare, imm,  new Register());
            lirMethod.addInstruction(compareInst);
            
            BranchInstruction branch = new BranchInstruction(LIRConstants.True, endLabel);
            lirMethod.addInstruction(branch);
            
            Register.incRegisterCounter(1);
            propagate(binaryOp.getSecondOperand(), scope);
            Register.decRegisterCounter(1);
            
            BinaryInstruction inst = new BinaryInstruction(op, new Register(1), new Register());
            lirMethod.addInstruction(inst);
            
            PseudoInstruction endLabelInst = new PseudoInstruction(endLabel, LIRConstants.Label);
            lirMethod.addInstruction(endLabelInst);
         
            return null;
        }
        
        Register.incRegisterCounter(1);
		propagate(binaryOp.getSecondOperand(), scope);
        Register.decRegisterCounter(1);
        
        String branchCond = null;
        
        switch(binaryOp.getOperator()) {
        case EQUAL:
            branchCond = LIRConstants.True;
            break;
        case NEQUAL:
            branchCond = LIRConstants.False;
            break;
        case GT:
            branchCond = LIRConstants.GT;
            break;
        case GTE:
            branchCond = LIRConstants.GET;
            break;
        case LT:
            branchCond = LIRConstants.LT;
            break;
        case LTE:
            branchCond = LIRConstants.LET;
            break;
		default:
			int x =5 ;
			break;
        }
        MoveInstruction moveInstruction;
        moveInstruction = new MoveInstruction(new Immediate(1), new Register(2));
        lirMethod.addInstruction(moveInstruction);
        
        BinaryInstruction compInst = new BinaryInstruction(LIRConstants.Compare, new Register(1), new Register());
        lirMethod.addInstruction(compInst);
        
        BranchInstruction branch = new BranchInstruction(branchCond, endLabel);
        lirMethod.addInstruction(branch);
        
        moveInstruction = new MoveInstruction(new Immediate(0), new Register(2));
        lirMethod.addInstruction(moveInstruction);
        
        PseudoInstruction endLabelInst = new PseudoInstruction(endLabel, LIRConstants.Label);
        lirMethod.addInstruction(endLabelInst);
        
        moveInstruction = new MoveInstruction(new Register(2), new Register());
        lirMethod.addInstruction(moveInstruction);
            
        return null;
    }

	@Override
	public Object visit(MathUnaryOp unaryOp, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        
        if (unaryOp.getOperator() == IC.UnaryOps.UMINUS) 
        {   
            propagate(unaryOp.getOperand(), scope);
            UnaryInstruction unaryInstruction = new UnaryInstruction(LIRConstants.Neg, new Register());
            lirMethod.addInstruction(unaryInstruction);               
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
			lirMethod.addInstruction(binaryLnegInstruction);            
		}
		return null;
	}

	@Override
	public Object visit(Literal literal, Object scope)  {
		LIRMethod lirMethod = (LIRMethod) scope;
        SymbolType type = literal.getSymbolType();
        
        Operand value = null;
        
		if (type == TypeTable.boolType) {
			if (literal.getType() == LiteralTypes.TRUE) {
				value = new Immediate(1);
			} else {
				value = new Immediate(0);
			}
		} else if (type == TypeTable.intType) {
			if (literal.getValue().equals("2147483648")){
				literal.setValue("-"+literal.getValue());		
			}
			int intVal = Integer.parseInt((String) literal.getValue());
			value = new Immediate(intVal);
		} else if (type == TypeTable.nullType) {
			value = new Immediate(0);
		} else if (type == TypeTable.strType) {
			Label label = program.getStringLiteralLabel((String)literal.getValue());
			if(label != null){
				value = label;	
			} else {
				//TODO: should this be an exception?
				return null;
			}
		}
        
        MoveInstruction movInstruction = new MoveInstruction(value, new Register());
        lirMethod.addInstruction(movInstruction);
        
        return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock, Object scope) {
		propagate (expressionBlock.getExpression(),scope);
		return null;
	}

	private SymbolTable getClassSymbolTableByNode(ASTNode node) {
		SymbolTable currentSymbolTable = node.getEnclosingScopeSymTable();
		while (!(currentSymbolTable instanceof ClassSymbolTable)) {
			currentSymbolTable = currentSymbolTable.getParentSymbolTable();
		}
		return (ClassSymbolTable) currentSymbolTable;
	}

	
	private SymbolTable getMethodSymbolTable(ICClass icClass, String methodName){
		SymbolTable classSymbolTable = icClass.getEnclosingScopeSymTable();
		SymbolTable methodSymbolTable = classSymbolTable.getChildSymbolTables().get(methodName);
		return methodSymbolTable;
	}
	
	public String getBinaryOperationString(MathBinaryOp binaryOp) {
		String binOp = null;

		switch(binaryOp.getOperator()) {
			case PLUS: 
				binOp = LIRConstants.Add;
				break;
			case MINUS:
				binOp = LIRConstants.Sub;
				break;
			case MULTIPLY:
				binOp = LIRConstants.Mul;
				break;
			case DIVIDE:
				binOp = LIRConstants.Div;
				break;
			case MOD:
				binOp = LIRConstants.Mod;
				break;
		}
		return binOp;
	}
}
