package IC.lir.instructions;

public class ArrIdxOutOfBoundsCheckInstruction extends Instruction {
	String arrReg;
	String idxReg;
	
	public ArrIdxOutOfBoundsCheckInstruction(String arrRegister, String idxRegister) {
		super("StaticCall");
		this.arrReg = arrRegister;
		this.idxReg = idxRegister;
	}
	
	public String toString(){
		return getName() + " __checkArrayAccess(a=" + arrReg + ", i=" + idxReg + "),Rdummy\n";
	}

}
