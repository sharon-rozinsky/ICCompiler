package IC.x86.Instructions;

import IC.lir.operands.*;

public class X86ArrayLengthInstruction extends X86Instruction {
	
    private static final String InstructionName = "ArrayLength";
    private Operand array;
    private Register dest;
    IC.x86.frame frame;
    
    public X86ArrayLengthInstruction(IC.x86.frame frame, Memory memory, Register dest) {
        super(InstructionName);
        this.array = memory;
        this.dest = dest;
        this.frame = frame;
    }
    
    public X86ArrayLengthInstruction(Register arrReg, Register dest) {
        super(InstructionName);
        this.array = arrReg;
        this.dest = dest;
    }

    public Operand getArray() {
        return array;
    }

    public Register getDest() {
        return dest;
    }

    @Override
    public String toString() {
        
        StringBuilder x86Code = new StringBuilder();
		x86Code.append("#" + this.toString() + "\n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(array.toString()) + "(%ebp),%ebx" + "\n");
		x86Code.append("\t movl -4(%ebx), %eax" + "\n");
		x86Code.append("\t movl $0,%edx" + "\n");
		x86Code.append("\t movl $4,%ebx" + "\n");
		x86Code.append("\t idiv %ebx" + "\n");
		x86Code.append("\t movl %eax," + frame.getVariableStackOffset(dest.toString()) + "(%ebp)" + "\n");
		
		return x86Code.toString();
    }
}
