package operands;

public class ParameterOperand extends SpecialOperand {
	
	public ParameterOperand(Operand memory, Operand param) {
		super(memory, param);
	}
	
	public Operand getMemory(){
		return getOperand1();
	}
	
	public Operand getParam(){
		return getOperand2();
	}
	
	@Override
	public String toString() {
		return getOperand1().toString() + "=" + getOperand1().toString();
	}
}
