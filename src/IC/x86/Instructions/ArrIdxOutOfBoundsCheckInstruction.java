package IC.x86.Instructions;

public class ArrIdxOutOfBoundsCheckInstruction extends X86Instruction {
	String arrReg;
	String idxReg;
	IC.x86.frame frame;
	
	public ArrIdxOutOfBoundsCheckInstruction(IC.x86.frame frame, String arrRegister, String idxRegister) {
		super("StaticCall");
		this.arrReg = arrRegister;
		this.idxReg = idxRegister;
		this.frame = frame;
	}
	
	public String toString(){
		
		StringBuilder x86Code = new StringBuilder();
		x86Code.append("# check array index outOfBounds (eax = array, ecx = index) \n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(idxReg) + "(%ebp),%ecx \n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(arrReg) + "(%ebp),%eax" + "\n");
		x86Code.append("\t mov -4(%eax),%ebx  # ebx = length \n");
		x86Code.append("\t movl $0,%edx" + "\n");
		x86Code.append("\t movl %ebx ,%eax" + "\n");
		x86Code.append("\t mov $4, %ebx" + "\n");
		x86Code.append("\t idiv %ebx" + "\n");
		x86Code.append("\t cmp %ecx,%eax \n");
		x86Code.append("\t jle labelABE       # eax <= ecx ? \n");
		x86Code.append("\t cmp $0,%ecx \n");
		x86Code.append("\t jl  labelABE       # ecx < 0 ? \n");
		
		return x86Code.toString();
	}

}
