package IC.x86.Instructions;

public class NullReferenceCheckInstruction extends X86Instruction{
	String reg;
	IC.x86.frame frame;

	public NullReferenceCheckInstruction(IC.x86.frame frame, String register) {
		super("StaticCall");
		this.reg = register;
		this.frame = frame;
	}
	
	public String toString(){
		StringBuilder x86Code = new StringBuilder();
		x86Code.append("# null reference check \n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(reg) + "(%ebp),%eax" + "\n");
		x86Code.append("\t cmp $0,%eax \n");
		x86Code.append("\t je labelNPE \n");
		
		return x86Code.toString();
	}

}
