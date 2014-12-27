package IC.lir.instructions;

import IC.lir.LIRConstants;
import IC.lir.operands.Register;

public class UnaryInstruction extends Instruction {

	private Register operand;
	private LIRConstants operator;

	public UnaryInstruction(LIRConstants operator, Register operand) {
		super(operator.toString());
		this.operand = operand;
		this.operator = operator;
	}

	public Register getOperand() {
		return operand;
	}

	public IC.lir.LIRConstants getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return getName() + " " + operand;
	}
}
