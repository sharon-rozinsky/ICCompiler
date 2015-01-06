package IC.lir.instructions;

public class arrIdxOutOfBoundsCheckInstruction extends Instruction {
	String arrReg;
	String idxReg;
	
	public arrIdxOutOfBoundsCheckInstruction(String instructionName, String arrRegister, String idxRegister) {
		super("StaticCall");
		this.arrReg = arrRegister;
		this.idxReg = idxRegister;
	}
	
	public String toString(){
		return getName() + " __checkArrayAccess(a=" + arrReg + ", i=" + idxReg + "),Rdummy\n";
	}

}
