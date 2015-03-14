package IC.x86.Instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class X86DivModInstruction extends X86Instruction {
	private Operand op1;
    private Register op2;
    private String operation;
    
    public X86DivModInstruction(String operation, Operand op1, Register op2) {
        super(operation.toString());
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
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
        return getName() + " " + op1 + "," + op2;
    }
}