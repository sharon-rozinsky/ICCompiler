package IC.lir.instructions;

public class NullPtrCheckInstruction extends Instruction{
	String reg;

	public NullPtrCheckInstruction(String register) {
		super("StaticCall");
		this.reg = register;
	}
	
	public String toString(){
		return getName() + " __checkNullRef(a=" + this.reg + "),Rdummy";
	}

}
