package IC.x86.Instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class X86MulInstruction extends X86Instruction {
	private Operand op1;
    private Register op2;
    private String operation;
    private boolean isImmediate;
	private IC.x86.frame frame;
    
    public X86MulInstruction(IC.x86.frame frame, String operation, Operand op1, Register op2, boolean isImmediate) {
        super(operation.toString());
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
        this.isImmediate = isImmediate;
        this.frame = frame;	
    }

    public Operand getOp1() {
        return op1;
    }

    public Register getOp2() {
        return op2;
    }

    public String getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
		StringBuilder x86Code = new StringBuilder();
		
		x86Code.append("#" + getName() + " \n");

		if (isImmediate)
		{
			x86Code.append("\t movl $" + op1.toString() + ", %eax \n");
		} 
		else 
		{
			x86Code.append("\t movl " + frame.getVariableStackOffset(op1.toString()) + "(%ebp),%eax" + "\n");
		}
		
		x86Code.append("\t movl " + frame.getVariableStackOffset(op2.toString()) + "(%ebp),%ebx" + "\n");
		x86Code.append("\t imul %eax, %ebx \n");
		x86Code.append("\t movl %ebx, " + frame.getVariableStackOffset(op2.toString()) + "(%ebp)" + "\n");
		
		return x86Code.toString();
    }
}
