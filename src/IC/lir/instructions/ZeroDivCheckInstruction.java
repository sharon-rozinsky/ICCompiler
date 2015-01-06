package IC.lir.instructions;

public class ZeroDivCheckInstruction extends Instruction {
	String intReg;
	
	public ZeroDivCheckInstruction(String instructionName, String intRegister) {
		super("StaticCall");
		this.intReg = intRegister;
	}
	
	public String toString(){
		return getName() + " __checkZero(b=" + intReg + "),Rdummy\n";
	}
}
