package IC.lir.instructions;

public class ArrSizeCheckInstruction extends Instruction {
	String sizeReg;
	
	public ArrSizeCheckInstruction(String sizeRegister) {
		super("StaticCall");
		this.sizeReg = sizeRegister;
	}
	
	public String toString(){
		return getName() + " __checkSize(n=" + sizeReg + "),Rdummy";
	}
}
