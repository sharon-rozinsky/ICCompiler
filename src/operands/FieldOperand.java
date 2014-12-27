package operands;

public class FieldOperand extends SpecialOperand{

	public FieldOperand(Operand location, Operand offset) {
		super(location, offset);
	}
	
	public Operand getLocation(){
		return getOperand1();
	}
	
	public Operand getOffset(){
		return getOperand2();
	}
	
	@Override
	public String toString() {
		return getOperand1().toString() + "." + getOperand1().toString();
	}
}
