package lir.operands;

public class Immediate implements Operand {
	private int value;

	public Immediate(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public String toString(){
		return Integer.toString(value);
	}
}
