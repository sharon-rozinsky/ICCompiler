package IC.x86.Instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class X86AddSubInstruction extends X86Instruction {
	private Operand op1;
    private Register op2;
    private String operation;
    private boolean isImmediate;
	private IC.x86.frame frame;
    
    public X86AddSubInstruction(IC.x86.frame frame, String operation, Operand op1, Register op2, boolean isImmediate) {
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
    	String x86operation;
		switch (operation) {
			case "Add":
				x86operation = "addl";
				break;
			case "Sub":
				x86operation = "subl";
				break;
			case "And":
				x86operation = "andl";
				break;
			case "Or":
				x86operation = "orl";
				break;
			default:
				x86operation = "";
				break;
		}
		StringBuilder x86Code = new StringBuilder();
		
		x86Code.append("#" + getName() + " \n");

		if (isImmediate)
		{
			x86Code.append("\t " +x86operation + " $" + op1 + ", " + frame.getVariableStackOffset(op2.toString()) + "(%ebp)" + "\n");
		} 
		else 
		{
			x86Code.append("\t movl " + frame.getVariableStackOffset(op2.toString()) + "(%ebp),%eax" + "\n");
			x86Code.append("\t " + x86operation + " " + frame.getVariableStackOffset(op1.toString()) + "(%ebp),%eax" + "\n");
			x86Code.append("\t movl " + "%eax, " + frame.getVariableStackOffset(op2.toString()) + "(%ebp)" + "\n");
		}
		return x86Code.toString();
    }
}
