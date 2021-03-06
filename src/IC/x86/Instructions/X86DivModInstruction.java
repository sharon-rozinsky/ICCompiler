package IC.x86.Instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class X86DivModInstruction extends X86Instruction {
	private Operand op1;
    private Register op2;
    private String operation;
	private IC.x86.frame frame;
    
    public X86DivModInstruction(IC.x86.frame frame, String operation, Operand op1, Register op2) {
        super(operation.toString());
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
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
		x86Code.append("\t movl $0,%edx" + "\n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(op2.toString()) + "(%ebp),%eax" + "\n");
		x86Code.append("\t movl " + frame.getVariableStackOffset(op1.toString()) + "(%ebp),%ebx" + "\n");
		x86Code.append("\t idiv %ebx" + "\n");
		
		if (operation.equals("Mod"))
			x86Code.append("\t movl %edx," + frame.getVariableStackOffset(op2.toString()) + "(%ebp)" + "\n");
		else 
			x86Code.append("\t movl %eax," + frame.getVariableStackOffset(op2.toString()) + "(%ebp)" + "\n");
			
		return x86Code.toString();

    }
}