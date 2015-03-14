package IC.x86.Instructions;

public class X86ZeroDivCheckInstruction extends X86Instruction {
	String intReg;
	private IC.x86.frame frame;
	
	public X86ZeroDivCheckInstruction(IC.x86.frame frame, String intRegister) {
		super("StaticCall");
		this.intReg = intRegister;
		this.frame = frame;
	}
	
	public String toString(){
		StringBuilder x86Code = new StringBuilder();
		x86Code.append("# check zero division\n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(intReg) + "(%ebp),%eax" + "\n");
		x86Code.append("\t cmp $0,%eax	# eax is divisor \n");
		x86Code.append("\t je labelDBE     # eax == 0 ? \n");
		
		return x86Code.toString();
	}
}
