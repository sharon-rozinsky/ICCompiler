package lir.operands;

public class SpecialOperand {
	private Operand operand1;
	private Operand operand2;
	
	public SpecialOperand(Operand operand1, Operand operand2){
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	protected Operand getOperand1() {
		return operand1;
	}

	protected void setOperand1(Operand operand1) {
		this.operand1 = operand1;
	}

	protected Operand getOperand2() {
		return operand2;
	}

	protected void setOperand2(Operand operand2) {
		this.operand2 = operand2;
	}
}
