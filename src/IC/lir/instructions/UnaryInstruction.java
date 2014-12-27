package IC.lir.instructions;

import IC.lir.LIRConstants;
import IC.lir.operands.Register;

public class UnaryInstruction extends Instruction {

	private Register operand;
	private String operator;

	public UnaryInstruction(String operator, Register operand) {
		super(operator);
		this.operand = operand;
		this.operator = operator;
	}

	public Register getOperand() {
		return operand;
	}

	public String getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return getName() + " " + operand;
	}
}
