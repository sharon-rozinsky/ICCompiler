package IC.lir.instructions;

public class zeroDivCheckInstruction extends Instruction {
	String intReg;
	
	public zeroDivCheckInstruction(String instructionName, String intRegister) {
		super("StaticCall");
		this.intReg = intRegister;
	}
	
	public String toString(){
		return getName() + " __checkZero(b=" + intReg + "),Rdummy\n";
	}
}
