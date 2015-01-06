package IC.lir.instructions;

public class arrIdxCheckInstruction extends Instruction {
	String sizeReg;
	
	public arrIdxCheckInstruction(String instructionName, String sizeRegister) {
		super("StaticCall");
		this.sizeReg = sizeRegister;
	}
	
	public String toString(){
		return getName() + " __checkSize(n=" + sizeReg + "),Rdummy\n";
	}
}
