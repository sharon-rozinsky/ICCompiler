package IC.lir.instructions;

public class ArrIdxCheckInstruction extends Instruction {
	String sizeReg;
	
	public ArrIdxCheckInstruction(String instructionName, String sizeRegister) {
		super("StaticCall");
		this.sizeReg = sizeRegister;
	}
	
	public String toString(){
		return getName() + " __checkSize(n=" + sizeReg + "),Rdummy\n";
	}
}
