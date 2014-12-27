package IC.lir.instructions;

import IC.lir.LirConstants;
import IC.lir.operands.Register;

public class UnaryInstruction extends Instruction {

	private Register operand;
	private LirConstants operator;

	public UnaryInstruction(LirConstants operator, Register operand) {
		super(operator.toString());
		this.operand = operand;
		this.operator = operator;
	}

	public Register getOperand() {
		return operand;
	}

	public IC.lir.LirConstants getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return getName() + " " + operand;
	}
}
