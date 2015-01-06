package IC.lir.instructions;

public class NullReferenceCheckInstruction extends Instruction{
	String reg;

	public NullReferenceCheckInstruction(String register) {
		super("StaticCall");
		this.reg = register;
	}
	
	public String toString(){
		return getName() + " __checkNullRef(a=" + this.reg + "),Rdummy\n";
	}

}
