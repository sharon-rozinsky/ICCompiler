package IC.lir.lirObject;

import java.util.ArrayList;
import java.util.List;

import IC.lir.instructions.Instruction;
import IC.lir.operands.AddressLabel;

public class LIRMethod {
	private AddressLabel methodName;
	private AddressLabel containingClassName;
	private List<Instruction> instructions = new ArrayList<>();
	//TODO: while scope id?
	
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
}
