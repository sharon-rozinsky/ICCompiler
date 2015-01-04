package IC.lir.operands;

public class ArrayOperand extends SpecialOperand{
	
	public ArrayOperand(Operand array, Operand index){
		super(array, index);
	}

	public Operand getArray() {
		return getOperand1();
	}

	public Operand getIndex() {
		return getOperand2();
	}

	@Override
	public String toString() {
		return getOperand1().toString() + "[" + getOperand2().toString() + "]";
	}
}
