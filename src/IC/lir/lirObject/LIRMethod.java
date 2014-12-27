package IC.lir.lirObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import IC.lir.instructions.Instruction;
import IC.lir.operands.AddressLabel;

public class LIRMethod {
	private AddressLabel methodName;
	private AddressLabel containingClassName;
	private List<Instruction> instructions = new ArrayList<>();
	private Stack<Integer> whileLoopsStack = new Stack<Integer>();
	
	public LIRMethod(AddressLabel methodName, AddressLabel containingClassName) {
		super();
		this.methodName = methodName;
		this.containingClassName = containingClassName;
	}

	public AddressLabel getMethodName() {
		return methodName;
	}

	public AddressLabel getContainingClassName() {
		return containingClassName;
	}

	public List<Instruction> getInstructions() {
		return instructions;
	}
	
	public void addInstruction(Instruction instruction){
		instructions.add(instruction);
	}
	
	public Stack<Integer> getWhileLoopsStack() {
        return whileLoopsStack;
    }
}
